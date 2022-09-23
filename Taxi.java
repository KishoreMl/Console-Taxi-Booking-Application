package Task1;
import java.util.*;
import Task1.TravelHistory;
public class Taxi {
	
	public int TaxiId;
	public boolean isFree=true;
	public int currentLocation;
	public int totalCost=0;
	public ArrayList<Object> travelHistory = new ArrayList<>();
	
	
	public void addTravelHistory(String pickup,String drop,String starttime,String endtime,int amount)
	{
	  TravelHistory obj = new TravelHistory();
	  
	  obj.pickup = pickup;
	  obj.drop = drop;
	  obj.amount = amount;
	  obj.startTime = starttime;
	  obj.endTime = endtime;  
	  
	  travelHistory.add(obj);
	}
	
	public void displayHistory(int taxiId)
	{
		System.out.println("Travel History of Taxi "+taxiId);
		for(int i=0;i<travelHistory.size();i++)
		{
			TravelHistory history = (TravelHistory) travelHistory.get(i);
			System.out.println(i+1+" "+history.pickup+" "+history.drop+" "+history.startTime+" "+history.endTime+" "+history.amount);
		}
		System.out.println();
	}
	
}
