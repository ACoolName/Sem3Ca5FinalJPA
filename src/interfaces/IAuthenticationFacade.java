package interfaces;

import entity.Customer;
import exceptions.NotFoundException;


public interface IAuthenticationFacade {
    Customer addCustomer(String json);
    
    String getCustomer(long id) throws NotFoundException;
    
    Customer deleteCustomer(long id) throws NotFoundException;
}
