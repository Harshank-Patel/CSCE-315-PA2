import java.sql.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import java.util.Date;  
import java.util.Calendar; 


public class CustomerView {
	private Table menu_table;
	private Table cart_table;
	public double total_cost;
	public double item_cost;
	public static String item_list = "";
	public static String CustFav_1;
	public static String CustFav_2;
	public static String topTrend_1;
	public static String topTrend_2;
	DecimalFormat df = new DecimalFormat("#0.00");
	public static String user_ID;
	public static String username;
	public String nextWindow;
	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CustomerView window = new CustomerView();
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
		shell.setSize(895, 595);
		shell.setText("SWT Application");
		
		cart_table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		cart_table.setBounds(535, 99, 313, 308);
		cart_table.setHeaderVisible(true);
		cart_table.setLinesVisible(true);
		
		TableColumn tblclmnItem_1 = new TableColumn(cart_table, SWT.NONE);
		tblclmnItem_1.setWidth(100);
		tblclmnItem_1.setText("Item");
		
		TableColumn tblclmnQuantity = new TableColumn(cart_table, SWT.NONE);
		tblclmnQuantity.setWidth(50);
		tblclmnQuantity.setText("Item ID");
		
		TableColumn tblclmnCost = new TableColumn(cart_table, SWT.NONE);
		tblclmnCost.setWidth(50);
		tblclmnCost.setText("Cost");
		
		TableColumn tblclmnToppings = new TableColumn(cart_table, SWT.NONE);
		tblclmnToppings.setWidth(100);
		tblclmnToppings.setText("Add-Ons");
		
		TableCursor tableCursor_1 = new TableCursor(cart_table, SWT.NONE);
		
		//dbSetup hides my user name and password
	    dbSetup my = new dbSetup();
	    //Building the connection
	     Connection conn = null;
	     try {
	        //Class.forName("org.postgresql.Driver");
	        conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/db905_group14_project2", my.user, my.pswd);
	     } catch (Exception e) {
	        e.printStackTrace();
	        System.err.println(e.getClass().getName()+": "+e.getMessage());
	        System.exit(0);
	     }//end try catch
	     System.out.println("Opened database successfully");
	     try{
	     //create a statement object
	       Statement stmt = conn.createStatement();
	       //create an SQL statement
	       String sqlStatement = "SELECT * FROM public.\"Menu\" WHERE \"availability\" = true";
	       //send statement to DBMS
	       ResultSet result = stmt.executeQuery(sqlStatement);
	       System.out.println("SQL query");

		Button btnSignOut = new Button(shell, SWT.NONE);
		btnSignOut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				nextWindow="signIn";
				shell.close();
			}
		});
		btnSignOut.setBounds(10, 10, 82, 30);
		btnSignOut.setText("Sign Out");
		
		Label lblMenu = new Label(shell, SWT.CENTER);
		lblMenu.setBounds(33, 77, 313, 15);
		lblMenu.setText("Menu");
		
		Label lblItemCart = new Label(shell, SWT.CENTER);
		lblItemCart.setBounds(535, 77, 313, 15);
		lblItemCart.setText("Item Cart");
		
		Label lblSuggestedItem = new Label(shell, SWT.NONE);
		lblSuggestedItem.setBounds(33, 413, 313, 15);
		lblSuggestedItem.setText("Suggested Item:");
		
		total_cost = 0;
		Label lblTotalCost = new Label(shell, SWT.NONE);
		lblTotalCost.setBounds(535, 413, 313, 15);
		lblTotalCost.setText("Total Cost: $" + df.format(total_cost));
		
		Table menu_table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		menu_table.setBounds(33, 98, 313, 309);
		menu_table.setHeaderVisible(true);
		menu_table.setLinesVisible(true);
		
		TableColumn tblclmnItem = new TableColumn(menu_table, SWT.NONE);
		tblclmnItem.setWidth(100);
		tblclmnItem.setText("Item");
		
		TableColumn tblclmnPrice = new TableColumn(menu_table, SWT.NONE);
		tblclmnPrice.setWidth(68);
		tblclmnPrice.setText("Price");
		
		TableColumn tblclmnCategory = new TableColumn(menu_table, SWT.NONE);
		tblclmnCategory.setWidth(61);
		tblclmnCategory.setText("Item ID");
		while (result.next()) {
			TableItem tableItem = new TableItem(menu_table, SWT.NONE);
			tableItem.setText(new String[] {result.getString("name"),result.getString("item_cost"),result.getString("menu_ID")});
			System.out.println(result.getString("item_cost"));
	    }
		
		TableCursor tableCursor = new TableCursor(menu_table, SWT.NONE);

		Button btnAddItem = new Button(shell, SWT.NONE);
		btnAddItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tableCursor.getRow() != null) {
					TableItem current = tableCursor.getRow();
					TableItem tableItem = new TableItem(cart_table, SWT.NONE);
					tableItem.setText(new String[] {current.getText(0), current.getText(2), current.getText(1)});
					total_cost += Double.parseDouble(current.getText(1));
					lblTotalCost.setText("Total Cost: $" + df.format(total_cost));
					System.out.println(current.getText(1).substring(1));
				}
			}
		});
		btnAddItem.setBounds(372, 200, 135, 40);
		btnAddItem.setText("Add Item >>");

		Button btnRemoveItem = new Button(shell, SWT.NONE);
		btnRemoveItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tableCursor_1.getRow() != null) {
					TableItem current = tableCursor_1.getRow();
					total_cost -= Double.parseDouble(current.getText(2));
					cart_table.remove(cart_table.getSelectionIndices());
					lblTotalCost.setText("Total Cost: $" + df.format(total_cost));
				}
				
			}
		});
		btnRemoveItem.setBounds(372, 259, 135, 40);
		btnRemoveItem.setText("<< Remove Item");
		
		Button btnCustomerInformation = new Button(shell, SWT.NONE);
		btnCustomerInformation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				nextWindow="customerInfo";
				shell.close();
			}
		});
		btnCustomerInformation.setBounds(372, 42, 135, 40);
		btnCustomerInformation.setText("Customer Information");
		
		Button btnEditItem = new Button(shell, SWT.NONE);
		btnEditItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String item_name = null;
				Double temp_cost = 0.0;
				TableItem current = null;
				if (tableCursor_1.getRow() != null) {
					current = tableCursor_1.getRow();
					temp_cost = Double.parseDouble(current.getText(2));
					item_name = current.getText(0);
				}
				System.out.println(item_name);
				ItemEdit itemWindow = new ItemEdit();
				itemWindow.current_item = item_name;
				itemWindow.open();
				item_cost = itemWindow.total_cost;
				current.setText(2, Double.toString(item_cost));
				total_cost += item_cost - temp_cost;
				lblTotalCost.setText("Total Cost: $" + df.format(total_cost));
				current.setText(3, itemWindow.toppings);
			}
		});
		btnEditItem.setText("Edit Item in Cart");
		btnEditItem.setBounds(372, 317, 135, 40);
		
	     }
	     
		catch (Exception e){
		     System.out.println(e);
		 }
		     try {
		      conn.close();
		      System.out.println("Connection Closed.");
		    } catch(Exception e) {
		      System.out.println("Connection NOT Closed.");
		}
		Button btnCheckout = new Button(shell, SWT.NONE);
		btnCheckout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String query1 = "SELECT MAX(\"order_ID\") FROM public.\"Transaction_History\"";
				Connection conn = null;
				try {
			        conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/db905_group14_project2", my.user, my.pswd);
			    } catch (Exception e5) {
			        e5.printStackTrace();
			        System.err.println(e5.getClass().getName()+": "+e5.getMessage());
			        System.exit(0);
			    }
				
				int new_order_ID = 0;
				
				
				try{
					String sqlStatement = query1;
					Statement stmt = conn.createStatement();
					ResultSet result = stmt.executeQuery(sqlStatement);
					result.next();
					new_order_ID = result.getInt("max") + 1;
					System.out.println("order ID: " + new_order_ID);
					
					Date date = Calendar.getInstance().getTime();  
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
					String strDate = dateFormat.format(date);  
					
					TableItem current = null;
					if (tableCursor_1.getRow() != null) {
						current = tableCursor_1.getRow();
						String item = current.getText(0);
					}
					
					TableItem[] items = cart_table.getItems();
					boolean first = true;
					for (int i = 0; i < items.length; i++) {
						String temp = items[i].getText(1);
							if (first == true) {
								item_list += "{" + temp;
								first = false;
							}
							else item_list += ", " + temp;
					}
					item_list += "}";
					System.out.println(strDate);
					String sqlStatement1 = "INSERT INTO public.\"Transaction_History\" (\"order_ID\", \"user_ID\", \"date\", \"total_cost\", \"item_list\") VALUES (" + new_order_ID + ", " + user_ID + ", \'" + strDate + "\', " + total_cost + ", \'" + item_list + "\')";
					System.out.println(sqlStatement1);
					ResultSet result1 = stmt.executeQuery(sqlStatement1);
					result.next();
					
				} catch (Exception e3){
					System.out.println(e3);
				}
				

				try {
					conn.close();
					System.out.println("Connection Closed.");
				} catch(Exception e4) {
					System.out.println("Connection NOT Closed.");
				}

			
				nextWindow = "signIn";
				shell.close();
				
			}
		});
		btnCheckout.setBounds(364, 451, 157, 64);
		btnCheckout.setText("Checkout");
		
		Label suggested_Items = new Label(shell, SWT.NONE);
		suggested_Items.setBounds(33, 434, 313, 53);
		if (CustFav_1 == null) {
			suggested_Items.setText(topTrend_1 + ", " + topTrend_2);
		} else {
			suggested_Items.setText(CustFav_1 + ", " + CustFav_2);
		}
		
	}
}
