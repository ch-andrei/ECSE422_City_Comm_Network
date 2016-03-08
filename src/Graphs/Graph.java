package Graphs;

import Graphs.NetworkGraph.RCEdge;

import java.util.Arrays;

/**
 * Created by Andrei-ch on 2016-02-27.
 */
public class Graph {

    private int n;
    private int total_entries;
    private Vertex[] V;         // size n
    private Edge[] E;           // size n(n-1)/2
    private int[] adjacencyMatrix;

    public Graph(int n) {
        this.n = n;
        this.total_entries = n * (n - 1) / 2;
        this.V = new Vertex[n];
        this.E = new Edge[total_entries];
        this.adjacencyMatrix = new int[total_entries];
        init();
    }

    public Graph(Graph G) {
        this.n = G.getN();
        this.total_entries = G.getTotal_entries();
        this.V = Arrays.copyOf(G.getV(), G.getV().length);
        this.adjacencyMatrix = Arrays.copyOf(G.getAdjacencyMatrix(), G.getAdjacencyMatrix().length);
        this.E = new Edge[total_entries];
        init_edge_values(G);
    }

    public void init_edge_values(Graph G){
        for (int i = 0 ; i < total_entries; i++){
            this.E[i] = new Edge(G.getE()[i].getV1(), G.getE()[i].getV2());
        }
    }

    public void init() {
        for (int tag = 0; tag < n; tag++) {
            V[tag] = new Vertex(tag);
        }
    }

    // GETTERS AND SETTERS

    public Vertex[] getV() {
        return V;
    }

    public void setV(Vertex[] v) {
        V = v;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public Edge[] getE() {
        return E;
    }

    public void setE(Edge[] e) {
        E = e;
    }

    public int getTotal_entries() {
        return total_entries;
    }

    public void setTotal_entries(int total_entries) {
        this.total_entries = total_entries;
    }

    public int[] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public void setAdjacencyMatrix(int[] adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
    }

    public String toString() {
        String str = "Vertices:\n";
        int cnt = 0;
        for (Vertex v : this.getV()) {
            if (v == null) continue;
            str += "[" + cnt + "]: " + v + ";\n";
            cnt++;
        }
        str += edgesToStr();
        return str;
    }

    protected String edgesToStr() {
        String str = "";
        str += "\nEdges:\n";
        int index = 0;
        for (Edge e : this.getE()) {
            if (e == null)
                continue;
            str += "[" + adjacencyMatrix[index] + "/3] " + e + ";\n";
            index++;
        }
        return str;
    }
}
