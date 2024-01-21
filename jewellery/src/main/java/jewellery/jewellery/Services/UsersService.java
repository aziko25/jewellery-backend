package jewellery.jewellery.Services;

import jewellery.jewellery.DTO.UsersDTO;
import jewellery.jewellery.Models.Users;
import jewellery.jewellery.Repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    //------------------------------------CRUD-----------------------------------------//

    @Transactional
    public String createUser(UsersDTO userDTO) {

        try {

            Users user = new Users();

            user.setUsername(userDTO.getUsername());
            user.setPassword(userDTO.getPassword());

            if (!Objects.equals(userDTO.getRole(), "ADMIN") && !Objects.equals(userDTO.getRole(), "USER")) {

                throw new IllegalArgumentException("Role Should Be ADMIN Or USER!");
            }

            user.setRole(userDTO.getRole());

            if (userDTO.getTelegramId() != null) {

                user.setTelegramId(userDTO.getTelegramId());
            }

            usersRepository.save(user);

            return "You Successfully Created User!";
        }
        catch (DataIntegrityViolationException exception) {

            throw new IllegalArgumentException("This Username Already Exists!");
        }
    }

    @Transactional
    public String updateUser(Long id, UsersDTO userDTO) {

        try {

            Users user = usersRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User Not Found!"));

            if (userDTO.getUsername() != null) {
                user.setUsername(userDTO.getUsername());
            }

            if (userDTO.getPassword() != null) {
                user.setPassword(userDTO.getPassword());
            }

            if (userDTO.getRole() != null) {

                if (!Objects.equals(userDTO.getRole(), "ADMIN") && !Objects.equals(userDTO.getRole(), "USER")) {

                    throw new IllegalArgumentException("Role Should Be ADMIN Or USER!");
                }

                user.setRole(userDTO.getRole());
            }

            if (userDTO.getTelegramId() != null) {

                user.setTelegramId(userDTO.getTelegramId());
            }

            usersRepository.save(user);

            return "You Successfully Updated User!";
        }
        catch (DataIntegrityViolationException exception) {

            throw new IllegalArgumentException("This Username Or Telegram ID Already Exists!");
        }
    }

    public String deleteUser(Long id) {

        Users user = usersRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User Not Found!"));

        usersRepository.delete(user);

        return "You Successfully Deleted " + user.getUsername() + "!";
    }

    //-----------------------------------VIEWS-------------------------------------------//

    public List<UsersDTO> allUsers() {

        return usersRepository.findAll(Sort.by("username"))
                .stream().map(UsersDTO::new).collect(Collectors.toList());
    }

    public UsersDTO singleUser(Long id) {

        return usersRepository.findById(id)
                .stream().map(UsersDTO::new).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User Not Found!"));
    }
}