package CommunicationNetwork;

import Graphs.Edge;
import Graphs.NetworkGraph.RCEdge;
import Graphs.NetworkGraph.RCGraph;
import Tools.GraphTools;
import Tools.Tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andrei-ch on 2016-02-28.
 */
public class Computation {

    final int MAX_EDGE_CAPACITY = 3;
    private List<Edge> e_list;

    public Computation() {
        e_list = null;
    }

    public RCGraph minimalCost_ReliabilityConstraint2(RCGraph G, double r) {
        RCGraph copyG = new RCGraph(G);

        build_minCostSpanningTree(copyG);

        sortEdges(copyG, false);

        double incremental_R = 0;
        int index = 0;
        boolean fast = false;

        while ((fast || incremental_R < r) && index < copyG.getTotal_entries()) {

            fast = false;
            int i, j, ij;

            i = e_list.get(index).getV1().getTag();
            j = e_list.get(index).getV2().getTag();
            ij = GraphTools.matrixToArrayIndex(i, j, copyG.getN());
            if (((RCEdge) copyG.getE()[ij]).getReliability() == 0) {
                index++;
                continue;
            }
            if (copyG.getAdjacencyMatrix()[ij] < 3) {
                copyG.getAdjacencyMatrix()[ij] += 1;
            } else {
                fast = true;
                index++;
            }
            if (G.getN() > 15 && r > 0.6) {
                if (index % 5 == 0)
                    incremental_R = computeNetworkReliability(copyG);
            } else
                incremental_R = computeNetworkReliability(copyG);
//            Tools.print("added edge " + (RCEdge)copyG.getE()[ij] + "; Incremental R = " + incremental_R);
        }

        if (incremental_R > r)
            return copyG;
        else
            return G;
    }

    public RCGraph minimalCost_ReliabilityConstraint1(RCGraph G, double r) {
        RCGraph copyG = new RCGraph(G);

        build_maxR2CSpanningTree(copyG);

        double incremental_R = 0;
        int index = 0;
        boolean fast = false;

        while ((fast || incremental_R < r) && index < copyG.getTotal_entries()) {

            fast = false;
            int i, j, ij;

            i = e_list.get(index).getV1().getTag();
            j = e_list.get(index).getV2().getTag();
            ij = GraphTools.matrixToArrayIndex(i, j, copyG.getN());
            if (((RCEdge) copyG.getE()[ij]).getReliability() == 0) {
                index++;
                continue;
            }
            if (copyG.getAdjacencyMatrix()[ij] < 3) {
                copyG.getAdjacencyMatrix()[ij] += 1;
            } else {
                fast = true;
                index++;
            }
            if (G.getN() > 15 && r > 0.6) {
                if (index % 5 == 0)
                    incremental_R = computeNetworkReliability(copyG);
            } else
                incremental_R = computeNetworkReliability(copyG);
//            Tools.print("added edge " + (RCEdge)copyG.getE()[ij] + "; Incremental R = " + incremental_R);
        }

        if (incremental_R > r)
            return copyG;
        else
            return G;
    }


    public RCGraph minimalCost_ReliabilityConstraint(RCGraph G, double r) {
        RCGraph copyG = new RCGraph(G);

        build_minCostSpanningTree(copyG);

        double incremental_R = 0;
        int index = 0;
        boolean fast = false;
        while ((fast || incremental_R < r) && index < copyG.getTotal_entries()) {
            fast = false;
            int i, j, ij;
            i = e_list.get(index).getV1().getTag();
            j = e_list.get(index).getV2().getTag();
            ij = GraphTools.matrixToArrayIndex(i, j, copyG.getN());
            if (((RCEdge) copyG.getE()[ij]).getReliability() == 0) {
                index++;
                continue;
            }
            if (copyG.getAdjacencyMatrix()[ij] < 3) {
                copyG.getAdjacencyMatrix()[ij] += 1;
            } else {
                fast = true;
                index++;
            }

            if (G.getN() > 15 && r > 0.6) {
                if (index % 5 == 0)
                    incremental_R = computeNetworkReliability(copyG);
            } else
                incremental_R = computeNetworkReliability(copyG);
//            Tools.print("added edge " + (RCEdge)copyG.getE()[ij] + "; Incremental R = " + incremental_R);
        }

        if (incremental_R > r)
            return copyG;
        else
            return G;
    }

    public RCGraph maximumReliability_CostConstraint(RCGraph G, double c) {
        RCGraph copyG = new RCGraph(G);

        build_minCostSpanningTree(copyG);

        double incremental_C = 0;
        int index = 0;
        boolean fast = false;
        while ((fast || incremental_C < c) && index < copyG.getTotal_entries()) {
            fast = false;
            int i, j, ij;
            i = e_list.get(index).getV1().getTag();
            j = e_list.get(index).getV2().getTag();
            ij = GraphTools.matrixToArrayIndex(i, j, copyG.getN());
            if (((RCEdge) copyG.getE()[ij]).getReliability() == 0) {
                index++;
                continue;
            }
            if (copyG.getAdjacencyMatrix()[ij] < 3) {
                copyG.getAdjacencyMatrix()[ij] += 1;
            } else {
                fast = true;
                index++;
            }

            incremental_C = computeCost(copyG);
//            Tools.print("added edge " + (RCEdge)copyG.getE()[ij] + "; Incremental R = " + incremental_R);
        }

        if (incremental_C > c)
            return copyG;
        else
            return G;
    }

    /**
     * @param G
     */
    public void build_maxR2CSpanningTree(RCGraph G) {
        sortEdges(G, false);
        spanningTree(G);
    }

    /**
     * @param G
     */
    public void build_minCostSpanningTree(RCGraph G) {
        sortEdges(G, true);
        spanningTree(G);
    }

    /**
     * computes the total cost of the input graph
     *
     * @param G
     * @return
     */
    public double computeCost(RCGraph G) {
        double c = 0;
        for (int i = 0; i < G.getTotal_entries(); i++) {
            RCEdge e = (RCEdge) G.getE()[i];
            int adj = G.getAdjacencyMatrix()[i];
            c += adj * e.getCost();
        }
        return c;
    }

    /**
     * @param G
     * @return
     */
    public double computeNetworkReliability(RCGraph G) {
        RCGraph copyG = new RCGraph(G);
        simpleReduce(copyG);
        if (!GraphTools.checkConnectedG(copyG))
            return 0;
        return computeRbyDecomposition(copyG);
    }

    /**
     * generates a  spanning tree for a graph G. Requires input of edges sorted by weight.
     * uses Kruskal's algorithm
     *
     * @param G
     */
    private void spanningTree(RCGraph G) {
        int index = 0;
        while (!GraphTools.checkConnectedG(G) && index < G.getTotal_entries()) {
            int i = e_list.get(index).getV1().getTag();
            int j = e_list.get(index).getV2().getTag();
            int ij = GraphTools.matrixToArrayIndex(i, j, G.getN());
            G.getAdjacencyMatrix()[ij] = 1;
            if (GraphTools.findCycleEdge(G) != -1) {
                G.getAdjacencyMatrix()[ij] = 0;
            }
            index++;
        }
    }

    /**
     * sorts edges according to their Cost or Reliability to Cost ratio.
     *
     * @param G       graph the edges of which are to be sorted
     * @param optimal false for R/C, true for cost sorting
     * @return
     */
    private List<Edge> sortEdges(RCGraph G, boolean optimal) {
        // sort edges
        e_list = new ArrayList<Edge>();
        for (int i = 0; i < G.getTotal_entries(); i++) {
            e_list.add(new RCEdge((RCEdge) G.getE()[i]));
        }
        e_list.sort((e1, e2) -> {
            Double v1, v2;
            if (optimal) {
                v1 = (((RCEdge) e1).getReliability() != 0) ? new Double(((RCEdge) e1).getCost()) : 1.0 / 0;
                v2 = (((RCEdge) e2).getReliability() != 0) ? new Double(((RCEdge) e2).getCost()) : 1.0 / 0;
            } else {
                v1 = new Double(((RCEdge) e1).r2cRatio());
                v2 = new Double(((RCEdge) e2).r2cRatio());
            }
            return (v1.compareTo(v2));
        });
        if (!optimal)
            Collections.reverse(e_list);
        return e_list;
    }

    /**
     * @param G
     * @return
     */
    private double computeRbyDecomposition(RCGraph G) {
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
    private double computeR(RCGraph G) {
        double R = 1;
        for (int i = 0; i < G.getTotal_entries(); i++) {
            RCEdge e = (RCEdge) G.getE()[i];
            if (G.getAdjacencyMatrix()[i] != 0)
                R *= e.getReliability();
        }
        return R;
    }

    private void simpleReduce(RCGraph G) {
        for (int ij = 0; ij < G.getTotal_entries(); ij++) {
            if (G.getAdjacencyMatrix()[ij] <= 1)
                continue;
            RCEdge e = (RCEdge) (G.getE()[ij]);
            double nR = e.getReliability();
            while (G.getAdjacencyMatrix()[ij] > 1) {
                nR = GraphTools.rParalel(nR, e.getReliability());
                G.getAdjacencyMatrix()[ij]--;
            }
            e.setReliability(nR);
        }
    }
}
