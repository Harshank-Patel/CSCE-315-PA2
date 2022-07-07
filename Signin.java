import java.sql.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class Signin {

	protected Shell shell;
	private Text inp_username;
	private Text inp_password;
	public String passEntry;
	public String userType;
	public String[] userInfo = new String[2];
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			//Signin window = new Signin();
			//window.open();
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
		shell.setSize(800, 650);
		shell.setText("SWT Application");
		
		Label lblUsername = new Label(shell, SWT.NONE);
		lblUsername.setBounds(150, 217, 55, 15);
		lblUsername.setText("Username");
		
		inp_username = new Text(shell, SWT.BORDER);
		inp_username.setBounds(211, 214, 427, 21);
		
		Label lblPassword = new Label(shell, SWT.NONE);
		lblPassword.setText("Password");
		lblPassword.setBounds(150, 252, 55, 15);
		
		inp_password = new Text(shell, SWT.BORDER);
		inp_password.setBounds(211, 249, 427, 21);
		
		Button btnSignin = new Button(shell, SWT.NONE);
		btnSignin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				String input_username = inp_username.getText();
				String input_password = inp_password.getText();
				String recieved_ID = "";

				
			    dbSetup my = new dbSetup(); //dbSetup hides my username and password
			     Connection conn = null; //Building the connection
			     try {
			        //Class.forName("org.postgresql.Driver");
			        conn = DriverManager.getConnection(
			          "jdbc:postgresql://csce-315-db.engr.tamu.edu/db905_group14_project2",
			           my.user, my.pswd);
			     } catch (Exception e1) {
			        e1.printStackTrace();
			        System.err.println(e1.getClass().getName()+": "+e1.getMessage());
			        System.exit(0);
			     }//end try catch
			     System.out.println("Opened database successfully");
			     
			     Statement stmt = null;
				try {
					stmt = conn.createStatement();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			    
				String sqlStatement1= "SELECT EXISTS (SELECT \"user_ID\" FROM public.\"User\" WHERE \"username\" = \'"+input_username+ "\' AND \"password\" = \'"+input_password+"\');";
				String userExists = "f";
				ResultSet result = null;
				
				try {
					result = stmt.executeQuery(sqlStatement1);
					while (result.next()) {
						userExists = result.getString("exists");
					}
				} catch (SQLException e2) {}
				
				if(userExists.equals("t")) { // IF USER EXISTS
					String sqlStatement = "SELECT \"user_ID\" FROM public.\"User\" WHERE \"username\" = \'"+input_username+ "\' AND \"password\" = \'"+input_password+"\';";	
					
					result = null;
					try {
						result = stmt.executeQuery(sqlStatement);
					} catch (SQLException e2) {
						e2.printStackTrace();
					}
					
					
					try {
						while (result.next()) {
							recieved_ID = result.getString("user_ID");
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
					stmt = null;
					try {
						stmt = conn.createStatement();
					} catch (SQLException e2) {
						e2.printStackTrace();
					}
					
					String strr= "Select exists(select \"user_ID\" from public.\"Manager\" where \"user_ID\" = "+"\'" +recieved_ID +"\');";
					String isManager = "f";
					result = null;
					try {
						result = stmt.executeQuery(strr);
						while (result.next()) {
							isManager=result.getString("exists");
						}
					} catch (SQLException e2) {}
					
					if(isManager.equals("t")) {
						//ManagerView ManagerWindow = new ManagerView();
						//ManagerWindow.open();
						userType = "manager";
						shell.close();
					}
					else {
						userInfo[0] = recieved_ID;
						userInfo[1]= input_username;
						userType = "customer";
						shell.close();
					}
					
					try {
						conn.close();
						System.out.println("Connection Closed.");
					} catch(Exception e3) {
						System.out.println("Connection NOT Closed.");
					}
				}
				else { 
					passEntry = input_password;
					userInfo[0] = recieved_ID;
					userInfo[1]= input_username;
					userType = "newUser";
					shell.close();
				}
				
			}
		});
		btnSignin.setBounds(302, 307, 193, 62);
		btnSignin.setText("Signin");

	}
	
	
}
