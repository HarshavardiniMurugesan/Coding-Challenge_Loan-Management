package dao;

import java.sql.SQLException;

import exception.InvalidLoanException;

public interface ILoanRepository {
	
	public void applyLoan() throws SQLException;
	public void calculateInterest() throws InvalidLoanException, SQLException;
	public void loanStatus() throws InvalidLoanException, SQLException;
	public void calculateEMI() throws InvalidLoanException, SQLException;
	public void loanRepayment() throws SQLException, InvalidLoanException;
	public void getAllLoan() throws SQLException;
	public void getLoanById() throws SQLException, InvalidLoanException;

}
