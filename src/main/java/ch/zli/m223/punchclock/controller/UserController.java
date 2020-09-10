package ch.zli.m223.punchclock.controller;

import ch.zli.m223.punchclock.domain.ApplicationUser;
import ch.zli.m223.punchclock.service.UserService;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserService userService,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping
    public ApplicationUser signUp(@RequestBody ApplicationUser user) {

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userService.save(user);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ApplicationUser> getAll() {
        return this.userService.findAll();
    }

    @GetMapping("/me")
    public ApplicationUser getMyself() {
        return this.userService.getMyself();
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        try {
            this.userService.delete(id);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getLocalizedMessage());
        }
    }
}
