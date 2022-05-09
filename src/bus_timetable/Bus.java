package bus_timetable;

import java.util.ArrayList;
import java.util.List;

public class Bus {
	int id ;
	int empty_seat ;
	int ride_seat ;
	int arrive_time;
	int from ;
	int go ;
	int state ;
	List<Student> rider = new ArrayList();
	public  Bus(int i) {
		id = i;
	}
	public void set_empty(int i){
		empty_seat = i;
	}
	public void set_ride(int j){
		ride_seat = j;
	}
	public void set_arrive(int gap_time) {
		// TODO Auto-generated method stub
		arrive_time = gap_time;
	}
	public void set_from(int j){
		from = j;
	}
	public void set_go(int i){
		go = i;
	}
	public void set_state(int j){
		state = j;
	}
	public void add_rider(Student now) {
		rider.add(now);
	}

}
