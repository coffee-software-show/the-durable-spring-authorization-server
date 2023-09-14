package bootiful.api;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@ResponseBody
class EmailController {

    private final MessageChannel requests;

    private final CustomerRepository repository;

    EmailController(CustomerRepository repository, MessageChannel requests) {
        this.requests = requests;
        this.repository = repository;
    }

    @PostMapping("/email")
    Map<String, Object> email(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam Integer customerId) {
        var token = jwt.getTokenValue();
        var message = MessageBuilder
                .withPayload(repository.findCustomerById(customerId))
                .setHeader("jwt", token)
                .build();
        var sent = this.requests.send(message);
        return Map.of("sent", sent, "customerId", customerId);
    }
}
