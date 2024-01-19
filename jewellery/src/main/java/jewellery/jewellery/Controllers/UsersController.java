package jewellery.jewellery.Controllers;

import jewellery.jewellery.Configurations.JWTAuthorization.Authorization;
import jewellery.jewellery.DTO.UsersDTO;
import jewellery.jewellery.Services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(maxAge = 3600)
@RequiredArgsConstructor
public class UsersController {

    private final UsersService service;

    //------------------------------------CRUD-----------------------------------------//

    @Authorization(requiredRoles = {"ADMIN"})
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UsersDTO userDTO) {

        return ResponseEntity.ok(service.createUser(userDTO));
    }

    @Authorization(requiredRoles = {"ADMIN"})
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UsersDTO userDTO) {

        return ResponseEntity.ok(service.updateUser(id, userDTO));
    }

    @Authorization(requiredRoles = {"ADMIN"})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        return ResponseEntity.ok(service.deleteUser(id));
    }

    //-----------------------------------VIEWS-------------------------------------------//

    @Authorization(requiredRoles = {"ADMIN"})
    @GetMapping("/all")
    public ResponseEntity<?> allUsers() {

        return ResponseEntity.ok(service.allUsers());
    }

    @Authorization(requiredRoles = {"ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<?> singleUser(@PathVariable Long id) {

        return ResponseEntity.ok(service.singleUser(id));
    }
}