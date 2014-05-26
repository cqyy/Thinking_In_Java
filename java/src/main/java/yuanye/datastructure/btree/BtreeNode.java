package yuanye.datastructure.btree;

/**
 * Created by Kali on 14-5-26.
 */
class BTreeNode<K extends Comparable<K>> {

    int size;
    boolean isLeaf;
    Object[] keys;
    BTreeNode[] children;

    BTreeNode(int degree){
        if (degree < 2){
            throw new IllegalArgumentException("degree should not < 2");
        }
        keys = new Object[degree*2 -1];
        children = new BTreeNode[degree*2];
    }
}
