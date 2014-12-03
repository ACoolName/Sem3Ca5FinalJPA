package facades;

import com.google.gson.Gson;
import entity.Customer;
import exceptions.NotFoundException;
import interfaces.IAuthenticationFacade;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationFacadeMock implements IAuthenticationFacade {

    private Gson gson = new Gson();
    private Map<Long, Customer> customers = new HashMap();
    private Long nextId = 0L;

    @Override
    public Customer addCustomer(String json) {
        Customer c = gson.fromJson(json, Customer.class);
        customers.put(nextId++, c);
        return c;
    }

    @Override
    public String getCustomer(long id) throws NotFoundException {
        Customer c = customers.get(id);
        if (c == null) {
            throw new NotFoundException("No customer exists for the given id");
        }
        return gson.toJson(c);
    }

    @Override
    public Customer deleteCustomer(long id) throws NotFoundException {
        Customer c = customers.get(id);
        if (c == null) {
            throw new NotFoundException("No customer exists for the given id");
        }
        customers.remove(id);
        return c;
    }
}
