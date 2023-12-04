package controller;

import view.ExpenseTrackerView;
import model.ExpenseTrackerModel;
import model.Transaction;
import model.Filter.TransactionFilter;
import model.ExpenseTrackerModelListener;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class ExpenseTrackerController implements ExpenseTrackerModelListener {
  
  private ExpenseTrackerModel model;
  private ExpenseTrackerView view;
  private TransactionFilter filter;

  public ExpenseTrackerController(ExpenseTrackerModel model, ExpenseTrackerView view) {
    this.model = model;
    this.view = view;
    this.model.register(this); // Register the controller as a listener to the model
  }

  public void setFilter(TransactionFilter filter) {
    this.filter = filter;
  }

  public boolean addTransaction(double amount, String category) {
    if (!InputValidation.isValidAmount(amount) || !InputValidation.isValidCategory(category)) {
      return false;
    }
    
    Transaction t = new Transaction(amount, category);
    model.addTransaction(t);
    return true;
  }

  public void applyFilter() {
    if (filter != null) {
      List<Transaction> transactions = model.getTransactions();
      List<Transaction> filteredTransactions = filter.filter(transactions);
      List<Integer> rowIndexes = new ArrayList<>();
      for (Transaction t : filteredTransactions) {
        int rowIndex = transactions.indexOf(t);
        if (rowIndex != -1) {
          rowIndexes.add(rowIndex);
        }
      }
      model.setMatchedFilterIndices(rowIndexes);
    } else {
      JOptionPane.showMessageDialog(view, "No filter applied");
      view.toFront();
    }
  }

  public boolean undoTransaction(int rowIndex) {
    if (rowIndex >= 0 && rowIndex < model.getTransactions().size()) {
      Transaction removedTransaction = model.getTransactions().get(rowIndex);
      model.removeTransaction(removedTransaction);
      return true;
    }
    return false;
  }

  // Implement the method from ExpenseTrackerModelListener
  public void update(ExpenseTrackerModel model) {
    view.update(model); // Notify the view about the model changes
  }
}
