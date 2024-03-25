package main;

import java.sql.SQLException;
import java.util.Scanner;

import dao.LoanController;
import exception.InvalidLoanException;
import dao.ILoanRepository;

public class LoanMain {
	
	
	public static void main(String[] args) throws SQLException,InvalidLoanException {
		
		ILoanRepository ec = new LoanController();
		Scanner sc = new Scanner(System.in);
		String ch = null;
		do {
			
			System.out.println("1. Apply Loan");
			System.out.println("2. Calculate Interest");
			System.out.println("3. Loan Status Checking");
			System.out.println("4. Calculate EMI");
			System.out.println("5. Loan Repayment");
			System.out.println("6. Get all Loan Details");
			System.out.println("7. Get Loan Details by ID");
			System.out.println("***Enter your Choice:***");

			int choice = sc.nextInt();
			switch (choice) {
			case 1: {
				ec.applyLoan();
				break;
			}
			case 2: {
				ec.calculateInterest();
				break;
			}
			case 3: {
				ec.loanStatus();
				break;
			}
			case 4: {
				ec.calculateEMI() ;

				break;
			}
			case 5: {
				
				ec.loanRepayment();
				break;
			}
			case 6: {

				ec.getAllLoan();
				break;
			}
			case 7: {

				ec.getLoanById();
				break;
			}
			
			default: {
				System.out.println("Enter the right choice! ");
			}
			}
			
			System.out.println("Do you want to continue? Type: Y or y");
			ch = sc.next();
		} while (ch.equals("Y") || ch.equals("y"));
		System.out.println("Thank you !\nVisit Again !!");
		System.exit(0);
	}
	}


