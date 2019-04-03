package edu.cwru.rise.dataflow;

import edu.cwru.rise.Java8BaseVisitor;
import edu.cwru.rise.Java8Parser;

import java.util.ArrayList;
import java.util.List;

public class AssignmentVisitor extends Java8BaseVisitor<List<Def>> {

    int id;
    List<Def> r = new ArrayList<>();


    public AssignmentVisitor(int id) {
        this.id = id;
    }

    @Override
    public List<Def> visitAssignment(Java8Parser.AssignmentContext ctx) {

        System.out.println(id + ":  " + ctx.getText());
        Def d = new Def();
        d.lineNbr = id;
        d.var = ctx.leftHandSide().expressionName().getText();


        r.add(d);
        return r;
    }

    @Override
    public List<Def> visitLocalVariableDeclarationStatement(Java8Parser.LocalVariableDeclarationStatementContext ctx) {

        for (Java8Parser.VariableDeclaratorContext ld :
                ctx.localVariableDeclaration().variableDeclaratorList().variableDeclarator()) {
            Def d = new Def();
            d.var = ld.variableDeclaratorId().getText();
            d.lineNbr = id;
            r.add(d);
        }

        return r;
    }
}
