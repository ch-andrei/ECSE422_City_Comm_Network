package Tools;

import Graphs.Edge;
import Graphs.NavigationGraph.NavGraph;
import Graphs.NetworkGraph.RCEdge;
import Graphs.Graph;
import Graphs.NetworkGraph.RCGraph;

import java.util.*;

/**
 * Created by Andrei-ch on 2016-02-28.
 */
public class Computation {

    /**
     * Get a maximum spanning tree from a graph with weighted edges
     * Kruskal's algorithm
     */
    public static void minimum_cost_spanning_tree(RCGraph G) {
        Queue<Edge> mst = new LinkedList<Edge>();
        RCGraph copyG = new RCGraph(G);
        Edge[] edges = (copyG.getE());

        List<Edge> e_list = Arrays.asList(edges);
        e_list.sort((e1, e2) -> {
            Double v1, v2;
            v1 = new Double(((RCEdge) e1).r2cRatio());
            v2 = new Double(((RCEdge) e2).r2cRatio());

            return (v1.compareTo(v2));
        });
        Collections.reverse(Arrays.asList(copyG.getE()));

        int index = 0;
        while (!GraphTools.checkConnectedG(G) && mst.size() < G.getN() - 1 && index < G.getTotal_entries()) {
            int i = copyG.getE()[index].getV1().getTag();
            int j = copyG.getE()[index].getV2().getTag();
            int ij = GraphTools.matrixToArrayIndex(i, j, G.getN());
            G.getAdjacencyMatrix()[ij] = 1;
            if (GraphTools.findCycleEdge(G) != -1) {
                G.getAdjacencyMatrix()[ij] = 0;
            }
            index++;
        }
    }

    /**
     * @param G
     * @return
     */
    public static double computeNetworkReliability(RCGraph G) {
        RCGraph copyG = new RCGraph(G);
        simpleReduce(copyG);
        if (!GraphTools.checkConnectedG(copyG))
            return 0;
        return computeRbyDecomposition(copyG);
    }

    /**
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
     * @param G
     * @return
     */
    private static double computeR(RCGraph G) {
        double R = 1;
        for (int i = 0; i < G.getTotal_entries(); i++) {
            RCEdge e = (RCEdge) G.getE()[i];
            if (G.getAdjacencyMatrix()[i] != 0)
                R *= e.getReliability();
        }
        return R;
    }

    public static double computeC(RCGraph G) {
        double c = 0;
        for (int i = 0; i < G.getTotal_entries(); i++) {
            RCEdge e = (RCEdge) G.getE()[i];
            int adj = G.getAdjacencyMatrix()[i];
            c += adj * e.getCost();
        }
        return c;
    }

    private static void simpleReduce(RCGraph G) {
        for (int ij = 0; ij < G.getTotal_entries(); ij++) {
            RCEdge e = (RCEdge) (G.getE()[ij]);
            if (G.getAdjacencyMatrix()[ij] <= 1)
                continue;
            if (G.getAdjacencyMatrix()[ij] == 2) {
                double nR = GraphTools.rParalel(e.getReliability(), e.getReliability());
                e.setReliability(nR);
            } else if (G.getAdjacencyMatrix()[ij] == 3) {
                double nR = GraphTools.rParalel(e.getReliability(), e.getReliability());
                nR = GraphTools.rParalel(nR, e.getReliability());
                e.setReliability(nR);
            }
            G.getAdjacencyMatrix()[ij] = 1;
        }
    }

}
