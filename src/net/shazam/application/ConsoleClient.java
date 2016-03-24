package net.shazam.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import net.shazam.backingbeans.GradesBean;
import net.shazam.entities.StudentTest;

/**
 * This class is in charge of our console client. It also serves as the entry point
 * into our application. It collects input from the user and calls the appropriate 
 * methods in GradesBean based of the user's input. 
 * 
 * @author alexdrawbond
 *
 */
public class ConsoleClient {
	//Save some typing when printing out to the console
	private static final PrintStream OUT = System.out;
	//Allows us to collect user input
	private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	/**
	 * Entry point to our application. It fires up our console client
	 * and does some clean up.
	 * @param args
	 */
	public static void main(String[] args) {
		//Start console client
		ConsoleClient client = new ConsoleClient();
		client.getCRUDOperationFromUser();
		
		//Be a good citizen: close the entity manager factory to recycle resources
		GradesBean.shutdownEntityManagerFactory();
		try {
			//Be a good citizen: close the buffered reader as it can lock files on OS
			br.close();
		} catch(IOException e) {
			OUT.println(e);
		}	
	}

	/**
	 * This method asks the user what operation they want to
	 * perform, and call the appropriate method. 
	 */
	private void getCRUDOperationFromUser() {
		try {
			boolean run = true;
			while(run) {
				OUT.println("Please select a CRUD operation:\n\t" 
						+ "1) Enter a new test record\n\t"
						+ "2) Query for a test record\n\t"
						+ "3) Update the score ofa test record\n\t"
						+ "4) Delete all test records for a student\n\t"
						+ "5) Exit");
				String userOperation = br.readLine();
				
				switch(userOperation) {
				case "1":
					createNewTestRecord();
					break;
				case "2":
					readTestRecords();
					break;
				case "3":
					updateTestRecord();
					break;
				case "4":
					deleteTestRecords();
					break;
				case "5":
					run = false;
					OUT.println("Shutting down system...");
					break;
				default:
					OUT.println("Invalid response");
				}
			}
		} catch(IOException e) {
			OUT.println(e);
		}
	}
	
	/**
	 * Collects information from user to create new test record. Calls appropriate
	 * method in GradesBean to persist record to the database.
	 */
	private void createNewTestRecord() {
		try {
			OUT.print("Please enter the student's first name: ");
			String firstName = br.readLine();
			
			OUT.print("\nPlease enter the student's last name: ");
			String lastName = br.readLine();
			
			OUT.print("\nPlease enter the date the test was taken: ");
			String stringDate = br.readLine();
			Calendar testDate = getCalendarFromString(stringDate);
			
			OUT.print("\nPlease enter the score the student earned on the test: ");
			String stringScore = br.readLine();
			BigDecimal score = new BigDecimal(stringScore);
			
			GradesBean gb = new GradesBean();
			boolean createSuccessful = gb.createTestRecord(firstName, lastName, score, testDate);
			
			if(createSuccessful)
				OUT.println("Record successfully create for " + firstName + " " + lastName);
			else
				OUT.println("Failed to create record");
		} catch(IOException e) {
			OUT.println(e);
		}
	}
	
	/**
	 * Prompts user which field they would like to use to query for records. 
	 * Options are: 
	 * 				query by last name
	 * 				query by date greater than or equal to
	 * 				query by score
	 */
	private void readTestRecords() {		
		try {
			OUT.print("Please select which field you want to query on"
					+ "\n\t1) Last Name" 
					+ "\n\t2) Date"
					+ "\n\t3) Score\n");
			String queryField = br.readLine();
			List<StudentTest> tests;
			
			switch(queryField) {
			case "1":
				tests = getTestRecordsByLastName();
				printTests(tests);
				break;
			case "2":
				tests = getTestRecordsByDate();
				printTests(tests);
				break;
			case "3":
				tests = getTestRecordsByScore();
				printTests(tests);
				break;
			default:
				OUT.println("Invalid response");
			}			
		} catch(IOException e) {
			OUT.println(e);
		}
	}
	
	/**
	 * Prompts user for which record is to be updated and what 
	 * the score should be changed to. Call appropriate method
	 * in GradesBean to update records in database.
	 */
	private void updateTestRecord() {
		try {
			OUT.print("Please enter a last name: ");
			String lastName = br.readLine();
			
			OUT.print("\nPlease enter a date: ");
			String stringDate = br.readLine();
			Calendar testDate = getCalendarFromString(stringDate);
			
			OUT.print("\nPlease enter the new score: ");
			String stringScore = br.readLine();
			BigDecimal score = new BigDecimal(stringScore);
			
			GradesBean gb = new GradesBean();
			boolean updateSuccessful = gb.updateTestRecords(lastName, testDate, score);
			
			if(updateSuccessful)
				OUT.println("Record updated successfully");
			else
				OUT.println("Record not found");
		} catch(IOException e) {
			OUT.println(e);
		}
	}
	
	/**
	 * Prompts user for which students whose records are to be deleted. 
	 * Calls appropriate method in GradesBean to delete records in database
	 */
	private void deleteTestRecords() {
		try {
			OUT.print("Please enter the last name of the student whose records you want to delete: ");
			String lastName = br.readLine();
			
			GradesBean gb = new GradesBean();
			boolean updateSuccessful = gb.deleteTestRecordsByLastName(lastName);
			
			if(updateSuccessful)
				OUT.println("Records deleted successfully");
			else
				OUT.println("Failed to delete records");
		} catch(IOException e) {
			OUT.println(e);
		}
	}
	
	/**
	 * Prompts user for last name to search against. Calls appropriate
	 * method in GradesBean to query all records belonging to the 
	 * given student.
	 * @return list of tests found in the database
	 */
	private List<StudentTest> getTestRecordsByLastName() {
		OUT.print("Please enter the last name: ");
		List<StudentTest> tests = null;
		try {
			String lastName = br.readLine();
			
			GradesBean gb = new GradesBean();
			tests = gb.queryTestRecords(lastName);
		} catch(IOException e) {
			OUT.println(e);
		}
		return tests;
	}
	
	/**
	 * Prompts user for date to search against. Calls appropriate
	 * method in GradesBean to query all records on or after the
	 * date supplied by the user.
	 * @return list of tests found in the database
	 */
	private List<StudentTest> getTestRecordsByDate() {
		OUT.print("Please enter a date: ");
		List<StudentTest> tests = null;
		try {
			String stringDate = br.readLine();
			Calendar testDate = getCalendarFromString(stringDate);
			
			GradesBean gb = new GradesBean();
			tests = gb.queryTestRecords(testDate);
		} catch(IOException e) {
			OUT.println(e);
		}
		return tests;
	}
	
	/**
	 * Prompts user for range of scores to search against. Calls appropriate
	 * method in GradesBean to query all records within (inclusive) the 
	 * score range.
	 * @return list of tests found in the database
	 */
	private List<StudentTest> getTestRecordsByScore() {
		List<StudentTest> tests = null;
		
		try {
			OUT.print("Please enter the minimum score: ");
			String stringMinScore = br.readLine();
			OUT.print("\nPlease enter the maximum score: ");
			String stringMaxScore = br.readLine();
			
			GradesBean gb = new GradesBean();
			tests = gb.queryTestRecords(new BigDecimal(stringMinScore), new BigDecimal(stringMaxScore));
		} catch(IOException e) {
			OUT.println(e);
		}		
		return tests;
	}
	
	/**
	 * Prints out all the tests in {@code tests}.
	 * @param tests list of test records found in database
	 */
	private void printTests(List<StudentTest> tests) {
		if(tests.size() == 0 || tests == null)
			OUT.println("No records found");
		for(StudentTest test : tests) 
			OUT.println(test);
	}
	
	/**
	 * Takes a string date with format MM/dd/YYYY and converts
	 * it to a {@code Calendar} object.
	 * @param stringDate String representation of date to be converted to {@code Calendar}
	 * @return a {@code Calendar} object converted from {@code stringDate}
	 */
	private static Calendar getCalendarFromString(String stringDate) {
		String year;
		String month;
		String day;
		String [] parsedStringDate = stringDate.split("/");
		Calendar date = Calendar.getInstance();
		
		year = parsedStringDate[2];
		month = parsedStringDate[0];
		day = parsedStringDate[1]; 
		
		date.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));

		return date;
	}
}
