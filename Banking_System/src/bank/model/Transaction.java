package bank.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    private final String description;
    private final double amount;
    private final double balanceAfter;
    private final String when;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Transaction(String description, double amount, double balanceAfter) {
        this.description  = description;
        this.amount       = amount;
        this.balanceAfter = balanceAfter;
        this.when         = LocalDateTime.now().format(FMT);
    }

    // Build a quick JSON string for the API response
    public String toJson() {
        return String.format(
            "{\"type\":\"%s\", \"amount\":%.2f, \"balance\":%.2f, \"timestamp\":\"%s\"}",
            description, amount, balanceAfter, when
        );
    }
}
