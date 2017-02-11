package net.shazam.backingbeans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.shazam.entities.StudentTest;

/**
 * This class serves as our interface between our console client 
 * and our persistent entity. 
 * @author alexdrawbond
 *
 */
public class GradesBean {
	//The Entity Manager Factory provides Entity Managers
	public static final EntityManagerFactory EMF  = Persistence.createEntityManagerFactory("GraidTraXDB");
	 
	/**
	 * Creates a {@code StudentTest} and saves it to the database.
	 * @param firstName student's first name
	 * @param lastName student's last name
	 * @param score score student earned on test
	 * @param testDate date test was taken
	 * @return a boolean indicating the record was successfully created
	 */
	public boolean createTestRecord(String firstName, String lastName, BigDecimal score, Calendar testDate) {
		boolean successful = true;
		//Manages persistence entities
		EntityManager em = EMF.createEntityManager();
		//Create object representing record
		StudentTest studentTest = new StudentTest(firstName, lastName, score, testDate);
		
		try {
			//begin transaction
			em.getTransaction().begin();
			//save object o database as record
			em.persist(studentTest);
			//commit the record to the database
			em.getTransaction().commit();
		} catch(Exception e) {
			successful = false;
			System.out.println(e);
		} finally {
			//Be a good citizen: close the entity manager so you
			//don't leave connections open to the database
			em.close();
		}
		
		return successful;
	}
	
	/**
	 * Queries for records by last name.
	 * @param lastName the last name to search for
	 * @return list of records found in database
	 */
	public List<StudentTest> queryTestRecords(String lastName) {
		//Manages persistence entities
		EntityManager em = EMF.createEntityManager();
		List<StudentTest> results = null;
		
		try {
			//Used to construct criteria queries, compound selections, expressions, predicates, orderings
			CriteriaBuilder cb = em.getCriteriaBuilder();
			//Used to construct all the criteria for our query
			CriteriaQuery<StudentTest> criteriaQuery = cb.createQuery(StudentTest.class);
			//Root object represents the "FROM" portion of a SELECT statement
			Root<StudentTest> rootItem = criteriaQuery.from(StudentTest.class);
			
			//Lets combine rootItem (FROM clause) with a WHERE clause
			criteriaQuery = criteriaQuery.select(rootItem).where(cb.equal(rootItem.<String>get("lastName"), lastName));
			//Create our SELECT statement in a type safe way
			TypedQuery<StudentTest> typedQuery = em.createQuery(criteriaQuery);
			//Run query against database and get results
			results = (List<StudentTest>) typedQuery.getResultList();
		} catch(Exception e) {
			System.out.println(e);
		} finally {
			//Be a good citizen: close the entity manager so you
			//don't leave connections open to the database
			em.close();
		}	
		
		return results;
	}
	
	/**
	 * Queries for records by date.
	 * @param testDate the date to search for
	 * @return list of records found in database
	 */
	public List<StudentTest> queryTestRecords(Calendar testDate) {
		//Manages persistence entities
		EntityManager em = EMF.createEntityManager();
		List<StudentTest> results = null;
		
		try {
			//Create a SELECT statement in a type safe way using JPQL
			TypedQuery<StudentTest> typedQuery = em.createQuery("select st from StudentTest st where st.testDate >= :testDate", StudentTest.class);
			//Substitute parameter with actual data
			typedQuery.setParameter("testDate", testDate);
			//Run query against database and get results
			results = typedQuery.getResultList();
		} catch(Exception e) {
			System.out.println(e);
		} finally {
			//Be a good citizen: close the entity manager so you
			//don't leave connections open to the database
			em.close();
		}
		
		return results;
	}
	
	/**
	 * Queries for records by a range of scores.
	 * @param minScore the minimum score to search for (inclusive)
	 * @param maxScore the maximum score to search for (inclusive)
	 * @return
	 */
	public List<StudentTest> queryTestRecords(BigDecimal minScore, BigDecimal maxScore) {
		//Manages persistence entities
		EntityManager em = EMF.createEntityManager();
		List<StudentTest> results = null;
		
		try {
			//Create a SELECT statement in a type safe way using a Named Query
			TypedQuery<StudentTest> typedQuery = em.createNamedQuery("QueryStudentTestByScore", StudentTest.class);
			//Substitute parameters with actual data
			typedQuery.setParameter("minScore", minScore);
			typedQuery.setParameter("maxScore", maxScore);
			//Run query against database and get results
			results = typedQuery.getResultList();
		} catch(Exception e) {
			System.out.println(e);
		} finally {
			//Be a good citizen: close the entity manager so you
			//don't leave connections open to the database
			em.close();
		}
		
		return results;
	}
	
	/**
	 * Queries for records by last name and date. Updates the records found with
	 * the new score entered by the user.
	 * 
	 * @param lastName the last name to search for
	 * @param testDate the date to search for
	 * @param score the score to search for
	 * @return boolean indicating that records were successfully updated
	 */
	public boolean updateTestRecords(String lastName, Calendar testDate, BigDecimal score) {
		boolean successful = true;
		//Manages persistence entities
		EntityManager em = EMF.createEntityManager();
		List<StudentTest> results = null;
		
		//Used to construct criteria queries, compound selections, expressions, predicates, orderings
		CriteriaBuilder cb = em.getCriteriaBuilder();
		//Used to construct all the criteria for our query
		CriteriaQuery<StudentTest> criteriaQuery = cb.createQuery(StudentTest.class);
		//Root object represents the "FROM" portion of a SELECT statement
		Root<StudentTest> rootItem = criteriaQuery.from(StudentTest.class);
		
		try {
			//List of Predicates to be ANDED together
			//Predicates are functions that map to a boolean value (true/false)
			List<Predicate> andedPredicates = new ArrayList<Predicate>();
			
			//Compare the last name enter by the user to last name in database
			andedPredicates.add(cb.equal(rootItem.<String>get("lastName"), lastName));
			//Compare the date entered by the user to the date in the database
			andedPredicates.add(cb.equal(rootItem.<Calendar>get("testDate"), testDate));
			
			//Use our Predicates to build a WHERE clause
			criteriaQuery.where(andedPredicates.toArray(new Predicate[andedPredicates.size()]));
			//Run query against database and get results
			results = em.createQuery(criteriaQuery).getResultList();
			
			if(results.size() == 0)
				successful = false;
			
			//begin transaction
			em.getTransaction().begin();
			//loop through each record retrieved from the database
			//and set its score to new value
			for(StudentTest test : results) {
				//set score
				test.setScore(score);
				//save record to database
				em.persist(test);
			}	
			//commit our changes to the database
			em.getTransaction().commit();
		} catch(Exception e) {
			System.out.println(e);
		} finally {
			//Be a good citizen: close the entity manager so you
			//don't leave connections open to the database
			em.close();
		}
		
		return successful;
	}
	
	/**
	 * Queries for records by last name, then deletes them all.
	 * @param lastName the last name to search for
	 * @return boolean indicating that records were successfully deleted
	 */
	public boolean deleteTestRecordsByLastName(String lastName) {
		boolean successful = true;
		//Manages persistence entities
		EntityManager em = EMF.createEntityManager();
		List<StudentTest> results = null;
		
		//Used to construct criteria queries, compound selections, expressions, predicates, orderings
		CriteriaBuilder cb = em.getCriteriaBuilder();
		//Used to construct all the criteria for our query
		CriteriaQuery<StudentTest> criteriaQuery = cb.createQuery(StudentTest.class);
		//Root object represents the "FROM" portion of a SELECT statement
		Root<StudentTest> rootItem = criteriaQuery.from(StudentTest.class);
		
		try {
			//Lets combine rootItem (FROM clause) with a WHERE clause
			criteriaQuery = criteriaQuery.select(rootItem).where(cb.equal(rootItem.<String>get("lastName"), lastName));
			//Create our SELECT statement in a type safe way
			TypedQuery<StudentTest> typedQuery = em.createQuery(criteriaQuery);
			//Run query against database and get results
			results = typedQuery.getResultList();
			
			//begin transaction
			em.getTransaction().begin();
			//loop through each record found in the database
			//and delete it
			for(StudentTest test : results) {
				//delete record
				em.remove(test);
			}
			//commit our deletes to the database
			em.getTransaction().commit();
		} catch(Exception e) {
			successful = false;
			System.out.println(e);
		} finally {
			//Be a good citizen: close the entity manager so you
			//don't leave connections open to the database
			em.close();
		}
		
		return successful;
	}
	
	/**
	 * Closes down the entity manager factory to free
	 * up resources.
	 */
	public static void shutdownEntityManagerFactory() {
		EMF.close();
	}
}
