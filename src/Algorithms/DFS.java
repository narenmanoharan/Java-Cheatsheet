package Algorithms;

import Datastructures.Node;
import java.util.Stack;

/**
 * Created by Naren on 5/21/17.
 */
public class DFS {

  void preOrderDFS(Node root) {

    if(root == null)
      return;

    Stack<Node> stack = new Stack<>();
    stack.push(root);

    while(!stack.isEmpty()){
      Node node = stack.pop();

      // Printing the traversal
      System.out.println(node.getData());

      Node left = node.getLeft();
      Node right = node.getRight();

      if(right != null){
        stack.push(right);
      }

      if(left != null){
        stack.push(left);
      }
    }
  }

}
