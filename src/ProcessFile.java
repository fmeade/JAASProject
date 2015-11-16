package src;

import java.util.*;
import java.io.*;

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


	public Boolean checkEmployeeList(List<Employee> _list, int _id) {

		boolean exist = false;
		
		for(int i=0; i < _list.size(); i++) {
			if((_list.get(i)).getId() == _id) {
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

	public Boolean checkLoginList(List<LoggedUser> _list, String _username, String _password) {

		boolean exist = false;

		for(int i=0; i < _list.size(); i++) {

			if((_list.get(i).getUsername()).equals(_username) && (_list.get(i).getPassword()).equals(_password)) {
				exist = true;
				i = _list.size();
			}
			else {
				exist = false;
			}
		}
		return exist;
	}
}