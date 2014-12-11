package yuanye.datastructure.collection;

import java.util.*;

/**
 * Created by Administrator on 14-3-28.
 */
public class MyHashMap<K, V> implements Map<K, V> {


    private final static int MAX_CAPACITY = 1 << 30;

    private final static int DEFAULT_CAPACITY = 1 << 3;

    private final static MyEntry[] EMPTY_TABLE = new MyEntry[0];

    private MyEntry<K, V>[] table = EMPTY_TABLE;

    private float load = 0.75f;

    private int theshold;
    private int size;
    private int modCount;

    public MyHashMap() {
        this(DEFAULT_CAPACITY);
    }

    public MyHashMap(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("negative capacity : " + capacity);
        }
        table = new MyEntry[roundUpToPowerOf2(capacity)];
        theshold = (int) (table.length * load);
    }

    private static class MyEntry<K, V> implements Map.Entry<K, V> {
        final K key;
        V value;
        int hash;
        MyEntry next;

        MyEntry(K key, V value, int hash, MyEntry next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof MyEntry)) {
                return false;
            }
            MyEntry e = (MyEntry) o;
            return eq(this.key, e.key) && eq(this.value, e.value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return Objects.toString(key) + " : " + Objects.toString(value);
        }
    }

    private int roundUpToPowerOf2(int capacity) {
        if (capacity > MAX_CAPACITY) {
            return MAX_CAPACITY;
        }
        int c = Integer.highestOneBit(capacity);
        if (Integer.bitCount(capacity) != 1) {
            while (c < MAX_CAPACITY) {
                c <<= 1;
            }
        }
        return c;
    }

    private int hash(int h, int length) {
        return h & (length - 1);
    }

    private void resize() {
        int newLength = table.length << 1;
        if (newLength > MAX_CAPACITY) {
            newLength = MAX_CAPACITY;
        }

        theshold = (int) (newLength * load);
        if (theshold > MAX_CAPACITY) {
            theshold = MAX_CAPACITY;
        }

        MyEntry[] ntable = new MyEntry[newLength];
        transfer(table, ntable);
        table = ntable;
    }

    private void transfer(MyEntry[] old, MyEntry[] dst) {
        for (int i = 0; i < old.length; i++) {
            MyEntry<K, V> entry = old[i];
            while (entry != null) {
                int index = hash(entry.hash, dst.length);
                MyEntry<K, V> e = new MyEntry<>(entry.key, entry.value, entry.hash, dst[index]);
                dst[index] = e;
                entry = entry.next;
            }
        }
    }


    private MyEntry getEntry(K key) {
        int hash = Objects.hash(key);
        int index = hash(hash, table.length);
        MyEntry<K, V> e = table[index];
        while (e != null) {
            K k = e.key;
            if (eq(k, key)) {
                return e;
            }
            e = e.next;
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return getEntry((K) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (int i = 0; i < table.length; i++) {
            MyEntry<K, V> e = table[i];
            while (e != null) {
                if (eq(value, e.value)) {
                    return true;
                }
                e = e.next;
            }
        }
        return false;
    }


    @Override
    public V get(Object key) {

        MyEntry<K, V> e;
        if (key == null) {
            e = getEntry(null);
        } else {
            e = getEntry((K) key);
        }
        if (e != null) {
            return e.value;
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        modCount++;

        if (size >= theshold) {
            resize();
        }

        MyEntry<K, V> e = getEntry(key);
        if (e != null) {
            V oldV = e.value;
            e.value = value;
            return oldV;

        }

        int hash = Objects.hash(key);
        int index = hash(hash, table.length);
        MyEntry<K, V> entry = new MyEntry<>(key, value, hash, table[index]);
        table[index] = entry;
        size++;
        return null;
    }

    @Override
    public V remove(Object key) {
        modCount++;
        int hash = Objects.hash(key);
        int index = hash(hash, table.length);
        MyEntry<K, V> pre = table[index];
        MyEntry<K, V> e = pre;
        while (e != null) {
            if (eq(e.key, key)) {
                if (e == pre) {
                    table[index] = e.next;
                } else {
                    pre.next = e.next;
                }
                size--;
                return e.value;
            }
            pre = e;
            e = e.next;
        }
        return null;
    }


    @Override
    public void putAll(Map m) {
        Set<Map.Entry<K, V>> entries = m.entrySet();
        for (Map.Entry<K, V> entry : entries) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        modCount++;
        for (MyEntry<K, V> e : table) {
            e = null;
            size = 0;
        }
    }

    private Set<Map.Entry<K, V>> entrySet;
    private Set<K> keySet;
    private Collection<V> values;

    private abstract class AbstractEntryIterator<E> implements Iterator<E> {
        MyEntry<K, V> current;
        MyEntry<K, V> next;
        int modCount;

        AbstractEntryIterator() {
            for (int i = 0; i < table.length; i++) {
                if (table[i] != null) {
                    next = table[i];
                    break;
                }
            }
            modCount = MyHashMap.this.modCount;
        }

        public Entry<K, V> nextEntry() {
            if (modCount != MyHashMap.this.modCount) {
                throw new ConcurrentModificationException();
            }
            if (next == null) {
                throw new IllegalStateException();
            }
            current = next;
            next = nextEntry(next);
            return current;
        }

        @Override
        public boolean hasNext() {
            if (modCount != MyHashMap.this.modCount) {
                throw new ConcurrentModificationException();
            }
            return !(next == null);
        }

        @Override
        public void remove() {
            if (modCount != MyHashMap.this.modCount) {
                throw new ConcurrentModificationException();
            }
            MyHashMap.this.remove(current.key);
            modCount = MyHashMap.this.modCount;
        }

        private MyEntry<K, V> nextEntry(MyEntry<K, V> e) {
            if (e.next != null) {
                return e.next;
            }
            int index = hash(e.hash, table.length) + 1;
            for (int i = index; i < table.length; i++) {
                if (table[i] != null) {
                    return table[i];
                }
            }
            return null;
        }
    }

    private class KeyIterator extends AbstractEntryIterator<K> {
        @Override
        public K next() {
            return super.nextEntry().getKey();
        }
    }

    private class ValueIterator extends AbstractEntryIterator<V> {

        @Override
        public V next() {
            return super.nextEntry().getValue();
        }
    }

    private class EntryIterator extends AbstractEntryIterator<Entry<K, V>> {
        @Override
        public Entry<K, V> next() {
            return nextEntry();
        }
    }

    private class EntrySet extends AbstractSet<Entry<K, V>> {

        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public int size() {
            return size;
        }
    }

    private class KeySet extends AbstractSet<K> {
        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public int size() {
            return size;
        }
    }

    private class valuesCollection extends AbstractCollection<V> {

        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public int size() {
            return size;
        }
    }

    @Override
    public Set keySet() {
        if (keySet == null) {
            keySet = new AbstractSet<K>() {
                @Override
                public Iterator<K> iterator() {
                    return new KeyIterator();
                }

                @Override
                public int size() {
                    return size;
                }
            };
        }
        return keySet;
    }

    @Override
    public Collection values() {
        if (values == null) {
            values = new AbstractCollection<V>() {
                @Override
                public Iterator<V> iterator() {
                    return new ValueIterator();
                }

                @Override
                public int size() {
                    return size;
                }
            };
        }
        return values;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        if (entrySet == null) {
            entrySet = new EntrySet();
        }
        return entrySet;
    }

    private static boolean eq(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (Entry<K, V> entry : entrySet()) {
            hashCode ^= entry.hashCode();
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Map)) {
            return false;
        }
        Map<K, V> map = (Map) obj;
        for (Entry<K, V> entry : map.entrySet()) {
            Entry<K, V> e = getEntry(entry.getKey());
            if (e == null) {
                return false;
            }
            if (!eq(e.getValue(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        Iterator<Entry<K,V>> iterator = entrySet().iterator();
        if (!iterator.hasNext()){
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        while(true){
            Entry<K,V> entry = iterator.next();
            sb.append(entry.getKey() == this ? "this map" : entry.getKey().toString())
                    .append(":")
                    .append(entry.getValue() == this ? " this map " : entry.getValue().toString());
            if (!iterator.hasNext()){
                return sb.append("}").toString();
            }
            sb.append(",").append(" ");
        }
    }

    public static void main(String[] args) {
        Map<String, String> map = new MyHashMap<>();
        for (int i = 0; i < 10; i++) {
            map.put("key" + i, "value" + i);
        }
System.out.println(map);

    }
}
