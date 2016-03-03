package Tools;

import Graphs.Graph;
import Graphs.NetworkGraph.RCEdge;
import Graphs.NetworkGraph.RCGraph;
import Graphs.Vertex;

import javax.tools.Tool;
//import Graphs.NetworkGraph.RCGraph;

/**
 * Created by Andrei-ch on 2016-02-29.
 */
public class TestTools {
    public static void test() {
        View V;
        RCGraph G;

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
        G.initEdgeValues(0.5, 0.5);
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(0, 1, 4)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(1, 3, 4)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(1, 2, 4)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(2, 3, 4)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(0, 2, 4)] = 1;
        Tools.print("Computing Network R: " + Computation.computeNetworkReliability(G));

        Tools.print("***********************");
        G = new RCGraph(4);
        G.initEdgeValues(0.5, 0.5);
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(0, 1, 4)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(1, 2, 4)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(2, 3, 4)] = 1;
        Tools.print("Computing Network R: " + Computation.computeNetworkReliability(G));

        Tools.print("***********************");
        G = new RCGraph(4);
        G.initEdgeValues(0.5, 0.5);
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(3, 0, 4)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(3, 1, 4)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(3, 2, 4)] = 1;
        Tools.print("Computing Network R: " + Computation.computeNetworkReliability(G));

        Tools.print("***********************");
        G = new RCGraph(6);
        G.initEdgeValues(0.5, 0.5);
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(0, 1, 6)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(1, 2, 6)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(1, 4, 6)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(3, 1, 6)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(2, 3, 6)] = 1;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(4, 5, 6)] = 1;
        Tools.print("Computing Network R: " + Computation.computeNetworkReliability(G));

        Tools.print("***********************");
        G = new RCGraph(3);
        G.initEdgeValues(0.5, 0.5);
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(0, 1, 3)] = 3;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(1, 2, 3)] = 3;
        G.getAdjacencyMatrix()[GraphTools.matrixToArrayIndex(2, 0, 3)] = 3;
        Tools.print("Computing Network R: " + Computation.computeNetworkReliability(G));

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

        Computation.minimum_cost_spanning_tree(G, false);
        V = new View(G);
        Tools.print("R = " + Computation.computeNetworkReliability(G) + ", C = " + Computation.computeC(G));

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

        Computation.minimum_cost_spanning_tree(G, true);
        V = new View(G);
        Tools.print("R = " + Computation.computeNetworkReliability(G) + ", C = " + Computation.computeC(G));
    }
}
