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
		for(;time<180;time++)
		{
			if(!bus_wait.isEmpty() && time/5==1) {
				Bus start_bus = bus_wait.poll();
				start_bus.set_state(1);
				start_bus.set_arrive(time+gap_time);
				System.out.println(start_bus.id+" 号校车在"+time+"时刻发车");
			}
			//long l4 = System.currentTimeMillis();
			//int d = (int)l4%4;
			//if(d==0)
			//{
				Student student_arrive = create_stu();
				school_station[student_arrive.up].add_wait(student_arrive);
			//}
			for(int i = 0;i<bus_size ; i++)
			{
				if(school_bus[i].arrive_time == time)
				{
					int j = school_bus[i].go;//到达的站点
					//处理上下车
					for(Student getdown:school_bus[i].rider)
					{
						if(getdown.down == j)
						{
							school_bus[i].rider.remove(getdown);
							school_bus[i].ride_seat--;
							school_bus[i].empty_seat++;
						}
					}
					while(!school_station[j].wait_line.isEmpty()  && school_bus[i].empty_seat!=0)
					{
						Student getup = school_station[j].wait_line.poll();
						school_bus[i].rider.add(getup);
						school_bus[i].ride_seat++;
						school_bus[i].empty_seat--;
					}
					school_bus[i].set_arrive(time+gap_time);
					school_bus[i].set_from(school_bus[i].go);//从新的站点出发
					school_bus[i].set_go((school_bus[i].go+1)%sta_size);//新的目标站点
				}
			}
			
			if(time/20==0)//每隔20分钟打印一次
			{
				for(int i = 0;i<sta_size;i++)
				{
					System.out.print(i+" 号车站： ");
					for(int j = 0; j<school_station[i].wait_line.size();j++)
					System.out.print("1");
				}
			}
		}
	}
	public static Student create_stu() {
		long l1 = System.currentTimeMillis();
		int a = Math.abs((int)l1%sta_size);
		long l2 = System.currentTimeMillis();
		int b = Math.abs((int)l2%sta_size);
		Student now = new Student(a,b);
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
