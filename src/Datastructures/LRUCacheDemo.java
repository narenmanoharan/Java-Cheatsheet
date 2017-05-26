package Datastructures;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Naren on 5/22/17.
 */
public class LRUCacheDemo {

  private class Node {
    int key, value;
    Node prev, next;
    Node(int k, int v){
      this.key = k;
      this.value = v;
    }
    Node(){
      this(0, 0);
    }
  }

  private int capacity, count;
  private Map<Integer, Node> map;
  private Node head, tail;

  public LRUCacheDemo(int capacity) {
    this.capacity = capacity;
    this.count = 0;
    map = new HashMap<>();
    head = new Node();
    tail = new Node();
    head.next = tail;
    tail.prev = head;
  }

  public int get(int key) {
    Node n = map.get(key);
    if(n == null){
      return -1;
    }
    update(n);
    return n.value;
  }

  public void set(int key, int value) {
    Node n = map.get(key);
    if(n == null){
      n = new Node(key, value);
      map.put(key, n);
      add(n);
      ++count;
    }
    else{
      n.value = value;
      update(n);
    }
    if(count>capacity){
      Node toDel = tail.prev;
      remove(toDel);
      map.remove(toDel.key);
      --count;
    }
  }

  private void update(Node node){
    remove(node);
    add(node);
  }
  private void add(Node node){
    Node after = head.next;
    head.next = node;
    node.prev = head;
    node.next = after;
    after.prev = node;
  }

  private void remove(Node node){
    Node before = node.prev;
    Node after = node.next;
    before.next = after;
    after.prev = before;
  }
}