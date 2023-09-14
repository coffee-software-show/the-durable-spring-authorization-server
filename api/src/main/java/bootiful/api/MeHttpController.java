package bootiful.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Map;

@Controller
@ResponseBody
class MeHttpController {

    @GetMapping("/me")
    Map<String, String> principal(Principal principal) {
        return Map.of("name", principal.getName());
    }
}
