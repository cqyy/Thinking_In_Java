package yuanye.datastructure.btree;

/**
 * Created by Kali on 14-5-26.
 */
class BTreeInternalNode<K extends Comparable<K>> extends AbstractBTreeNode<K> {

    private final Object[] keys;
    private final AbstractBTreeNode<K>[] children;
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

    @Override
    K search(K key) {
        int index = indexOf(key);
        if (index >= 0){
            return (K) keys[index];
        }
        index = 0;
        while (key.compareTo((K)keys[index++]) > 0);
        return children[index].search(key);
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
        //key in this node
        if (this.existsKey(key)) {
            int index = indexOf(key);
            //predecessor child could delete a key
            AbstractBTreeNode<K> node;
            if ((node = children[index]).keys() >= degree){
                K repKey = node.getKey(node.keys()-1);                  //maximum key in predecessor
                node.deleteNotEmpty(repKey);
                replaceKey(repKey,index);
            }
            //follow child could delete a key
            else if ((node = children[index +1]).keys() >= degree){
                K repKey = node.getKey(0);                              //minimum key in follow
                node.deleteNotEmpty(repKey);
                replaceKey(repKey,index);
            }
            //merge predecessor with follow
            else {
                node = children[index];
                node.merge(key,children[index+1]);
                this.deleteKey(index);
                this.deleteChild(index+1);
                node.deleteNotEmpty(key);
            }
        }
        //key may exist in child
        else {
            int i = 0;
            //find proper child the key may exists in
            while ( i < size){
                if(key.compareTo((K)keys[i]) < 0)
                    break;
                i++;
            }
            AbstractBTreeNode<K> target = children[i];
            //child has enough key
            if (target.keys() >= degree){
                target.deleteNotEmpty(key);
            }else{
                AbstractBTreeNode<K> sibling;
                //try to find replacement from predecessor
                if (i > 0 && (sibling = children[i-1]).keys() >= degree){
                    int lastIndex = sibling.keys();
                    if (!target.isLeaf()){
                        AbstractBTreeNode<K> sub = sibling.deleteChild(lastIndex); //last child
                        target.insertChild(sub,0);
                    }
                    K repKey = sibling.deleteKey(sibling.keys()-1);    //maximum key
                    repKey = replaceKey(repKey,i-1);
                    target.insertKey(repKey);
                    target.deleteNotEmpty(key);
                }
                //try to find replacement from follower
                else if ( i < size && (sibling = children[i+1]).keys() >= degree){
                    if (!target.isLeaf()){
                        AbstractBTreeNode<K> sub = sibling.deleteChild(0);  //first child
                        target.insertChild(sub,target.keys());
                    }
                    K repKey = sibling.deleteKey(0);                    //minimum key
                    repKey = replaceKey(repKey,i);
                    target.insertKey(repKey);
                    target.deleteNotEmpty(key);
                }
                //merge child with one of it's sibling
                else {
                    //merge with predecessor sibling
                    if ( i > 0){
                        K repKey = this.deleteKey(i-1);
                        sibling = children[i -1];
                        sibling.merge(repKey,target);
                        this.deleteChild(target);
                        sibling.deleteNotEmpty(key);
                    }else {
                        K repKey = this.deleteKey(i);
                        sibling = children[i+1];
                        target.merge(repKey,sibling);
                        deleteChild(i+1);
                        target.deleteNotEmpty(key);
                    }
                }
            }
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
    K getKey(int idx) {
        return (K) keys[idx];
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
    K replaceKey(K newKey, int oldKeyIndex) {
        K result = null;
        if (oldKeyIndex < size) {
            result = (K) keys[oldKeyIndex];
            this.replaceKey((K) keys[oldKeyIndex], newKey);
        }
        return result;
    }


    @Override
    void insertChild(AbstractBTreeNode<K> sub, int index) {
        int i = children.length -1 ;
        while (i > index) {
            this.children[i] = this.children[i-1];
            i--;
        }
        this.children[index] = sub;
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
    AbstractBTreeNode deleteChild(int index) {
        AbstractBTreeNode result = null;
        int childs = childSize();
        if (childs > 0) {
            result = children[index];
            while (index < childs - 1) {
                children[index] = children[index + 1];
                index++;
            }
            children[childs - 1] = null;
        }
        return result;
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
