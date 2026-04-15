package bank.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Account {

    private final String accNumber;
    protected double balance;
    private final List<Transaction> history = new ArrayList<>();

    public Account(String accNumber, double openingBalance) {
        this.accNumber = accNumber;
        this.balance   = openingBalance;
    }

    public String getAccountNumber()      { return accNumber; }
    public double getBalance()            { return balance; }
    public List<Transaction> getTransactions() { return history; }

    public void deposit(double amount) {
        if (amount <= 0) return;
        balance += amount;
        history.add(new Transaction("DEPOSIT", amount, balance));
    }

    // Each account type enforces its own withdrawal rules
    public abstract boolean withdraw(double amount);

    // Helper so subclasses don't have to build Transaction objects themselves
    protected void recordWithdrawal(double amount) {
        history.add(new Transaction("WITHDRAW", amount, balance));
    }

    protected void recordTransferOut(double amount, String toAcc) {
        history.add(new Transaction("TRANSFER OUT to " + toAcc, amount, balance));
    }

    protected void recordTransferIn(double amount, String fromAcc) {
        history.add(new Transaction("TRANSFER IN from " + fromAcc, amount, balance));
    }
}
