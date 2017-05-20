package Datastructures;

/**
 * Created by Naren on 5/19/17.
 */
public class BinarySearchTree implements BST {

    private TreeNode root;

    @Override
    public void add(int val) {
        root = add(val, root);
    }

    private TreeNode add(int val, TreeNode node) {
        if(node == null) {
            node = new TreeNode(val);
            return node;
        }
        if(node.val > val)
            node.left = add(val, node.left);
        else if(node.val < val)
            node.right = add(val, node.right);
        return node;
    }

    @Override
    public TreeNode search(int val, TreeNode node) {
        if(node == null || node.val == val) {
            return node;
        }

        if(val < node.val)
            return search(val, root.left);

        return search(val, root.right);
    }

    @Override
    public int minVal(TreeNode r) {
        while (r.left != null) {
            r = r.left;
        }

        return r.val;
    }

    @Override
    public void preOrder(TreeNode root) {
        if(root == null) {
            return;
        }
        else {
            System.out.println(root.val);
            preOrder(root.left);
            preOrder(root.right);
        }
    }

    @Override
    public void inOrder(TreeNode root) {
        if(root == null) {
            return;
        }
        else {
            inOrder(root.left);
            System.out.println(root.val);
            inOrder(root.right);
        }
    }

    @Override
    public void postOrder(TreeNode root) {
        if(root == null) {
            return;
        }
        else {
            postOrder(root.left);
            postOrder(root.right);
            System.out.println(root.val);
        }
    }

    @Override
    public void del(int val) {
        root = del(root, val);
    }


    private TreeNode del(TreeNode root, int val) {
        // If tree is empty, return null.
        if (root == null) return null;

        // Else, recursively traverse the tree.
        if (val < root.val)
            root.left = del(root.left, val);
        else if (val > root.val)
            root.right = del(root.right, val);
        else {
            // If node has only one child or no children.
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;

            // If node has two children, replace current node's value with the
            // minimum value in the node's right subtree.
            root.val = minVal(root.right);

            // Delete the inorder successor.
            root.right = del(root.right, root.val);
        }
        return root;
    }

    public static void main(String[] args) {
        BinarySearchTree bst = new BinarySearchTree();
        bst.add(10);
        bst.add(5);
        bst.add(15);
        bst.add(16);
        bst.add(13);
        bst.add(152);
        System.out.println(bst.search(5, bst.root));
        bst.preOrder(bst.root);
        bst.inOrder(bst.root);
        bst.postOrder(bst.root);
    }
}

interface BST {

    void add(int val);

    TreeNode search(int val, TreeNode node);

    int minVal(TreeNode r);

    void del(int val);

    void preOrder(TreeNode root);

    void inOrder(TreeNode root);

    void postOrder(TreeNode root);

}
