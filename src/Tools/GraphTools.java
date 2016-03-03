package Tools;

import Graphs.Edge;
import Graphs.Graph;
import Graphs.NavigationGraph.NavGraph;
import Graphs.NetworkGraph.RCEdge;
import Graphs.NetworkGraph.RCGraph;

import javax.tools.Tool;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Andrei-ch on 2016-02-28.
 */
public class GraphTools {

    /**
     * convert i,j indexes of an nxn matrix to a single array index.
     * NOTE: Matrix must be symmetrical (Mij = Mji), and must have 0s on the diagonal entries.
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
     * @param G
     * @param edgeIndex
     * @return
     */
    public static void mergeEdge(Graph G, int edgeIndex) {
        // TODO FINISH

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
     * @param G
     * @param edgeIndex
     * @return
     */
    public static void removeEdge(Graph G, int edgeIndex) {
        G.getAdjacencyMatrix()[edgeIndex] = 0;
    }

    /**
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
     * @param G
     * @return
     */
    public static boolean checkConnectedG(Graph G) {
        NavGraph nG = new NavGraph(G);
        Queue<Integer> queue = new LinkedList<Integer>();
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

    public static RCGraph buildGraphFromFile(String filename, Integer[] a_b, double[] RC) {
        String constraints = Tools.readConstrains("./resources/" + filename);
        if (constraints.isEmpty())
            return null;

        String[] vals = null;
        try {
            vals = constraints.split("\\s+");
        } catch (PatternSyntaxException ex) {
            ex.printStackTrace();
        }

        double[] d_vals = null;
        if (vals != null) {
            d_vals = new double[vals.length];
            for (int i = 0; i < vals.length; i++) {
                d_vals[i] = Double.parseDouble(vals[i]);
            }
        }

        int index = (int) (d_vals[0] + (d_vals[0] * (d_vals[0] - 1) / 2));
        if (a_b != null)
            a_b[0] = Integer.parseInt(vals[index]);
        if (RC != null) {
            RC[0] = Integer.parseInt(vals[index + 1]);
            RC[1] = Integer.parseInt(vals[index + 2]);
        }
        return buildRCGraphFromString(d_vals);
    }

    private static RCGraph buildRCGraphFromString(double[] d_vals) {
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
            for (int i = 1; i < total_entries; i++) {
                costs[i] = d_vals[i];
                reliabilities[i] = d_vals[i + total_entries];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }

        return new RCGraph(N, costs, reliabilities);
    }

    public static double rSeries(double r1, double r2) {
        return r1 * r2;
    }

    public static double rParalel(double r1, double r2) {
        return r1 + r2 - rSeries(r1, r2);
    }
}
