package Graphs;

/**
 * Created by Andrei-ch on 2016-02-27.
 */
public class Vertex {

    private int tag;

    public Vertex(int tag){
        this.tag = tag;
    }

    public int getTag(){
        return this.tag;
    }

    public String toString(){
        return "T" + tag;
    }
}