package Leetcode;

import Datastructures.TreeNode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Naren on 5/21/17.
 */
public class BinaryTreeLevelOrder {

  public List<List<Integer>> levelOrder(TreeNode root) {

    List<List<Integer>> result = new ArrayList<>();

    if(root == null)
      return result;

    Queue<TreeNode> queue = new LinkedList<>();
    queue.add(root);

    while(!queue.isEmpty()) {
      Queue<TreeNode> currentLevel = new LinkedList<>();
      List<Integer> list = new ArrayList<>();

      while(!queue.isEmpty()) {
        TreeNode current = queue.remove();

        if(current.left != null) {
          currentLevel.add(current.left);
          list.add(current.left.val);
        }

        if(current.right != null) {
          currentLevel.add(current.right);
          list.add(current.right.val);
        }
      }

      if(list.size() > 0) {
        result.add(list);
      }
      queue = currentLevel;
    }
    return result;
  }
}
