package edu.cwru.rise.dataflow;

import edu.cwru.rise.Java8BaseVisitor;
import edu.cwru.rise.Java8Parser;
import edu.cwru.rise.Java8Visitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jgrapht.ext.ComponentNameProvider;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.ExportException;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedPseudograph;

import java.io.File;
import java.util.*;


class CFGNode {
    int id;
    ParserRuleContext unit;
    public String text;
    public String label = "";


    public static int NUM = 0;

    public CFGNode(ParserRuleContext ctx) {
        this.unit = ctx;
        id = ++NUM;
    }

    @Override
    public String toString() {

        if (text == null) {
            return id + ": " + unit.getText();
        }
        return id + ": " + text;
    }
}

class CFGNodeIdProvider implements ComponentNameProvider<CFGNode> {
    @Override
    public String getName(CFGNode e) {
        return "" + e.id;
    }

}

class CFGNodeNameProvider implements ComponentNameProvider<CFGNode> {
    @Override
    public String getName(CFGNode e) {
        return "" + e.toString().replace("\"", "'");
    }

}

class CFGEdgeNameProvider implements ComponentNameProvider<CFGEdge> {
    @Override
    public String getName(CFGEdge e) {
        return "" + e.label.replace("\"", "'");
    }

}

class FlowNameProvider implements ComponentNameProvider<CFGNode> {
    HashMap<Integer, HashSet<Def>> IN = new HashMap<>();
    HashMap<Integer, HashSet<Def>> OUT = new HashMap<>();

    public FlowNameProvider(HashMap<Integer, HashSet<Def>> IN, HashMap<Integer, HashSet<Def>> OUT) {
        this.IN = IN;
        this.OUT = OUT;
    }

    @Override
    public String getName(CFGNode e) {
        return "" + e.toString().replace("\"", "'") + "\n" + "IN: " + IN.get(e.id) + "\n" + "OUT: " + OUT.get(e.id);
    }

}

class Def {
    public String var;
    public int lineNbr;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Def def = (Def) o;
        return lineNbr == def.lineNbr &&
                Objects.equals(var, def.var);
    }

    @Override
    public int hashCode() {

        return Objects.hash(var, lineNbr);
    }

    @Override
    public String toString() {
        return "<" +
                var +
                ", " + lineNbr +
                ">";
    }
}

public class ReachDefinitionVisitor extends Java8BaseVisitor<String> {

    DirectedPseudograph<CFGNode, DefaultEdge> cfg = new DirectedPseudograph(CFGEdge.class);
    List<CFGNode> last = new ArrayList<>();



    public void reachDefinitionAnalysis() {

        HashMap<Integer, HashSet<Def>> IN = new HashMap<>();
        HashMap<Integer, HashSet<Def>> OUT = new HashMap<>();

        Set<CFGNode> cfgNodes = cfg.vertexSet();

        for (CFGNode n : cfgNodes
                ) {
            IN.put(n.id, new HashSet<>());
            OUT.put(n.id, new HashSet<>());

        }


        while (true) {

            boolean changed = false;
            for (CFGNode n : cfgNodes
                    ) {
                Set<DefaultEdge> inedges = cfg.incomingEdgesOf(n);
                HashSet<Def> newin = new HashSet<>();
                for (DefaultEdge inedge : inedges
                        ) {
                    newin.addAll(OUT.get(cfg.getEdgeSource(inedge).id));
                }
                if (!newin.equals(IN.get(n.id))) {
                    changed = true;
                }
                IN.put(n.id, newin);

                List<Def> defs = new ArrayList<>();
                if (n.unit instanceof Java8Parser.ExpressionStatementContext || n.unit instanceof Java8Parser.LocalVariableDeclarationStatementContext) {
                    if (n.id == 3) {
                        System.out.println();
                    }
                    AssignmentVisitor av = new AssignmentVisitor(n.id);
                    av.visit(n.unit);
                    defs = av.r;

                }


                HashSet<Def> newout = new HashSet<>(newin);
                if (defs != null && !defs.isEmpty()) {
                    for (Def din : newin
                            ) {

                        for (Def d : defs
                                ) {
                            if (d.lineNbr > 2) {
                                System.out.println();
                            }

                            if (din.var.equals(d.var)) {
                                newout.remove(din); // KILL
                            }
                        }

                    }
                    for (Def d : defs
                            ) {
                        newout.add(d); // GEN
                    }
                }

                if (!newout.equals(OUT.get(n.id))) {
                    changed = true;
                }

                OUT.put(n.id, newout);
            }

            if (!changed) {
                break;
            }
        }


        DOTExporter<CFGNode, DefaultEdge> exporter = new DOTExporter(new CFGNodeIdProvider(), new FlowNameProvider(IN, OUT), (ComponentNameProvider) null);
        try {
            exporter.exportGraph(cfg, new File("flow.dot"));
        } catch (ExportException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String visitClassBody(Java8Parser.ClassBodyContext ctx) {
        System.out.println(ctx.toStringTree());
        return super.visitClassBody(ctx);
    }

    @Override
    public String visitMethodBody(Java8Parser.MethodBodyContext ctx) {


        System.out.println("body: " + ctx.getText());
        Java8Parser.BlockContext block = ctx.block();
        Java8Parser.MethodDeclarationContext md = (Java8Parser.MethodDeclarationContext) ctx.getParent();
        System.out.println("method name: " + md.methodHeader().methodDeclarator().Identifier().getText());

        for (Java8Parser.BlockStatementContext bsc :
                ctx.block().blockStatements().blockStatement()) {
            System.out.println("statement: " + bsc.localVariableDeclarationStatement());
            System.out.println("statement: " + bsc.statement());
        }

        String s = super.visitMethodBody(ctx);


        DOTExporter<CFGNode, DefaultEdge> exporter = new DOTExporter(new CFGNodeIdProvider(), new CFGNodeNameProvider(), new CFGEdgeNameProvider());
        try {
            exporter.exportGraph(cfg, new File("cfg.dot"));
        } catch (ExportException e) {
            e.printStackTrace();
        }
        return s;
    }

    @Override
    public String visitIfThenStatement(Java8Parser.IfThenStatementContext ctx) {
        Java8Parser.StatementContext thenst = ctx.statement();

        CFGNode n = new CFGNode(ctx);
        n.text = "if (" + ctx.expression().getText() + " )";
        cfg.addVertex(n);
        if (!last.isEmpty()) {
            for (CFGNode c : last
                    ) {
                CFGEdge e = (CFGEdge) cfg.addEdge(c, n);
                e.label = c.label;
            }
        }
        last.clear();
        last.add(n);
        n.label = "true";

        super.visitStatement(thenst);

        last.add(n);
        n.label = "false";

        return "";
    }

    @Override
    public String visitIfThenElseStatement(Java8Parser.IfThenElseStatementContext ctx) {
//        ctx.

        Java8Parser.StatementContext thenst = ctx.statement();
        Java8Parser.StatementNoShortIfContext elsest = ctx.statementNoShortIf();

        CFGNode n = new CFGNode(ctx);
        n.text = "if (" + ctx.expression().getText() + " )";
        cfg.addVertex(n);
        if (!last.isEmpty()) {
            for (CFGNode c : last
                    ) {
                CFGEdge e = (CFGEdge) cfg.addEdge(c, n);
                e.label = c.label;
            }
        }
        last.clear();
        last.add(n);

        n.label = "true";
        super.visitStatement(thenst);
        List<CFGNode> thenlast = new ArrayList<>(last);
        last.clear();
        last.add(n);

        n.label = "false";
        super.visitStatementNoShortIf(elsest);

        last.addAll(thenlast);

        for (CFGNode cfgn:last
             ) {
            cfgn.label = "";
        }

        return "";
    }

    @Override
    public String visitBasicForStatement(Java8Parser.BasicForStatementContext ctx) {

        Java8Parser.ForInitContext forInitContext = ctx.forInit();
        Java8Parser.ExpressionContext forexpression = ctx.expression();
        Java8Parser.ForUpdateContext forUpdateContext = ctx.forUpdate();

        CFGNode n = new CFGNode(forInitContext);
        cfg.addVertex(n);
        n.text = forInitContext.getText();
        if (!last.isEmpty()) {
            for (CFGNode c : last
                    ) {
                CFGEdge e = (CFGEdge) cfg.addEdge(c, n);
                e.label = c.label;
            }
        }


        CFGNode e = new CFGNode(forexpression);
        cfg.addVertex(e);
        e.text = forexpression.getText();

        cfg.addEdge(n, e);

        last.clear();
        last.add(e);

        e.label = "true";
        super.visitStatement(ctx.statement());

        CFGNode u = new CFGNode(forUpdateContext);
        cfg.addVertex(u);
        u.text = forUpdateContext.getText();
        if (!last.isEmpty()) {
            for (CFGNode c : last
                    ) {
                CFGEdge e2 = (CFGEdge) cfg.addEdge(c, u);
                e2.label = c.label;
            }
        }

        cfg.addEdge(u, e);

        last.clear();
        last.add(e);
        e.label = "false";

        return "";
    }

    @Override
    public String visitWhileStatement(Java8Parser.WhileStatementContext ctx) {


        CFGNode n = new CFGNode(ctx);
        cfg.addVertex(n);
        n.text = ctx.expression().getText();
        if (!last.isEmpty()) {
            for (CFGNode c : last
                    ) {
                CFGEdge e = (CFGEdge) cfg.addEdge(c, n);
                e.label = c.label;
            }
        }

        last.clear();
        last.add(n);

        n.label = "true";
        super.visitStatement(ctx.statement());

        if (!last.isEmpty()) {
            for (CFGNode c : last
                    ) {
                CFGEdge e = (CFGEdge) cfg.addEdge(c, n);
                e.label = c.label;
            }
        }

        last.clear();
        last.add(n);

        n.label = "false";
        return "";
    }

    @Override
    public String visitLocalVariableDeclarationStatement(Java8Parser.LocalVariableDeclarationStatementContext ctx) {
        CFGNode n = new CFGNode(ctx);
        cfg.addVertex(n);
        if (!last.isEmpty()) {
            for (CFGNode c : last
                    ) {
                CFGEdge e = (CFGEdge) cfg.addEdge(c, n);
                e.label = c.label;
            }
        }
        last.clear();
        last.add(n);
        n.label = "";
        return super.visitLocalVariableDeclarationStatement(ctx);
    }

    @Override
    public String visitExpressionStatement(Java8Parser.ExpressionStatementContext ctx) {
        CFGNode n = new CFGNode(ctx);
        cfg.addVertex(n);
        if (!last.isEmpty()) {
            for (CFGNode c : last
                    ) {
                CFGEdge e = (CFGEdge) cfg.addEdge(c, n);
                e.label = c.label;
            }
        }
        last.clear();
        last.add(n);
        n.label = "";
        return super.visitExpressionStatement(ctx);
    }


}
