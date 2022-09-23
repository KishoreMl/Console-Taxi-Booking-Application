package Task1;
import java.util.*;
import Task1.Taxi;
public class CallTaxiBooking {
	public static HashMap<String,Integer> location = new HashMap<>();
	public static HashMap<Integer,Object> taxies = new HashMap<>();
	
	public static Scanner input = new Scanner(System.in);
	
	//Loading initial 5 taxi datas into HashMap 
	public static void loadTaxiData()
	{
		for(int i=1;i<=5;i++)
		{
			Taxi taxi = new Taxi();
			taxi.TaxiId=i;
			taxi.currentLocation=1;
			taxies.put(taxi.TaxiId, taxi);
		}
	}
	
	public static Taxi getMinimumCostTaxi(ArrayList<Object> taxies)
	{
		int min = 99999;
		Taxi finalTaxi=null;
		for(int i=0;i<taxies.size();i++)
		{
			Taxi taxi = (Taxi) taxies.get(i);
			
			if(taxi.totalCost<min)
			{
				//System.out.println(taxi.totalCost);
				min = taxi.totalCost;
				finalTaxi = taxi;
			}
		}
		System.out.println(finalTaxi.TaxiId);
		return finalTaxi;
	}
	//Searching for Near Taxi
	public static Taxi getTaxi(int start,int end)
	{
		int min = 5;
		Taxi taxi=null;
		if(taxies.size()==0)
			return null;
		
		ArrayList<Object> totalTaxies = new ArrayList<>();
		for(Map.Entry m :taxies.entrySet())
		{
			Taxi obj =(Taxi)m.getValue();
			int val = obj.currentLocation-start>0?obj.currentLocation-start:start-obj.currentLocation;
			
			if(val<=min && obj.isFree)
			{
				min = val;
				taxi = obj;
			}
		}
	
		for(Map.Entry m : taxies.entrySet())
		{
			Taxi obj = (Taxi)m.getValue();
			int val = obj.currentLocation-start>0?obj.currentLocation-start:start-obj.currentLocation;
			if(val==min && obj.isFree)
			{
				totalTaxies.add(obj);
			}
			
		}
		if(totalTaxies.size()>1)
		{
			//System.out.println("Function called");
			return getMinimumCostTaxi(totalTaxies);
		}
		else if(start-1<min)
			return null;
		else 
			return taxi;
					
	}
	
	public static void assignNewTaxi(int start,int end,String pickup,String drop,String starttime,String endtime)
	{
		int taxiId =taxies.size()+1;
		System.out.println("Taxi allocated: "+taxiId);
		Taxi taxi = new Taxi();
		taxi.TaxiId = taxiId;
		taxi.currentLocation  = end;
		
		//cost calculation
		int val = end-start>0?end-start:start-end;
		int amount = (val*15)*10;
		amount-=((start-1)*15)*5;
		
		//removing am or pm from user input time
		String times[] = starttime.split(" ");
		
		//adding travel history to the taxi 
		taxi.addTravelHistory(pickup,drop,times[0],endtime,amount);
		
		
		taxi.totalCost+=amount;//updating total cost for a taxi
		
		//adding the new taxi to hashmap
		taxies.put(taxiId, taxi);
		
	}
	
	//Time Calculation
	public static String calculateTime(String time,int start,int end)
	{
		
		try {
			String splittime[] = time.split(" ");
			String splittime2[] = splittime[0].split(":");
			int gaps = start>end?start-end:end-start;
			int hours = Integer.parseInt(splittime2[0]);
			int min = Integer.parseInt(splittime2[1]);
			int val=min+15*gaps;
			hours+=val/60;
			hours%=24;
			min=val%60;
			
			String endtime=hours+":"+min;
			
			return endtime;
		}
		catch(Exception e)
		{
			System.out.println("Invalid time");
			displayMenu();
			return "00:00";
		}
	}
	
	//Getting Taxi Booking Deatils as User input 
	public static void getInput()
	{
		
		String custId = input.nextLine();
		String startingPoint = input.nextLine();
		String destination = input.nextLine();
		String time = input.nextLine();
		
		int start=-1,end=-1;
		
		for(Map.Entry m:location.entrySet())
		{
			if(startingPoint.equals((String)m.getKey()))
			{
				start=(Integer)m.getValue();
			}
			else if(destination.equals((String)m.getKey()))
			{
				end = (Integer)m.getValue();
			}
		}
		
		//getting nearer Taxi
		Taxi taxi=getTaxi(start,end);
		
		//time calculation
		String endtime = calculateTime(time,start,end);
		
		if(taxi==null)
		{
			//assigning new taxi if no taxi is near
			assignNewTaxi(start,end,startingPoint,destination,time,endtime);
			displayMenu();
		}
		else
		{
			//cost calculation
			int val = end-start>0?end-start:start-end;
			int amount = (val*15)*10;
			amount-=((start-1)*15)*5;
			
			taxi.totalCost+=amount; //updating total cost for a taxi
			taxi.currentLocation = end;
			
			
			//adding travel history to the selected taxi4
			taxi.addTravelHistory(startingPoint, destination, time, endtime,amount);
			
			//Printing allocated Taxi
			System.out.println("Taxi allocated: "+taxi.TaxiId);
			displayMenu();
		}
		
	}
	
	//display Travel history for selected Taxi
	public static void displayTravelHistory()
	{
		System.out.println("Enter Taxi ID");
		String tId = input.nextLine();
		int taxiId = Integer.parseInt(tId);
		Taxi resultTaxi=null;
		for(Map.Entry m: taxies.entrySet())
		{
			Taxi taxi = (Taxi)m.getValue();
			if(taxiId==taxi.TaxiId)
				resultTaxi=taxi;
		}
		if(resultTaxi==null)
		{
			System.out.println("Taxi not found");
			displayMenu();
		}
		else
		{
			resultTaxi.displayHistory(resultTaxi.TaxiId);
			displayMenu();
		}
		
	}
	
	//User Menu
	public static void displayMenu()
	{
		 
		 System.out.println("1.Book Taxi \n2.Travel History \n3.Assign FreeWindow \n4.Exit");
		 String choice = input.nextLine();
		 int ch = Integer.parseInt(choice);
		 switch(ch)
		 {
		 case 1:getInput();
		   break;
		 case 2:displayTravelHistory();
		   	break;
		 case 3:assignFreeTime();
		 	break;
		 case 4:exit();
		 	break;
		 default:
			 System.out.println("Invalid option");
			 displayMenu();
		 }
		 
	}
	
	//Assigning FreeWindow for a taxi
	public static void assignFreeTime()
	{
		System.out.println("Enter Taxi ID");
		String tId = input.nextLine();
		int taxiId = Integer.parseInt(tId);
		Taxi resultTaxi = null;
		
		for(Map.Entry m: taxies.entrySet())
		{
			Taxi taxi = (Taxi)m.getValue();
			if(taxiId==taxi.TaxiId)
				resultTaxi=taxi;
		}
		if(resultTaxi==null)
		{
			System.out.println("Taxi not found");
			displayMenu();
		}
		else
		{
			resultTaxi.isFree = false;
			displayMenu();
		}
		
		
	}
	
	//Program exit function
	public static void exit()
	{
		System.out.println("Logged out");
	}
	
	
	public static void main(String args[])
	{
		 location.put("DLF", 1);
		 location.put("Velachery", 2);
		 location.put("Tambaram",3);
		 location.put("TNagar",4);
		 location.put("Nungambakam", 5);
		 loadTaxiData();
		 
		 displayMenu();
		 
	}
}
