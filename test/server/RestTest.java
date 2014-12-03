package server;

import com.google.gson.Gson;
import entity.Customer;
import facades.AuthenticationFacadeMock;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class RestTest {

    static AuthenticationFacadeMock facade;
    static Rest restServer;
    private final Gson gson = new Gson();
    final int BEGIN_ID = 0;

    public RestTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        String[] args = {"127.0.0.1", "8080"};
        restServer = Rest.getRestServer(args);
        restServer.run();
    }

    @Before
    public void setUp() {
        facade = new AuthenticationFacadeMock();
        restServer.setFacade(facade);
    }

    @After
    public void tearDown() {
        restServer.getServer().stop(0);
    }

    @Test
    public void handleGetCustomerValidRequest() throws IOException {
        Customer expected = new Customer("h4sh");
        facade.addCustomer(gson.toJson(expected));
        URLConnection connection = new URL("http://localhost:8080/customer/" + Integer.toString(BEGIN_ID)).openConnection();
        connection.connect();
        InputStreamReader isr = new InputStreamReader(connection.getInputStream(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String response = br.readLine();
        Customer given = gson.fromJson(response, Customer.class);
        assertEquals(expected.getHash(), given.getHash());
    }

}
