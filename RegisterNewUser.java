import java.sql.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class RegisterNewUser {

	protected Shell shell;
	private Text inp_l_name;
	private Text inp_f_name;
	public String username;
	public String password;
	public String nextWindow;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			RegisterNewUser window = new RegisterNewUser();
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
		shell.setSize(658, 469);
		shell.setText("SWT Application");
		
		Shell shell_1 = new Shell(shell, SWT.DIALOG_TRIM);
		shell_1.setText("SWT Dialog");
		shell_1.setBounds(24, 28, 621, 471);
		
		Label lblAUserWith = new Label(shell_1, SWT.NONE);
		lblAUserWith.setText("A user with that name does not exist, if you would like to register a new user \r\nput the information below:");
		lblAUserWith.setBounds(111, 113, 408, 37);
		
		Label lblUsername = new Label(shell_1, SWT.NONE);
		lblUsername.setText("Username");
		lblUsername.setBounds(58, 168, 55, 15);
		
		Label lblPassword = new Label(shell_1, SWT.NONE);
		lblPassword.setText("Password");
		lblPassword.setBounds(58, 203, 55, 15);
		
		Button btnRegister = new Button(shell_1, SWT.NONE);
		btnRegister.setText("Register");
		btnRegister.setBounds(210, 249, 193, 62);
		
		inp_l_name = new Text(shell, SWT.BORDER);
		inp_l_name.setBounds(166, 211, 394, 21);
		
		Label lblUsername_1 = new Label(shell, SWT.NONE);
		lblUsername_1.setText("Last Name:");
		lblUsername_1.setBounds(72, 214, 75, 15);
		
		Label lblPassword_1 = new Label(shell, SWT.NONE);
		lblPassword_1.setText("First Name: ");
		lblPassword_1.setBounds(72, 186, 75, 15);
		
		inp_f_name = new Text(shell, SWT.BORDER);
		inp_f_name.setBounds(166, 183, 394, 21);
		
		Label lblAUserWith_1 = new Label(shell, SWT.NONE);
		lblAUserWith_1.setBounds(97, 131, 448, 46);
		lblAUserWith_1.setText("A user with that name does not exist, if you would like to register a new user add the \r\nfollowing information (username and password were already recorded):");
		
		Button btnRegister_1 = new Button(shell, SWT.NONE);
		btnRegister_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				////ADD SQL COMMAND TO ADD USER
				String first_name = inp_f_name.getText();
				String last_name = inp_l_name.getText();
				
				dbSetup my = new dbSetup(); //dbSetup hides my username and password
			     Connection conn = null; //Building the connection
			     try {
			        //Class.forName("org.postgresql.Driver");
			        conn = DriverManager.getConnection(
			          "jdbc:postgresql://csce-315-db.engr.tamu.edu/db905_group14_project2", my.user, my.pswd);
			     } catch (Exception e1) {
			        e1.printStackTrace();
			        System.err.println(e1.getClass().getName()+": "+e1.getMessage());
			        System.exit(0);
			     }//end try catch
			     System.out.println("Opened database successfully");
			     
			     Statement stmt = null;
				try {
					stmt = conn.createStatement();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			    
				String sqlStatement1= "SELECT MAX(\"user_ID\") FROM public.\"User\";";
				System.out.println(sqlStatement1);
				int newUserID = 0;
				ResultSet result = null;
				
				try {
					result = stmt.executeQuery(sqlStatement1);
					while (result.next()) {
						newUserID = result.getInt("max");
					}
				} catch (SQLException e2) {}
				newUserID++;
				
				try {
					stmt = conn.createStatement();
					String sqlStatement2= "INSERT INTO public.\"User\" (\"user_ID\", \"first_name\", \"last_name\", \"username\", \"password\") VALUES (" + newUserID +", \'" + first_name + "\', \'" + last_name + "\', \'" + username + "\', \'" + password + "\');";
					System.out.println(sqlStatement2);
					result = stmt.executeQuery(sqlStatement2);
				} catch (SQLException e3) {}
				
				try {
					stmt = conn.createStatement();
					String sqlStatement3= "INSERT INTO public.\"Customer\" (\"user_ID\") VALUES (" + newUserID + ");";
					System.out.println(sqlStatement3);
					result = stmt.executeQuery(sqlStatement3);
				} catch (SQLException e4) {}
				
				try {
					conn.close();
					System.out.println("Connection Closed.");
				} catch(Exception e3) {
					System.out.println("Connection NOT Closed.");
				}
				nextWindow = "signIn";
				shell.close();
			}
		});
		btnRegister_1.setText("Register");
		btnRegister_1.setBounds(224, 274, 193, 62);

	}

}
