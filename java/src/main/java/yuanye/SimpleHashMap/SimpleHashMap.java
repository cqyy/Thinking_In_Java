package yuanye.SimpleHashMap;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class SimpleHashMap<K,V> extends AbstractMap<K,V> {
	private static final int SIZE = 512;
	@SuppressWarnings("unchecked")
	LinkedList<Map.Entry<K,V>>[] buckets = new LinkedList[SIZE];
	
	class MapEntry implements Map.Entry<K, V>{
		K key;
		V value;
		public MapEntry(K key,V value){
			this.key = key;
			this.value = value;
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
		public boolean equals(Object obj){
			if(obj instanceof SimpleHashMap.MapEntry){
				@SuppressWarnings("unchecked")
				MapEntry entry = (MapEntry)obj;
				return entry.key.equals(key)
						&&entry.value.equals(value);
			}
			return false;
		}
		@Override
		public String toString(){
			return key.toString()+":"+value.toString();
		}
	}
	
	private int index(Object obj){
		return Math.abs(obj.hashCode())%SIZE;
	} 
	
	@Override
	public V put(K key,V value){
		V oldValue = null;
		int index = index(key);
		if(buckets[index] == null){
			buckets[index] = new LinkedList<Entry<K,V>>();
		}
		LinkedList<Map.Entry<K, V>> list = buckets[index];
		boolean found = false;
		Iterator<Entry<K, V>> it = list.iterator();
		while(it.hasNext()){
			Map.Entry<K, V>  entry = it.next();
			if(entry.getKey().equals(key)){
				oldValue = entry.getValue();
				entry.setValue(value);
				found = true;
				break;
			}
		}
		if(!found) {
			list.add(new MapEntry(key,value));
		}
		return oldValue;
	}
	
	@Override
	public V get(Object key){
		int index = index(key);
		LinkedList<Map.Entry<K, V>> list = buckets[index];
		if (list != null) {
			Iterator<Map.Entry<K, V>> it = list.iterator();
			while (it.hasNext()) {
				Map.Entry<K, V> temp = it.next();
				if (temp.getKey().equals(key))
					return temp.getValue();
			}
		}
		return null;
	}
	
	@Override
	public void clear(){
		for(int i=0;i<buckets.length;i++){
			buckets[i] = null;
		}
	}
	
	@Override
	public V remove(Object key){
		int index = index(key);
		LinkedList<Map.Entry<K, V>> list = buckets[index];
		V value = null;
		
		if(list != null){
			Iterator<Map.Entry<K, V>> it = list.iterator();
			while(it.hasNext()){
				Map.Entry<K, V> entry = it.next();
				if(entry.getKey().equals(key)){
					value = entry.getValue();
					list.remove(entry);
				}
			}
		}
		
		return value;
	}
	
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		Set<Map.Entry<K, V>> set = new HashSet<>();
		for(LinkedList<Map.Entry<K, V>> list : buckets){
			if(list!=null){
				Iterator<Map.Entry<K, V>> it = list.iterator();
				while(it.hasNext()){
					set.add(it.next());
				}
			}
		}
		return set;
	}
	
	public static void main(String[] args){
		SimpleHashMap<String,String> map = new SimpleHashMap<String,String>();
		map.put( "UESTC", "ChengDu");
		map.put("UPC", "DongYin");
		map.put("UPC", "QinDao");
		map.put("BUAA", "BeiJing");
		map.put("CQU", "ChongQing");
		System.out.println("remove(UESTC):"+map.remove("UESTC"));
		for(Map.Entry<String, String> entry : map.entrySet()){
			System.out.println(entry);
			entry.setValue("New Place");
		}
		for(Map.Entry<String, String> entry : map.entrySet()){
			System.out.println(entry);
		}
		
	}
}
