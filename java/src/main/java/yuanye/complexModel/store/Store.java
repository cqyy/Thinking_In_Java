package yuanye.complexModel.store;

import java.util.ArrayList;

//building up a complex model using generic containers 
public class Store extends ArrayList<Aisle> {

	private static final long serialVersionUID = -7367494312448384033L;
	
	public Store(int nAisles,int nShelves,int nProducts){
		for(int i = 0;i<nAisles;i++){
			add(new Aisle(nShelves,nProducts));
		}
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(Aisle a:this)
			for(Shelf s: a)
				for(Product p : s){
					sb.append(p);
					sb.append("\n");
				}
		return sb.toString();
	}
	
	public static void main(String[] args){
		System.out.println(new Store(2,2,3));
	}
}
