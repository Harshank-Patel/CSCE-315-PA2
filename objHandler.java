import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.sql.Array;

public class objHandler {
	protected static String windowType;
	public static boolean endWindows= false;
	public static String userType;
	public static String[] userInfo = new String[2];
	public static boolean isManager = false;
	public static String input_password;
	public static String CustFav_1;
	public static String CustFav_2;
	public static String topTrend_1;
	public static String topTrend_2;
	public static String botTrend_1;
	public static String botTrend_2;
	public static dbSetup my = new dbSetup();
	public static Connection conn = null;
	
	
public static void main(String[] args) {
    //Building the connection
    try {
       //Class.forName("org.postgresql.Driver");
       conn = DriverManager.getConnection(
         "jdbc:postgresql://csce-315-db.engr.tamu.edu/db905_group14_project2",
          my.user, my.pswd);
    } catch (Exception e) {
       e.printStackTrace();
       System.err.println(e.getClass().getName()+": "+e.getMessage());
       System.exit(0);
    }

	try {
		windowType= "signIn";
		while(endWindows==false) {
			windowView();
		}
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}

public static void windowView() {
	
	//Trending Items
	try{
		HashMap<String, Integer> Recent_Items = new HashMap<String,Integer>();
		HashMap<String, Integer> Recent_Items2 = new HashMap<String,Integer>();
		//create a statement object
	    Statement stmt2 = conn.createStatement();
	    //create an SQL statement
	    String sqlStatement2 = "SELECT COUNT(*) FROM public.\"Transaction_History\"";			    //send statement to DBMS
	    ResultSet result = stmt2.executeQuery(sqlStatement2);
	    System.out.println("SQL query");
	    result.next();
	    int TransCount = result.getInt("count");
	    
	    Statement stmt3 = conn.createStatement();
	    //create an SQL statement
	    String sqlStatement3 = "SELECT * FROM public.\"Transaction_History\" WHERE \"order_ID\" > " + (TransCount-100);			    //send statement to DBMS
	    result = stmt2.executeQuery(sqlStatement3);
	    
	    while (result.next()) {
	    	Array z = result.getArray("item_list");
	    	String[] items = (String[])z.getArray();
	    	for (int i =0; i < items.length;i++) {
	    		//System.out.println(items[i]);
	    		if(Recent_Items.containsKey(items[i]) == true) {
	    			Recent_Items.put(items[i],Recent_Items.get(items[i]) +1);
	    			Recent_Items2.put(items[i],Recent_Items2.get(items[i]) +1);
	    		}
	    		else {
	    			Recent_Items.put(items[i],1);
	    			Recent_Items2.put(items[i],1);
	    		}
	    	}
	    }
	    
	    
	    int maxOne = -1;
	    for (String key : Recent_Items.keySet()) {
	    	if(Recent_Items.get(key) > maxOne) {
	    		maxOne = Recent_Items.get(key);
	    		topTrend_1 = key;
	    	}
	    }
	    Recent_Items.put(topTrend_1, -1);
	    
	    int maxTwo = -1;
	    for (String key : Recent_Items.keySet()) {
	    	if(Recent_Items.get(key) > maxTwo) {
	    		maxTwo = Recent_Items.get(key);
	    		topTrend_2 = key;
	    	}
	    }
	    
	    int minOne = 10000;
	    for (String key : Recent_Items2.keySet()) {
	    	if(Recent_Items2.get(key) < minOne) {
	    		minOne = Recent_Items2.get(key);
	    		botTrend_1 = key;
	    	}
	    }
	    Recent_Items2.put(botTrend_1, 10000);
	    
	    int minTwo = 10000;
	    for (String key : Recent_Items2.keySet()) {
	    	if(Recent_Items2.get(key) < minTwo) {
	    		minTwo = Recent_Items2.get(key);
	    		botTrend_2 = key;
	    	}
	    }
	} catch (Exception e2) { }
    
	if(windowType.equals("signIn")) {
		HashMap<String, Integer> individual_Items = new HashMap<String,Integer>();
		Signin window = new Signin();
		window.open();
		userType=window.userType;
		userInfo[0]=window.userInfo[0];
		userInfo[1]=window.userInfo[1];
		System.out.println(userType);
		System.out.println(userInfo[0] + userInfo[1]);
		if(userType.equals("customer")) {
			windowType="customerView";
			isManager=false;
		    System.out.println("Opened database successfully");
		    try{
		    Statement stmt = conn.createStatement();
		    String sqlStatement = "SELECT * FROM public.\"Transaction_History\" WHERE \"user_ID\" = " + userInfo[0];
		    ResultSet result = stmt.executeQuery(sqlStatement);
		    System.out.println("SQL query");
		    
		    while (result.next()) {
			    	Array z = result.getArray("item_list");
			    	String[] items = (String[])z.getArray();
			    	for (int i =0; i < items.length;i++) {
			    		if(individual_Items.containsKey(items[i]) == true) {
			    			individual_Items.put(items[i],individual_Items.get(items[i]) +1);
			    		}
			    		else {
			    			individual_Items.put(items[i],1);
			    		}
			    	}
		    }
		    
		    int maxOne = -1;
		    for (String key : individual_Items.keySet()) {
		    	if(individual_Items.get(key) > maxOne) {
		    		maxOne = individual_Items.get(key);
		    		CustFav_1 = key;
		    	}
		    }
		    individual_Items.put(CustFav_1, -1);
		    
		    int maxTwo = -1;
		    for (String key : individual_Items.keySet()) {
		    	if(individual_Items.get(key) > maxTwo) {
		    		maxTwo = individual_Items.get(key);
		    		CustFav_2 = key;
		    	}
		    }
		    } catch (Exception e){
			     System.out.println(e);
			}
		}
		if(userType.equals("manager")) {
			windowType="ManagerView";
			isManager=true;
		}
		if(userType.equals("newUser")) {
			windowType="newUser";
			isManager=false;
			input_password = window.passEntry;
	}
		
	}
	else if(windowType.equals("customerView")) {
		CustomerView CustomerWindow = new CustomerView();
		CustomerWindow.user_ID = userInfo[0];
		CustomerWindow.username = userInfo[1];
		CustomerWindow.CustFav_1 = CustFav_1; 
		CustomerWindow.CustFav_2 = CustFav_2;
		CustomerWindow.topTrend_1 = topTrend_1;
		CustomerWindow.topTrend_2 = topTrend_2;
		CustomerWindow.open();
		windowType = CustomerWindow.nextWindow;
	}
	else if(windowType.equals("ManagerView")) {
		ManagerView ManagerWindow = new ManagerView();
		ManagerWindow.topTrend_1 = topTrend_1;
		ManagerWindow.topTrend_2 = topTrend_2;
		ManagerWindow.botTrend_1 = botTrend_1;
		ManagerWindow.botTrend_2 = botTrend_2;
		ManagerWindow.open();
		windowType = ManagerWindow.nextWindow;
		userInfo[0]=ManagerWindow.user_ID;
		userInfo[1]=ManagerWindow.username;
		
	}
	else if(windowType.equals("customerInfo")) {
		CustomerInfo CustomerInfoWindow = new CustomerInfo();
		CustomerInfoWindow.user_ID= userInfo[0];
		CustomerInfoWindow.username=userInfo[1];
		CustomerInfoWindow.isManager= isManager;
		CustomerInfoWindow.open();
		windowType=CustomerInfoWindow.nextWindow;
	}
	else if(windowType.equals("newUser")) {
		RegisterNewUser RegisterNewUserWindow = new RegisterNewUser();
		RegisterNewUserWindow.username = userInfo[1];
		RegisterNewUserWindow.password = input_password;
		System.out.println(userInfo[1] + ", " + input_password);
		RegisterNewUserWindow.open();
		windowType=RegisterNewUserWindow.nextWindow;
	}
	System.out.println("nextWindow");
}

}