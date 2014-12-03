package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import exceptions.InvalidRequestException;
import exceptions.NotFoundException;
import facades.AuthenticationFacade;
import interfaces.IAuthenticationFacade;

public class Rest {

    static int port = 8080;
    static String ip = "127.0.0.1";
    private static final boolean DEVELOPMENT_MODE = true;
    private IAuthenticationFacade facade;
    private Gson gson;
    private HttpServer server;

    public void run() throws IOException {
        server = HttpServer.create(new InetSocketAddress(ip, port), 0);
        //REST Routes
        server.createContext("/user", new HandlerUser());
        facade = new AuthenticationFacade();
        gson = new Gson();
        server.start();
    }

    public static void main(String[] args) throws Exception {
        if (args.length >= 2) {
            port = Integer.parseInt(args[0]);
            ip = args[1];
        }
        new Rest().run();
    }

    //used for testing
    public HttpServer getServer() {
        return server;
    }

    //method used for testing
    public static Rest getRestServer(String[] args) throws Exception {
        if (args.length >= 3) {
            port = Integer.parseInt(args[0]);
            ip = args[1];
        }
        return new Rest();
    }

    public void setFacade(IAuthenticationFacade facade) {
        this.facade = facade;
    }

    class HandlerUser implements HttpHandler {

        public HandlerUser() {
        }

        @Override
        public void handle(HttpExchange he) throws IOException {
            int status = 200;
            String response = "";
            String method = he.getRequestMethod().toUpperCase();
            switch (method) {
                case "GET":
                    try {
                        response = handleGet(he);
                    } catch (NotFoundException nfe) {
                        response = nfe.getMessage();
                        status = 404;
                    }
                    break;
                case "POST":
                    try {
                        response = handlePost(he);
                    } catch (IllegalArgumentException iae) {
                        status = 400;
                        response = iae.getMessage();
                    } catch (IOException e) {
                        status = 500;
                        response = "Internal Server Problem";
                    }
                    break;
                case "PUT":
                    break;
                case "DELETE":
                    try {
                        response = handleDelete(he);
                    } catch (NotFoundException ex) {
                        response = ex.getMessage();
                        status = 404;
                    } catch (InvalidRequestException ex) {
                        response = ex.getMessage();
                        status = 400;
                    }
                    break;
            }
            if (status == 200) {
                he.getResponseHeaders().add("Content-Type", "application/json");
                System.out.println("content json");
            } else {
                he.getResponseHeaders().add("Content-Type", "text/plain");
                System.out.println("content text");
            }
            he.sendResponseHeaders(status, 0);
            try (OutputStream os = he.getResponseBody()) {
                os.write(response.getBytes());
            }
        }

        private String handleGet(HttpExchange he) throws NotFoundException {
            String response = "";
            String path = he.getRequestURI().getPath();
            int lastIndex = path.lastIndexOf("/");
            if (lastIndex > 0) {  //person/id
                String username = path.substring(lastIndex + 1);
                //get user by user name
                //response = facade.getPerson(id);
                //throw not found exception
            }
            return response;
        }

        private String handlePost(HttpExchange he) throws UnsupportedEncodingException, IOException {
            InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String jsonQuery = br.readLine();
            String response = "";
            if (jsonQuery.contains("<") || jsonQuery.contains(">")) {
                throw new IllegalArgumentException("Illegal characters in input");
            }
            //convert and add user
//            Person p = gson.fromJson(jsonQuery, Person.class);
//            p = facade.addPerson(jsonQuery);
//            response = new Gson().toJson(p);
            return response;
        }

        private String handleDelete(HttpExchange he) throws InvalidRequestException, NotFoundException {
            String response = "";
            String path = he.getRequestURI().getPath();
            int lastIndex = path.lastIndexOf("/");
            if (lastIndex > 0) {  //person/id
                int id = Integer.parseInt(path.substring(lastIndex + 1));
                // Call to facade for what ever you want to delete
                //response = new Gson().toJson(<--Deleted-->);
                //throw not found exception
            } else {
                throw new InvalidRequestException("Request is missing Username.");
            }
            return response;
        }

    }
}
