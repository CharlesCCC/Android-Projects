package edu.cwru.rise.dataflow;

import edu.cwru.rise.Java8Lexer;
import edu.cwru.rise.Java8Parser;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.jgrapht.ext.ComponentNameProvider;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.ExportException;
import org.jgrapht.graph.DefaultEdge;

import java.io.File;


public class ReachDefinitionAnalysis {

    public static boolean profile = false;
    public static boolean notree = false;
    public static boolean gui = false;
    public static boolean printTree = false;
    public static boolean SLL = false;
    public static boolean diag = false;
    public static boolean bail = false;
    public static boolean x2 = false;
    public static boolean threaded = false;
    public static boolean quiet = false;

    public static void main(String[] args) {
        parseFile("ForLoop.java");
    }

    public static void parseFile(String f) {
        try {
            if ( !quiet ) System.err.println(f);
            // Create a scanner that reads from the input stream passed to us
            Lexer lexer = new Java8Lexer(new ANTLRFileStream(f));

            CommonTokenStream tokens = new CommonTokenStream(lexer);
//			long start = System.currentTimeMillis();
//			tokens.fill(); // load all and check time
//			long stop = System.currentTimeMillis();
//			lexerTime += stop-start;

            // Create a parser that reads from the scanner
            Java8Parser parser = new Java8Parser(tokens);
            if ( diag ) parser.addErrorListener(new DiagnosticErrorListener());
            if ( bail ) parser.setErrorHandler(new BailErrorStrategy());
            if ( SLL ) parser.getInterpreter().setPredictionMode(PredictionMode.SLL);

            // start parsing at the compilationUnit rule
            ParserRuleContext t = parser.compilationUnit();
            if ( notree ) parser.setBuildParseTree(false);
            if ( printTree ) System.out.println(t.toStringTree(parser));

            ReachDefinitionVisitor visitor = new ReachDefinitionVisitor();
            visitor.visit(t);
            visitor.reachDefinitionAnalysis();
        }
        catch (Exception e) {
            System.err.println("parser exception: "+e);
            e.printStackTrace();   // so we can get stack trace
        }
    }
}
