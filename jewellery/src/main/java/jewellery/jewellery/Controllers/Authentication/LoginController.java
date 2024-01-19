package jewellery.jewellery.Controllers.Authentication;

import jewellery.jewellery.DTO.UsersDTO;
import jewellery.jewellery.Services.Authentication.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(maxAge = 3600)
@RequiredArgsConstructor
public class LoginController {

    private final LoginService service;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody UsersDTO userDTO) {

        return ResponseEntity.ok(service.login(userDTO.getUsername(), userDTO.getPassword()));
    }
}