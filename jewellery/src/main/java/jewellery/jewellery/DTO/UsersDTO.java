package jewellery.jewellery.DTO;

import jewellery.jewellery.Models.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO {

    private Long id;
    private String username;
    private String password;
    private String role;

    public UsersDTO(Users user) {

        setId(user.getId());
        setUsername(user.getUsername());
        setPassword(user.getPassword());
        setRole(user.getRole());
    }
}