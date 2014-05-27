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

    abstract void insertNotFull(K key);

    abstract void insertKey(K key);

    abstract void replace(K oldKey,K  newKey);

    abstract void insertChild(AbstractBTreeNode<K> sub,int index);

    abstract void splitChild(int child);

    abstract K splitSelf(AbstractBTreeNode<K> newNode);

    abstract AbstractBTreeNode<K> getChild(int index);

    abstract int keys();

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
