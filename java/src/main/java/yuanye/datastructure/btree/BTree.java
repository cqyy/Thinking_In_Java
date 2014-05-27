package yuanye.datastructure.btree;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Kali on 14-5-26.
 */
public class BTree<K extends Comparable<K>> {
    private final int degree;
    private AbstractBTreeNode<K> root;

    public BTree(int degree){
        if (degree < 2){
            throw new IllegalArgumentException("degree mustn't < 2");
        }
        this.degree = degree;
        root = new BTreeLeaf<>(degree);
    }

    public void insert(K key){
        AbstractBTreeNode<K> n = root;
        if (root.isFull()){
            AbstractBTreeNode<K> newRoot = new BTreeInternalNode<>(degree);
            newRoot.insertChild(n,0);
            newRoot.splitChild(0);
            n = newRoot;
            root = newRoot;
        }
        n.insertNotFull(key);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        AbstractBTreeNode node;
        Queue<AbstractBTreeNode> queue = new LinkedList<>();
        queue.add(root);
        String newLine = System.getProperty("line.separator");
        while (!queue.isEmpty()){
            node = queue.poll();
            sb.append(node).append(newLine);
            int i = 0;
            while (node.getChild(i) != null){
                queue.offer(node.getChild(i));
                i++;
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        BTree<Integer> bTree = new BTree(3);
        int[] numbers = new int[]{6,18,16,22,3,12,8,10,20,21,13,17};
        for(int i = 0; i < numbers.length; i++){
            bTree.insert(numbers[i]);
        }
        System.out.println(bTree);
    }

}
