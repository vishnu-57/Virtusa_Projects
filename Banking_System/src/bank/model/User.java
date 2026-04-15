package bank.model;

// Simple container for user info + their linked account
public class User {

    private String username;
    private String password;   // TODO: hash this before any real deployment
    private Account account;

    public User(String username, String password, Account account) {
        this.username = username;
        this.password = password;
        this.account  = account;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Account getAccount() { return account; }
}
