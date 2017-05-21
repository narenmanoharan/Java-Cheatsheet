package Datastructures;

/**
 * Created by Naren on 5/21/17.
 */
public class Node {
  private Object data;
  private Node left;
  private Node right;

  Node(Object item, Node left, Node right){
    this.data = item;
    this.left = left;
    this.right = right;
  }

  public Object getData() {
    return data;
  }

  public Node getLeft() {
    return left;
  }

  public Node getRight() {
    return right;
  }
}

