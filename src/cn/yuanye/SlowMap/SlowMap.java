package cn.yuanye.SlowMap;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SlowMap<K,V> extends AbstractMap<K,V>{
	private List<K> keys = new LinkedList<K>();
	private List<V> values = new LinkedList<V>();
	
	private class MapEntry implements Map.Entry<K,V>{
		int index = -1;
		public MapEntry(int index){
			this.index = index;
		}
		@Override
		public String toString(){
			return keys.get(index).toString()
					+ ":"
					+ values.get(index).toString();
		}
		@Override
		public K getKey() {
			return keys.get(index);
		}

		@Override
		public V getValue() {
			return values.get(index);
		}

		@Override
		public V setValue(V value) {
			V oldValue = values.get(index);
			values.set(index, value);
			return oldValue;
		}
		
		@Override
		public int hashCode(){
			return (index == -1 ? 0 : keys.get(index).hashCode());
		}
		
		@Override
		public boolean equals(Object obj){
			if(obj instanceof SlowMap.MapEntry){
				@SuppressWarnings({ "unchecked" })
				MapEntry me = (MapEntry)obj;
				return (index == me.index);
			}
			return false;
		}

	}
	
	///provide a view on the keys and values
	private class  EntrySet extends AbstractSet<Map.Entry<K, V>>{
		
		@Override
		public Iterator<Map.Entry<K, V>> iterator() {
			return new Iterator<Map.Entry<K, V>>(){
				int index = -1;
				@Override
				public boolean hasNext() {
					return ++index < keys.size();
				}

				@Override
				public MapEntry next() {
					return new MapEntry(index);
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
					
				}
			};
		}

		@Override
		public int size() {
			return keys.size();
		}
		
	}
	
	@Override

	public V get(Object key){
		if(keys.contains(key))
			return values.get(keys.indexOf(key));
		
		return null;
	}
	
	@Override 
	public V put(K key,V value){
		V oldValue = get(key);
		if(keys.contains(key)){
			values.set(keys.indexOf(key), value);
		}else{
			keys.add(key);
			values.add(value);
		}
		return oldValue;
	}
	
	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return new EntrySet();
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<keys.size();i++){
			sb.append(keys.get(i).toString());
			sb.append(":");
			sb.append(values.get(i).toString());
			sb.append("   ");
		}
		return sb.toString();
	}
	
	public static void main(String[] args){
		SlowMap<String,String> map = new SlowMap<String,String>();
		map.put("Beijing", "roast duck");
		map.put("Chongqing", "hot pot");
		map.put("Lanzou","noodles");
		System.out.println(map);
		System.out.println(map.get("Beijing"));
		System.out.println(map.get("Shanghai"));
		System.out.println("map size:"+map.size());
		for(Map.Entry<String, String> entry : map.entrySet()){
			System.out.println(entry);
			entry.setValue("new Value");
		}
		
		System.out.println(map);

	}
}
