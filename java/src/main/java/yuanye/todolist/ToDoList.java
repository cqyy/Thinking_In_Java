package yuanye.todolist;

import java.util.PriorityQueue;

public class ToDoList extends PriorityQueue<ToDoList.ToDoItem> {
	private static final long serialVersionUID = -1432358453335823511L;

	static class ToDoItem implements Comparable<ToDoItem> {
		private char priority;
		private int secondary;
		private String desc;

		public ToDoItem(char pri, int sec, String des) {
			priority = pri;
			secondary = sec;
			desc = des;
		}

		@Override
		public String toString() {
			return "" + priority + secondary + ":" + desc;
		}

		@Override
		public int compareTo(ToDoItem o) {
			if (priority > o.priority)
				return +1;
			if(priority == o.priority){
				if (secondary > o.secondary)
					return +1;
				else if (secondary == o.secondary)
					return 0;
			}
			return -1;
		}
	}
	
	
	public static void main(String[] args){
		ToDoList list = new ToDoList();
		list.add(new ToDoItem('B',2,"Have meal"));
		list.add(new ToDoItem('A',1,"Go to work"));
		list.add(new ToDoItem('C',1,"Go shopping"));
		list.add(new ToDoItem('B',1,"Badminton"));
		
		while(!list.isEmpty()){
			System.out.println(list.remove());
		}
	}
}
