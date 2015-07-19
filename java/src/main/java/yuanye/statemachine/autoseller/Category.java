package yuanye.statemachine.autoseller;

import static yuanye.statemachine.autoseller.Input.*;

import java.util.EnumMap;

public enum Category {
	MONEY(NICKEL,DIME,QUARTER,DOLLAR),
	ITEM(TOOTHPASTE,CHIPS,SODA,SOAP),
	QUIT_TRANSCATION(ABORT_TRANSACTION),
	SHUT_DOWN(STOP);
	
	private Input[] values;
	
	Category(Input... types){ values = types;}
	
	private static EnumMap<Input,Category> categories =
			new EnumMap<Input,Category>(Input.class);
	
	static{
		for(Category c : Category.class.getEnumConstants()){
			for(Input type : c.values){
				categories.put(type,c);
			}
		}
	}
	public static Category categorize(Input input){
		return categories.get(input);
	}
}
