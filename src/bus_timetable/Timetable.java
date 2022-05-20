package bus_timetable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Timetable {
	static int seat_size = 30;
    static int bus_size = 6;
	static int sta_size = 10;
	static int gap_time = 5;
	static int start_gap = 5;
	static int time_size = 720;
	static int bus_satisfy = 0;
	static int people_satisfy = 0;
	static int people_num = 0;
	static Bus[] school_bus = new Bus[bus_size] ;//所有校车
	static Station[] school_station = new Station[sta_size];//所有车站
	
	public static void main(String[] args)
	{
		float aver1 = 0;
		float aver2 = 0;
		for(int o = 5 ; o < 60 ; o = o + 5)
		{
			start_gap = o;
			bus_satisfy = 0;
			people_num = 0;
			people_satisfy = 0;//初始化
			
			Queue<Bus> bus_wait =new LinkedList();
			for(int i = 0;i<bus_size ; i++)
			{
				school_bus[i] = new Bus(i);
				bus_init(school_bus[i]);
				bus_wait.offer(school_bus[i]);
			}
			//System.out.println("校车创建成功");
			
			for(int i = 0;i<sta_size;i++)
			{
				school_station[i] = new Station(i);
			}
			//System.out.println("校车站创建成功");
			//System.out.println("校车开始运行");
			int time=0;
			for(;time<time_size;time++)
			{
				
//				//等间隔发车法
//				if(!bus_wait.isEmpty() && time/start_gap==1) {
//					Bus start_bus = bus_wait.poll();
//					start_bus.set_state(1);
//					start_bus.set_arrive(time+gap_time);
//					//System.out.println("******"+start_bus.id+" 号校车在"+time+"时刻发车"+"******");
//				}
				
				
				//调度算法发车法
				//System.out.println("******现在乘客满意度为"+start_now(time)+"******");
				//System.out.println("******现在乘客满意度平均为"+(float)start_now(time)/(people_num+1)+"******");
				if(!bus_wait.isEmpty() && start_now(time)/(people_num + 1) >1.5) {
					Bus start_bus = bus_wait.poll();
					start_bus.set_state(1);
					start_bus.set_arrive(time+gap_time);
					//System.out.println("******"+start_bus.id+" 号校车在"+time+"时刻发车"+"******");
				}
				
				for(int i = 0;i<bus_size ; i++)
				{
					if(school_bus[i].arrive_time == time)
					{
						int j = school_bus[i].go;//到达的站点
						
						//处理下车
						int down_num = 0;
						for(int k = 0; k < school_bus[i].rider.size();k++)
						{
							Student getdown = school_bus[i].rider.get(k);
							if(getdown.down == j)
							{
								school_bus[i].rider.remove(getdown);
								school_bus[i].ride_seat--;
								school_bus[i].empty_seat++;
								down_num++;
							}
						}
						//System.out.println("******"+i+" 号校车在"+j+"站点下车"+down_num+"人******");
						//处理上车
						int up_num = 0;
						while(!school_station[j].wait_line.isEmpty()  && school_bus[i].empty_seat!=0)
						{
							Student getup = school_station[j].wait_line.poll();
							int t = getup.wait_time;
							school_bus[i].rider.add(getup);
							school_bus[i].ride_seat++;
							school_bus[i].empty_seat--;
							people_satisfy += time - t;
							up_num++;
						}
						if(school_bus[i].ride_seat==0 )//计算校车空闲时间
						{
							bus_satisfy += gap_time;
							//ASystem.out.println("******"+i+" 号校车在"+time+"空闲"+bus_satisfy+"******");
						}
						//System.out.println("******"+i+" 号校车在"+j+"站点上车"+up_num+"人******");
						school_bus[i].set_arrive(time+gap_time);
						school_bus[i].set_from(school_bus[i].go);//从新的站点出发
						school_bus[i].set_go((school_bus[i].go+1)%sta_size);//新的目标站点
						if(school_bus[i].go==0)//如果到了始发站，需要再一次等待发车
						{
							 bus_init(school_bus[i]);
							 bus_wait.offer(school_bus[i]);
						}
					}
				}
				
//				if(true)//打印一次
//				{
//					System.out.println("现在时刻："+time+"");
//					for(int i = 0;i<sta_size;i++)
//					{
//						System.out.print(i+" 号车站： ");
//						for(int j = 0; j<school_station[i].wait_line.size();j++)
//						System.out.print("1");
//						System.out.print("\n");
//					}
//					System.out.print("\n");
//	
//				}
				
				//long l3 = System.currentTimeMillis();
				//int flag = Math.abs((int)l3%2);  //是否要创建学生也是随机生成的
				//if(flag==1){
					Student student_arrive = create_stu(time);//创建新学生
				    school_station[student_arrive.up].add_wait(student_arrive);//加到相应车站
				    Student student_arrive2 = create_stu(time);
				    school_station[(student_arrive2.up+3)%sta_size].add_wait(student_arrive2);
				    people_num = people_num + 2;
				//}
			}
			//时间循环完毕
			float aver_time = (float)people_satisfy/(float)people_num;
			float aver_empty = (float)bus_satisfy/(float)(bus_size*time_size);
			//等间隔发车打印
			//System.out.println("时间长度为"+time_size+"校车发车间隔为"+start_gap+"时"+(int)people_num+"个乘客平均等车时间为"+aver_time+"  校车空闲率为"+aver_empty);
			//调度算法发车打印
			System.out.println("时间长度为"+time_size+"时使用调度算法"+(int)people_num+"个乘客平均等车时间为"+aver_time+"  校车空闲率为"+aver_empty);
			aver1 = aver1 + aver_time;
			aver2 = aver2 + aver_empty;
		}
		System.out.println("学生平均等车时间为"+aver1+"校车平均空弦率为"+aver2);
	}
	//产生一个学生
	public static Student create_stu(int t) {
		long l1 = System.currentTimeMillis();
		int a = Math.abs((int)l1%sta_size);
		long l2 = System.currentTimeMillis();
		int b = Math.abs((int)l2%sta_size);
		Student now = new Student(a,b,t);
		return now;
	}
	//初始化校车
	public static void bus_init(Bus a)
	{
		a.set_empty(seat_size);
		a.set_ride(0);
		a.set_arrive(-1);
		a.set_from(-1);
		a.set_go(0);
		a.set_state(0);
	}
	//计算当前是否要发车
	public static int start_now(int time) {
		int student_cost = 0;
		for(int i = 0 ;i < sta_size; i++)
		{
			for(Student s : school_station[i].get_line())
			{
				student_cost +=time - s.wait_time;
				//System.out.println("现在总的等待时间"+student_cost);
			}
		}
		return student_cost; 
	}
}
