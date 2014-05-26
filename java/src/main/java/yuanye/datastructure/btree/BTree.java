package yuanye.datastructure.btree;

/**
 * Created by Kali on 14-5-26.
 */
public class BTree<K extends Comparable<K>> {
    private final int degree;
    private BTreeNode<K> root;

    public BTree(int degree){
        if (degree < 2){
            throw new IllegalArgumentException("degree mustn't < 2");
        }
        this.degree = degree;
        root = new BTreeNode<>(degree);
        root.isLeaf = true;
        root.size = 0;
    }

    public void insert(K key){
        BTreeNode node = root;
        while (!node.isLeaf){
            int i = 0;
            while (i < node.size && key.compareTo(((K)node.keys[i])) < 0){
                i++;
            }
            node = node.children[i];
        }

        if (!isFull(node)){
            int i = node.size -1;
            while (i >=  0 && key.compareTo((K)node.keys[i]) < 0){
                node.keys[i+1] = node.keys[i];
                i--;
            }
            i++;
            node.keys[i] = key;
            return;
        }
        //full,split
        K middle = (K) node.keys[node.size/2];

    }

    private void insertNotFull(K key,BTreeNode node){

    }

    private boolean isFull(BTreeNode node){
        return node.size == 2*degree - 1;
    }

    BTreeNode splitChild(BTreeNode node,int idx){
        BTreeNode x = node.children[idx];
        BTreeNode y = new BTreeNode(degree);
        y.isLeaf = x.isLeaf;
        int t = degree -1;
        int i = 0;
        while (i < t){
            y.children[i] = x.children[i + degree];
            i++;
        }
        int n = node.size-1;
    }
}
