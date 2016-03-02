package Graphs.NetworkGraph;

import Graphs.Graph;
import Tools.GraphTools;

/**
 * Created by Andrei-ch on 2016-02-27.
 */
public class RCGraph extends Graph {

    public RCGraph(int n){
        super(n);
        initEdgeValues();
    }

    public RCGraph(int n, double[] costs, double[] reliabilities){
        super(n);
        initEdgeValues(costs,reliabilities);
    }

    public RCGraph(Graph G){
        super(G);
        initEdgeValues();
    }

    /**
     * clones graph G and create a new cloned object
     * @param G
     */
    public RCGraph(RCGraph G){
        super(G);
        initEdgeValues(G);
    }

    private void initEdgeValues(){
        int index = 0;
        for (int j = 0; j < this.getN(); j++){
            for (int i = j+1; i < this.getN(); i++){
                this.getE()[index] = new RCEdge(this.getV()[i],this.getV()[j], 0, 0);
                index++;
            }
        }
    }
    private void initEdgeValues(RCGraph G){
        int index = 0;
        for (int j = 0; j < this.getN(); j++){
            for (int i = j+1; i < this.getN(); i++){
                this.getE()[index] = new RCEdge(this.getV()[i],this.getV()[j], ((RCEdge)G.getE()[index]).getCost(), ((RCEdge)G.getE()[index]).getReliability());
                index++;
            }
        }
    }

    private void initEdgeValues(double[] costs, double[] reliabilities){
        int index = 0;
        for (int j = 0; j < this.getN(); j++){
            for (int i = j+1; i < this.getN(); i++){
                this.getE()[index] = new RCEdge(this.getV()[i],this.getV()[j], costs[index], reliabilities[index]);
                index++;
            }
        }
    }

    public void initEdgeValues(double cost, double reliability){
        int index = 0;
        for (int j = 0; j < this.getN(); j++){
            for (int i = j+1; i < this.getN(); i++){
                this.getE()[index] = new RCEdge(this.getV()[i],this.getV()[j], cost, reliability);
                index++;
            }
        }
    }
}
