import java.sql.*;
import java.util.HashMap;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.DateTime;

public class ManagerView {

	protected Shell shell;
	private Text Name_Field;
	private Text Availability_Field;
	private Text Cost_Field;
	private Text Category_Field;
	public String nextWindow;
	public static Statement stmt;
	public static ResultSet result;
	public static Connection conn;
	public static dbSetup my;
	public static boolean isManager=true;
	private Text inp_name2;
	private Text inp_user_ID;
	public String user_ID;
	public String username;
	private Table menu_table;
	private Text text_2;
	private Text text_3;
	public static String topTrend_1;
	public static String topTrend_2;
	public static String botTrend_1;
	public static String botTrend_2;
	private Text MenuID_Field;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ManagerView window = new ManagerView();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(1142, 713);
		shell.setText("SWT Application");
		shell.setLayout(null);
		
		MenuID_Field = new Text(shell, SWT.BORDER);
		MenuID_Field.setBounds(201, 50, 399, 26);
		
		Name_Field = new Text(shell, SWT.BORDER);
		Name_Field.setBounds(201, 82, 399, 26);
		
		Availability_Field = new Text(shell, SWT.BORDER);
		Availability_Field.setBounds(201, 146, 399, 26);
		
		Cost_Field = new Text(shell, SWT.BORDER);
		Cost_Field.setBounds(201, 178, 399, 26);
		
		Category_Field = new Text(shell, SWT.BORDER);
		Category_Field.setBounds(201, 114, 399, 26);
		
		menu_table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		menu_table.setBounds(712, 50, 391, 425);
		menu_table.setHeaderVisible(true);
		menu_table.setLinesVisible(true);
		TableColumn tblclmnID = new TableColumn(menu_table, SWT.NONE);
		tblclmnID.setWidth(34);
		tblclmnID.setText("ID");
		TableColumn tblclmnItem = new TableColumn(menu_table, SWT.NONE);
		tblclmnItem.setWidth(150);
		tblclmnItem.setText("Item");
		TableColumn tblclmnPrice = new TableColumn(menu_table, SWT.NONE);
		tblclmnPrice.setWidth(52);
		tblclmnPrice.setText("Price");
		TableColumn tblclmnCategory = new TableColumn(menu_table, SWT.NONE);
		tblclmnCategory.setWidth(61);
		tblclmnCategory.setText("Category");
		TableColumn tblclmnAvailable = new TableColumn(menu_table, SWT.NONE);
		tblclmnAvailable.setWidth(90);
		tblclmnAvailable.setText("Available");
		
		/*START THE DATABASE*/
		my = new dbSetup();
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/db905_group14_project2", my.user, my.pswd);
		} catch (Exception e1) {
			e1.printStackTrace();
			System.err.println(e1.getClass().getName()+": "+e1.getMessage());
			System.exit(0);
		}
		/*START THE DATABASE*/
		
		try{
			Statement stmt = null;
			try {
				stmt = conn.createStatement();
			} catch (SQLException e2) { e2.printStackTrace(); }
			String sqlStatement = "SELECT * FROM public.\"Menu\"";
		    ResultSet result = stmt.executeQuery(sqlStatement);
		    System.out.println("SQL query");
		    while (result.next()) {
					TableItem tableItem = new TableItem(menu_table, SWT.NONE);
					tableItem.setText(new String[] {result.getString("menu_ID"),result.getString("name"),result.getString("item_cost"),result.getString("category"),result.getString("availability")});
			}
		} catch (Exception e3){ System.out.println(e3); }
		
		/*CLOSE DATABASE */
		try {
		    conn.close();
		    System.out.println("Connection Closed.");
		  } 
		  catch(Exception e1) {
		    System.out.println("Connection NOT Closed.");
		}
		/*CLOSE DATABASE */
		
		Button Update_Button = new Button(shell, SWT.NONE);
		Update_Button.setBounds(293, 219, 105, 30);
		Update_Button.setText("Add/Edit Item");
		Update_Button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				/*START THE DATABASE*/
				my = new dbSetup();
				try {
					conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/db905_group14_project2", my.user, my.pswd);
				} catch (Exception e1) {
					e1.printStackTrace();
					System.err.println(e1.getClass().getName()+": "+e1.getMessage());
					System.exit(0);
				}
				/*START THE DATABASE*/
				
				/*GET INFORMATION*/
				String item_ID = MenuID_Field.getText();
				String item_Name = Name_Field.getText();
				String item_Type = Category_Field.getText();
				String item_Avalability = Availability_Field.getText();
				String item_Cost = Cost_Field.getText();
				/*GET INFORMATION*/
				
				Statement stmt = null;
				try {
					stmt = conn.createStatement();
				} catch (SQLException e2) { e2.printStackTrace(); }
				
				String sqlStatementt = "SELECT EXISTS(SELECT * FROM public.\"Menu\" WHERE \"menu_ID\" = \'" + item_ID + "\');";
				//System.out.println(sqlStatementt);
				String itemExists = "f";
				ResultSet result = null;
				try {
					result = stmt.executeQuery(sqlStatementt);
					while (result.next()) {
						itemExists = result.getString("exists");
					}
				} catch (SQLException e2) {}
				if (itemExists.equals("t")) {
					String sqlStatement1 = "UPDATE public.\"Menu\" SET \"item_cost\" = \'"  + item_Cost + "\', \"availability\" = \'" + item_Avalability + "\' WHERE \"name\" = \'" + item_Name + "\' AND \"category\" = \'" + item_Type + "\';";
					//System.out.println(sqlStatement1);
					result = null;
					try {
						stmt = conn.createStatement();
						result = stmt.executeQuery(sqlStatement1);
						
					}
					catch (SQLException e1) {
						System.out.println("Done!");	
					}
				} else {
					ResultSet result1 = null;
					try {
						stmt = conn.createStatement();
						String sqlStatement2 = "INSERT INTO public.\"Menu\" (\"menu_ID\", \"name\", \"category\", \"availability\", \"item_cost\") VALUES ('" + item_ID + "\', \'" + item_Name + "\', \'" + item_Type + "\', \'" + item_Avalability + "\', " + item_Cost + ");";
						result1 = stmt.executeQuery(sqlStatement2);
					}
					catch (SQLException e2) { }
				}
				
				menu_table.setItemCount(0);
				try{
				     //create a statement object
				       //create an SQL statement
				       String sqlStatement = "SELECT * FROM public.\"Menu\"";
				       //send statement to DBMS
				       result = stmt.executeQuery(sqlStatement);
				       System.out.println("SQL query");
				       
				       while (result.next()) {
							TableItem tableItem = new TableItem(menu_table, SWT.NONE);
							tableItem.setText(new String[] {result.getString("menu_ID"),result.getString("name"),result.getString("item_cost"),result.getString("category"),result.getString("availability")});
					    }
				       
				     } 
				     catch (Exception e3){
					     System.out.println(e3);
				}
				
				/*CLOSE DATABASE */
				try {
				    conn.close();
				    System.out.println("Connection Closed.");
				  } 
				  catch(Exception e1) {
				    System.out.println("Connection NOT Closed.");
				}
				/*CLOSE DATABASE */
			}		
		});
		
		
		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(0, 262, 689, 2);
		
		Label Menu_ID = new Label(shell, SWT.NONE);
		Menu_ID.setBounds(99, 53, 70, 26);
		Menu_ID.setText("Menu ID:");
		
		Label Name = new Label(shell, SWT.NONE);
		Name.setText("Name:");
		Name.setBounds(99, 85, 70, 26);
		
		Label Availability = new Label(shell, SWT.NONE);
		Availability.setBounds(99, 149, 96, 26);
		Availability.setText("New Availability:");
		
		Label Category = new Label(shell, SWT.NONE);
		Category.setBounds(99, 117, 70, 26);
		Category.setText("Catagory:");
		
		Label Cost = new Label(shell, SWT.NONE);
		Cost.setBounds(99, 181, 96, 26);
		Cost.setText("New Item Cost:");
		
		Label lblAddMenuItem = new Label(shell, SWT.CENTER);
		lblAddMenuItem.setBounds(95, 21, 501, 15);
		lblAddMenuItem.setText("Add/Edit Menu Item");
		
		Label label_1 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setBounds(0, 394, 689, 8);
		
		Label Name_1 = new Label(shell, SWT.NONE);
		Name_1.setBounds(100, 305, 70, 26);
		Name_1.setText("Menu ID:");
		
		inp_name2 = new Text(shell, SWT.BORDER);
		inp_name2.setBounds(202, 302, 399, 26);
		
		Label lblRemoveMenuItem = new Label(shell, SWT.CENTER);
		lblRemoveMenuItem.setBounds(95, 270, 501, 15);
		lblRemoveMenuItem.setText("Remove Menu Item");
		
		Button Update_Button_1 = new Button(shell, SWT.NONE);
		Update_Button_1.setBounds(293, 345, 105, 30);
		Update_Button_1.setText("Remove Item");
		Update_Button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				/*START THE DATABASE*/
				my = new dbSetup();
				try {
					conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/db905_group14_project2", my.user, my.pswd);
				} catch (Exception e1) {
					e1.printStackTrace();
					System.err.println(e1.getClass().getName()+": "+e1.getMessage());
					System.exit(0);
				}
				
				Statement stmt = null;
				try {
					stmt = conn.createStatement();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
				/*START THE DATABASE*/
				
				String rem_name = inp_name2.getText();
				ResultSet result = null;
				try {
					stmt = conn.createStatement();
					String sqlStatement1 = "DELETE FROM public.\"Menu\" WHERE \"menu_ID\" = \'" + rem_name + "\';";
					result = stmt.executeQuery(sqlStatement1);
				}
				catch (SQLException e1) {
					System.out.println("Done!");	
				}
				
				menu_table.setItemCount(0);
				 try{
				     //create a statement object
				       //create an SQL statement
				       String sqlStatement = "SELECT * FROM public.\"Menu\"";
				       //send statement to DBMS
				       result = stmt.executeQuery(sqlStatement);
				       System.out.println("SQL query");
				       
				       while (result.next()) {
							TableItem tableItem = new TableItem(menu_table, SWT.NONE);
							tableItem.setText(new String[] {result.getString("menu_ID"),result.getString("name"),result.getString("item_cost"),result.getString("category"),result.getString("availability")});
							System.out.println(result.getString("item_cost"));
					    }
				       
				     } 
				     catch (Exception e3){
					     System.out.println(e3);
				}
				
				/*CLOSE DATABASE */
				try {
				    conn.close();
				    System.out.println("Connection Closed.");
				  } 
				  catch(Exception e1) {
				    System.out.println("Connection NOT Closed.");
				}
				/*CLOSE DATABASE */
			}		
		});
		
		
		Label label_1_1 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1_1.setBounds(0, 498, 689, 2);
		
		inp_user_ID = new Text(shell, SWT.BORDER);
		inp_user_ID.setBounds(97, 620, 331, 30);
		
		Button Customer_Info_1 = new Button(shell, SWT.NONE);
		Customer_Info_1.setBounds(444, 620, 204, 30);
		Customer_Info_1.setText("Get Customer Information");
		
		Customer_Info_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				user_ID = inp_user_ID.getText();
				System.out.println(user_ID);
				my = new dbSetup();
				try {
					conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/db905_group14_project2", my.user, my.pswd);
				} catch (Exception e1) {
					e1.printStackTrace();
					System.err.println(e1.getClass().getName()+": "+e1.getMessage());
					System.exit(0);
				}
				try{
				     //create a statement object
					   System.out.println("test");
				       Statement stmt2 = conn.createStatement();
				       System.out.println("test 2");
				       //create an SQL statement
				       String sqlStatement = "SELECT \"username\" FROM public.\"User\" WHERE \"user_ID\" = " + user_ID;
				       //send statement to DBMS
				       System.out.println("Result:"+ sqlStatement);
				       ResultSet result = stmt2.executeQuery(sqlStatement);
						while (result.next()) {
							System.out.println("Result: "+result.getString("username"));
							username = result.getString("username");
						}
						nextWindow="customerInfo";
						shell.close();	

				} catch (Exception e7){
				     System.out.println(e);
				   }
				     try {
					      conn.close();
					      System.out.println("Connection Closed.");
					    } catch(Exception e7) {
					      System.out.println("Connection NOT Closed.");
				}
			}
		});
		
		
		Label lblUserid_1 = new Label(shell, SWT.NONE);
		lblUserid_1.setBounds(43, 624, 48, 26);
		lblUserid_1.setText("User_ID:");
		
		Label lblRecommendedChanges = new Label(shell, SWT.CENTER);
		lblRecommendedChanges.setBounds(43, 408, 605, 15);
		lblRecommendedChanges.setText("Recommended Changes:");
		
		Label rec_Change1 = new Label(shell, SWT.NONE);
		rec_Change1.setBounds(64, 433, 563, 15);
		String recChanges = "- Increase the price of: " + topTrend_1 + ".";
		rec_Change1.setText(recChanges);
		
		Label rec_Change2 = new Label(shell, SWT.NONE);
		rec_Change2.setBounds(64, 454, 563, 15);
		String recChanges2 = "- Decrease the price of: " + botTrend_1 + ".";
		rec_Change2.setText(recChanges2);
		
		
		Button btnSignOut = new Button(shell, SWT.NONE);
		btnSignOut.setBounds(10, 11, 82, 30);
		btnSignOut.setText("Sign Out");
		
		Label label_2 = new Label(shell, SWT.SEPARATOR | SWT.VERTICAL);
		label_2.setBounds(687, 0, 2, 674);
		
		Label label_1_1_1 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1_1_1.setBounds(0, 598, 689, 2);
		
		Label lblTrendingItems = new Label(shell, SWT.CENTER);
		lblTrendingItems.setBounds(45, 511, 605, 15);
		lblTrendingItems.setText("Trending Items:");
		
		Label lblstItem = new Label(shell, SWT.NONE);
		lblstItem.setBounds(66, 536, 88, 48);
		lblstItem.setText("- Top Item: \r\n- 2nd Top Item:");

		
		Label lblLastItem = new Label(shell, SWT.NONE);
		lblLastItem.setBounds(362, 532, 86, 48);
		lblLastItem.setText("- Last Item: \r\n- 2nd Last Item: ");
		
		Label top2item = new Label(shell, SWT.NONE);
		top2item.setBounds(160, 553, 96, 15);
		top2item.setText(topTrend_2);
		
		Label top1item = new Label(shell, SWT.NONE);
		top1item.setBounds(160, 536, 96, 15);
		top1item.setText(topTrend_1);
		
		Label bottom2item = new Label(shell, SWT.NONE);
		bottom2item.setBounds(454, 549, 96, 15);
		bottom2item.setText(botTrend_2);
		
		Label bottom1item = new Label(shell, SWT.NONE);
		bottom1item.setBounds(454, 532, 96, 15);
		bottom1item.setText(botTrend_1);
		
		Label lblStartDate = new Label(shell, SWT.NONE);
		lblStartDate.setBounds(722, 523, 55, 15);
		lblStartDate.setText("Start Date:");
		
		Label lblEndDate = new Label(shell, SWT.NONE);
		lblEndDate.setBounds(944, 525, 55, 15);
		lblEndDate.setText("End Date:");
		
		Label rev_name = new Label(shell, SWT.NONE);
		rev_name.setBounds(722, 556, 80, 15);
		rev_name.setText("Menu Item ID:");
		
		text_2 = new Text(shell, SWT.BORDER);
		text_2.setBounds(804, 554, 279, 21);
		
		text_3 = new Text(shell, SWT.BORDER);
		text_3.setBounds(804, 583, 279, 21);
		
		Label rev_price = new Label(shell, SWT.NONE);
		rev_price.setBounds(722, 585, 82, 15);
		rev_price.setText("Change Price: ");
		
		DateTime startDateTime = new DateTime(shell, SWT.BORDER);
		startDateTime.setBounds(782, 518, 80, 24);
		
		DateTime endDateTime = new DateTime(shell, SWT.BORDER);
		endDateTime.setBounds(1002, 518, 80, 24);
		
		Label revenue = new Label(shell, SWT.NONE);
		revenue.setBounds(804, 649, 70, 15);
		
		Label revenue_1 = new Label(shell, SWT.NONE);
		revenue_1.setBounds(1040, 648, 63, 15);
		
		HashMap<String, Integer> revenueMap = new HashMap<String, Integer>();
		Button btnGetUpdatedPrice = new Button(shell, SWT.NONE);
		btnGetUpdatedPrice.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Button Pressed");
				String rev_Name = text_2.getText();
				double rev_Cost = Double.parseDouble(text_3.getText());
				int start_Day = startDateTime.getDay();
				int start_Month = startDateTime.getMonth();
				int start_Year = startDateTime.getYear();
				int end_Day = endDateTime.getDay();
				int end_Month = endDateTime.getMonth();
				int end_Year = endDateTime.getYear();

				String start_Date = start_Year + "-" + start_Month + "-" + start_Day;
				String end_Date = end_Year + "-" + end_Month + "-" + end_Day;
				
				/*START THE DATABASE*/
				my = new dbSetup();
				try {
					conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/db905_group14_project2", my.user, my.pswd);
				} catch (Exception e1) {
					e1.printStackTrace();
					System.err.println(e1.getClass().getName()+": "+e1.getMessage());
					System.exit(0);
				}
				
				/*START THE DATABASE*/
				double old_Revenue = 0;
				try {
					String sqlStatement = "SELECT * FROM public.\"Transaction_History\" WHERE \"date\" >= '" + start_Date + "' AND \"date\" <= '" + end_Date + "';";
					//
					ResultSet result = null;
					Statement stmt = conn.createStatement();
					result = stmt.executeQuery(sqlStatement);
					
					while (result.next()) {
				    	Array z = result.getArray("item_list");
				    	String[] items = (String[])z.getArray();
				    	for (int i =0; i < items.length;i++) {
				    		if(revenueMap.containsKey(items[i]) == true) {
				    			revenueMap.put(items[i],revenueMap.get(items[i]) + 1);
				    		}
				    		else {
				    			revenueMap.put(items[i],1);
				    		}
				    	}
				    }
					
					String sqlStatement1 = "SELECT COUNT(\"total_cost\") FROM public.\"Transaction_History\" WHERE \"date\" >= '" + start_Date + "' AND \"date\" <= '" + end_Date + "';";
					stmt = conn.createStatement();
					result = stmt.executeQuery(sqlStatement1);
					while (result.next()) {
				    	old_Revenue = result.getInt("count");
				    }
				} catch (Exception e1) {}
				
				int number_Items = (int)revenueMap.get(rev_Name);
				double new_Rev = number_Items * rev_Cost;
				System.out.println(rev_Name + ": " + number_Items + " items  $" +new_Rev);
				
				revenue.setText("$" + String.valueOf(old_Revenue));
				revenue_1.setText("$" + String.valueOf(old_Revenue += new_Rev));
				
				/*CLOSE DATABASE */
				try {
				    conn.close();
				    System.out.println("Connection Closed.");
				  } 
				  catch(Exception e1) {
				    System.out.println("Connection NOT Closed.");
				}
				/*CLOSE DATABASE */
			}
		});		
		btnGetUpdatedPrice.setBounds(849, 616, 126, 25);
		btnGetUpdatedPrice.setText("Get Updated Revenue");
		
		Label label_1_1_2 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1_1_2.setBounds(687, 498, 439, 2);
		
		Label lblS = new Label(shell, SWT.NONE);
		lblS.setBounds(712, 649, 88, 15);
		lblS.setText("Actual Revenue: ");
		
		Label lblCalculatedRevenue = new Label(shell, SWT.NONE);
		lblCalculatedRevenue.setText("Calculated Revenue: ");
		lblCalculatedRevenue.setBounds(921, 648, 113, 15);
		
		Label rev_price_1 = new Label(shell, SWT.NONE);
		rev_price_1.setBounds(736, 603, 48, 15);
		rev_price_1.setText("(+/- 0.5)");
		
		btnSignOut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				nextWindow="signIn";
				shell.close();
			}
		});
	}
}
