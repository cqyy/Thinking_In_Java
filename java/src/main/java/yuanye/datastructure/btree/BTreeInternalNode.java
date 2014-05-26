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

    @Override
    AbstractBTreeNode insert(K key) {
        int i = 0;
        while ( i < size && key.compareTo((K)keys[i]) >0)
            i++;
        return children[i].insert(key);
    }

    @Override
    K delete(K key) {
        return null;
    }

    @Override
    AbstractBTreeNode split() {
        BTreeInternalNode node = new BTreeInternalNode(degree);
        int start = size/2 +1;             //first key to copy
        int i = 0;
        while (i < size/2){
            node.keys[i] = keys[i + start];
            keys[i + start] = null;
            i++;
        }
        keys[size/2] = null;
        i = 0;
        while (i < (size +1)/2){
            node.children[i] = children[i + start];
            children[i + start] = null;
            i++;
        }
        size = size/2 -1;
        node.size = size;
        return node;
    }

    @Override
    void merge(AbstractBTreeNode<K> sibling, K key) {

    }

    @Override
    int keys() {
        return size;
    }

    @Override
    AbstractBTreeNode copy(AbstractBTreeNode<K> node) {
        return null;
    }
}
