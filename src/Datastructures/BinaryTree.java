package Datastructures;

/**
 * Created by Naren on 5/19/17.
 */
public class BinaryTree {

    private TreeNode root;

    public static void main(String[] args) {

        BinaryTree binaryTree = new BinaryTree();
        TreeNode root = new TreeNode(10);
        binaryTree.root = root;
        TreeNode left_1 = new TreeNode(5);
        TreeNode right_1 = new TreeNode(5);
        root.left = left_1;
        root.right = right_1;

    }
}
