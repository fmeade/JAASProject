public class LoggedUser {

	private int id;
	private String username;
	private String password;

	public LoggedUser(int _id, String _username, String _password) {
		id = _id;
		username = _username;
		password = _password;
	}

	public int getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}

	public void changePassword(String _password) {
		password = _password;
	}

	public String toString() {
		return id + ", " + username + ", " + password;
	}
}