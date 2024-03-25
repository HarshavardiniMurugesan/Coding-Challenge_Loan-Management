package dao;
import java.sql.SQLException;
import java.util.Scanner;

import dao.LoanDao;

import entity.*;
import exception.InvalidLoanException;

public class LoanController implements ILoanRepository {
	
	Scanner sc=new Scanner(System.in);
	LoanDao dao=new LoanDao();
	Loan loan=new Loan();
	HomeLoan hl=new HomeLoan();
	CarLoan cl=new CarLoan();
	Customer c=new Customer();
	
	public void applyLoan() throws SQLException {
		
		
		System.out.println("Do you want to apply loan? (yes/no)");
        String str = sc.nextLine().trim().toLowerCase();
        if (str.equals("yes")) {
          
		System.out.println("Enter Loan ID:");
		int newId = sc.nextInt();
		loan.setLoanId(newId);
		
		System.out.println("Enter Customer ID:");
		int newId2 = sc.nextInt();
		c.setCustomerId(newId2);

		System.out.println("Enter Principal Amount");
		double newAmt = sc.nextDouble();
		loan.setPrincipalAmount(newAmt);

		System.out.println("Enter Interest Rate");
		double newRate = sc.nextDouble();
		loan.setInterestRate(newRate);
		
		System.out.println("Enter Loan Term");
		int newTerm = sc.nextInt();
		loan.setLoanTerm(newTerm);

		System.out.println("Enter Loan Type(Home Loan/Car Loan)");
		String newType = sc.next();
		loan.setLoanType(newType);
		
		String str1 = sc.nextLine().trim().toLowerCase();
        if (str1.equals("homeloan")) {
        	System.out.println("Enter Property Address");
    		String newAddress= sc.next();
    		hl.setPropertyAddress(newAddress);
    		
    		System.out.println("Enter Property Value");
    		int newValue = sc.nextInt();
    		hl.setPropertyValue(newValue);
    		
        }else {
        	System.out.println("Enter Car Model");
    		String newModel= sc.next();
    		cl.setCarModel(newModel);
    		
    		System.out.println("Enter Car Value");
    		int newValue = sc.nextInt();
    		cl.setCarValue(newValue);
    		
        }
        dao.applyLoan(loan,hl,cl,c);

		
		
	}else {
		  System.out.println("Loan application cancelled.");
	        
			}}
	
	public void calculateInterest() throws InvalidLoanException, SQLException {
		
		System.out.println("Enter Loan ID:");
		int newId = sc.nextInt();
		loan.setLoanId(newId);
		double amount=dao.calculateInterest(newId);
		System.out.println("Interest="+amount);
	}
	
	public void loanStatus() throws InvalidLoanException, SQLException {
		
		System.out.println("Enter Loan ID:");
		int newId = sc.nextInt();
		loan.setLoanId(newId);
		dao.loanStatus(newId);
		
	}
	
	public void calculateEMI() throws InvalidLoanException, SQLException {
		
		System.out.println("Enter Loan ID:");
		int newId = sc.nextInt();
		loan.setLoanId(newId);
		double amount=dao.calculateEMI(newId);
		System.out.println("EMI="+amount);
	}
	
	public void loanRepayment() throws SQLException, InvalidLoanException {
		
		System.out.println("Enter Loan ID:");
		int newId = sc.nextInt();
		loan.setLoanId(newId);
		
		System.out.println("Enter Amount");
		double newAmt = sc.nextDouble();
		loan.setPrincipalAmount(newAmt);
		dao.loanRepayment(newId, newAmt);
	}
	
	public void getAllLoan() throws SQLException {
		
		dao.getAllLoan();
	}
	
	public void getLoanById() throws SQLException, InvalidLoanException {
		
		System.out.println("Enter Loan ID:");
		int newId = sc.nextInt();
		loan.setLoanId(newId);
		
		dao.getAllLoanById(newId);
	}


}
