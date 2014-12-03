package facades;

import com.google.gson.Gson;
import entity.Customer;
import exceptions.NotFoundException;
import interfaces.IAuthenticationFacade;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class AuthenticationFacade implements IAuthenticationFacade {

    private EntityManager em;
    private Gson gson = new Gson();

    public AuthenticationFacade() {
        this.em = createEntityManager();
    }
    
    protected AuthenticationFacade(EntityManager em) {
        this.em = em;
    }

    private static EntityManager createEntityManager() {
        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory("Sem3CA2PU");
        return emf.createEntityManager();
    }

    @Override
    public Customer addCustomer(String json) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        Customer c = gson.fromJson(json, Customer.class);

        try {
            em.persist(c);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return c;
    }

    @Override
    public String getCustomer(long id) throws NotFoundException {
        Customer c = em.find(Customer.class, id);
        if (c == null) {
            throw new NotFoundException("No user with that id");
        }
        return gson.toJson(c);
    }

    @Override
    public Customer deleteCustomer(long id) throws NotFoundException {
        Customer c = em.find(Customer.class, id);
        if (c == null) {
            throw new NotFoundException("No user with that id");
        }
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            em.remove(c);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return c;
    }

}
