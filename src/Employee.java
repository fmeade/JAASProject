package src;

import java.util.*;

public class Employee {
	
	private String name;
	private int id;
	private String position;
	private String supervisor; 
	private long salary;
	
	public Employee(String _name, int _id, String _position, String _supervisor, long _salary) {
		name = _name;
		id = _id;
		position = _position;
		supervisor = _supervisor;
		salary = _salary;
	}
	
	public String getName() { 
		return name; 
	}
	
	public int getId() { 
		return id; 
	}
	
	public String getPosition() { 
		return position; 
	}
	public void setPosition(String _position) { 
		position = _position; 
	}
	
	public String getSupervisor() { 
		return supervisor; 
	}
	
	public long getSalary() { 
		return this.salary; 
	}
	public void setSalary(long _salary) {
		salary = _salary; 
	}

	public String toString() {
		return "First Name: " + name + "\n" + 
				"ID: " + id + "\n" +
				"Position: " + position + "\n" +
				"Supervisor: " + supervisor + "\n" +
				"Salary: " + salary + "\n";
	}
}
