package yuanye.complexModel.store;

import java.util.Random;

public class Product {
	private final int id;
	private String description;
	private double price;
	
	public Product(int id,String dsc,double price){
		this.id =  id;
		this.description = dsc;
		this.price =  price;
	}
	
	@Override
	public String toString(){
		return id + "  " + description + "  $"+price;
	}
	
	public void changePrive(double price){
		this.price += price;
	}
	
	public static Generator<Product> generator = new Generator<Product>(){
		private Random random = new Random(47);
		public Product next(){
			return new Product(random.nextInt(1000),"product description",Math.round(random.nextDouble()*1000)+0.99);
		}
	};
}
