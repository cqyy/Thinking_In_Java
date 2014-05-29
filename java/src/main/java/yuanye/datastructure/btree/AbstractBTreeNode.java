package yuanye.datastructure.btree;

/**
 * Created by Kali on 14-5-26.
 */
abstract class AbstractBTreeNode<K extends Comparable<K>> {

    protected final int degree;
    protected final static int defaultDegree = 2;

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

    abstract K search(K key);

    abstract void insertNotFull(K key);

    abstract void deleteNotEmpty(K key);

    abstract void insertKey(K key);

    abstract K getKey(int idx);

    abstract K deleteKey(K key);

    abstract K deleteKey(int index);

    abstract boolean existsKey(K key);

    abstract void replaceKey(K oldKey,K newKey);

    abstract K replaceKey(K newKey,int oldKeyIndex);

    abstract void insertChild(AbstractBTreeNode<K> sub,int index);

    abstract void deleteChild(AbstractBTreeNode<K> sub);

    abstract AbstractBTreeNode deleteChild(int index);

    abstract AbstractBTreeNode<K> getChild(int index);

    abstract void splitChild(int child);

    abstract K splitSelf(AbstractBTreeNode<K> newNode);

    abstract void merge(K middle,AbstractBTreeNode<K> sibling);

    abstract int keys();

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    boolean isFull(){
        return keys() == degree*2 -1;
    }

}
