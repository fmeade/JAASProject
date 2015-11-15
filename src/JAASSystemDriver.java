package src;

import java.io.*;
import java.util.*;

import java.security.*;

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
	
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

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

	public static void menu() throws IOException, NoSuchAlgorithmException {

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
				int id = 0;
				String username;
				String password;
				Boolean user_exists = true;
				Boolean username_exists = true;
				Boolean user_accepted = false;


				while(user_exists) {

					System.out.print("First Name: ");
						name = scan.next();
						scan.nextLine();

					try {
						System.out.print("ID: ");
							id = scan.nextInt();
							scan.nextLine();

						user_exists = processFile.checkLoginList(loginList, id);

						if(user_exists) {
							System.out.println("\nEmployee account already exists.\n");
							// overwrite?
						}
					} catch (InputMismatchException e) {
						System.err.println("ERROR: " + e);
							scan.nextLine();
					}
				}

				while(!user_accepted) {

					System.out.print("Username: ");
						username = scan.next();
						scan.nextLine();

					username_exists = processFile.checkLoginList(loginList, username);

					if(username_exists) {
						System.out.println("\nUsername already exists.\n");
					}
					else {

						System.out.print("Password: ");
							char[] pass = console.readPassword();
							password = new String(pass);

						System.out.print("Re-enter Password: ");

						if((new String(console.readPassword())).equals(password)) {
							user_accepted = true;

							MD5Hash hasher = new MD5Hash();

							loginList.add(new LoggedUser(id,username,hasher.hash(password))); // still needs hash

							FileWriter login = new FileWriter("files/loginList.txt",true);
							BufferedWriter bw = new BufferedWriter(login);
							
							
							if(loginList.size() == 1){
								bw.write((loginList.get(loginList.size()-1)).toString());
							}
							else {
								bw.write("\n" + (loginList.get(loginList.size()-1)).toString());
							}
							bw.flush();
							
							if(bw != null) {
								bw.close();
							}
						}
						else {
							System.out.println("\nPassword did not match.\n");
						}
					}
				}


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
