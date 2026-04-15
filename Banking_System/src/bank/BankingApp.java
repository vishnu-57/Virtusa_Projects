package bank;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.Executors;

public class BankingApp {

    public static void main(String[] args) throws IOException {
        int port = 8080;

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        BankService bankService = new BankService();

        server.createContext("/api/", new ApiHandler(bankService));
        server.createContext("/",     new StaticFileHandler());

        // A small thread pool is enough for this demo
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();

        System.out.println("Server started — open http://localhost:" + port);
    }

    // Serves everything under /public
    static class StaticFileHandler implements HttpHandler {

        private static final String WEB_ROOT = "public";

        // Map file extensions to MIME types
        private static final Map<String, String> MIME = Map.of(
            ".html", "text/html",
            ".css",  "text/css",
            ".js",   "application/javascript"
        );

        @Override
        public void handle(HttpExchange ex) throws IOException {
            String urlPath = ex.getRequestURI().getPath();
            if ("/".equals(urlPath)) urlPath = "/index.html";

            File requested = new File(WEB_ROOT + urlPath);

            if (requested.exists() && requested.isFile()) {
                String ext         = urlPath.substring(urlPath.lastIndexOf('.'));
                String contentType = MIME.getOrDefault(ext, "text/plain");

                ex.getResponseHeaders().set("Content-Type", contentType);
                ex.sendResponseHeaders(200, requested.length());

                try (OutputStream out = ex.getResponseBody()) {
                    Files.copy(requested.toPath(), out);
                }
            } else {
                byte[] msg = "404 - Page not found".getBytes();
                ex.sendResponseHeaders(404, msg.length);
                try (OutputStream out = ex.getResponseBody()) {
                    out.write(msg);
                }
            }
        }
    }
}
