package Graphs.NetworkGraph;

import Graphs.Graph;

/**
 * Created by Andrei-ch on 2016-02-27.
 */
public class RCGraph extends Graph {

    public RCGraph(int n){
        super(n);
        init_edge_values();
    }

    public RCGraph(int n, double[] costs, double[] reliabilities){
        super(n);
        init_edge_values(costs,reliabilities);
    }

    public RCGraph(Graph G){
        super(G);
        init_edge_values();
    }

    /**
     * clones graph G and create a new cloned object
     * @param G
     */
    public RCGraph(RCGraph G){
        super(G);
        init_edge_values(G);
    }

    public void init_edge_values(){
        int index = 0;
        for (int j = 0; j < this.getN(); j++){
            for (int i = j+1; i < this.getN(); i++){
                this.getE()[index] = new RCEdge(this.getV()[i],this.getV()[j], 0, 0);
                index++;
            }
        }
    }

    @Override
    public void init_edge_values(Graph G){
        int index = 0;
        for (int j = 0; j < this.getN(); j++){
            for (int i = j+1; i < this.getN(); i++){
                this.getE()[index] = new RCEdge(this.getV()[i],this.getV()[j], ((RCEdge)G.getE()[index]).getCost(), ((RCEdge)G.getE()[index]).getReliability());
                index++;
            }
        }
    }

    public void init_edge_values(double[] costs, double[] reliabilities){
        int index = 0;
        for (int j = 0; j < this.getN(); j++){
            for (int i = j+1; i < this.getN(); i++){
                this.getE()[index] = new RCEdge(this.getV()[i],this.getV()[j], costs[index], reliabilities[index]);
                index++;
            }
        }
    }

    public void init_edge_values(double cost, double reliability){
        int index = 0;
        for (int j = 0; j < this.getN(); j++){
            for (int i = j+1; i < this.getN(); i++){
                this.getE()[index] = new RCEdge(this.getV()[i],this.getV()[j], cost, reliability);
                index++;
            }
        }
    }
}
