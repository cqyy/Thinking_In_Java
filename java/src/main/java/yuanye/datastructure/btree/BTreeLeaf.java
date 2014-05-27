package yuanye.datastructure.btree;

/**
 * Created by Kali on 14-5-26.
 */
public class BTreeLeaf<K extends Comparable<K>> extends AbstractBTreeNode<K> {

    private final Object[] keys;
    private int size;

    BTreeLeaf(){
        this(defaultDegree);
    }

    BTreeLeaf(int degree){
        super(degree);
        keys = new Object[2*degree - 1];
    }


    @Override
    boolean isLeaf() {
        return true;
    }

    private void checkNotFull(){
        if (isFull()){
            throw new RuntimeException("node is full");
        }
    }

    @Override
    void insertNotFull(K key) {
        checkNotFull();
        this.insertKey(key);
    }

    @Override
    void insertKey(K key) {
        checkNotFull();
        int i = size -1;
        while (i >= 0 && key.compareTo((K)keys[i]) < 0){
            keys[i+1] = keys[i];
            i--;
        }
        i++;
        keys[i] = key;
        size ++;
    }

    @Override
    void replace(K oldKey, K newKey) {
    }

    @Override
    void insertChild(AbstractBTreeNode<K> sub, int index) {
        throw new RuntimeException("Leaf node does not support insertChild");
    }

    @Override
    void splitChild(int child) {
        throw new RuntimeException("Leaf dose not support split child");
    }

    @Override
    K splitSelf(AbstractBTreeNode<K> newNode) {
        if (! (newNode instanceof BTreeLeaf)){
            throw new RuntimeException("Instance not match.");
        }
        if (!isFull()){
            throw new RuntimeException("Node is not full");
        }

        K middle = (K)keys[degree -1];
        BTreeLeaf<K> node = (BTreeLeaf)newNode;
        int i = 0;
        while (i < degree-1){
            node.keys[i] = this.keys[i + degree];
            this.keys[i + degree] = null;
            i++;
        }
        this.keys[degree -1] = null;
        this.size = degree -1;
        node.size = degree -1;
        return middle;
    }

    @Override
    AbstractBTreeNode<K> getChild(int index) {
        return null;
    }

    @Override
    int keys() {
        return size;
    }
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("leaf----").append("size: ").append(size).append(" keys:").append("[");
        for(int i = 0; i < size; i++){
            sb.append(keys[i]).append(",");
        }
        sb.append("]");
        return sb.toString();
    }

}