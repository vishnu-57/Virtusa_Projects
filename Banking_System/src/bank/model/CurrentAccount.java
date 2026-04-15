package bank.model;

// Current accounts get an overdraft facility up to 5000
public class CurrentAccount extends Account {

    private static final double OVERDRAFT_LIMIT = 5000.0;

    public CurrentAccount(String accNumber, double openingBalance) {
        super(accNumber, openingBalance);
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) return false;

        // Allow going negative, but only up to the overdraft limit
        if ((balance - amount) < -OVERDRAFT_LIMIT) return false;

        balance -= amount;
        recordWithdrawal(amount);
        return true;
    }
}
