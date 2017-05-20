package Datastructures;

/**
 * Created by Naren on 5/19/17.
 */
public class TrieNode {
    char c;

    TrieNode[] children = new TrieNode[26];
    boolean isEnd;

    public TrieNode(char c) {
        this.c = c;
    }

    public TrieNode() {
    }
}
