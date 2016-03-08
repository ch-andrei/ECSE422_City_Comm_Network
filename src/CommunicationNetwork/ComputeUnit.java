package CommunicationNetwork;

import Graphs.Graph;
import Graphs.NetworkGraph.RCGraph;
import Tools.TestTools;
import Tools.Tools;

/**
 * Created by Andrei-ch on 2016-03-04.
 */
public class ComputeUnit {
    public ComputeUnit(RCGraph G, double r, int s) {
        Computation c = new Computation();
        if (s == 0) {
            TestTools.G = c.minimalCost_ReliabilityConstraint(G, r);
            TestTools.d1 = true;
        } else if (s == 1) {
            TestTools.GG = c.minimalCost_ReliabilityConstraint1(G, r);
            TestTools.d2 = true;
        } else if (s == 2) {
            TestTools.GGG = c.minimalCost_ReliabilityConstraint2(G, r);
            TestTools.d3 = true;
        }
        c = null;
    }
}

