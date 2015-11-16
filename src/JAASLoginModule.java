package src;
import java.io.IOException;
import java.security.*;
import java.util.*;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;


public class JAASLoginModule implements LoginModule {

	// Flag to keep track of successful login.
	Boolean successfulLogin = false;

	// Variable that keeps track of the principal.
	Principal userPrincipal;
	
	/*
	 * Subject keeps track of who is currently logged in.
	 */
	Subject subject;
	
	/*
	 * String username
	 * String password 
	 * Temporary storage for usernames and passwords (before authentication).
	 * After authentication we can clear these variables. 
	 */
	String username, password;
	
	/*
	 * Other variables that are initialized by the login context. 
	 */
	
	CallbackHandler cbh;
	Map sharedState;
	Map options;
	
	/*
	 * This method is called by the login context automatically.
	 * @see javax.security.auth.spi.LoginModule#initialize(javax.security.auth.Subject, 
	 *         javax.security.auth.callback.CallbackHandler, java.util.Map, java.util.Map)
	 */
	public void initialize(Subject subject, 
			CallbackHandler cbh,
			Map sharedState,
			Map options) {
		
		this.subject = subject;
		this.cbh = cbh;
		this.sharedState = sharedState;
		this.options = options;
		
	}

	public Subject getSubject() {
		return subject;
	}
	
	
	
	/*
	 * If a user tries to abort a login then the state is reset. 
	 * @see javax.security.auth.spi.LoginModule#abort()
	 */
	public boolean abort() throws LoginException {
		if (!successfulLogin) {
			
			username = null;
			password = null;
			return false; 
		} else {
			logout(); 
			return true; 
		}
		
	}

	/*
	 * If login is valid, then the commit method is called by the LoginContext object. Here
	 * the logged in user is associated with a "principle". Think of this as a token
	 * that can from now on be used for authorization. 
	 * @see javax.security.auth.spi.LoginModule#commit()
	 */
	public boolean commit() throws LoginException {
		
		if (successfulLogin) {
			
			// Example Principal object stores the logged in user name.
				userPrincipal = new UserPrincipal(username);
				// subject stores the current logged in user.
				subject.getPrincipals().add(userPrincipal);
				return true; 
		}
		
		return false;
	}

	
	
	/*
	 * The actual login method that performs the authentication
	 * @see javax.security.auth.spi.LoginModule#login()
	 */
	public boolean login() throws LoginException {

		System.out.print("\n--------------------------\n" + 
						 "Login\n" +
						 "--------------------------\n\n");

		// We will use two call backs - one for username and the other
		// for password. 
		Callback exampleCallbacks[] = new Callback[2];
		exampleCallbacks[0] = new NameCallback("username: ");
		exampleCallbacks[1] = new PasswordCallback("password: ", false);
		// pass the callbacks to the handler. 
		try {
			cbh.handle(exampleCallbacks);
		} catch (IOException e) {
			 e.printStackTrace();
		} catch (UnsupportedCallbackException e) {
			e.printStackTrace();
		}
		
		
		// Now populate username/passwords etc. from the handler
		username = ((NameCallback) exampleCallbacks[0]).getName();
		password = new String (
					((PasswordCallback) exampleCallbacks[1]).getPassword());
		
		// Now perform validation. This part, you can either read from a file or a 
		// database. You can also incorporate secure password  handling here. 

		try {


			ProcessFile processFile = new ProcessFile();
			MD5Hash hasher = new MD5Hash();

			List<LoggedUser> loginList = processFile.buildLoginList();

			boolean login_successful = processFile.checkLoginList(loginList, username, hasher.hash(password));

			if(login_successful) {
				successfulLogin = true;
				return true; // successful login
			}
		}
		catch(IOException e) {
			System.err.println("ERROR: " + e);
		} 
		catch(NoSuchAlgorithmException e) {
			System.err.println("ERROR: " + e);
		}

		return false;
	}
		

	/*
	 * 
	 * @see javax.security.auth.spi.LoginModule#logout()
	 */
	public boolean logout() throws LoginException {
		username = null;
		password = null;		
		subject.getPrincipals().remove(userPrincipal);
		return true;
	}

}
