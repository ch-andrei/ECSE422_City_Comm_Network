import CommunicationNetwork.Computation;
import Graphs.NetworkGraph.RCGraph;
import Toolset.GraphTools;
import Toolset.TestTools;
import Toolset.Tools;
import Toolset.View;

/**
 * Created by Andrei-ch on 2016-02-27.
 */
public class Driver {
    public static void main(String[] args) {

        boolean debug = false;
        if (debug) {
            TestTools.test();
        } else {
            // declare variables
            int[] a_b = new int[1];
            double[] desired_R_C = new double[2];
            Computation C;
            RCGraph G;
            View V;

            // build graph
            G = Toolset.Tools.buildGraphFromFile("prj1_input.txt", a_b, desired_R_C);
            C = new Computation();

            Tools.print("CONFIGURATION:");
            Tools.print("Mode: " + a_b[0] + ";\nIf Mode 0, desired Reliability = " + desired_R_C[0] + ";\nIf Mode 1, desired Cost = " + desired_R_C[1]);
            Tools.print("");
            Tools.print("COMPUTED RESULTS:");

            // compute results
            if (a_b[0] == 1) {
                G = C.getBestMinC_Rconstraint(G, desired_R_C[0]);
            } else {
                G = C.getBestMaxR_Cconstraint(G, desired_R_C[1]);
            }

            Tools.print("\nPicked Best Graph with: R = " + C.computeNetworkReliability(G) + ", C = " + C.computeCost(G));

            GraphTools.printPrettyAdjList(G);
            V = new View(G);
        }
    }
}
