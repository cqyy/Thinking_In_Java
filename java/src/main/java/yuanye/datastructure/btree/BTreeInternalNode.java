package yuanye.datastructure.btree;

/**
 * Created by Kali on 14-5-26.
 */
class BTreeInternalNode<K extends Comparable<K>> extends AbstractBTreeNode<K> {

    private final Object[] keys;
    private final AbstractBTreeNode[] children;
    private int size;

    BTreeInternalNode() {
        this(defaultDegree);
    }

    BTreeInternalNode(int degree) {
        super(degree);
        keys = new Object[2 * degree - 1];
        children = new AbstractBTreeNode[2 * degree];
    }

    @Override
    boolean isLeaf() {
        return false;
    }


    private void checkNotFull() {
        if (isFull()) {
            throw new RuntimeException("node is full");
        }
    }

    @Override
    void insertNotFull(K key) {
        checkNotFull();
        int i = 0;
        while (i < size && key.compareTo((K) keys[i]) > 0) {
            i++;
        }
        if (this.children[i].isFull()) {
            this.splitChild(i);

            if (key.compareTo((K) this.keys[i]) > 0) {
                i++;
            }
        }

        this.children[i].insertNotFull(key);
    }

    @Override
    void deleteNotEmpty(K key) {
        if (size == degree -1){
            throw new RuntimeException("size must lager than degree -1");
        }
        if (this.existsKey(key)) {
            int index = indexOf(key);
            //predecessor child could delete
            if (index > 0 && children[index -1].keys() >= degree){
               start from here next time;
            }
        } else {

        }
    }

    private int indexOf(K key) {
        int i = 0;
        if (size > 0) {
            while (i < size) {
                if (key.equals(keys[i]))
                    return i;
                i++;
            }
        }
        return -1;
    }

    @Override
    void insertKey(K key) {
        checkNotFull();
        int i = size - 1;
        while (i >= 0 && key.compareTo((K) keys[i]) < 0) {
            keys[i + 1] = keys[i];
            i--;
        }
        i++;
        keys[i] = key;
        size++;
    }

    @Override
    K deleteKey(K key) {
        int i = indexOf(key);
        return this.deleteKey(i);
    }

    @Override
    K deleteKey(int index) {
        K result = null;
        if (index < size) {
            result = (K) keys[index];
            while (index < size - 1) {
                keys[index] = keys[index + 1];
                index++;
            }
            keys[size - 1] = null;
            size--;
        }
        return result;
    }

    @Override
    boolean existsKey(K key) {
        return indexOf(key) >= 0;
    }

    @Override
    void replaceKey(K oldKey, K newKey) {
        int i = indexOf(oldKey);
        //old key exists
        if (i >=0 && i < size) {
            if ((i > 0 && newKey.compareTo((K) keys[i - 1]) < 0)
                    || (i < size - 1) && newKey.compareTo((K) keys[i + 1]) > 0) {
                throw new RuntimeException("new key conflict with neighbor");
            }
            keys[i] = newKey;
        }
    }

    @Override
    void replaceKey(K newKey, int oldKeyIndex) {
        if (oldKeyIndex < size) {
            this.replaceKey((K) keys[oldKeyIndex], newKey);
        }
    }


    @Override
    void insertChild(AbstractBTreeNode<K> sub, int index) {
        int i = childSize() - 1;
        while (i >= index) {
            this.children[i + 1] = this.children[i];
            i--;
        }
        i++;
        this.children[i] = sub;
    }

    @Override
    void deleteChild(AbstractBTreeNode<K> sub) {
        int childs = childSize();
        int i = 0;
        while (i < childs) {
            if (sub.equals(keys[i]))
                break;
            i++;
        }
        if (i < childs) {
            this.deleteChild(i);
        }
    }

    @Override
    void deleteChild(int index) {
        int childs = childSize();
        if (childs > 0) {
            while (index < childs - 1) {
                children[index] = children[index + 1];
            }
            children[childs - 1] = null;
        }
    }

    @Override
    void splitChild(int child) {
        AbstractBTreeNode<K> old = children[child];
        AbstractBTreeNode<K> neo = old.isLeaf()
                ? new BTreeLeaf<>(degree)
                : new BTreeInternalNode<>(degree);
        K middle = old.splitSelf(neo);
        this.insertKey(middle);
        this.insertChild(neo, child + 1);
    }
//
//    private void insertChild(int index, AbstractBTreeNode<K> child) {
//        int i = size - 1;
//        while (i >= index) {
//            children[i + 1] = children[i];
//            i--;
//        }
//        children[index] = child;
//    }

    @Override
    K splitSelf(AbstractBTreeNode<K> newNode) {
        if (!(newNode instanceof BTreeInternalNode)) {
            throw new RuntimeException("Instance not match.");
        }
        if (!isFull()) {
            throw new RuntimeException("Node is not full");
        }

        K middle = (K) keys[degree - 1];
        BTreeInternalNode<K> node = (BTreeInternalNode) newNode;
        int i = 0;
        while (i < degree - 1) {
            node.keys[i] = this.keys[i + degree];
            this.keys[i + degree] = null;
            i++;
        }
        this.keys[degree - 1] = null;

        i = 0;
        while (i < degree) {
            node.children[i] = this.children[i + degree];
            this.children[i + degree] = null;
            i++;
        }

        this.size = degree - 1;
        node.size = degree - 1;
        return middle;
    }

    @Override
    void merge(K middle, AbstractBTreeNode<K> sibling) {
        if (!(sibling instanceof BTreeInternalNode)) {
            throw new RuntimeException("Sibling is not leaf node");
        }
        BTreeInternalNode node = (BTreeInternalNode) sibling;
        this.insertKey(middle);
        for (int i = 0; i < node.keys(); i++) {
            this.insertKey((K) node.keys[i]);
        }
        for (int i = 0; i < childSize(); i++) {
            insertChild(node.children[i], i + degree);
        }
    }

    @Override
    AbstractBTreeNode<K> getChild(int index) {
        if (index < children.length) {
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
        for (int i = 0; i < size; i++) {
            sb.append(keys[i]).append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private int childSize() {
        return size > 0 ? size + 1 : 0;
    }
}
