package bus_timetable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Timetable {
	static int seat_size = 20;
    static int bus_size = 10;
	static int sta_size = 8;
	static int gap_time = 10;
	public static void main(String[] args)
	{
		Bus[] school_bus = new Bus[bus_size] ;
		Queue<Bus> bus_wait =new LinkedList();
		for(int i = 0;i<bus_size ; i++)
		{
			school_bus[i] = new Bus(i);
			bus_init(school_bus[i]);
			bus_wait.offer(school_bus[i]);
		}
		System.out.println("校车创建成功");
		Station[] school_station = new Station[sta_size];
		for(int i = 0;i<sta_size;i++)
		{
			school_station[i] = new Station(i);
			school_station[i].set_wait(0);
		}
		System.out.println("校车站创建成功");
		System.out.println("校车开始运行");
		int time=0;
		for(;time<540;time++)
		{
			if(!bus_wait.isEmpty() && time/5==0) {
				Bus start_bus = bus_wait.poll();
				start_bus.set_state(1);
			}
			Student student_arrive = create_stu();
			school_station[student_arrive.on].add_wait(student_arrive);
			for(int i = 0;i<bus_size ; i++)
			{
				school_bus[i] = new Bus(i);
				bus_init(school_bus[i]);
				bus_wait.offer(school_bus[i]);
			}
		}
	}
	public static Student create_stu() {
		long l1 = System.currentTimeMillis();
		int a = (int)l1%sta_size;
		long l2 = System.currentTimeMillis();
		int b = (int)l2%sta_size;
		long l3 = System.currentTimeMillis();
		int c = (int)l3%sta_size;
		Student now = new Student(a,b,c);
		return now;
	}
	public static void bus_init(Bus a)
	{
		a.set_empty(seat_size);
		a.set_ride(0);
		a.set_arrive(gap_time);
		a.set_from(0);
		a.set_go(1);
		a.set_state(0);
	}
}
