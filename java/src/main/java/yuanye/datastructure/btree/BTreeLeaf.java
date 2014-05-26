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

    @Override
    AbstractBTreeNode insert(K key) {
        if (!isFull()){
            int i = size - 1;
            while (i > 0 && key.compareTo(((K)keys[i])) < 0){
                keys[i+1] = keys[i];
                i--;
            }
            keys[i] = key;
            return null;
        }
        K middle = (K) keys[size/2];
        AbstractBTreeNode leaf = this.split();
        if (middle.compareTo(key) > 0){
            insert(key);
        }else{
            leaf.insert(key);
        }

        AbstractBTreeNode p = parent;
        //not root
        if (p != null){

        }
        return null;
    }

    @Override
    K delete(K key) {
        return null;
    }

    @Override
    AbstractBTreeNode split() {
        return null;
    }

    @Override
    void merge(AbstractBTreeNode<K> sibling, K key) {

    }

    @Override
    int keys() {
        return 0;
    }

    @Override
    AbstractBTreeNode copy(AbstractBTreeNode<K> node) {
        return null;
    }
}
