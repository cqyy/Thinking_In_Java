package test.yuanye.datastructure.btree;

import org.junit.Test;
import yuanye.datastructure.rbtree.NodeColor;
import yuanye.datastructure.rbtree.RBTree;
import yuanye.datastructure.rbtree.RBTreeNode;

import java.util.*;

import static org.junit.Assert.fail;

/**
 * Created by kali on 14-2-24.
 */
public class RBTreeTest {
    /**
     * check color of tree
     *
     * @param tree
     * @return false if exists a red tree node has red child.
     */
    private boolean checkColor(RBTree tree) {

        if (tree.root == RBTree.NIL) {
            return true;
        }
        Queue<RBTreeNode> nodes = new LinkedList<RBTreeNode>();
        nodes.offer(tree.root);
        while (!nodes.isEmpty()) {
            RBTreeNode node = nodes.poll();

            if (node.left != RBTree.NIL) {
                nodes.offer(node.left);
            }
            if (node.right != RBTree.NIL) {
                nodes.offer(node.right);
            }
            if (node.color == NodeColor.Red) {
                if (node.left.color == NodeColor.Red
                        || node.right.color == NodeColor.Red) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * check black height of tree
     *
     * @param tree
     * @return false if exists subtree whose black height of left subtree doesn't equal to right's
     */
    private boolean checkBheight(RBTree tree) {
        int height = bheight(tree.root);
        if (height > 0) {
            return true;
        }
        return false;
    }

    /**
     * get black of tree
     *
     * @param subTree
     * @return positive number if black height of left subtree equals to right's or -1.
     */
    private int bheight(RBTreeNode subTree) {
        if (subTree == RBTree.NIL) {
            return 1;
        }

        int leftBheight = bheight(subTree.left);
        int rightBheight = bheight(subTree.right);
        if (leftBheight == rightBheight && leftBheight != -1) {
            return (subTree.color==NodeColor.Black)
                    ?(leftBheight+1)
                    :leftBheight;
        }
        return -1;
    }

    @Test
    public void testInsert() throws Exception {
        int testTimes = 1000;
        int num = 20000;
        Random random = new Random();
        for (int i = 0; i < testTimes; i++) {
            RBTree tree = new RBTree();
            for (int j = 0; j < num; j++) {
                int n = random.nextInt(num);
                tree.insert(n);
            }
            if (!checkColor(tree) || !checkBheight(tree)) {
                fail();
            }
        }
    }

    @Test
    public void testDelete() throws Exception {
        int testTimes = 1000;
        int num = 2000;
        Random random = new Random();

        for (int i = 0; i < testTimes; i++) {
            RBTree tree = new RBTree();
            List<Integer> numbers = new ArrayList<Integer>();

            for (int j = 0; j < num; j++) {
                int n = random.nextInt(num);
                numbers.add(n);                      // generate test numbers
                tree.insert(n);
            }
            for (int n : numbers) {
                tree.delete(n);
                if (!checkColor(tree) ) {
                    fail("color not match");
                }
                if( !checkBheight(tree) ){
                    fail(" height not match");
                }
            }
        }
    }
}
