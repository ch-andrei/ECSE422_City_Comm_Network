package Toolset;

import CommunicationNetwork.Computation;
import Graphs.Graph;
import Graphs.NavigationGraph.NavGraph;
import Graphs.NetworkGraph.RCEdge;
import Graphs.NetworkGraph.RCGraph;


import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Andrei-ch on 2016-02-28.
 */
public class GraphTools {

    /**
     * convert i,j indexes of an nxn matrix to an ij array index (sparse matrix representation of a symmetric graph).
     * NOTE: Graph matrix must be symmetrical (Mij = Mji), and must have 0s on the diagonal entries.
     *
     * @param j
     * @param i
     * @param n
     * @return
     */
    public static int matrixToArrayIndex(int i, int j, int n) {
        int index = 0;
        if (j >= n || i >= n) return -1;
        if (j > i) { // swap if needed (since Mij = M ji in the original matrix)
            int tmp = j;
            j = i;
            i = tmp;
        }
        for (int ind = 0; ind < j; ind++) {
            index += (n - 2 - ind);
        }
        index += i - 1;
        return index;
    }

    /**
     * converts an ij index (sparse matrix representation of a symmetric graph) to i,j array indices.
     * NOTE: Graph matrix must be symmetrical (Mij = Mji), and must have 0s on the diagonal entries.
     * @param index
     * @param n
     * @return
     */
    public static int[] arrayToMatrixIndex(int index, int n) {
        int max = n * (n - 1) / 2 - 1;
        if (index > max) return null;

        int i, j;
        i = j = 0;
        int row_entries = n - 1;
        while (index + 1 - row_entries > 0) {
            index -= row_entries;
            row_entries--;
            j++;
        }
        i = index + j + 1;
        return new int[]{i, j};
    }

    /**
     * merges a given edge within a graph to its neigbhors.
     * @param G
     * @param edgeIndex
     * @return
     */
    public static void mergeEdge(Graph G, int edgeIndex) {
        // move edges from v1 to v2 by disconnecting v1, and connecting v2 to neighbors of v1
        int v1 = G.getE()[edgeIndex].getV1().getTag(),
                v2 = G.getE()[edgeIndex].getV2().getTag();
        // merge to v2
        for (int index = 0; index < G.getN(); index++) {
            if (v1 == index)
                continue;
            int ij1 = GraphTools.matrixToArrayIndex(v1, index, G.getN());
            int ij2 = GraphTools.matrixToArrayIndex(v2, index, G.getN());
            if (G.getAdjacencyMatrix()[ij1] != 0) {
                if (v2 == index)
                    continue;
                if (G.getAdjacencyMatrix()[ij2] != 0) {
                    RCEdge e = (RCEdge) G.getE()[ij2];
                    double newR = rParalel(e.getReliability(), ((RCEdge) G.getE()[edgeIndex]).getReliability());
                    e.setReliability(newR);
                }
            }
        }
        // disconnect v1
        for (int i = 0; i < G.getN(); i++) {
            if (i == v1)
                continue;
            int ij = GraphTools.matrixToArrayIndex(v1, i, G.getN());
            G.getAdjacencyMatrix()[ij] = 0;
        }
    }

    /**
     * Sets adjacency matrix element at index ij to 0 (disconnects the edge)
     * @param G
     * @param ij
     * @return
     */
    public static void removeEdge(Graph G, int ij) {
        G.getAdjacencyMatrix()[ij] = 0;
    }

    /**
     * uses breadth first traversal on the input graph to find a loop.
     * the ij index of the edge contained in the first discovered loop is returned.
     * @param G
     * @return
     */
    public static int findCycleEdge(Graph G) {
        int start = 0;
        while (start < G.getN()) {
            NavGraph nG = new NavGraph(G);
            Queue<Integer[]> queue = new LinkedList<Integer[]>();
            nG.getVisited()[start] = true;
            queue.add(new Integer[]{start, -1});
            Integer[] crt;
            while (!queue.isEmpty()) {
                crt = queue.remove();
                for (int next = 0; next < nG.getN(); next++) {
                    if (crt[0] == next || crt[1] == next)
                        continue;
                    int ij = matrixToArrayIndex(crt[0], next, nG.getN());
                    if (nG.getAdjacencyMatrix()[ij] != 0) {
                        if (nG.getVisited()[next]) {
                            // found a cycle
                            return ij;
                        } else if (!nG.getVisited()[next]) {
                            nG.getVisited()[next] = true;
                            queue.add(new Integer[]{next, crt[0]});
                        }
                    }
                }
            }
            start++;
        }
        return -1;
    }

    /**
     * Runs Breadth first traversal to find if a graph is connected
     * @param G
     * @return
     */
    public static boolean checkConnectedG(Graph G) {
        NavGraph nG = new NavGraph(G);
        Queue<Integer> queue = new LinkedList<>();
        nG.getVisited()[0] = true;
        queue.add(0);
        while (!queue.isEmpty()) {
            int crt = queue.remove();
            for (int next = 0; next < nG.getN(); next++) {
                if (crt == next)
                    continue;
                int ij = matrixToArrayIndex(crt, next, nG.getN());
                if (nG.getAdjacencyMatrix()[ij] != 0) {
                    if (!nG.getVisited()[next]) {
                        nG.getVisited()[next] = true;
                        queue.add(next);
                    }
                }
            }
        }
        for (boolean visited : nG.getVisited()) {
            if (!visited) return false;
        }
        return true;
    }

    /**
     * build a new RCGraph from an input array of doubles of the form:
     * N, N(N-1)/2 C entries, N(N-1)/2 R entries
     *
     * @param d_vals
     * @return
     */
    public static RCGraph buildRCGraphFromDoubleArray(double[] d_vals) {
        // split constraints
        if (d_vals == null)
            return null;
        int N, total_entries;
        double[] costs, reliabilities;
        N = (int) d_vals[0];
        total_entries = N * (N - 1) / 2;
        costs = new double[total_entries];
        reliabilities = new double[total_entries];
        try {
            for (int i = 1; i <= total_entries; i++) {
                costs[i-1] = d_vals[i];
                reliabilities[i-1] = d_vals[i + total_entries];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
        return new RCGraph(N, costs, reliabilities);
    }

    /**
     * prints graph adjacency matrix
     * @param G
     */
    public static void printPrettyAdjMatrix(Graph G) {
        Tools.print("Printing Adjacency Matrix:");
        for (int i = 0; i < G.getN(); i++) {
            for (int j = 0; j < G.getN(); j++) {
                String str;
                if (i == j) str = "0";
                else {
                    int ij = matrixToArrayIndex(i, j, G.getN());
                    str = "" + G.getAdjacencyMatrix()[ij];
                }
                System.out.print(str);
            }
            Tools.print("");
        }
    }

    /**
     * prints graph adjacency list
     * @param G
     */
    public static void printPrettyAdjList(Graph G) {
        Tools.print("Printing Adjacency List:");
        for (int i = 0; i < G.getTotal_entries(); i++) {
            System.out.print(G.getAdjacencyMatrix()[i] + ",");
        }
        Tools.print("");
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
