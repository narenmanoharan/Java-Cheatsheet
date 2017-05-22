package Datastructures;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Naren on 5/19/17.
 */
public class Graph
{
    private int V;   // No. of vertices
    private LinkedList<Integer>[] adj; //Adjacency Lists

    // Constructor
    Graph(int v)
    {
        V = v;
        adj = new LinkedList[v];
        for (int i=0; i<v; ++i)
            adj[i] = new LinkedList<>();
    }

    // Function to add an edge into the graph
    private void addEdge(int v, int w)
    {
        adj[v].offer(w);
    }


    private void BFS(int v) {

        boolean visited[] = new boolean[v];

        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(v);
        while (queue.size() != 0) {
            int s = queue.poll();
            System.out.println(s + " ->");

            for (Integer i : queue) {
                if (!visited[i]) {
                    visited[i] = true;
                    queue.offer(i);
                }
            }

        }

    }


    public static void main(String[] args) {

        int[][] adjMatrix = new int[][]{{1, 1, 0, 0, 0},
                                        {0, 1, 0, 0, 1},
                                        {1, 0, 0, 1, 1},
                                        {0, 0, 0, 0, 0},
                                        {1, 0, 1, 0, 1}};

        Graph g = new Graph(4);

        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        g.addEdge(2, 3);
        g.addEdge(3, 3);

        g.BFS(2);

    }

}
