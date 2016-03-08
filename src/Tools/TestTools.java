package Tools;

import CommunicationNetwork.ComputeUnit;
import Graphs.Graph;
import Graphs.NetworkGraph.RCEdge;
import Graphs.NetworkGraph.RCGraph;
import CommunicationNetwork.Computation;

import java.util.*;

/**
 * Created by Andrei-ch on 2016-02-29.
 */
public class TestTools {

    public static volatile boolean d1, d2, d3;
    public static volatile RCGraph G, GG, GGG;

    public static void test() {
        View V;

        // G5 TEST
        String g5 = "Vertices:\n" +
                "[0]: tag 0;\n" +
                "[1]: tag 1;\n" +
                "[2]: tag 2;\n" +
                "[3]: tag 3;\n" +
                "[4]: tag 4;\n" +
                "\n" +
                "Edges:\n";
        if (g5.compareTo((new Graph(5)).toString()) != 0)
            Tools.print("Failed G5 test.");

        // GRAPH COPY TEST
        Graph G1 = new RCGraph(3);
        Graph G2 = new RCGraph(G1);
        ((RCEdge) G1.getE()[0]).setCost(10);
        ((RCEdge) G2.getE()[0]).setCost(15);
        if (G1 == G2 || G1.equals(G2) || G1.getE() == G2.getE()) Tools.print("Failed graph copy test.");

        // INDEX TESTS
        int i = 4, j = 3, n = 5;
        // INDEX TEST1
        int index = GraphTools.matrixToArrayIndex(i, j, n);
        if (index != 9)
            Tools.print("Failed index test1.");
        // INDEX TEST2
        index = GraphTools.matrixToArrayIndex(i, j, n);
        if (index != 9)
            Tools.print("Failed index test2.");
        // INDEX TEST3
        int[] ij = GraphTools.arrayToMatrixIndex(9, 5);
        if (ij[0] != 4 && ij[1] != 3)
            Tools.print("Failed index test3.");
        // INDEX TEST4
        ij = GraphTools.arrayToMatrixIndex(3, 5);
        if (ij[0] != 4 && ij[1] != 0)
            Tools.print("Failed index test4.");

        // CONNECTION TEST
        G = new RCGraph(5);
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(0, 1, 5)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(1, 2, 5)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(2, 3, 5)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(3, 1, 5)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(3, 4, 5)] = 1;
        if (!GraphTools.checkConnectedG(G))
            Tools.print("Failed connection test.");
        // CYCLE TEST
        if (GraphTools.findCycleEdge(G) == -1)
            Tools.print("Failed cycle test.");

        // CONNECTION TEST 2
        G = new RCGraph(5);
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(0, 1, 5)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(1, 2, 5)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(2, 3, 5)] = 1;
        if (GraphTools.checkConnectedG(G))
            Tools.print("Failed connection test2.");

        Tools.print("***********************");
        // RELIABLITY COMPUTATION TESTS
        G = new RCGraph(4);
        G.init_edge_values(0.5, 0.5);
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(0, 1, 4)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(1, 3, 4)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(1, 2, 4)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(2, 3, 4)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(0, 2, 4)] = 1;
//        Tools.print("Computing Network R: " + Computation.computeNetworkReliability(G));

        Tools.print("***********************");
        G = new RCGraph(4);
        G.init_edge_values(0.5, 0.5);
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(0, 1, 4)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(1, 2, 4)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(2, 3, 4)] = 1;
//        Tools.print("Computing Network R: " + Computation.computeNetworkReliability(G));

        Tools.print("***********************");
        G = new RCGraph(4);
        G.init_edge_values(0.5, 0.5);
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(3, 0, 4)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(3, 1, 4)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(3, 2, 4)] = 1;
//        Tools.print("Computing Network R: " + Computation.computeNetworkReliability(G));

        Tools.print("***********************");
        G = new RCGraph(6);
        G.init_edge_values(0.5, 0.5);
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(0, 1, 6)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(1, 2, 6)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(1, 4, 6)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(3, 1, 6)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(2, 3, 6)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(4, 5, 6)] = 1;
//        Tools.print("Computing Network R: " + Computation.computeNetworkReliability(G));

        Tools.print("***********************");
        G = new RCGraph(3);
        G.init_edge_values(0.5, 0.5);
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(0, 1, 3)] = 3;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(1, 2, 3)] = 3;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(2, 0, 3)] = 3;
//        Tools.print("Computing Network R: " + Computation.computeNetworkReliability(G));

        G = GraphTools.buildGraphFromFile("graph.txt", null, null);
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(0, 1, 3)] = 3;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(1, 2, 3)] = 3;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(2, 0, 3)] = 3;

        Tools.print("***********************");
        G = new RCGraph(4);
        ((RCEdge) G.getE()[GraphTools.matrixToArrayIndex(0, 1, 4)]).setCost(2);
        ((RCEdge) G.getE()[GraphTools.matrixToArrayIndex(0, 2, 4)]).setCost(5);
        ((RCEdge) G.getE()[GraphTools.matrixToArrayIndex(0, 3, 4)]).setCost(10);
        ((RCEdge) G.getE()[GraphTools.matrixToArrayIndex(1, 2, 4)]).setCost(10);
        ((RCEdge) G.getE()[GraphTools.matrixToArrayIndex(1, 3, 4)]).setCost(0);
        ((RCEdge) G.getE()[GraphTools.matrixToArrayIndex(2, 3, 4)]).setCost(1);

        ((RCEdge) G.getE()[GraphTools.matrixToArrayIndex(0, 1, 4)]).setReliability(0.2);
        ((RCEdge) G.getE()[GraphTools.matrixToArrayIndex(0, 2, 4)]).setReliability(0);
        ((RCEdge) G.getE()[GraphTools.matrixToArrayIndex(0, 3, 4)]).setReliability(0.8);
        ((RCEdge) G.getE()[GraphTools.matrixToArrayIndex(1, 2, 4)]).setReliability(0.8);
        ((RCEdge) G.getE()[GraphTools.matrixToArrayIndex(1, 3, 4)]).setReliability(0.1);
        ((RCEdge) G.getE()[GraphTools.matrixToArrayIndex(2, 3, 4)]).setReliability(0.05);

//        G = Computation.minimalCost_ReliabilityConstraint1(G, 0.3);
//        Tools.print("R = " + Computation.computeNetworkReliability(G) + ", C = " + Computation.computeCost(G) + "\n" + G);

        Integer[] winR = new Integer[3], winC = new Integer[3];
        winR[0] = new Integer(0);
        winR[1] = new Integer(0);
        winR[2] = new Integer(0);

        winC[0] = new Integer(0);
        winC[1] = new Integer(0);
        winC[2] = new Integer(0);

        long time = System.currentTimeMillis(), time1, time2 = time;
        int tests = 500, total = tests;
        while (tests > 0) {
            n = 20;
            G = new RCGraph(n);
            Random random = new Random(System.currentTimeMillis());
            for (int ii = 0; ii < G.getN(); ii++) {
                for (int jj = 0; jj < G.getN(); jj++) {
                    if (ii == jj)
                        continue;
                    ((RCEdge) G.getE()[GraphTools.matrixToArrayIndex(ii, jj, G.getN())]).setCost(1 + random.nextInt(99));
                    ((RCEdge) G.getE()[GraphTools.matrixToArrayIndex(ii, jj, G.getN())]).setReliability(0.8 + 0.2 * random.nextDouble());
                }
            }
            GG = new RCGraph(G);
            GGG = new RCGraph(G);

            d1 = d2 = d3 = false;

            Computation Comput = new Computation();
            G = Comput.minimalCost_ReliabilityConstraint(G, 0.8);
            GG = Comput.minimalCost_ReliabilityConstraint1(GG, 0.8);
            GGG = Comput.minimalCost_ReliabilityConstraint2(GGG, 0.8);

            time1 = System.currentTimeMillis();

            Tools.print("iteration " + (total - tests) + "; time = " + (time1 - time2) + ", total " + (time1-time));

            double c1, c2, c3, r1, r2, r3;
            r1 = Comput.computeNetworkReliability(G);
            r2 = Comput.computeNetworkReliability(GG);
            r3 = Comput.computeNetworkReliability(GGG);
            c1 = Comput.computeCost(G);
            c2 = Comput.computeCost(GG);
            c3 = Comput.computeCost(GGG);

            List<Double> r = new ArrayList<>();
            r.add(r1);
            r.add(r2);
            r.add(r3);
            r.sort((v1, v2) -> {
                return (v1.compareTo(v2));
            });

            List<Double> c = new ArrayList<>();
            c.add(c1);
            c.add(c2);
            c.add(c3);
            c.sort((v1, v2) -> {
                return (v1.compareTo(v2));
            });

            if (r1 == r.get(2))
                winR[0]++;
            if (r2 == r.get(2))
                winR[1]++;
            if (r3 == r.get(2))
                winR[2]++;

            if (c1 == c.get(0))
                winC[0]++;
            if (c2 == c.get(0))
                winC[1]++;
            if (c3 == c.get(0))
                winC[2]++;

            tests--;
            time2 = time1;
        }

        Tools.print("WINR " + Arrays.asList(winR));
        Tools.print("WINC " + Arrays.asList(winC));


//
//        G = Computation.minimalCost_ReliabilityConstraint(G, 0.5);
//        Tools.print("MIN COST: R = " + Computation.computeNetworkReliability(G) + ", C = " + Computation.computeCost(G));
//
//        GG = Computation.minimalCost_ReliabilityConstraint1(GG, 0.5);
//        Tools.print("MAX R2Cr: R = " + Computation.computeNetworkReliability(GG) + ", C = " + Computation.computeCost(GG));
//
//        GGG = Computation.minimalCost_ReliabilityConstraint2(GGG, 0.5);
//        Tools.print("minC+R2C: R = " + Computation.computeNetworkReliability(GGG) + ", C = " + Computation.computeCost(GGG));
//        View v2 = new View(GG);
    }
}
