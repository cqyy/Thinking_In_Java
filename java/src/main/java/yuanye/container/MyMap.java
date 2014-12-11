package yuanye.container;

import java.util.*;

/**
 * Created by Administrator on 14-3-24.
 */
public class MyMap<K,V> extends AbstractMap<K, V> {

    static class MyEntry<K,V> implements Map.Entry<K,V>{

        private K key;
        private V value;

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
            V old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public boolean equals(Object o) {
            if ( !(o instanceof MyEntry) ){
                return false;
            }

            MyEntry e = (MyEntry)o;
            K key = this.key;
            V value = this.value;

            return eq(key,e.getKey()) && eq(value,e.getValue());
        }

        @Override
        public int hashCode() {
            return (key == null?0 : key.hashCode())^
                    (value == null?0 : value.hashCode());
        }

        private boolean eq(Object o1,Object o2){
            return o1 == null?o2==null:o1.equals(o2);
        }
    }

    static class EntrySet<K,V> extends AbstractSet<Entry<K,V>>{
        private Set<MyEntry<K,V>> entries = new HashSet<>();
        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new Iterator<Entry<K, V>>() {
                Iterator<MyEntry<K, V>> i = entries.iterator();
                @Override
                public boolean hasNext() {
                    return i.hasNext();
                }

                @Override
                public Entry<K, V> next() {
                    return i.next();
                }

                @Override
                public void remove() {
                    i.remove();
                }
            };
        }

        @Override
        public int size() {
            return 0;
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new EntrySet<>();
    }
}
