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
	static Bus[] school_bus = new Bus[bus_size] ;//����У��
	static Station[] school_station = new Station[sta_size];//���г�վ
	
	public static void main(String[] args)
	{
		float aver1 = 0;
		float aver2 = 0;
		for(int o = 5 ; o < 60 ; o = o + 5)
		{
			start_gap = o;
			bus_satisfy = 0;
			people_num = 0;
			people_satisfy = 0;//��ʼ��
			
			Queue<Bus> bus_wait =new LinkedList();
			for(int i = 0;i<bus_size ; i++)
			{
				school_bus[i] = new Bus(i);
				bus_init(school_bus[i]);
				bus_wait.offer(school_bus[i]);
			}
			//System.out.println("У�������ɹ�");
			
			for(int i = 0;i<sta_size;i++)
			{
				school_station[i] = new Station(i);
			}
			//System.out.println("У��վ�����ɹ�");
			//System.out.println("У����ʼ����");
			int time=0;
			for(;time<time_size;time++)
			{
				
//				//�ȼ��������
//				if(!bus_wait.isEmpty() && time/start_gap==1) {
//					Bus start_bus = bus_wait.poll();
//					start_bus.set_state(1);
//					start_bus.set_arrive(time+gap_time);
//					//System.out.println("******"+start_bus.id+" ��У����"+time+"ʱ�̷���"+"******");
//				}
				
				
				//�����㷨������
				//System.out.println("******���ڳ˿������Ϊ"+start_now(time)+"******");
				//System.out.println("******���ڳ˿������ƽ��Ϊ"+(float)start_now(time)/(people_num+1)+"******");
				if(!bus_wait.isEmpty() && start_now(time)/(people_num + 1) >1.5) {
					Bus start_bus = bus_wait.poll();
					start_bus.set_state(1);
					start_bus.set_arrive(time+gap_time);
					//System.out.println("******"+start_bus.id+" ��У����"+time+"ʱ�̷���"+"******");
				}
				
				for(int i = 0;i<bus_size ; i++)
				{
					if(school_bus[i].arrive_time == time)
					{
						int j = school_bus[i].go;//�����վ��
						
						//�����³�
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
						//System.out.println("******"+i+" ��У����"+j+"վ���³�"+down_num+"��******");
						//�����ϳ�
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
						if(school_bus[i].ride_seat==0 )//����У������ʱ��
						{
							bus_satisfy += gap_time;
							//ASystem.out.println("******"+i+" ��У����"+time+"����"+bus_satisfy+"******");
						}
						//System.out.println("******"+i+" ��У����"+j+"վ���ϳ�"+up_num+"��******");
						school_bus[i].set_arrive(time+gap_time);
						school_bus[i].set_from(school_bus[i].go);//���µ�վ�����
						school_bus[i].set_go((school_bus[i].go+1)%sta_size);//�µ�Ŀ��վ��
						if(school_bus[i].go==0)//�������ʼ��վ����Ҫ��һ�εȴ�����
						{
							 bus_init(school_bus[i]);
							 bus_wait.offer(school_bus[i]);
						}
					}
				}
				
//				if(true)//��ӡһ��
//				{
//					System.out.println("����ʱ�̣�"+time+"");
//					for(int i = 0;i<sta_size;i++)
//					{
//						System.out.print(i+" �ų�վ�� ");
//						for(int j = 0; j<school_station[i].wait_line.size();j++)
//						System.out.print("1");
//						System.out.print("\n");
//					}
//					System.out.print("\n");
//	
//				}
				
				//long l3 = System.currentTimeMillis();
				//int flag = Math.abs((int)l3%2);  //�Ƿ�Ҫ����ѧ��Ҳ��������ɵ�
				//if(flag==1){
					Student student_arrive = create_stu(time);//������ѧ��
				    school_station[student_arrive.up].add_wait(student_arrive);//�ӵ���Ӧ��վ
				    Student student_arrive2 = create_stu(time);
				    school_station[(student_arrive2.up+3)%sta_size].add_wait(student_arrive2);
				    people_num = people_num + 2;
				//}
			}
			//ʱ��ѭ�����
			float aver_time = (float)people_satisfy/(float)people_num;
			float aver_empty = (float)bus_satisfy/(float)(bus_size*time_size);
			//�ȼ��������ӡ
			//System.out.println("ʱ�䳤��Ϊ"+time_size+"У���������Ϊ"+start_gap+"ʱ"+(int)people_num+"���˿�ƽ���ȳ�ʱ��Ϊ"+aver_time+"  У��������Ϊ"+aver_empty);
			//�����㷨������ӡ
			System.out.println("ʱ�䳤��Ϊ"+time_size+"ʱʹ�õ����㷨"+(int)people_num+"���˿�ƽ���ȳ�ʱ��Ϊ"+aver_time+"  У��������Ϊ"+aver_empty);
			aver1 = aver1 + aver_time;
			aver2 = aver2 + aver_empty;
		}
		System.out.println("ѧ��ƽ���ȳ�ʱ��Ϊ"+aver1+"У��ƽ��������Ϊ"+aver2);
	}
	//����һ��ѧ��
	public static Student create_stu(int t) {
		long l1 = System.currentTimeMillis();
		int a = Math.abs((int)l1%sta_size);
		long l2 = System.currentTimeMillis();
		int b = Math.abs((int)l2%sta_size);
		Student now = new Student(a,b,t);
		return now;
	}
	//��ʼ��У��
	public static void bus_init(Bus a)
	{
		a.set_empty(seat_size);
		a.set_ride(0);
		a.set_arrive(-1);
		a.set_from(-1);
		a.set_go(0);
		a.set_state(0);
	}
	//���㵱ǰ�Ƿ�Ҫ����
	public static int start_now(int time) {
		int student_cost = 0;
		for(int i = 0 ;i < sta_size; i++)
		{
			for(Student s : school_station[i].get_line())
			{
				student_cost +=time - s.wait_time;
				//System.out.println("�����ܵĵȴ�ʱ��"+student_cost);
			}
		}
		return student_cost; 
	}
}
