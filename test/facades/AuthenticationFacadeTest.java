package facades;

import com.google.gson.Gson;
import entity.Customer;
import exceptions.NotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class AuthenticationFacadeTest {

    private final Gson gson = new Gson();
    private static AuthenticationFacade facade;
    private static EntityManager em;

    public AuthenticationFacadeTest() {
        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory("Sem3CA5FinalJPAPU"); //Add your persistant name here!!
        em = emf.createEntityManager();
        facade = new AuthenticationFacade(em);
    }

    @BeforeClass
    public static void init() {

    }

    @Before
    public void setUp() {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.createNativeQuery("DELETE FROM CUSTOMER").executeUpdate();
        transaction.commit();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAddAndGetUser() {
        Customer c = new Customer("aGoodHash");
        Customer c2 = facade.addCustomer(gson.toJson(c));
        assertEquals(c2.getHash(), c.getHash());
    }

    @Test(expected = NotFoundException.class)
    public void testDeletePerson() throws NotFoundException {
        Customer c = new Customer("hash");
        Customer c2 = facade.addCustomer(gson.toJson(c));
        facade.deleteCustomer(c2.getId());
        facade.getCustomer(c2.getId());
    }

}
