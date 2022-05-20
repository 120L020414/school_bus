package bus_timetable;

import java.util.LinkedList;
import java.util.Queue;

public class Station {
	int id;
	Queue<Student> wait_line = new LinkedList();
	public Station(int i) {
		id = i;
	}
	public void add_wait(Student a) {
		wait_line.add(a);
	}
	public Queue<Student> get_line() {
		Queue<Student> line = new LinkedList();
		line = wait_line;
		return line;
	}
}
