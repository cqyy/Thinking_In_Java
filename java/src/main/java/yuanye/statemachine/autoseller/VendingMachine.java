package yuanye.statemachine.autoseller;

class Generator{
	public Input next(){
		return Input.randSelection();
	}
}

public class VendingMachine {
	private static State state = State.RESTING;
	private static int amount = 0;
	private static Input selection = null;
	enum StateDuration{TRANSIENT};
	
	
	
	enum State{
		
		RESTING{
			@Override
			public void next(Input input){
				switch(Category.categorize(input)){
					case MONEY:
						amount += input.amount();
						state = State.ADD_MONEY;
						break;
					case SHUT_DOWN:
						state = TERMINAL;
					default:
				}
			}
		},
		ADD_MONEY{
			@Override
			public void next(Input input){
				switch(Category.categorize(input)){
				case MONEY:
					amount += input.amount();
					break;
				case ITEM:
					if(amount < input.amount()){
						System.out.println("not enough money for " + input);
					}else{
						selection = input;
						state = DESPENSING;
					}
					break;
				case QUIT_TRANSCATION:
					state = GIVING_CHANGE;
					break;
				case SHUT_DOWN:
					state = TERMINAL;
					break;
				default:
				}
			}
		},
		DESPENSING(StateDuration.TRANSIENT){
			@Override 
			public void next(){
				System.out.println("give you the goods : " + selection);
				amount -= selection.amount();
				state = GIVING_CHANGE;
			}
		},
		GIVING_CHANGE(StateDuration.TRANSIENT){
			@Override
			public void next(){
				if(amount > 0){
					System.out.println("give you the change : " + amount);
					amount = 0;
				}
				state = RESTING;
			}
		},
		TERMINAL{
			@Override
			public void output(){
				System.out.println("Halted");
		}
	};

		private boolean isTransient = false;
		
		public boolean isTransient(){
			return isTransient;
		}
		public void next(){
			throw new UnsupportedOperationException("next()");
		}
		public void next(Input input){
			throw new UnsupportedOperationException("next(Input int)");
		}
		public void output(){System.out.println(amount);}
		State(){};
		State(StateDuration sd){ isTransient = true; }

	}
	
	static void run(Generator gen){
		while(state != State.TERMINAL){
			state.next(gen.next());
			while(state.isTransient)
				state.next();
			state.output();
		}
	}
	
	public static void main(String[] args){
		Generator gen = new Generator();
		run(gen);
	}
	
}
