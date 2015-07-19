package yuanye.statemachine.autoseller;

import java.util.Random;



/**
 *define all input to the auto sellers,
 *include money amount and input event
 *@author YuanYe
 */
public enum Input {
	NICKEL(5),DIME(10),QUARTER(25),DOLLAR(100),
	TOOTHPASTE(200),CHIPS(75),SODA(100),SOAP(50),
	ABORT_TRANSACTION{
		@Override
		public int amount(){
			throw new RuntimeException("Abort.amount()");
		}
	},
	STOP{
		@Override
		public int amount(){
			throw new RuntimeException("Stop.amount()");
		}
	};
	
	
	int value;
	
	Input(int v){value = v;}
	Input(){};
	
	int amount(){
		return value;
	}
	
	private static Random rand = new Random(13);
	
	public static Input randSelection(){
		return Input.values()[rand.nextInt(Input.values().length)];
	}
}
