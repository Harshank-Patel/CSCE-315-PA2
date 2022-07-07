import java.sql.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class CustomerInfo {

	protected Shell shell;
	public String username;
	public String user_ID;
	public static boolean isManager;
	public boolean closed=false;
	public String nextWindow;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CustomerInfo window = new CustomerInfo();
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
		shell.setSize(750, 500);
		shell.setText("SWT Application");
		//dbSetup hides my user name and password
	    dbSetup my = new dbSetup();
	    //Building the connection
	     Connection conn = null;
	     try {
	        //Class.forName("org.postgresql.Driver");
	        conn = DriverManager.getConnection(
	          "jdbc:postgresql://csce-315-db.engr.tamu.edu/db905_group14_project2",
	           my.user, my.pswd);
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
	       String sqlStatement = "SELECT * FROM public.\"Transaction_History\" WHERE \"user_ID\" = " + user_ID;
	       //send statement to DBMS
	       ResultSet result = stmt.executeQuery(sqlStatement);
	       System.out.println("SQL query");
	       
		
		
		//setLayout(null);

		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isManager) {
					nextWindow="ManagerView";
					shell.close();
				} else {
					nextWindow="customerView";
					shell.close();
				}
			}
		});
		button.setBounds(18, 18, 69, 30);
		button.setText("<- Menu");
		
		Label lblName = new Label(shell, SWT.NONE);
		lblName.setBounds(18, 109, 58, 20);
		lblName.setText("Username:");
		
		Label lblUserId = new Label(shell, SWT.NONE);
		lblUserId.setBounds(18, 84, 42, 20);
		lblUserId.setText("User ID: ");
		
		Label lblTransactionHistory = new Label(shell, SWT.CENTER);
		lblTransactionHistory.setBounds(333, 56, 341, 20);
		lblTransactionHistory.setText("Transaction History: ");
		
		Table table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(332, 83, 341, 362);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnOrderId = new TableColumn(table, SWT.NONE);
		tblclmnOrderId.setWidth(100);
		tblclmnOrderId.setText("Order ID");
		
		TableColumn tblclmnDate = new TableColumn(table, SWT.NONE);
		tblclmnDate.setWidth(100);
		tblclmnDate.setText("Date");
		
		TableColumn tblclmnPrice = new TableColumn(table, SWT.NONE);
		tblclmnPrice.setWidth(100);
		tblclmnPrice.setText("Price");
		
		// loop through and add transaction data to the list?
		while (result.next()) {
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(new String[] {result.getString("order_ID"),result.getString("date"),"$"+result.getString("total_cost")});
	    }
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setBounds(76, 109, 64, 20);
		label_1.setText(username);
		
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(61, 84, 64, 20);
		label.setText(user_ID);
		
	} catch (Exception e){
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


