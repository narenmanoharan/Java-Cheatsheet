package Algorithms;

import Datastructures.Node;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Naren on 5/21/17.
 */
public class BFS {

  void bfs(Node root) {

    Queue<Node> queue = new LinkedList<>();
    queue.offer(root);

    while(!queue.isEmpty()){
      Node node = queue.poll();

      // Printing the traversal
      System.out.println(node.getData());

      if(node.getLeft() != null){
        queue.offer(node.getLeft());
      }

      if(node.getRight() != null){
        queue.offer(node.getRight());
      }
    }
  }

}
