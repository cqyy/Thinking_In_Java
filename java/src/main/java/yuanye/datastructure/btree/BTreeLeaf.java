package yuanye.datastructure.btree;

/**
 * Created by Kali on 14-5-26.
 */
public class BTreeLeaf<K extends Comparable<K>> extends AbstractBTreeNode<K> {

    private final Object[] keys;
    private int nkey;

    BTreeLeaf(int degree){
        super(degree);
        keys = new Object[2*degree - 1];
    }


    @Override
    boolean isLeaf() {
        return true;
    }

    @Override
    K search(K key) {
        int index = indexOfKey(key);
        if (index >=0)
            return (K) keys[index];
        return null;
    }

    @Override
    K getKey(int idx) {
        return (K) keys[idx];
    }

    @Override
    K setKey(K newKey, int oldKeyIndex) {
        K old = (K) keys[oldKeyIndex];
        keys[oldKeyIndex] = newKey;
        return old;
    }

    @Override
    void setChild(AbstractBTreeNode<K> sub, int index) {
        throw new RuntimeException("Could not set child of leaf node.");
    }

    @Override
    AbstractBTreeNode<K> getChild(int index) {
        return null;
    }

    @Override
    void splitChild(int child) {
        throw new  RuntimeException("Could not split child of leaf node.");
    }

    @Override
    int setNKey(int nkey) {
        int old = this.nkey;
        this.nkey = nkey;
        return old;
    }

    @Override
    int nkey() {
        return nkey;
    }

    @Override
    int nchild() {
        return 0;
    }

    @Override
    int setNChild(int nchild) {
        throw new RuntimeException("Could set NChild of leaf node.");
    }

    @Override
    void insertNotFull(K key) {
        checkNotFull();
        this.insertKey(key);
    }

    @Override
    void deleteNotEmpty(K key) {
        if (nkey <= degree -1){
            throw new RuntimeException("size of node <= degree -1");
        }
        this.deleteKey(key);
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
        this.nkey = degree -1;
        node.nkey = degree -1;
        return middle;
    }

    @Override
    void merge(K middle, AbstractBTreeNode<K> sibling) {
        if ( !(sibling instanceof BTreeLeaf)){
            throw new RuntimeException("Sibling is not leaf node");
        }
        BTreeLeaf node = (BTreeLeaf)sibling;
        this.insertKey(middle);
        for (int i = 0; i < node.nkey(); i++){
            this.insertKey((K)node.keys[i]);
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("leaf----").append("size: ").append(nkey).append(" keys:").append("[");
        for(int i = 0; i < nkey; i++){
            sb.append(keys[i]).append(",");
        }
        sb.append("]");
        return sb.toString();
    }

}
