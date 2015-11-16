package src;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class ProcessFile {

	public List<Employee> buildEmployeeList() throws IOException {

		FileReader database = new FileReader("files/employeeList.txt");
		BufferedReader reader = new BufferedReader(database);
		
		List<Employee> tempList = new ArrayList<Employee>();
		String line;
		
		while((line = reader.readLine()) != null) {
			String[] employee = line.split(", ");
			
			if(employee[0].equals("")) {
			}
			else {
				
				tempList.add(new Employee(employee[0], 
									  Integer.parseInt(employee[1]), 
									  employee[2], 
									  employee[3], 
									  Long.parseLong(employee[4].substring(1,employee[4].length()))));
			}
		}
		return tempList;
	}


	public Boolean checkEmployeeList(List<Employee> _list, String _name, int _id) {

		boolean exist = false;
		
		for(int i=0; i < _list.size(); i++) {
			if(_name.equals(_list.get(i).getName()) && (_list.get(i)).getId() == _id) {
				exist = true;
				i = _list.size();
			}
			else {
				exist = false;
			}
		}
		return exist;
	}


	public Employee getCurrentEmployee(List<Employee> _list, int _id) {

		Employee employee = null;
		
		for(int i=0; i < _list.size(); i++) {
			if(_list.get(i).getId() == _id) {
				employee = _list.get(i);
			}
		}
		
		return employee;
	}


	public List<LoggedUser> buildLoginList() throws IOException {

		FileReader database = new FileReader("files/loginList.txt");
		BufferedReader reader = new BufferedReader(database);
		
		List<LoggedUser> tempList = new ArrayList<LoggedUser>();
		String line;
		
		while((line = reader.readLine()) != null) {
			String[] employee = line.split(", ");
			
			if(employee[0].equals("")) {
			}
			else {
				tempList.add(new LoggedUser(Integer.parseInt(employee[0]), 
											employee[1], 
											employee[2]));
			}
		}

		return tempList;
	}


	public Boolean checkLoginList(List<LoggedUser> _list, int _id) {

		boolean exist = false;

		for(int i=0; i < _list.size(); i++) {
			if((_list.get(i).getId()) == _id) {
				exist = true;
				i = _list.size();
			}
			else {
				exist = false;
			}
		}
		return exist;
	}

	public Boolean checkLoginList(List<LoggedUser> _list, String _username) {

		boolean exist = false;

		for(int i=0; i < _list.size(); i++) {
			if((_list.get(i).getUsername()).equals(_username)) {
				exist = true;
				i = _list.size();
			}
			else {
				exist = false;
			}
		}
		return exist;
	}

	public Boolean checkLoginList(List<LoggedUser> _list, String _username, String _password) throws IOException {

		boolean exist = false;

		for(int i=0; i < _list.size(); i++) {

			if((_list.get(i).getUsername()).equals(_username) && (_list.get(i).getPassword()).equals(_password)) {

				exist = true;
				i = _list.size();

			}
			else if((_list.get(i).getUsername()).equals(_username) && !(_list.get(i).getPassword()).equals(_password)) {

				writeDeniedLogger(_username);
				
				exist = false;

			}
			else {
				exist = false;
			}
		}
		return exist;
	}

	public void writeEmployeeFile(List<Employee> _employeeList) throws IOException {
		FileWriter login = new FileWriter("files/employeeList.txt",false);
		BufferedWriter bw = new BufferedWriter(login);
		
		
		for(int i=0; i < _employeeList.size(); i++) {
			if(i == 0) {
				bw.write((_employeeList.get(i)).stringWrite());
			}
			else {
				bw.write("\n" + (_employeeList.get(i).stringWrite()));
			}
		}

		bw.flush();
		
		if(bw != null) {
			bw.close();
		}
	}


	public void writeSuccessfulLogger(String _username) throws IOException {
		SimpleDateFormat utcDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
		utcDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-4"));
		FileWriter login = new FileWriter("files/successfulLogger.txt",true);
		BufferedWriter bw = new BufferedWriter(login);
		bw.write(_username + " -- " + utcDateFormat.format(new Date()) + "\n");
		bw.close();
	}


	public void writeDeniedLogger(String _username) throws IOException {
		SimpleDateFormat utcDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
		utcDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-4"));
		FileWriter login = new FileWriter("files/deniedLogger.txt",true);
		BufferedWriter bw = new BufferedWriter(login);
		bw.write(_username + " -- " + utcDateFormat.format(new Date()) + "\n");
		bw.close();
	}
}
