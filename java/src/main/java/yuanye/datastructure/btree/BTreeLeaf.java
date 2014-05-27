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
    void deleteNotEmpty(K key) {
        if (size <= degree -1){
            throw new RuntimeException("size of node <= degree -1");
        }
        this.deleteKey(key);
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
    K deleteKey(K key) {

        int i = indexOf(key);
        return this.deleteKey(i);
    }

    private int indexOf(K key) {
        int i = 0;
        if (size > 0) {
            while (i < size) {
                if (key.equals(keys[i]))
                    return i;
                i++;
            }
        }
        return -1;
    }

    @Override
    K deleteKey(int index) {
        K result = null;
        if (index < size){
            result = (K) keys[index];
            while (index < size-1){
                keys[index] = keys[index + 1];
                index++;
            }
            keys[size-1] = null;
            size--;
        }
        return result;
    }

    @Override
    boolean existsKey(K key) {
        boolean result = false;
        int i = 0;
        while (i < size){
            if (key.equals(keys[i])){
                result = true;
                break;
            }
            i++;
        }
        return result;
    }

    @Override
    void replaceKey(K oldKey, K newKey) {
        int i = 0;
        while (i < size){
            if (oldKey.equals(keys[i]))
                break;
            i++;
        }
        //old key exists
        if (i < size){
            if (    (i > 0 && newKey.compareTo((K)keys[i -1]) < 0)
                 || ( i < size -1) && newKey.compareTo((K)keys[i +1]) > 0 ){
                throw new RuntimeException("new key conflict with neighbor");
            }
            keys[i] = newKey;
        }
    }

    @Override
    void replaceKey(K newKey, int oldKeyIndex) {
        if (oldKeyIndex < size){
            this.replaceKey((K)keys[oldKeyIndex],newKey);
        }
    }

    @Override
    void insertChild(AbstractBTreeNode<K> sub, int index) {
        throw new RuntimeException("Leaf node does not support insertChild");
    }

    @Override
    void deleteChild(AbstractBTreeNode<K> sub) {
        throw new RuntimeException("Leaf does not support deleteChild()");
    }

    @Override
    void deleteChild(int index) {
        throw new RuntimeException("Leaf does not support deleteChild()");
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
    void merge(K middle, AbstractBTreeNode<K> sibling) {
        if ( !(sibling instanceof BTreeLeaf)){
            throw new RuntimeException("Sibling is not leaf node");
        }
        BTreeLeaf node = (BTreeLeaf)sibling;
        this.insertKey(middle);
        for (int i = 0; i < node.keys(); i++){
            this.insertKey((K)node.keys[i]);
        }
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
