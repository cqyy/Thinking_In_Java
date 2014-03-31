package cn.yuanye.datastructure;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Administrator on 14-2-19.
 */
public class RBTree {
    public static final RBTreeNode NIL = new RBTreeNode(NodeColor.Black);        // sentinel node.

    public RBTreeNode root = NIL;                                       // tree root


    /**
     * if node.right if NIL,this will do nothing.
     *
     * @param node
     */
    private void leftRotate(RBTreeNode node) {
        RBTreeNode r = node.right;
        if (r == NIL) {
            return;
        }

        //node is root
        if (node.p == NIL) {
            root = r;
        } else if (node == node.p.left) {
            node.p.left = r;
        } else {
            node.p.right = r;
        }

        node.right = r.left;
        if (r.left != NIL) {
            r.left.p = node;
        }

        r.left = node;
        r.p = node.p;
        node.p = r;
    }

    private void rightRotate(RBTreeNode node) {
        RBTreeNode l = node.left;
        if (l == NIL) {
            return;
        }

        if (node.p == NIL) {
            root = l;
        } else if (node == node.p.left) {
            node.p.left = l;
        } else {
            node.p.right = l;
        }

        node.left = l.right;
        if (l.right != NIL) {
            l.right.p = node;
        }

        l.right = node;
        l.p = node.p;
        node.p = l;
    }

    private RBTreeNode find(int v) {
        RBTreeNode x = root;
        while (x != NIL) {
            if (x.value == v) {
                break;
            }
            if (x.value > v) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        return x;
    }

    /**
     * Get the node in subtree node with max value
     *
     * @param node
     * @return
     */
    private RBTreeNode maxMum(RBTreeNode node) {
        if (node == NIL) {
            return NIL;
        }
        RBTreeNode max = node;
        while (max.right != NIL) {
            max = max.right;
        }
        return max;
    }

    private RBTreeNode minMun(RBTreeNode node) {
        if (node == NIL) {
            return NIL;
        }

        RBTreeNode min = node;
        while (min.left != NIL) {
            min = min.left;
        }
        return min;
    }

    /**
     * replace node u with v
     *
     * @param u
     * @param v
     */
    private void transplant(RBTreeNode u, RBTreeNode v) {
        if (u.p == NIL) {
            root = v;
        } else if (u == u.p.left) {
            u.p.left = v;
        } else {
            u.p.right = v;
        }
        v.p = u.p;
    }

    public void insert(int v) {
        RBTreeNode newNode = new RBTreeNode(NodeColor.Red);
        newNode.value = v;
        newNode.left = NIL;
        newNode.right = NIL;

        //empty tree
        if (root == NIL) {
            root = newNode;
            newNode.p = NIL;
        } else {
            RBTreeNode y = root;
            RBTreeNode x = y;

            //find the parent node of newNode
            while (x != NIL) {
                y = x;
                if (x.value > newNode.value) {
                    x = x.left;
                } else {
                    x = x.right;
                }
            }

            if (y.value > newNode.value) {
                y.left = newNode;
                newNode.p = y;
            } else {
                y.right = newNode;
                newNode.p = y;
            }

        }
        //fix up the tree ,to keep the properties
        insertFixUp(newNode);
    }

    private void insertFixUp(RBTreeNode node) {
        RBTreeNode x = node;
        RBTreeNode y = null;
        while (x.p.color == NodeColor.Red) {
            if (x.p == x.p.p.left) {
                y = x.p.p.right;
                if (y.color == NodeColor.Red) {                       //case 1
                    x.p.color = NodeColor.Black;
                    y.color = NodeColor.Black;
                    x.p.p.color = NodeColor.Red;
                    x = x.p.p;
                    continue;
                }
                if (x == x.p.right) {                                //case 2
                    x = x.p;
                    leftRotate(x);
                }
                x.p.color = NodeColor.Black;                         //case 3
                x.p.p.color = NodeColor.Red;
                rightRotate(x.p.p);
            } else {
                y = x.p.p.left;
                if (y.color == NodeColor.Red) {                       //case 1
                    x.p.color = NodeColor.Black;
                    y.color = NodeColor.Black;
                    x.p.p.color = NodeColor.Red;
                    x = x.p.p;
                    continue;
                }
                if (x == x.p.left) {                                 //case 2
                    x = x.p;
                    rightRotate(x);
                }
                x.p.color = NodeColor.Black;                        //case 3
                x.p.p.color = NodeColor.Red;
                leftRotate(x.p.p);
            }

        }
        root.color = NodeColor.Black;
    }

    public boolean delete(int v) {
        boolean result = false;

        RBTreeNode x = find(v);
        //v exists in the tree
        if (x != NIL) {

            NodeColor originalColor = x.color;
            RBTreeNode y = null;
            RBTreeNode z = null;
            if (x.left == NIL) {
                z = x.right;
                transplant(x, x.right);
            } else if (x.right == NIL) {
                z = x.left;
                transplant(x, x.left);
            } else {
                y = minMun(x.right);
                z = y.right;

                originalColor = y.color;
                if (y.p == x) {
                    z.p = y;
                } else {
                    transplant(y, y.right);
                    y.right = x.right;
                    y.right.p = y;
                }
                transplant(x, y);
                y.left = x.left;
                y.left.p = y;
                y.color = x.color;
            }
            if (originalColor == NodeColor.Black) {
                deleteFixUp(z);
            }
            result = true;
        }
        return result;
    }

    private void deleteFixUp(RBTreeNode node) {
        RBTreeNode x = node;
        while (x != root && x.color == NodeColor.Black) {
            RBTreeNode y = null;
            if (x == x.p.left) {
                y = x.p.right;
                if (y.color == NodeColor.Red) {                     //case 1
                    y.color = NodeColor.Black;
                    y.p.color = NodeColor.Red;
                    leftRotate(y.p);
                    y = x.p.right;
                }
                if (y.left.color == NodeColor.Black
                        && y.right.color == NodeColor.Black) {     //case 2
                    y.color = NodeColor.Red;
                    x = x.p;
                    continue;
                }
                if (y.right.color == NodeColor.Black) {            //case 3
                    y.left.color = NodeColor.Black;
                    y.color = NodeColor.Red;
                    rightRotate(y);
                    y = x.p.right;
                }
                //case 4
                y.color = y.p.color;                           //change y and y.p color;
                y.p.color = NodeColor.Black;                   //original color of y is black
                y.right.color = NodeColor.Black;
                leftRotate(y.p);
                x = root;
            } else {
                y = x.p.left;
                if (y.color == NodeColor.Red) {                     //case 1
                    y.color = NodeColor.Black;
                    y.p.color = NodeColor.Red;
                    rightRotate(y.p);
                    y = x.p.left;
                }
                if (y.left.color == NodeColor.Black
                        && y.right.color == NodeColor.Black) {     //case 2
                    y.color = NodeColor.Red;
                    x = x.p;
                    continue;
                }
                if (y.left.color == NodeColor.Black) {            //case 3
                    y.right.color = NodeColor.Black;
                    y.color = NodeColor.Red;
                    leftRotate(y);
                    y = x.p.left;
                }
                //case 4
                y.color = y.p.color;                           //change y and y.p color;
                y.p.color = NodeColor.Black;                   //original color of y is black
                y.left.color = NodeColor.Black;
                rightRotate(y.p);
                x = root;
            }
        }
        x.color = NodeColor.Black;
    }

    public void print() {
        if(root == NIL){
            System.out.println("empty tree");
            return;
        }
        Queue<RBTreeNode> nodes = new LinkedList<RBTreeNode>();
        nodes.offer(root);
        int level = 1;
        RBTreeNode n = null;
        while (!nodes.isEmpty()) {
            n = nodes.poll();
            if (n.left != NIL) {
                nodes.offer(n.left);
            }
            if (n.right != NIL) {
                nodes.offer(n.right);
            }
            //   System.out.print((level++) + " : ");
            if (n == n.p.left) {
                System.out.print("left : ");
            } else {
                System.out.print("right : ");
            }
            System.out.println(n.color + " -- " + n.value);
        }
    }

    public static void main(String[] args) {
        RBTree tree = new RBTree();

        for (int i = 8; i >= 1; i--) {
            tree.insert(i);
        }
        tree.delete(3);
        tree.print();
    }
}
