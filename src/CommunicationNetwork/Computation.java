package CommunicationNetwork;

import Graphs.Edge;
import Graphs.NetworkGraph.RCEdge;
import Graphs.NetworkGraph.RCGraph;
import Toolset.GraphTools;
import Toolset.Tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andrei-ch on 2016-02-28.
 */
public class Computation {

    /**
     * stores edges of the input graph
     */
    private List<Edge> sorted_edges;

    /**
     * static counter used for statistic purposes
     */
    private static int counter = 0;

    /**
     * Constuctior for the computation unit
     */
    public Computation() {
        sorted_edges = null;
    }

    /**
     * Network generation Strategy 1.
     * sorts edges based on cost (minimal cost edges are added first)
     * @param G
     * @param r
     * @return
     */
    public RCGraph minimalCost_ReliabilityConstraint(RCGraph G, double r) {
        RCGraph copyG = new RCGraph(G);
        build_minCostSpanningTree(copyG);
        return minimalCost_ReliabilityConstraint(G, copyG, r);
    }

    /**
     * Network generation Strategy 2.
     * sorts edges based on reliability to cost ratio (maximum r2c ratio edges are added first)
     * @param G
     * @param r
     * @return
     */
    public RCGraph minimalCost_ReliabilityConstraint2(RCGraph G, double r) {
        RCGraph copyG = new RCGraph(G);
        build_maxR2CSpanningTree(copyG);
        return minimalCost_ReliabilityConstraint(G, copyG, r);
    }

    /**
     * Network generation Strategy 3.
     * first builds a minimum cost spanning tree, then
     * sorts edges based on reliability to cost ratio (maximum r2c ratio edges are added first)
     * @param G
     * @param r
     * @return
     */
    public RCGraph minimalCost_ReliabilityConstraint3(RCGraph G, double r) {
        RCGraph copyG = new RCGraph(G);
        build_minCostSpanningTree(copyG);
        sortEdges(copyG, false);
        return minimalCost_ReliabilityConstraint(G, copyG, r);
    }

    /**
     * Main network generation code for Stragies 1-3.
     * Loops over edges and adds the most promising ones to the graph.
     * Paralel connections between two cities are prioritized
     * (adds up to 3 edges between two cities before moving to next promising edge).
     * @param G
     * @param copyG
     * @param r
     * @return
     */
    public RCGraph minimalCost_ReliabilityConstraint(RCGraph G, RCGraph copyG, double r) {
        double incremental_R = 0;
        int index = 0;
        while ((incremental_R < r) && index < copyG.getTotal_entries()) {
            int i, j, ij;
            i = sorted_edges.get(index).getV1().getTag();
            j = sorted_edges.get(index).getV2().getTag();
            ij = GraphTools.matrixToArrayIndex(i, j, copyG.getN());
            if (((RCEdge) copyG.getE()[ij]).getReliability() == 0) {
                index++;
                continue;
            }
            if (copyG.getAdjacencyMatrix()[ij] < 3) {
                copyG.getAdjacencyMatrix()[ij] += 1;
            } else {
                index++;
                continue;
            }
            incremental_R = computeNetworkReliability(copyG);
        }
        if (incremental_R > r)
            return copyG;
        else
            return G;
    }

    /**
     * Network generation Strategy 4
     * Loops over edges and adds the most promising ones to the graph.
     * Paralel connection between two cities are NOT prioritized.
     * (adds 1 edge between two cities and moves to next promising edge;
     * once all edges were considered, start from the beginning and loop over all edges again.
     * the algorithm loops over all edges 3 times, since 3 edges between each two cities is a maximum).
     * Additionally, only the edges with satisfactory cost and r2c ratio are added to the graph.
     * @param G
     * @param r
     * @return
     */
    public RCGraph minimalCost_ReliabilityConstraint4(RCGraph G, double r) {
        RCGraph copyG = new RCGraph(G);
        build_minCostSpanningTree(copyG);
        double acceptable_c = ((RCEdge)sorted_edges.get(sorted_edges.size()/3)).getCost();
        sortEdges(copyG, false);
        double acceptable_r2c = ((RCEdge)sorted_edges.get(sorted_edges.size()/2)).r2cRatio();
        int i, j, ij;
        double incremental_R = 0;
        int index = 0, mod_index = 0;
        while ((incremental_R < r) && index < G.getTotal_entries() * 2) {
            mod_index = index % copyG.getTotal_entries();
            i = sorted_edges.get(mod_index).getV1().getTag();
            j = sorted_edges.get(mod_index).getV2().getTag();
            ij = GraphTools.matrixToArrayIndex(i, j, copyG.getN());
            RCEdge e = ((RCEdge) copyG.getE()[ij]);
            if ( e.getReliability() == 0) {
                index++;
                continue;
            }
            if (copyG.getAdjacencyMatrix()[ij] < 3 &&
                    e.r2cRatio() > acceptable_r2c &&
                    e.getCost() < acceptable_c) {
                copyG.getAdjacencyMatrix()[ij] += 1;
                index++;
            } else {
                index++;
                continue;
            }
            incremental_R = computeNetworkReliability(copyG);
        }
        if (incremental_R > r)
            return copyG;
        else
            return G;
    }

    /**
     * PROBLEM: RELIABILITY CONSTRAINT, FIND MINIMUM COST GRAPH
     * Use this method to find the best available network graph (the best out of 4 available strategies).
     * Runs all 4 strategies, and returns the one that scores the best balance between cost and reliability.
     * Attempts to return a graph with a reliability that is the closest to the requested,
     * as well as a cost that is the lowest available.
     * Each of the 4 strategies is evaluated and a score is assigned to each.
     * See
     * @param G
     * @param r
     * @return
     */
    public RCGraph getBestMinC_Rconstraint(RCGraph G, double r) {
        RCGraph copyG1, copyG2, copyG3, copyG4;
        copyG1 = minimalCost_ReliabilityConstraint(G, r);
        copyG2 = minimalCost_ReliabilityConstraint2(G, r);
        copyG3 = minimalCost_ReliabilityConstraint3(G, r);
        copyG4 = minimalCost_ReliabilityConstraint4(G, r);
        double c1, c2, c3, c4, r1, r2, r3, r4;
        r1 = computeNetworkReliability(copyG1);
        r2 = computeNetworkReliability(copyG2);
        r3 = computeNetworkReliability(copyG3);
        r4 = computeNetworkReliability(copyG4);
        c1 = computeCost(copyG1);
        c2 = computeCost(copyG2);
        c3 = computeCost(copyG3);
        c4 = computeCost(copyG4);

        Tools.print("R1=" + r1 + "\nC1=" + c1);
        Tools.print("R2=" + r2 + "\nC2=" + c2);
        Tools.print("R3=" + r3 + "\nC3=" + c3);
        Tools.print("R4=" + r4 + "\nC4=" + c4);

        List<Double> rL = new ArrayList<>();
        rL.add(r1);
        rL.add(r2);
        rL.add(r3);
        rL.add(r4);
        rL.sort((v1, v2) -> {
            return (v1.compareTo(v2));
        });

        List<Double> cL = new ArrayList<>();
        cL.add(c1);
        cL.add(c2);
        cL.add(c3);
        cL.add(c4);
        cL.sort((v1, v2) -> {
            return (v1.compareTo(v2));
        });

        double cVar, rVar, avgC, avgR;
        cVar = cL.get(3) - cL.get(0);
        rVar = rL.get(3) - rL.get(0);
        avgC = (cL.get(3) + cL.get(0)) / 2;
        avgR = (rL.get(3) + rL.get(0)) / 2;
        Tools.print("RVAR = " + rVar + "; CVAR = " + cVar);
        Tools.print("AVGR = " + avgR + "; AVGC= " + avgC);
        double[] win = new double[4];
        for (int i = 0; i < 4; i++) {
            if (i == 0)
                win[i] = r1 / c1;
            else if (i == 1)
                win[i] = r2 / c2;
            else if (i == 2)
                win[i] = r3 / c3;
            else if (i == 3)
                win[i] = r4 / c4;
        }
        double max = 0;
        int winner = 0;
        for (int i = 0; i < 4; i++) {
            if (win[i] > max) {
                max = win[i];
                winner = i;
            }
        }
        Tools.print("scores");
        for (double d : win) {
            System.out.print(d + ",");
        }
        Tools.print("");
        switch (winner) {
            case 0:
                return copyG1;
            case 1:
                return copyG2;
            case 2:
                return copyG3;
            case 3:
                return copyG4;
            default:
                return copyG1;
        }
    }

    /**
     * Strategy for generating a network with maximum reliability and a cost contraint.
     * @param G
     * @param c
     * @return
     */
    public RCGraph maximumReliability_CostConstraint(RCGraph G, double c) {
        RCGraph copyG = new RCGraph(G);
        build_maxR2CSpanningTree(copyG);
        double incremental_C = 0;
        int index = 0;
        while ((incremental_C < c) && index < copyG.getTotal_entries()) {
            int i, j, ij;
            i = sorted_edges.get(index).getV1().getTag();
            j = sorted_edges.get(index).getV2().getTag();
            ij = GraphTools.matrixToArrayIndex(i, j, copyG.getN());
            if (((RCEdge) copyG.getE()[ij]).getReliability() == 0) {
                index++;
                continue;
            }
            if (copyG.getAdjacencyMatrix()[ij] < 3) {
                copyG.getAdjacencyMatrix()[ij] += 1;
            } else {
                index++;
            }
            incremental_C = computeCost(copyG);
            if (incremental_C > c)
                copyG.getAdjacencyMatrix()[ij] -= 1;
            Tools.print(computeNetworkReliability(copyG));
        }
        incremental_C = computeCost(copyG);
        if (incremental_C < c)
            return copyG;
        else
            return G;
    }

    public RCGraph maximumReliability_CostConstraint1(RCGraph G, double c) {
        RCGraph copyG = new RCGraph(G);
        build_maxR2CSpanningTree(copyG);
        double acceptable_r2c = ((RCEdge)sorted_edges.get(sorted_edges.size()/2)).r2cRatio();
        sortEdges(copyG, true);
        double acceptable_c = ((RCEdge)sorted_edges.get(sorted_edges.size()/2)).getCost();
        int i, j, ij;
        double incremental_C = 0;
        int index = 0, mod_index = 0;
        // loop over edges 3 times at maximum
        while ((incremental_C < c) && index < copyG.getTotal_entries() * 3) {
            mod_index = index % copyG.getTotal_entries();
            i = sorted_edges.get(mod_index).getV1().getTag();
            j = sorted_edges.get(mod_index).getV2().getTag();
            ij = GraphTools.matrixToArrayIndex(i, j, copyG.getN());
            RCEdge e = ((RCEdge) copyG.getE()[ij]);
            if ( e.getReliability() == 0) {
                index++;
                continue;
            }
            // if this edge is acceptably good, add it
            if (copyG.getAdjacencyMatrix()[ij] < 3 &&
                    e.r2cRatio() > acceptable_r2c &&
                    e.getCost() < acceptable_c) {
                copyG.getAdjacencyMatrix()[ij] += 1;
                // increment index and move on to next edge
                index++;
            } else {
                index++;
                continue;
            }
            incremental_C = computeCost(copyG);
            if (incremental_C > c)
                copyG.getAdjacencyMatrix()[ij] -= 1;
        }
        incremental_C = computeCost(copyG);
        if (incremental_C < c)
            return copyG;
        else
            return G;
    }
    
    /**
     * PROBLEM: COST CONSTRAINT, FIND MAXIMUM R GRAPH
     * Use this method to find the best available network graph (the best out of 4 available strategies).
     * Runs all 2 strategies, and returns the one that scores the best balance between cost and reliability.
     * Each of the 2 strategies is evaluated and a score is assigned to each.
     * See
     * @param G
     * @param c
     * @return
     */
    public RCGraph getBestMaxR_Cconstraint(RCGraph G, double c) {
        RCGraph copyG1, copyG2;
        copyG1 = maximumReliability_CostConstraint(G, c);
        copyG2 = maximumReliability_CostConstraint1(G, c);

        double c1, c2, r1, r2;
        r1 = computeNetworkReliability(copyG1);
        r2 = computeNetworkReliability(copyG2);
        c1 = computeCost(copyG1);
        c2 = computeCost(copyG2);

        Tools.print("R1=" + r1 + "\nC1=" + c1);
        Tools.print("R2=" + r2 + "\nC2=" + c2);

        List<Double> rL = new ArrayList<>();
        rL.add(r1);
        rL.add(r2);
        rL.sort((v1, v2) -> {
            return (v1.compareTo(v2));
        });

        List<Double> cL = new ArrayList<>();
        cL.add(c1);
        cL.add(c2);
        cL.sort((v1, v2) -> {
            return (v1.compareTo(v2));
        });

        double cVar, rVar, avgC, avgR;
        cVar = cL.get(1) - cL.get(0);
        rVar = rL.get(1) - rL.get(0);
        avgC = (cL.get(1) + cL.get(0)) / 2;
        avgR = (rL.get(1) + rL.get(0)) / 2;
        Tools.print("RVAR = " + rVar + "; CVAR = " + cVar);
        Tools.print("AVGR = " + avgR + "; AVGC= " + avgC);
        double[] win = new double[2];
        for (int i = 0; i < 2; i++) {
            if (i == 0)
                win[i] = r1 / c1;
            else if (i == 1)
                win[i] = r2 / c2;
        }
        double max = 0;
        int winner = 0;
        for (int i = 0; i < 2; i++) {
            if (win[i] > max) {
                max = win[i];
                winner = i;
            }
        }
        Tools.print("scores");
        for (double d : win) {
            System.out.print(d + ",");
        }
        Tools.print("");
        switch (winner) {
            case 0:
                return copyG1;
            case 1:
                return copyG2;
            default:
                return copyG1;
        }
    }

    /**
     * generates a maximal reliability to ratio spanning tree for input graph
     * @param G
     */
    public void build_maxR2CSpanningTree(RCGraph G) {
        sortEdges(G, false);
        spanningTree(G);
    }

    /**
     * generates a minimal cost spanning tree for input graph
     * @param G
     */
    public void build_minCostSpanningTree(RCGraph G) {
        sortEdges(G, true);
        spanningTree(G);
    }

    /**
     * generates a spanning tree for a graph G.
     * Uses the static list sorted_edges (from Computation class) as the ordering for addition of edges.
     * based of Kruskal's algorithm for MST.
     *
     * @param G
     */
    private void spanningTree(RCGraph G) {
        int index = 0;
        while (!GraphTools.checkConnectedG(G) && index < G.getTotal_entries()) {
            int i = sorted_edges.get(index).getV1().getTag();
            int j = sorted_edges.get(index).getV2().getTag();
            int ij = GraphTools.matrixToArrayIndex(i, j, G.getN());
            G.getAdjacencyMatrix()[ij] = 1;
            if (GraphTools.findCycleEdge(G) != -1) {
                G.getAdjacencyMatrix()[ij] = 0;
            }
            index++;
        }
    }

    /**
     * computes the total cost of the input graph
     * @param G
     * @return
     */
    public double computeCost(RCGraph G) {
        double c = 0;
        for (int i = 0; i < G.getTotal_entries(); i++) {
            int adj = G.getAdjacencyMatrix()[i];
            RCEdge e = (RCEdge) G.getE()[i];
            c += adj * e.getCost();
        }
        return c;
    }

    /**
     * Returns reliability of a graph. Uses helper methods to find reliability of the graph.
     * @param G
     * @return
     */
    public double computeNetworkReliability(RCGraph G) {
        counter = 0;
        RCGraph copyG = new RCGraph(G);
        simpleReduce(copyG);
        if (!GraphTools.checkConnectedG(copyG))
            return 0;
        return computeRbyDecomposition(copyG);
    }

    /**
     * sorts edges according to their Cost or Reliability to Cost ratio (boolean optimal selects the sorting strategy).
     * @param G       graph the edges of which are to be sorted
     * @param optimal false for R/C, true for cost sorting
     * @return
     */
    private List<Edge> sortEdges(RCGraph G, boolean optimal) {
        // sort edges
        sorted_edges = new ArrayList<Edge>();
        for (int i = 0; i < G.getTotal_entries(); i++) {
            sorted_edges.add(new RCEdge((RCEdge) G.getE()[i]));
        }
        sorted_edges.sort((e1, e2) -> {
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
            Collections.reverse(sorted_edges);
        return sorted_edges;
    }

    /**
     * recursive method to compute reliablity of a graph.
     * the graph is decomposed using the decomposition property: R(G) = (1-Rc) * R(G-c) + Rc * R(G/c)
     * where R(G-c) represents a graph where the edge c is removed,
     * and R(G/c) represents a graph where the edge c is merged,
     * and Rc represents the reliability of the edge c.
     * (see Peter Tittman, "Network Reliabilty", 2007 for theory on graph decomposition)
     * Note: this function gets slow for n > 20
     * Assumes that the graph only has at most 1 edge between any two given cities
     * (assumes that the simpleReduce() method was run on the graph prior to calling this).
     * @param G
     * @return
     */
    private double computeRbyDecomposition(RCGraph G) {
        // update static class counter for usage statistics
        counter++;
        int ijCycle = GraphTools.findCycleEdge(G);
        // if an edge was found
        if (ijCycle != -1) {
            // decompose G:
            // create a copy of the graph
            RCGraph G1 = new RCGraph(G);
            RCGraph G2 = new RCGraph(G);
            // make R(G/c)
            GraphTools.mergeEdge(G1, ijCycle);
            // make R(G-c)
            GraphTools.removeEdge(G2, ijCycle);
            // cimpute graph weights Rc and 1-Rc
            double rG1 = ((RCEdge) G1.getE()[ijCycle]).getReliability();
            // recursive decomposition
            return rG1 * computeRbyDecomposition(G1) +
                    (1 - rG1) * computeRbyDecomposition(G2);
        } else {
            // if no edges just compute Reliability as Product series of all edges
            return computeR(G);
        }
    }

    /**
     * computes Reliability of a simple graph where no loops are present
     * Assumes that no loops are present in the input graph.
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

    /**
     * peroforms simple reduction on graph (multiple parallel edges between two cities are merged into one)
     * adjacency matrix and edge reliabilities are updated according to the reduction results.
     * @param G
     */
    private void simpleReduce(RCGraph G) {
        for (int ij = 0; ij < G.getTotal_entries(); ij++) {
            if (G.getAdjacencyMatrix()[ij] <= 1)
                continue;
            RCEdge e = (RCEdge) (G.getE()[ij]);
            double nR = e.getReliability();
            while (G.getAdjacencyMatrix()[ij] > 1) {
                nR = rParalel(nR, e.getReliability());
                G.getAdjacencyMatrix()[ij]--;
            }
            e.setReliability(nR);
        }
    }

    /**
     * returns the reliability value of two edges in series
     * @param r1
     * @param r2
     * @return
     */
    public static double rSeries(double r1, double r2) {
        return r1 * r2;
    }

    /**
     * returns the reliability value of two edges in parallel
     * @param r1
     * @param r2
     * @return
     */
    public static double rParalel(double r1, double r2) {
        return r1 + r2 - rSeries(r1, r2);
    }
}
