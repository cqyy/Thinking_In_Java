package yuanye.complexModel.store;

import java.util.ArrayList;

public class Aisle extends ArrayList<Shelf>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2408739804353341929L;
	
	public Aisle(int nShelves,int nProducts){
		for(int i=0;i<nProducts;i++){
			add(new Shelf(nProducts));
		}
	}
	
	
}
