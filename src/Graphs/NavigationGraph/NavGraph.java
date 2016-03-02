package Graphs.NavigationGraph;

import Graphs.Edge;
import Graphs.Graph;

/**
 * Created by Andrei-ch on 2016-02-29.
 */
public class NavGraph extends Graph{

    private boolean[] visited;

    public NavGraph(Graph G){
        super(G);
        visited = new boolean[G.getN()];
    }

    public boolean[] getVisited() {
        return visited;
    }

    public void setVisited(boolean[] visited) {
        this.visited = visited;
    }
}
