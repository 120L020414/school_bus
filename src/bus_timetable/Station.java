package bus_timetable;

import java.util.Queue;

public class Station {
	int id;
	int wait_num;
	Queue<Student> wait_line;
	public Station(int i) {
		id = i;
	}
	public void add_wait(Student a) {
		wait_line.add(a);
	}
	public void set_wait(int i) {
		// TODO Auto-generated method stub
		wait_num = i;
	}
}
