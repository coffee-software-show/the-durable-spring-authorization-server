package bootiful.api;

import org.springframework.data.repository.ListCrudRepository;

interface CustomerRepository extends ListCrudRepository<Customer, Integer> {
    Customer findCustomerById(Integer id);
}
