package edu.cwru.rise.dataflow;

import org.jgrapht.graph.DefaultEdge;

public class CFGEdge extends DefaultEdge {
    public String label="";

    public CFGEdge(String label) {
        this();
        this.label = label;
    }

    public CFGEdge() {
        super();
    }
}
