package yuanye.datastructure.btree;

/**
 * Created by Kali on 14-5-26.
 */
abstract class AbstractBTreeNode<K extends Comparable<K>> {

    protected final int degree;
    protected final static int defaultDegree = 2;

    protected AbstractBTreeNode parent ;

    AbstractBTreeNode(){
        this(defaultDegree);
    }

    AbstractBTreeNode(int degree){
        if(degree<2){
            throw new IllegalArgumentException("degree must >= 2");
        }
        this.degree = degree;
    }

    abstract boolean isLeaf();

    abstract AbstractBTreeNode insert(K key);

    abstract K delete(K key);

    abstract AbstractBTreeNode split();

    abstract void merge(AbstractBTreeNode<K> sibling,K key);

    abstract int keys();

    abstract AbstractBTreeNode copy(AbstractBTreeNode<K> node);

    boolean isFull(){
        return keys() == degree*2 -1;
    }

    AbstractBTreeNode parent(){
        return parent;
    }
    void parent(AbstractBTreeNode parent){
        this.parent = parent;
    }
}
