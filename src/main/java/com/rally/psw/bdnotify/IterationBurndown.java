package com.rally.psw.bdnotify;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rally.psw.slope.CalculateSlope;
//import com.lexmark.burndownapp.BurndownCheck;
import com.rallydev.rest.RallyRestApi;
import com.rallydev.rest.request.QueryRequest;
import com.rallydev.rest.response.QueryResponse;
import com.rallydev.rest.util.Fetch;
import com.rallydev.rest.util.QueryFilter;
//import com.example.bdsched.BurndownCheck;

public class IterationBurndown {

	private QueryRequest iterationRequest;
	private QueryResponse iterationResponse;
	private JsonObject iterationJsonObject;
	private RallyRestApi restApi;
	
	private double totalTeamCapacity;
	private int startDay;
	private int dayCount;
	//private double totalToDo;
	private double dailyBurndown;
	//private double remainingBurndown;
	private int sprintSize;
	private boolean burndownViolation;
	private List<BurndownHelper> burndownList = new ArrayList<BurndownHelper>();
	
	
	public IterationBurndown(final String project, final Teams team) throws IOException {
		//System.out.println("           Project Name " + project + " Team Name: " + team);
		System.out.println("           Project Name " + "         Team Name ");
		System.out.println("            " + project + "           " + team + "\n");
		
		this.createIterationRequest(project);
		this.createIterationResponse();
		this.createIterationJsonObject();
		
		this.printIteration();
		this.calculateSprintSize();
		
		this.loadIterationData();
		this.setDailyBurndown();
		this.setStartDay();
		this.readCumulativeFlowData();
		
		System.out.println("\nDaily Burndown (Ideal Slope): " + this.dailyBurndown);
	}
	
	public void createIterationRequest(final String project) {
		System.out.println("           Setting Current Sprint Burndown Data\n");
		
		Calendar calendar = new GregorianCalendar();
		Date now = calendar.getTime();
		
		SimpleDateFormat  iso = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
		//System.out.println("ISO format date: " + iso.format(now) + " :: Day -> " + calendar.get(Calendar.DAY_OF_WEEK));
		
		this.iterationRequest = new QueryRequest("Iteration");
		
		iterationRequest.setFetch(new Fetch("ObjectID", "EndDate", "Name", "StartDate", "UserIterationCapacities"));
		iterationRequest.setQueryFilter(new QueryFilter("EndDate", ">", iso.format(now)));
		
		
		iterationRequest.setScopedDown(false);
		iterationRequest.setScopedUp(false);
		iterationRequest.setProject("/project/" + project);
	}
	
	private void createIterationResponse() {
		try {
			this.iterationResponse = this.getRestApi().query(this.iterationRequest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createIterationJsonObject() {
		this.iterationJsonObject = this.iterationResponse.getResults().get(0).getAsJsonObject();
	}
	
	private void loadIterationData() throws IOException {
		
		if(this.iterationResponse.wasSuccessful()) {
			calculateTotalTeamCapacity();
		}
	}
	
	private void printIteration() {
		String name = this.iterationJsonObject.get("Name").getAsString();
		String objectId = this.iterationJsonObject.get("ObjectID").getAsString();
		String startDate = this.iterationJsonObject.get("StartDate").getAsString();
		String endDate = this.iterationJsonObject.get("EndDate").getAsString();
		
		System.out.printf("[Sprint Name]: %s [Object ID]: %s [Start Date]: %s [End Date]: %s%n", name, objectId, startDate, endDate);
		System.out.println();
		/*System.out.println("Sprint Name                ObjectID              Start Date                 End Date");
		System.out.println(name + "            " + objectId + "            " + startDate + "            " + endDate + "\n");*/
	}
	
	private void calculateSprintSize() {
		String ISO8601DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
		String startDate = this.iterationJsonObject.get("StartDate").getAsString();
		String endDate = this.iterationJsonObject.get("EndDate").getAsString();
		
		final DateTimeFormatter formatter = DateTimeFormat.forPattern(ISO8601DATEFORMAT);
		DateTime startTime = formatter.parseDateTime(startDate);
		DateTime endTime = formatter.parseDateTime(endDate);
		Duration duration = new Duration(startTime, endTime);
		//this.sprintSize = endTime.getDayOfMonth() - startTime.getDayOfMonth();
		this.sprintSize = (int) duration.getStandardDays() + 1;
		System.out.println("Actual Sprint Duration: " + this.sprintSize);
		if(this.sprintSize == 28) {
			this.sprintSize = 20;
		} else {
			this.sprintSize = 10;
		}
			
		
		//System.out.println("Start Date: " + startTime.getDayOfMonth() + " End Date:: " + endTime.getDayOfMonth());
		System.out.println("Sprint Size: " + this.sprintSize + " day sprint\n");
	}
	
	private void calculateTotalTeamCapacity() throws IOException {
		
		int capacityCount = this.iterationJsonObject.getAsJsonObject("UserIterationCapacities").get("Count").getAsInt();
		System.out.println("Capacity Count: " + capacityCount);
		
		QueryRequest capRequest = new QueryRequest(this.iterationJsonObject.getAsJsonObject("UserIterationCapacities"));
		capRequest.setFetch(new Fetch("Capacity"));
		
		JsonArray capacities = restApi.query(capRequest).getResults();
		
		for (int j=0; j < capacityCount; j++){
			if(capacities.get(j).getAsJsonObject().get("Capacity").isJsonNull() ){
				System.out.println("Null capacity found");
				continue;
			} else {
				System.out.println(capacities.get(j).getAsJsonObject().get("Capacity").getAsString());
				this.totalTeamCapacity += capacities.get(j).getAsJsonObject().get("Capacity").getAsDouble();
			}
			//continue;
	    }
		System.out.println("Total team capacity: " + this.totalTeamCapacity + "\n");
				
	}

	private void readCumulativeFlowData() throws IOException {
		QueryRequest flowRequest = new QueryRequest("IterationCumulativeFlowData");
		flowRequest.setFetch(new Fetch("IterationObjectID", "CardCount", "CardEstimateTotal", "CardState", "CardToDoTotal", "TaskEstimateTotal"));
		
		flowRequest.setQueryFilter( new QueryFilter("IterationObjectID", "=", this.iterationJsonObject.get("ObjectID").getAsString()));
		
		QueryResponse res = restApi.query(flowRequest);
		
		if(res.wasSuccessful()) {
			//System.out.println("CumulativeIteration response success: Result size = " + res.getResults().size());
			/*JsonObject obj = res.getResults().get(0).getAsJsonObject();
			
			System.out.println("Card Estimate Total: " + obj.get("CardEstimateTotal").getAsString());*/
			//filter.and(q)
			Calendar calendar = new GregorianCalendar();
			calendar.set(Calendar.DAY_OF_WEEK, this.startDay);
			int count = 0;
			double burndown = 0;
			System.out.println("Card State\t Card Count\t Card Estimate Total\t ToDo Total\t Total Task Estimate\t Daily Burndown");
			for(JsonElement result : res.getResults()) {
				JsonObject obj = result.getAsJsonObject();
				
				if(obj.get("CardState").getAsString().equals("In-Progress")) {
					calendar.add(Calendar.DAY_OF_WEEK, count);
					//System.out.println("DAY OF THE WEEK is: " + calendar.get(Calendar.DAY_OF_WEEK));
					if(calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY &&
							calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
					
					this.dayCount++; count = 1;
					burndown = this.totalTeamCapacity - (this.dailyBurndown * this.dayCount);
					//this.totalToDo = obj.get("CardToDoTotal").getAsDouble();
					/*System.out.println( "Card State: " + obj.get("CardState").getAsString() +
							" Card Count: " + obj.get("CardCount").getAsString() +
							" Card Estimate Total: " + obj.get("CardEstimateTotal").getAsString() +  
							" ToDo Total: " + obj.get("CardToDoTotal").getAsString() + 
							" Total Task Estimate: " + obj.get("TaskEstimateTotal").getAsString() +
							" Daily burndown value: " + burndown);*/
					
					
					System.out.println(obj.get("CardState").getAsString() + "\t\t" +
							           obj.get("CardCount").getAsString() + "\t\t" +
							           obj.get("CardEstimateTotal").getAsString() + "\t\t\t" +
							           obj.get("CardToDoTotal").getAsString() + "\t\t" +
							           obj.get("TaskEstimateTotal").getAsString() + "\t\t\t" +
							           burndown);
					this.burndownList.add(new BurndownHelper(obj.get("CardToDoTotal").getAsDouble(), burndown));
					}
				}
				
			}
		}
	}
	
	public boolean isBurndownViolation() {
		double lastDayBurndown = 0;
		double secondLastDayBurndown = 0;
		this.burndownViolation = false;
		
		int listSize = this.burndownList.size();
	//System.out.println("List Size: " + listSize);	
		if(listSize > 1) {
			BurndownHelper bd = this.burndownList.get(listSize -1);
			lastDayBurndown = bd.getRemainingBurndown() - bd.getTotalTodo();
			
			bd = this.burndownList.get(listSize -2);
			secondLastDayBurndown = bd.getRemainingBurndown() - bd.getTotalTodo();
		}
			System.out.println("\nLast Day Burndown: " + lastDayBurndown + " Last but one day Burndown: " + secondLastDayBurndown + "\n");
			if(lastDayBurndown < 0 && secondLastDayBurndown < 0)
				this.burndownViolation = true;
			else
				this.burndownViolation = false;
			
			return this.burndownViolation;
		
	}
	
	public void calculateBurndownSlope() {
		List<Double> todoList = new ArrayList<Double>();
		
		for(BurndownHelper helper : this.burndownList) {
			todoList.add(helper.getTotalTodo());
		}
		
		CalculateSlope slope = new CalculateSlope(todoList);
		slope.findSlope();
	}
	
	private void setDailyBurndown() {
		this.dailyBurndown = this.totalTeamCapacity / this.sprintSize;
	}
	
	private void setStartDay() {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		
		this.startDay = calendar.get(Calendar.DAY_OF_WEEK);
		
		//System.out.println("Start Day of the week is: " + this.startDay);
		//calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	
	
	public RallyRestApi getRestApi() {
		try {
			this.restApi = SetupRallyService.getRallyRestApi();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.restApi;
	}
}
