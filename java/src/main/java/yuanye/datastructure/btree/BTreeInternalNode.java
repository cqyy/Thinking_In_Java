package yuanye.datastructure.btree;

/**
 * Created by Kali on 14-5-26.
 */
class BTreeInternalNode<K extends Comparable<K>> extends AbstractBTreeNode<K> {

    private final Object[] keys;
    private final AbstractBTreeNode[] children;
    private int size;

    BTreeInternalNode(){
        this(defaultDegree);
    }

    BTreeInternalNode(int degree){
        super(degree);
        keys = new Object[2*degree -1];
        children = new AbstractBTreeNode[2*degree];
    }

    @Override
    boolean isLeaf() {
        return false;
    }


    private void checkNotFull(){
        if (isFull()){
            throw new RuntimeException("node is full");
        }
    }

    @Override
    void insertNotFull(K key) {
        checkNotFull();
        int i = 0;
        while (i < size &&key.compareTo((K)keys[i])>0){
            i++;
        }
        if (this.children[i].isFull()){
            this.splitChild(i);

            if (key.compareTo((K)this.keys[i]) >0){
                i++;
            }
        }

        this.children[i].insertNotFull(key);
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
        int i = size -1;
        while (i>= index){
            this.children[i+1] = this.children[i];
            i--;
        }
        i++;
        this.children[i] = sub;
    }

    @Override
    void splitChild(int child) {
        AbstractBTreeNode<K> old = children[child];
        AbstractBTreeNode<K> neo = old.isLeaf()
                ?new BTreeLeaf<>(degree)
                :new BTreeInternalNode<>(degree);
        K middle = old.splitSelf(neo);
        this.insertKey(middle);
        this.insertChild(child+1,neo);
    }

    private void insertChild(int index,AbstractBTreeNode<K> child){
        int i = size - 1;
        while (i >= index){
            children[i+1] = children[i];
            i--;
        }
        children[index] = child;
    }

    @Override
    K splitSelf(AbstractBTreeNode<K> newNode) {
        if (! (newNode instanceof BTreeInternalNode)){
            throw new RuntimeException("Instance not match.");
        }
        if (!isFull()){
            throw new RuntimeException("Node is not full");
        }

        K middle = (K)keys[degree -1];
        BTreeInternalNode<K> node = (BTreeInternalNode)newNode;
        int i = 0;
        while (i < degree-1){
            node.keys[i] = this.keys[i + degree];
            this.keys[i + degree] = null;
            i++;
        }
        this.keys[degree -1] = null;

        i = 0;
        while (i < degree){
            node.children[i] = this.children[i + degree];
            this.children[i + degree] = null;
            i++;
        }

        this.size = degree - 1;
        node.size = degree - 1;
        return middle;
    }

    @Override
    AbstractBTreeNode<K> getChild(int index) {
        if (index < children.length){
            return children[index];
        }
        return null;
    }


    @Override
    int keys() {
        return size;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" internal node ---- ").append("size: ").append(size).append(" keys:").append("[");
        for(int i = 0; i < size; i++){
            sb.append(keys[i]).append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}
