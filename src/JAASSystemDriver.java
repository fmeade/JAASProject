package src;

import java.io.*;
import java.util.*;

import java.security.PrivilegedAction;
import java.security.Principal;

import javax.security.auth.Subject;
import javax.security.auth.login.*;

import src.RootAction; 


/* 
 * This class demonstrates 
 *        how to create a logincontext;
 *        how to invoke the login method; 
 *        how to store and extract the logged in subject.
 * This class is also the driver program. 
 * This class uses: 
 *       class LoginModuleExample: implements login method 
 *                                 to perform authentication, 
 *                                 creates a Subject object, and
 *                                 implements the logout method.                     
 */

public class JAASSystemDriver {
	
		
    static LoginContext lc; 	
    static JAASSystem system;

    static ProcessFile processFile;
    static List<Employee> employeeList;
    static List<LoggedUser> loginList;
	
    public static void main(String[] args) throws IOException {

		/* Create a call back handler. This call back handler will be populated with
                 * different callbacks by the various login modules. For example, 
                 * if a login module implements a username/password style login, it populates this object
                 * with NameCallback (to get username) and PasswordCallback (which gets password).
		 */
		JAASCallbackHandler cbe = new JAASCallbackHandler(); 
		system = new JAASSystem();

		processFile = new ProcessFile();

		employeeList = new ArrayList<Employee>();
		employeeList = processFile.buildEmployeeList();

		loginList = new ArrayList<LoggedUser>();
		loginList = processFile.buildLoginList();
		
		/* Create a new login context. 
		 * @param Policy Name : We defined a policy in the file JAASPolicy.txt 
		 *                      and it is called "JAASExample"
		 * @param Call Back Handler
		 */
		try {
			lc = new LoginContext("JAASMaster", cbe);
		}
		catch (LoginException e) {
			System.err.println("Login exception.");
		}
		
		menu();
	}

	public static void menu() {

		try {

    		Scanner scan = new Scanner(System.in);
    		Console console = System.console();

			int choice = 0;

			System.out.print("\n=============================\n" +
							 "Welcome to the Radsburg, Inc.\nEmployee Directory\n" +
						     "=============================\n\n" +
						     "1. Create a Login\n" + 
					    	 "2. Login\n\n");

			choice = scan.nextInt();
			scan.nextLine();

			if(choice == 1) {

				String name;
				int id;
				String username;
				String password;
				Boolean user_exists = true;
				Boolean user_accepted = false;


				while(user_exists) {

					System.out.print("First Name: ");
						name = scan.next();
						scan.nextLine();

					System.out.print("ID: ");
						id = scan.nextInt();
						scan.nextLine();

					// check for user

					if(user_exists) {
						System.out.println("\nEmployee account already exists.\n");
						// overwrite?
					}
				}

				while(!user_accepted) {

					System.out.print("Username: ");
						username = scan.next();
						scan.nextLine();

					//checks file for username
					if(username.equals("user")) {
						System.out.println("\nUsername already exists.\n");
					}
					else {

						System.out.print("Password: ");
							char[] pass = console.readPassword();
							password = new String(pass);

						System.out.print("Re-enter Password: ");

						if((new String(console.readPassword())).equals(password)) {
							user_accepted = true;
						}
						else {
							System.out.println("\nPassword did not match.\n");
						}
					}
				}

				// add to login file
				// re-load login file

				menu();
			}
			else if(choice == 2) {
				lc.login();

				// If we reach this point then login has succeeded.
				System.out.println("You are now logged in!");


				/* 
			 	* Print the various Principals attached with the logged Subject.
			 	* In this example, we attach only one principal with each subject.
			 	*/
				Subject loggedUser = lc.getSubject();
				Set principals = lc.getSubject().getPrincipals();
				Iterator i = principals.iterator();
				while (i.hasNext()) {
					String s = ((Principal)i.next()).getName();
				}
			} 
			else {
				System.err.println("ERROR: Invalid Input.");
				menu();
			}
		}
		catch (LoginException e) {
			System.out.println("Username/password incorrect! " + e);
		}
		catch (SecurityException e) {
			System.out.println(" " + e);
		}
	}
	
}
