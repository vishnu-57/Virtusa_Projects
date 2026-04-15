package bank;

import bank.model.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;

public class BankService {

    // In-memory stores — would swap these for a real DB in production
    private final ConcurrentHashMap<String, User>    userStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Account> accStore  = new ConcurrentHashMap<>();

    private final AtomicInteger accSeed = new AtomicInteger(1000);

    public boolean register(String username, String password, String accountType, double openingDeposit) {
        if (userStore.containsKey(username)) {
            return false; // username already taken
        }

        String newAccNo = "ACC" + accSeed.incrementAndGet();
        Account acc;

        if ("SAVINGS".equalsIgnoreCase(accountType)) {
            if (openingDeposit < 500) return false; // savings need a minimum opening
            acc = new SavingsAccount(newAccNo, openingDeposit);
        } else {
            acc = new CurrentAccount(newAccNo, openingDeposit);
        }

        User newUser = new User(username, password, acc);
        userStore.put(username, newUser);
        accStore.put(newAccNo, acc);
        return true;
    }

    public User login(String username, String password) {
        User u = userStore.get(username);
        if (u != null && u.getPassword().equals(password)) return u;
        return null;
    }

    public User getUser(String username) {
        return userStore.get(username);
    }

    public boolean deposit(String username, double amount) {
        User u = userStore.get(username);
        if (u == null) return false;
        u.getAccount().deposit(amount);
        return true;
    }

    public boolean withdraw(String username, double amount) {
        User u = userStore.get(username);
        if (u == null) return false;
        return u.getAccount().withdraw(amount);
    }

    public boolean transfer(String senderUsername, String recipientAccNo, double amount) {
        User sender     = userStore.get(senderUsername);
        Account destAcc = accStore.get(recipientAccNo);

        if (sender == null || destAcc == null || amount <= 0) return false;

        Account srcAcc = sender.getAccount();

        // Debit the sender using withdraw, then replace the generic transaction with a
        // proper "transfer out" entry so the history reads clearly
        if (!srcAcc.withdraw(amount)) return false;

        List<Transaction> txList = srcAcc.getTransactions();
        txList.remove(txList.size() - 1);
        txList.add(new Transaction("TRANSFER OUT to " + recipientAccNo, amount, srcAcc.getBalance()));

        // Credit the recipient similarly
        destAcc.deposit(amount);
        List<Transaction> destTxList = destAcc.getTransactions();
        destTxList.remove(destTxList.size() - 1);
        destTxList.add(new Transaction("TRANSFER IN from " + srcAcc.getAccountNumber(), amount, destAcc.getBalance()));

        return true;
    }
}
