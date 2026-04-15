package bank.model;

// Savings accounts must always keep a minimum balance of 500
public class SavingsAccount extends Account {

    private static final double MIN_BALANCE = 500.0;

    public SavingsAccount(String accNumber, double openingBalance) {
        super(accNumber, openingBalance);
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) return false;

        double remaining = balance - amount;
        if (remaining < MIN_BALANCE) {
            // Can't dip below the minimum — reject the withdrawal
            return false;
        }

        balance -= amount;
        recordWithdrawal(amount);
        return true;
    }
}
