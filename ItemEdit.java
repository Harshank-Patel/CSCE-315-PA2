import java.sql.*;
import java.text.DecimalFormat;
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



public class ItemEdit {
	//public TableItem current;
	public static String current_item;
	public static String toppings = ""; 
	private Table menu_table;
	private Table extras_table;
	public static double total_cost;
	public static double original_cost;
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
			ItemEdit window = new ItemEdit();
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
	       String sqlStatement = "SELECT * FROM public.\"Menu\" WHERE \"name\" = " + "\'"+current_item+"\'";
	       //send statement to DBMS
	       ResultSet result = stmt.executeQuery(sqlStatement);
	       System.out.println("SQL query");

		Button btnSignOut = new Button(shell, SWT.NONE);
		btnSignOut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				total_cost = original_cost;
				shell.close();
			}
		});
		btnSignOut.setBounds(10, 10, 82, 30);
		btnSignOut.setText("Cancel");
		
		Label lblMenu = new Label(shell, SWT.CENTER);
		lblMenu.setBounds(33, 77, 313, 15);
		lblMenu.setText("Toppings");
		
		Label lblItemCart = new Label(shell, SWT.CENTER);
		lblItemCart.setBounds(535, 77, 313, 15);
		lblItemCart.setText("Extras");
		
		Label lblTotalCost = new Label(shell, SWT.NONE);
		lblTotalCost.setAlignment(SWT.CENTER);
		lblTotalCost.setBounds(33, 476, 313, 15);
		
		//Declare stock topping table
		Table topping_table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		topping_table.setBounds(33, 98, 313, 309);
		topping_table.setHeaderVisible(true);
		topping_table.setLinesVisible(true);
		// loop through and add transaction data to the list?
		
		TableColumn tblclmnItem = new TableColumn(topping_table, SWT.NONE);
		tblclmnItem.setWidth(58);
		tblclmnItem.setText("Item");
		
		TableColumn tblclmnPrice = new TableColumn(topping_table, SWT.NONE);
		tblclmnPrice.setWidth(68);
		tblclmnPrice.setText("Price");
				
		TableCursor tableCursor = new TableCursor(topping_table, SWT.NONE);
		
		//declare extras topping table
		extras_table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		extras_table.setBounds(535, 99, 313, 308);
		extras_table.setHeaderVisible(true);
		extras_table.setLinesVisible(true);
		
		TableColumn tblclmnItem_1 = new TableColumn(extras_table, SWT.NONE);
		tblclmnItem_1.setWidth(100);
		tblclmnItem_1.setText("Item");
		
		TableColumn tblclmnCost = new TableColumn(extras_table, SWT.NONE);
		tblclmnCost.setWidth(100);
		tblclmnCost.setText("Cost");
		
		TableCursor tableCursor_1 = new TableCursor(extras_table, SWT.NONE);

		String[] items, addons; 
		String[] addon_cost;
		 while (result.next()) {
		    	Array z = result.getArray("item_list");
		    	Array z1 = result.getArray("addon_list");
		    	Array z2 = result.getArray("addon_cost");
		    	items = (String[])z.getArray();
		    	addons = (String[])z1.getArray();
		    	addon_cost = (String[])z2.getArray();
		    	//item_cost
		    	for (int i =0; i < items.length;i++) {
					TableItem tableItem = new TableItem(topping_table, SWT.NONE);
					tableItem.setText(new String[] {items[i], "0.00"});
				}
		    	
		    	for (int i =0; i < addons.length;i++) {
					TableItem tableItem = new TableItem(extras_table, SWT.NONE);
					tableItem.setText(new String[] {addons[i], addon_cost[i]});
				}
		    	total_cost = result.getDouble("item_cost");
		    	original_cost = total_cost;
		    	
		    	
		 }
		
		
		Button btnAddItem = new Button(shell, SWT.NONE);
		btnAddItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tableCursor_1.getRow() != null) {
					TableItem current = tableCursor_1.getRow();
					TableItem tableItem = new TableItem(topping_table, SWT.NONE);
					tableItem.setText(new String[] {current.getText(0), current.getText(1)});
					total_cost += Double.parseDouble(current.getText(1));
					lblTotalCost.setText("Total Cost: $" + df.format(total_cost));
				}
			}
		});
		btnAddItem.setBounds(372, 200, 135, 40);
		btnAddItem.setText("Add Item");

		Button btnRemoveItem = new Button(shell, SWT.NONE);
		btnRemoveItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tableCursor.getRow() != null) {
					TableItem current = tableCursor.getRow();
					total_cost -= Double.parseDouble(current.getText(1));
					topping_table.remove(topping_table.getSelectionIndices());
					lblTotalCost.setText("Total Cost: $" + df.format(total_cost));
				}
				
			}
		});
		btnRemoveItem.setBounds(372, 259, 135, 40);
		btnRemoveItem.setText("Remove Item");
		
	
		Button btnCheckout = new Button(shell, SWT.NONE);
		btnCheckout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = topping_table.getItems();
				//toppings = items;
				boolean first = true;
				for (int i = 0; i < items.length; i++) {
					String temp = items[i].getText(0);
					if (Double.parseDouble(items[i].getText(1)) != 0) {
						if (first == true) {
							toppings += temp;
							first = false;
						}
						else toppings += ", " + temp;
					}
					
					
				}
				shell.close();
			}
		});
		btnCheckout.setBounds(364, 451, 157, 64);
		btnCheckout.setText("Add to Cart");
		
		Label Item = new Label(shell, SWT.NONE);
		Item.setAlignment(SWT.CENTER);
		Item.setText("Item");
		Item.setBounds(413, 18, 55, 15);
		
		
		lblTotalCost.setText("Item Cost: $" + df.format(total_cost));
		
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

	}
}
