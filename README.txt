The source files are in the directory: ./ .


To run the program from Command line (in Mac/Linux or Windows command prompt): 
	(1) Compile the code: 
		javac *.java
	(2) Run the code: 
		java -Djava.security.manager \
			-Djava.security.policy==policies.txt \
			 -Djava.security.auth.login.config==policy.txt \
			  Driver 

To run the program on Eclipse:
 	(1) Create a new Java project in Eclipse. 
	(2) Select the directory . to import the source files into
		Eclipse.
	(3) Select the Driver.java file. 
	(4) In the menu option: Run->Run Configurations, select the tab 
		"Arguments"
	(5) In the VM arguments add: 
 		-Djava.security.manager -Djava.security.policy==policies.txt -Djava.security.auth.login.config==policy.txt  
	(6) Run. 