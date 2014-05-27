package yuanye.datastructure.rbtree;


/**
 * Created by Administrator on 14-2-19.
 * Node of red-black tree
 */
public class RBTreeNode {

    public RBTreeNode(NodeColor color){
        this.color = color;
    }

    public NodeColor color;
    public RBTreeNode p;         // parent of the node
    public RBTreeNode left;      // left subtree of the node
    public RBTreeNode right;     // right subtree of the node

    public int value;            //value of the node
}
