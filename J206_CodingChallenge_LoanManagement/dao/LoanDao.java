package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import util.*;
import entity.*;
import exception.InvalidLoanException;

public class LoanDao {

	Connection com;
	PreparedStatement ps;
	Statement stmt;
	ResultSet rs;
	List<Loan> loan=new ArrayList<Loan>();
	List<Customer> c=new ArrayList<Customer>();

	public void applyLoan(Loan loan,HomeLoan hl,CarLoan cl, Customer c) throws SQLException {

		com = LoanUtil.getDBConn();
		ps = com.prepareStatement("insert into loan values(?,?,?,?,?,?,?)");
		ps.setInt(1, loan.getLoanId());
		ps.setInt(2, c.getCustomerId());
		ps.setDouble(3, loan.getPrincipalAmount());
		ps.setDouble(4, loan.getInterestRate());
		ps.setInt(5, loan.getLoanTerm());
		ps.setString(6, loan.getLoanType());
		ps.setString(7, "Pending");
		if(loan.getLoanType().equals("homeloan")) {
		ps = com.prepareStatement("insert into home_loan values(?,?)");
		ps.setString(1, hl.getPropertyAddress());
		ps.setInt(2, hl.getPropertyValue());
		}
		else {
			ps = com.prepareStatement("insert into car_loan values(?,?)");
			ps.setString(1, cl.getCarModel());
			ps.setInt(2, cl.getCarValue());
		}
		int no = ps.executeUpdate();
		if (no > 0) {
			System.out.println("Loan application submitted successfully.");
		} else {
			System.out.println("Failed to submit loan application.");
		}

	}

	public double calculateInterest(int loanId) throws InvalidLoanException, SQLException {

		com = LoanUtil.getDBConn();
		ps = com.prepareStatement("SELECT principal_Amount, interest_Rate, loan_Term FROM Loan WHERE loan_Id = ?");
		ps.setInt(1, loanId);
		rs = ps.executeQuery();
		// System.out.println("selected successfully");
		// rs.next();
		if (rs.next()) {
			double principalAmount = rs.getDouble(1);
			double interestRate = rs.getDouble(2);
			int loanTerm = rs.getInt(3);
			return (principalAmount * interestRate * loanTerm) / 12;
		} else {
			throw new InvalidLoanException("Loan not found with ID: " + loanId);
		}}

	public void loanStatus(int loanId) throws SQLException, InvalidLoanException {

		com = LoanUtil.getDBConn();
		ps = com.prepareStatement(
				"SELECT credit_Score FROM Customer WHERE customer_Id = (SELECT customer_Id FROM Loan WHERE loan_Id = ?)");
		ps.setInt(1, loanId);
		rs = ps.executeQuery();
		System.out.println("selected successfully");

		if (rs.next()) {
			int creditScore = rs.getInt(1);
			String status = (creditScore > 5) ? "Approved" : "Rejected";
			ps = com.prepareStatement("UPDATE Loan SET loan_Status = ? WHERE loan_Id = ?");
			ps.setString(1, status);
			ps.setInt(2, loanId);
			int no = ps.executeUpdate();
			if (no > 0) {
				System.out.println("Loan status updated successfully: " + status);
			} else {
				System.out.println("Failed to update loan status.");
			}
		} else {
			throw new InvalidLoanException("Loan not found with ID: " + loanId);
		}
	}

	public double calculateEMI(int loanId) throws InvalidLoanException, SQLException {

		com = LoanUtil.getDBConn();

		ps = com.prepareStatement("SELECT principal_Amount, interest_Rate, loan_Term FROM Loan WHERE loan_Id = ?");
		ps.setInt(1, loanId);
		rs = ps.executeQuery();
		if (rs.next()) {
			double principalAmount = rs.getDouble(1);
			double interestRate = rs.getDouble(2);
			int loanTerm = rs.getInt(3);
			double monthlyInterestRate = interestRate / 12 / 100;
			int numberOfPayments = loanTerm;
			return (principalAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, numberOfPayments))
					/ (Math.pow(1 + monthlyInterestRate, numberOfPayments) - 1);
		} else {
			throw new InvalidLoanException("Loan not found with ID: " + loanId);
		}
	}

	public void loanRepayment(int loanId, double amount) throws SQLException, InvalidLoanException {

		com = LoanUtil.getDBConn();

		ps = com.prepareStatement("SELECT principal_Amount, interest_Rate, loan_Term FROM Loan WHERE loan_Id = ?");

		ps.setInt(1, loanId);
		rs = ps.executeQuery();
		if (rs.next()) {
			double principalAmount = rs.getDouble(1);
			double interestRate = rs.getDouble(2);
			int loanTerm = rs.getInt(3);
			double monthlyInterestRate = interestRate / 12 / 100;
			int numberOfPayments = loanTerm;
			double emi = (principalAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, numberOfPayments))
					/ (Math.pow(1 + monthlyInterestRate, numberOfPayments) - 1);
			System.out.println("===" + emi);
			int noOfEmi = (int) (amount / emi);
			System.out.println("*****" + noOfEmi);
			if (noOfEmi == 0) {
				System.out.println("Amount is less than one EMI. Repayment rejected.");
			} else {
				System.out.println("No. of EMIs paid from the amount: " + noOfEmi);
				double remainingAmount = principalAmount - (noOfEmi * emi);
				ps = com.prepareStatement("UPDATE Loan SET principal_Amount = ? WHERE loan_Id = ?");
				ps.setDouble(1, remainingAmount);
				ps.setInt(2, loanId);
				int no = ps.executeUpdate();
				if (no > 0) {
					System.out.println("Loan repayment successful. Remaining principal amount: " + remainingAmount);
				} else {
					System.out.println("Failed to update loan amount.");

				}
			}
		} else {
			throw new InvalidLoanException("Loan not found with ID: " + loanId);
		}

	}

	public void getAllLoan() throws SQLException {

		com = LoanUtil.getDBConn();
		stmt = com.createStatement();
		rs = stmt.executeQuery("select c.name,l.* from loan l inner join customer c on c.customer_id=l.customer_id");
		
		 /*while(rs.next()) {
		   loan.add(new Loan(rs.getString(1),rs.getInt(2),rs.getInt(3),
		  rs.getDouble(4),rs.getDouble(5),rs.getInt(6),rs.getString(7),rs.getString(8)));
		  }*/
		 
		while (rs.next()) {
			System.out.println("\nName: " + rs.getString(1));
			System.out.println("LoanID: " + rs.getInt(2));
			System.out.println("CustomerID: " + rs.getInt(3));
			System.out.println("Principal Amount: " + rs.getDouble(4));
			System.out.println("Interest Rate: " + rs.getDouble(5));
			System.out.println("Loan Term in Months: " + rs.getInt(6));
			System.out.println("Loan Type: " + rs.getString(7));
			System.out.println("Loan Status: " + rs.getString(8));

		}
	}

	public void getAllLoanById(int loanid) throws SQLException, InvalidLoanException {

		com = LoanUtil.getDBConn();
		ps = com.prepareStatement(
				"select c.name,l.* from loan l inner join customer c on c.customer_id=l.customer_id where loan_Id=?");
		ps.setInt(1, loanid);

		rs = ps.executeQuery();
		if (rs.next()) {
			System.out.println("\nName: " + rs.getString(1));
			System.out.println("LoanID: " + rs.getInt(2));
			System.out.println("CustomerID: " + rs.getInt(3));
			System.out.println("Principal Amount: " + rs.getDouble(4));
			System.out.println("Interest Rate: " + rs.getDouble(5));
			System.out.println("Loan Term in Months: " + rs.getInt(6));
			System.out.println("Loan Type: " + rs.getString(7));
			System.out.println("Loan Status: " + rs.getString(8));
		} else {
			throw new InvalidLoanException("Loan not found with ID: " + loanid);
		}
	}
}
