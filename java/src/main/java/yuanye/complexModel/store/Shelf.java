package yuanye.complexModel.store;

import java.util.ArrayList;

public class Shelf extends ArrayList<Product> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6059342617574601641L;

	public Shelf(int nProducts){
		Generators.fill(this, Product.generator, nProducts);
	}
	
	
}
