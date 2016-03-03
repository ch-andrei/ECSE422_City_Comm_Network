package Graphs;

/**
 * Created by Andrei-ch on 2016-02-27.
 */
public class Edge {

    private Vertex v1, v2;

    public Edge(Vertex v1, Vertex v2){
        this.v1 = v1;
        this.v2 = v2;
    }

    public Edge(Edge e){
        this.v1 = e.getV1();
        this.v2 = e.getV2();
    }

    public Vertex getV1() {
        return v1;
    }

    public void setV1(Vertex v1) {
        this.v1 = v1;
    }

    public Vertex getV2() {
        return v2;
    }

    public void setV2(Vertex v2) {
        this.v2 = v2;
    }

    public String toString(){
        return "[" + v1.getTag() + "," + v2.getTag() + "] " + v1 + " to " + v2;
    }
}
