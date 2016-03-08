package Graphs.NetworkGraph;

import Graphs.Edge;
import Graphs.Vertex;
import Tools.Tools;

/**
 * Created by Andrei-ch on 2016-02-29.
 */
public class RCEdge extends Edge {

    private double cost, reliability;

    public RCEdge(Vertex v1, Vertex v2, double cost, double reliability){
        super(v1,v2);
        this.cost = cost;
        this.reliability = reliability;
    }

    public RCEdge(Vertex v1, Vertex v2){
        super(v1,v2);
        this.cost = 0;
        this.reliability = 0;
    }

    public RCEdge(RCEdge e){
        super(e.getV1(), e.getV2());
        this.cost = e.getCost();
        this.reliability = e.getReliability();
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getReliability() {
        return reliability;
    }

    public void setReliability(double reliability) {
        this.reliability = reliability;
    }

    public double r2cRatio(){
        return this.reliability / this.cost;
    }

    @Override
    public String toString(){
        return "[" + this.getV1().getTag() + "," + this.getV2().getTag() + "] C = " + Tools.smaller_double(this.cost) + ", R = " +
                Tools.smaller_double(this.reliability) +  ", R/C = " + Tools.smaller_double(r2cRatio());
    }
}
