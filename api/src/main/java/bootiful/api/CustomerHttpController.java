package bootiful.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
class CustomerHttpController {

    private final CustomerRepository repository;

    CustomerHttpController(CustomerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/customers")
    Collection<Customer> customers() {
        return this.repository.findAll();
    }
}
