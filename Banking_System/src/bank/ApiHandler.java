package bank;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import bank.model.Account;
import bank.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ApiHandler implements HttpHandler {

    private final BankService svc;

    public ApiHandler(BankService svc) {
        this.svc = svc;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        String path   = ex.getRequestURI().getPath();
        String method = ex.getRequestMethod();

        String body = "{\"error\": \"Not Found\"}";
        int    code = 404;

        try {
            if ("POST".equals(method) && "/api/register".equals(path)) {
                Map<String, String> fields = readBody(ex.getRequestBody());

                String user    = fields.get("username");
                String pass    = fields.get("password");
                String type    = fields.get("accountType");
                double deposit = parseDouble(fields.getOrDefault("initialDeposit", "0"));

                if (svc.register(user, pass, type, deposit)) {
                    body = "{\"status\": \"success\"}";
                    code = 200;
                } else {
                    body = "{\"error\": \"Registration failed. Check the minimum deposit for a savings account.\"}";
                    code = 400;
                }

            } else if ("POST".equals(method) && "/api/login".equals(path)) {
                Map<String, String> fields = readBody(ex.getRequestBody());
                User u = svc.login(fields.get("username"), fields.get("password"));

                if (u != null) {
                    body = "{\"status\": \"success\", \"username\": \"" + u.getUsername() + "\"}";
                    code = 200;
                } else {
                    body = "{\"error\": \"Wrong username or password\"}";
                    code = 401;
                }

            } else if ("GET".equals(method) && "/api/account".equals(path)) {
                Map<String, String> params = parseQuery(ex.getRequestURI().getQuery());
                User u = svc.getUser(params.get("username"));

                if (u != null) {
                    Account acc = u.getAccount();
                    String txJson = buildTransactionArray(acc);

                    body = String.format(
                        "{\"accountNumber\":\"%s\", \"balance\":%.2f, \"type\":\"%s\", \"transactions\":%s}",
                        acc.getAccountNumber(),
                        acc.getBalance(),
                        acc.getClass().getSimpleName(),
                        txJson
                    );
                    code = 200;
                } else {
                    body = "{\"error\": \"User not found\"}";
                    code = 404;
                }

            } else if ("POST".equals(method) && "/api/transaction".equals(path)) {
                Map<String, String> fields = readBody(ex.getRequestBody());
                String action   = fields.get("action");
                String username = fields.get("username");
                double amount   = parseDouble(fields.getOrDefault("amount", "0"));

                boolean ok = switch (action != null ? action : "") {
                    case "DEPOSIT"  -> svc.deposit(username, amount);
                    case "WITHDRAW" -> svc.withdraw(username, amount);
                    case "TRANSFER" -> svc.transfer(username, fields.get("targetAccNo"), amount);
                    default         -> false;
                };

                if (ok) {
                    body = "{\"status\": \"success\"}";
                    code = 200;
                } else {
                    body = "{\"error\": \"Transaction failed — check the amount or account details\"}";
                    code = 400;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            body = "{\"error\": \"Something went wrong on the server\"}";
            code = 500;
        }

        sendResponse(ex, code, body);
    }

    // ---- helpers ----

    private void sendResponse(HttpExchange ex, int statusCode, String responseBody) throws IOException {
        byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream out = ex.getResponseBody()) {
            out.write(bytes);
        }
    }

    private Map<String, String> readBody(InputStream in) throws IOException {
        String raw = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        return parseQuery(raw);
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null || query.isBlank()) return result;

        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                String k = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
                String v = URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
                result.put(k, v);
            }
        }
        return result;
    }

    private double parseDouble(String s) {
        try { return Double.parseDouble(s); }
        catch (NumberFormatException e) { return 0.0; }
    }

    private String buildTransactionArray(Account acc) {
        var txns = acc.getTransactions();
        if (txns.isEmpty()) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < txns.size(); i++) {
            sb.append(txns.get(i).toJson());
            if (i < txns.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}
