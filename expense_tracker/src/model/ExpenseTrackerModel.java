package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class applies the Observer design pattern as the Observable class.
 * Represents the model component of an Expense Tracker application.
 * Manages expenses and provides functionalities for adding, removing,
 * and observing transactions.
 */
public class ExpenseTrackerModel {

  //encapsulation - data integrity
  private List<Transaction> transactions;
  private List<Integer> matchedFilterIndices;

  // This is applying the Observer design pattern.                          
  // Specifically, this is the Observable class. 
    
  public ExpenseTrackerModel() {
    transactions = new ArrayList<Transaction>();
    matchedFilterIndices = new ArrayList<Integer>();
  }

  /**
   * Adds a transaction to the tracker.
   * @param t The transaction to be added
   */
  public void addTransaction(Transaction t) {
    // Perform input validation to guarantee that all transactions added are non-null.
    if (t == null) {
      throw new IllegalArgumentException("The new transaction must be non-null.");
    }
    transactions.add(t);
    // The previous filter is no longer valid.
    matchedFilterIndices.clear();
    stateChanged();
  }

  /**
   * Removes a transaction from the tracker.
   * @param t The transaction to be removed
   */
  public void removeTransaction(Transaction t) {
    transactions.remove(t);
    // The previous filter is no longer valid.
    matchedFilterIndices.clear();
    stateChanged();
  }

  /**
   * Returns all the transactions.
   * @return List of transactions
   */
  public List<Transaction> getTransactions() {
    //encapsulation - data integrity
    return Collections.unmodifiableList(new ArrayList<>(transactions));
  }

  /**
   * Sets the index of the rows of transactions which match the specified filter value.
   * @param newMatchedFilterIndices List of matched indices
   */
  public void setMatchedFilterIndices(List<Integer> newMatchedFilterIndices) {
      
      if (newMatchedFilterIndices == null) {
	  throw new IllegalArgumentException("The matched filter indices list must be non-null.");
      }
      for (Integer matchedFilterIndex : newMatchedFilterIndices) {
	  if ((matchedFilterIndex < 0) || (matchedFilterIndex > this.transactions.size() - 1)) {
	      throw new IllegalArgumentException("Each matched filter index must be between 0 (inclusive) and the number of transactions (exclusive).");
	  }
      }
      // For encapsulation, copy in the input list 
      this.matchedFilterIndices.clear();
      this.matchedFilterIndices.addAll(newMatchedFilterIndices);
      stateChanged();
  }

  /**
   * Returns all the indices of the rows of transactions.
   * @return List of all Matched Filter Indices
   */
  public List<Integer> getMatchedFilterIndices() {
      // For encapsulation, copy out the output list
      List<Integer> copyOfMatchedFilterIndices = new ArrayList<Integer>();
      copyOfMatchedFilterIndices.addAll(this.matchedFilterIndices);
      return copyOfMatchedFilterIndices;
  }
  
  //List to hold registered observers
  private List<ExpenseTrackerModelListener> listeners = new ArrayList<>();

  /**
   * Registers the given ExpenseTrackerModelListener for
   * state change events.
   *
   * @param listener The ExpenseTrackerModelListener to be registered
   * @return If the listener is non-null and not already registered,
   *         returns true. If not, returns false.
   */   
  public boolean register(ExpenseTrackerModelListener listener) {
      // For the Observable class, this is one of the methods.
      
	  if (listener != null && !listeners.contains(listener)) {
          listeners.add(listener);
          return true;
      }
      return false;
  }

  /**
   * Gives the number of listeners
   *
   * @return Total number of listeners
   */ 
  public int numberOfListeners() {
      // For testing, this is one of the methods.
      
		  return listeners.size();
  }

  /**
   * Checks if a listener is present
   *
   * @param listener The listener object
   * @return True if it is present, false if not.
   */ 
  public boolean containsListener(ExpenseTrackerModelListener listener) {
      // For testing, this is one of the methods.
	  
	  return listeners.contains(listener);
  }

  /**
   * Updates the listeners' list when the state of the application is changed
   *
   */ 
  protected void stateChanged() {
      // For the Observable class, this is one of the methods.
      
	  for (ExpenseTrackerModelListener listener : listeners) {
		  listener.update(this);
      }
  }
}
