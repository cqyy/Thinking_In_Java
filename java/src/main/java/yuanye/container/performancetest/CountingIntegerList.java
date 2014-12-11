package yuanye.container.performancetest;

import java.util.AbstractList;

public class CountingIntegerList extends AbstractList<Integer>{
	private int[] data ;
	@Override
	public Integer get(int index) {
		if(index < 0 || index > data.length){
			throw new IndexOutOfBoundsException();
		}
		return data[index];
	}

	@Override
	public int size() {
		return data.length;
	}
	public CountingIntegerList(int size){
		if(size < 0){size = 0;}
		data = new int[size];
		for(int i = 0;i < size;i++){
			data[i] = i;
		}
	}
}
