package src;

import java.io.*;
import java.util.*;
import java.security.*;

import javax.security.auth.Subject;
import javax.security.auth.login.*;

/**
 * The JAASSystemDriver class contains the main method as well as 
 	* helper functions for running specific menus.
 */
public class JAASSystemDriver {
	
		
    static LoginContext lc; 	

    static ProcessFile processFile;
    static List<Employee> employeeList;
    static List<LoggedUser> loginList;

	/*
	 * The main method instantiates the gobal variables, and calls the initial menu
	 */
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

		/* Create a call back handler. This call back handler will be populated with
                 * different callbacks by the various login modules. For example, 
                 * if a login module implements a username/password style login, it populates this object
                 * with NameCallback (to get username) and PasswordCallback (which gets password).
		 */
		JAASCallbackHandler cbe = new JAASCallbackHandler(); 

		/* Object to handle reading and writing from a file into a list */
		processFile = new ProcessFile();

		/* An ArrayList for the Employee file */
		employeeList = processFile.buildEmployeeList();

		/* AN ArrayList for the Login file */
		loginList = processFile.buildLoginList();
		
		/* Create a new login context. 
		 * @param Policy Name : We defined a policy in the file JAASPolicy.txt 
		 *                      and it is called "JAASMaster"
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





	/* Prints the main menu and gets a selection from the user.
	 	* 1. Create a login user account
	 	* 2. Login an existing user
	 	* 3. Exit system
	 */
	static void menu() throws IOException, NoSuchAlgorithmException {

		try {

    		Scanner scan = new Scanner(System.in);
    		Console console = System.console();

			int choice = 0;

			System.out.print("\n=============================\n" +
							 "Welcome to the Radsburg, Inc.\nEmployee Directory\n" +
						     "=============================\n\n" +
						     "1. Create a Login\n" + 
					    	 "2. Login\n" + 
					    	 "3. Exit\n\n");

			
			try {
				choice = scan.nextInt();
				scan.nextLine();

				clearScreen();
			}
			catch(java.util.InputMismatchException e) {
				choice = 0;
			}

			if(choice == 1) {

				System.out.println("\n--------------------------\n" + 
						 			"Create an account\n" +
						 			"--------------------------\n");
				createAccount();

				/* Reloads menu, allows user to create another account 
					or login with an existing one. */
				menu();

			} // end Create a Login account
			else if(choice == 2) { //Login to an existing account
				
				lc.login();
				clearScreen();

				// If we reach this point then login has succeeded.
				System.out.println("\nLogin Successful!");

				String user = null;
				/* 
			 	* Print the various Principals attached with the logged Subject.
			 	* In this example, we attach only one principal with each subject.
			 	*/
				Subject loggedUser = lc.getSubject();
				Set principals = lc.getSubject().getPrincipals();
				Iterator i = principals.iterator();
				while (i.hasNext()) {
					user = ((Principal)i.next()).getName();
				}

				/* Accesses the user's menu */
				userMenu(user);

			} // end login
			else if(choice == 3) { // exit system
				System.out.println("\nHave a Nice Day!\n");
			}
			else { 
				clearScreen();
				System.err.println("ERROR: Invalid Input.");
				menu();
			}
		}
		catch (LoginException e) {
			clearScreen();
			System.out.println("\nUsername/password incorrect! \n");
			menu();
		}
		catch (SecurityException e) {
			System.out.println(" " + e);
		}
	}





	/* Prints the user menu, and prompts user for selection.
	 	* 1. Query Employee Information
	 	* 2. Change Password
	 	* 3. Logout
	 */
	static void userMenu(String _loggedUser) throws IOException, NoSuchAlgorithmException  {
		System.out.print("\n-----------------------------\n" +
						 "User Menu\n" +
						 "-----------------------------\n" +
						 "1. Query Personal Information\n" +
						 "2. Change Password\n" +
						 "3. Logout\n\n");

			Scanner scan = new Scanner(System.in);
		
			int choice = 0;

			try {
				choice = scan.nextInt();
				scan.nextLine();
				clearScreen();
			}
			catch(java.util.InputMismatchException e) {
				choice = 0;
			}


			if(choice == 1) {
				queryMenu(_loggedUser);
			}
			else if(choice == 2) {
				changePassword(_loggedUser);
			}
			else if(choice == 3) {
				try {
					lc.logout();
					menu();
				}
				catch (LoginException e) {
					System.out.println("\nLogout Failed \n");
					userMenu(_loggedUser);
				}
			}
			else {
				System.err.println("ERROR: Invalid Input.");
				userMenu(_loggedUser);
			}
	}





	/*
	 * Menu for querying personal information or information of the user's employees
	 */
	static void queryMenu(String _loggedUser) throws IOException, NoSuchAlgorithmException {
		System.out.print("\n--------------------------\n" + 
						 "Query Menu\n" +
						 "--------------------------\n");
		
		System.out.print("1. Show Personal Information.\n" +
						 "2. See Your Employee's Information.\n" +
						 "3. Change Salary of Employee.\n" +
						 "4. Change Position of Employee.\n" +
						 "5. Back\n\n");

		Scanner scan = new Scanner(System.in);
		
		int choice = 0;

		try {
			choice = scan.nextInt();
			scan.nextLine();
			clearScreen();
		}
		catch(java.util.InputMismatchException e) {
			choice = 0;
		}


		if(choice == 1) {

			Employee employee = loggedEmployee(_loggedUser);
			
			System.out.println("\n" + employee.toString());
			queryMenu(_loggedUser);

		}
		else if(choice == 2) {

			Employee employee = selectedEmployee(_loggedUser);
			clearScreen();
			
			System.out.println("\n" + employee.toString());
			queryMenu(_loggedUser);

		}
		else if(choice == 3) {

			Employee employee = selectedEmployee(_loggedUser);

			try {

				System.out.println("\nCurrent Salary: $" + employee.getSalary());

				System.out.print("\nNew Salary: $");


				int newSalary = scan.nextInt();
				scan.nextLine();
				clearScreen();

				employee.setSalary(newSalary);

				List<Employee> tempList = new ArrayList<Employee>();

				for(int i=0; i < employeeList.size(); i++) {
					if(employee.getId() == employeeList.get(i).getId()) {
						tempList.add(employee);
					}
					else {
						tempList.add(employeeList.get(i));
					}
				}

				employeeList = tempList;


				processFile.writeEmployeeFile(employeeList);


				System.out.println("\nSalary Change Successful!\n");
				queryMenu(_loggedUser);

			}
			catch(java.util.InputMismatchException e) {
				choice = 0;
				queryMenu(_loggedUser);
			}
			catch(NullPointerException e) {
				choice = 0;
			}

		}
		else if(choice == 4) {

			Employee employee = selectedEmployee(_loggedUser);

			try {

				System.out.println("\nCurrent Position: " + employee.getPosition());

				System.out.print("\nNew Position: ");

			
				String newPosition = scan.nextLine();
				clearScreen();

				employee.setPosition(newPosition);

				List<Employee> tempList = new ArrayList<Employee>();

				for(int i=0; i < employeeList.size(); i++) {
					if(employee.getId() == employeeList.get(i).getId()) {
						tempList.add(employee);
					}
					else {
						tempList.add(employeeList.get(i));
					}
				}

				employeeList = tempList;


				processFile.writeEmployeeFile(employeeList);

				
				System.out.println("\nPosition Change Successful!\n");
				queryMenu(_loggedUser);

			}
			catch(java.util.InputMismatchException e) {
				choice = 0;
				queryMenu(_loggedUser);
			}
			catch(NullPointerException e) {
				choice = 0;
			}

		}
		else if(choice == 5) {
			userMenu(_loggedUser);
		}
		else {
			System.out.println("ERROR: Invalid Input.");
				queryMenu(_loggedUser);
		}
	}



	/*
	 * Gets an Employee object of the current user
	 */
	static Employee loggedEmployee(String _loggedUser) throws IOException, NoSuchAlgorithmException {
		Employee employee = null;
		int userId = -1;

		for(int i=0; i < loginList.size(); i++) {
			if(_loggedUser.equals(loginList.get(i).getUsername())) {
				userId = loginList.get(i).getId();
				i = loginList.size();
			}
		}

		for(int i=0; i < employeeList.size(); i++) {
			if(userId == employeeList.get(i).getId()) {
				employee = employeeList.get(i);
				i = employeeList.size();
			}
		}
		return employee;

	}



	/*
	 * Gets an Employee object of the selected Employee by the user
	 */
	static Employee selectedEmployee(String _loggedUser) throws IOException, NoSuchAlgorithmException {
		Scanner scan = new Scanner(System.in);
		Employee employee = null;
		Employee currentEmployee = null;
		List<Employee> userEmployeeList = new ArrayList<Employee>();
		int userId = -1;
		String userName = null;

		for(int i=0; i < loginList.size(); i++) {
			if(_loggedUser.equals(loginList.get(i).getUsername())) {
				userId = loginList.get(i).getId();
				i = loginList.size();
			}
		}

		for(int i=0; i < employeeList.size(); i++) {
			if(userId == employeeList.get(i).getId()) {
				userName = employeeList.get(i).getName();
				currentEmployee = employeeList.get(i);
				i = employeeList.size();
			}
		}

		for(int i=0; i < employeeList.size(); i++) {
			if((currentEmployee.getPosition().equals("CEO") || currentEmployee.getPosition().equals("President")) 
				&& currentEmployee.getId() != employeeList.get(i).getId()) {
				userEmployeeList.add(employeeList.get(i));
			}
			else if((userName + " (" + userId + ")").equals(employeeList.get(i).getSupervisor())) {
				userEmployeeList.add(employeeList.get(i));
			}
		}


		for(int i=0; i < userEmployeeList.size(); i++) {
			System.out.println(userEmployeeList.get(i).getId() + " " + userEmployeeList.get(i).getName() );
		}



		System.out.print("\nSelect employee: ");

		try {
			int employeeId = scan.nextInt();
			scan.nextLine();

			for(int i=0; i < userEmployeeList.size(); i++) {
				if(employeeId == userEmployeeList.get(i).getId()) {
					employee = userEmployeeList.get(i);
				}
			}


			if(employee == null) {
				clearScreen();
				throw new java.util.InputMismatchException("\nERROR: Invalid Employee ID.\n");
			}
		}
		catch(java.util.InputMismatchException e) {
			System.out.println("\nERROR: Invalid Employee ID.\n");
			queryMenu(_loggedUser);
		}


		return employee;
	}



	/*
	 * Creates a new Login account for an existing Employee
	 */
	static void createAccount() throws IOException, NoSuchAlgorithmException  {
		Scanner scan = new Scanner(System.in);
    	Console console = System.console();

		String name;
		int id = 0;
		String username;
		String password;


		/* Gets user's first name and id number, 
			checks to make sure the Employee exists */

		Boolean user_exists = true;
		Boolean employee_exists = false;

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
					clearScreen();
					System.out.println("\nEmployee account already exists.\n");
					// overwrite?
					//menu();
				}

				employee_exists = processFile.checkEmployeeList(employeeList, name, id);

				if(!employee_exists) {
					clearScreen();
					System.out.println("\nEmployee record does not exist.\n");
				}
			} catch (InputMismatchException e) {
				System.err.println("ERROR: " + e);
					scan.nextLine();
			}
		}


		
		/* Gets a username and password from the user, 
		 	checks if username is already used,
		 	and has user double enter password. */

		Boolean username_exists = true;
		Boolean user_accepted = false;


		while(!user_accepted && employee_exists) {

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

					loginList.add(new LoggedUser(id,username,hasher.hash(password)));

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
					clearScreen();
					System.out.println("\nUser created successfully!\n");
				}
				else {
					System.out.println("\nPassword did not match.\n");
				}
			}
		}
	}



	/*
	 * Changes the password of the current user, with restrictions.
	 */
	static void changePassword(String _loggedUser) throws IOException, NoSuchAlgorithmException {
		Scanner scan = new Scanner(System.in);
		Console console = System.console();
		MD5Hash hasher = new MD5Hash();

		String username;
		char[] pass;
		String oldPassword;
		String newPassword;
		String newPassword2;


		System.out.print("username: ");
			username = scan.next();
			scan.nextLine();

		System.out.print("old password: ");
			pass = console.readPassword();
			oldPassword = new String(pass);

		boolean correct_user = processFile.checkLoginList(loginList, username, hasher.hash(oldPassword));

		if(!username.equals(_loggedUser) || !correct_user) {
			System.out.println("\nInvalid username/password.\n");
			userMenu(_loggedUser);
		}
		else {

			System.out.print("new password: ");
				pass = console.readPassword();
				newPassword = new String(pass);


			System.out.print("re-enter new password: ");
				pass = console.readPassword();
				newPassword2 = new String(pass);



			if(oldPassword.equals(newPassword)) {
				System.out.println("\nCannot use old password.\n");
				userMenu(_loggedUser);
			}
			else if(!newPassword.equals(newPassword2)) {
				System.out.println("\nNew Password did not match.\n");
				userMenu(_loggedUser);
			}
			else {
				List<LoggedUser> tempList = new ArrayList<LoggedUser>();

				for(int i=0; i < loginList.size(); i++) {
					if(_loggedUser.equals((loginList.get(i)).getUsername())) {
						tempList.add(new LoggedUser(loginList.get(i).getId(), loginList.get(i).getUsername(), hasher.hash(newPassword)));
					}
					else {
						tempList.add(loginList.get(i));
					}
				}

				loginList = tempList;


				FileWriter login = new FileWriter("files/loginList.txt",false);
				BufferedWriter bw = new BufferedWriter(login);
				
				
				for(int i=0; i < loginList.size(); i++) {
					if(i == 0) {
						bw.write((loginList.get(i)).toString());
					}
					else {
						bw.write("\n" + (loginList.get(i).toString()));
					}
				}

				bw.flush();
				
				if(bw != null) {
					bw.close();
				}

				clearScreen();
				System.out.println("\nPassword Change Successful!\n");
				userMenu(_loggedUser);
			}
		}
	}



	/*
	 * Clears the terminal screen
	 */
	static void clearScreen() {
		System.out.print("\033[H\033[2J"); // clears screen
	}
	
}







