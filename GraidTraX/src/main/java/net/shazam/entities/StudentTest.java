package net.shazam.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This is a Persistence Entity class. An instance of this class represents
 * a record in the STUDENT_TESTS table. Persistence Entities provide a 
 * layer of abstraction between your application and the database, allowing
 * you to work with a Java object as opposed to a database.
 * @author alexdrawbond
 *
 */

//This annotation tells JPA our class is a Persistence Entity
@Entity
//This annotation tell JPA which table our Persistence Entity represents
@Table(name="STUDENT_TESTS", schema = "SHAZAM")
//This annotation binds a static query to our Persistence Entity
@NamedQuery(name = "QueryStudentTestByScore", 
			query = "SELECT st FROM StudentTest st where st.score >= :minScore and st.score <= :maxScore")
public class StudentTest implements Serializable {
	//This annotation tells JPA that this column contains a unique value for each record
	//Essentially telling JPA that this is our primary key
	@Id
	//This annotation tells JPA that our database will handle generating a unique value
	//for ID for each record
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//This annotation tells JPA which column to map our attribute to
	@Column(name = "ID")
	private String id;
	
	//This annotation tells JPA which column to map our attribute to
	@Column(name = "FIRST_NAME")
	private String firstName;
	
	//This annotation tells JPA which column to map our attribute to
	@Column(name = "LAST_NAME")
	private String lastName;
	
	//This annotation tells JPA to save our attribute as a date
	//It is always required when persisting attributes of type 
	//{@code Date} or {@code Calendar}
	@Temporal(TemporalType.DATE)
	//This annotation tells JPA which column to map our attribute to
	@Column(name = "TEST_DATE")
	private Calendar testDate;
	
	//This annotation tells JPA which column to map our attribute to
	@Column(name = "SCORE")
	private BigDecimal score;
	
	//JPA does NOT require Persistence Entities to be serializable
	//In our simple application it is not necessary, however, in 
	//most enterprise applications Persistence entities will typically
	//need to be serializable
	private static final long serialVersionUID = 1L;

	//JPA requires a public, no-arg constructor
	public StudentTest() {
		super();
	}   
	
	//This constructor is for us, to instantiate new StudentTest instances
	public StudentTest(String firstName, String lastName, BigDecimal score, Calendar testDate) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.testDate = testDate;
		this.score = score;
	}
	
	//Getters and setters for our attributes, required by JPA
	//and generally a good design principal
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}   
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}   
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}   
	public Calendar getTestDate() {
		return this.testDate;
	}

	public void setTestDate(Calendar testDate) {
		this.testDate = testDate;
	}   
	public BigDecimal getScore() {
		return this.score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}
	
	//JPA does NOT require you to override Object's toString(), however it
	//is often convenient to do so for logging and debugging purposes
	@Override
	public String toString() {
		return (testDate.get(Calendar.MONTH) + 1) + "/" + testDate.get(Calendar.DATE) + "/" + testDate.get(Calendar.YEAR) +
				" " + firstName + " " + lastName + ": " + score.toString();
	}   
}