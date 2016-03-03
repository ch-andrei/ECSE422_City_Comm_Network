package Tools;

import Graphs.NavigationGraph.NavGraph;
import Graphs.NetworkGraph.RCEdge;
import Graphs.Graph;
import Graphs.NetworkGraph.RCGraph;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Andrei-ch on 2016-02-28.
 */
public class Computation {

    /**
     * @param G
     * @return
     */
    public static double computeNetworkReliability(RCGraph G) {
        RCGraph copyG = new RCGraph(G);
        simpleReduce(copyG);
        if ( !GraphTools.checkConnectedG(copyG) )
            return 0;
        return computeRbyDecomposition(copyG);
    }

    /**
     *
     * @param G
     * @return
     */
    private static double computeRbyDecomposition(RCGraph G) {
        // find an edge that is part of a cycle
        int ijCycle = GraphTools.findCycleEdge(G);
        // if an edge was found
        if (ijCycle != -1) {
            // decompose G:
            // create a copy of the graph
            RCGraph G1 = new RCGraph(G);
            RCGraph G2 = new RCGraph(G);
            GraphTools.mergeEdge(G1, ijCycle);
            GraphTools.removeEdge(G2, ijCycle);

            double rG1 = ((RCEdge) G1.getE()[ijCycle]).getReliability();
            return rG1 * computeRbyDecomposition(G1) +
                    (1 - rG1) * computeRbyDecomposition(G2);
        } else {
            // if no edges just compute Reliability as Product series of all edges
            return computeR(G);
        }
    }

    /**
     *
     * @param G
     * @return
     */
    private static double computeR(RCGraph G) {
        double R = 1;
        for (int i = 0; i < G.getTotal_entries(); i++){
            RCEdge e = (RCEdge) G.getE()[i];
            if (G.getAdjacencyMatrix()[i] != 0)
                R *= e.getReliability();
        }
        return R;
    }

    private static void simpleReduce(RCGraph G) {
        for (int ij = 0 ; ij < G.getTotal_entries(); ij++){
            RCEdge e = (RCEdge)(G.getE()[ij]);
            if (G.getAdjacencyMatrix()[ij] <= 1)
                continue;
            if (G.getAdjacencyMatrix()[ij] == 2){
                double nR = GraphTools.rParalel(e.getReliability(), e.getReliability());
                e.setReliability(nR);
            }
            else if (G.getAdjacencyMatrix()[ij] == 3){
                double nR = GraphTools.rParalel(e.getReliability(), e.getReliability());
                nR = GraphTools.rParalel(nR, e.getReliability());
                e.setReliability(nR);
            }
            G.getAdjacencyMatrix()[ij] = 1;
        }
    }
}
