package Leetcode;

import Datastructures.TreeNode;

/**
 * Created by Naren on 5/23/17.
 */
public class InvertTree {

  private TreeNode invertTree(TreeNode root) {
    if (root == null)
      return null;
    TreeNode tmp = root.left;
    root.left = invertTree(root.right);
    root.right = invertTree(tmp);
    return root;
  }

}
