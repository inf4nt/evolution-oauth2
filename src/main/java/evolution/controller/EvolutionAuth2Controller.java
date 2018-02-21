package evolution.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

@RestController
@RequestMapping(value = "/")
public class EvolutionAuth2Controller {

    @GetMapping("/principal")
    public ResponseEntity<Principal> currentPrincipal(@AuthenticationPrincipal Principal principal) {
        return ResponseEntity.ok(principal);
    }
}
