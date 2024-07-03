package org.omar.recipes.users.boundary;

import jakarta.validation.Valid;
import org.omar.recipes.users.controller.UserAccountService;
import org.omar.recipes.users.entity.RegistrationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("api/")
public class UserController {

    private final UserAccountService userService;
    public UserController(UserAccountService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "register")
    public ResponseEntity register(@RequestBody @Valid RegistrationRequest request) {
        return userService.saveNewUser(request);
    }

    @PutMapping(path = "register")
    public ResponseEntity editUser(@RequestBody @Valid RegistrationRequest request) {
        return userService.saveUser(request);
    }
}
