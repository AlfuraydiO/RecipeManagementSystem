package org.omar.recipemanagementsystem.users.boundary;

import jakarta.validation.Valid;
import org.omar.recipemanagementsystem.users.controller.UserAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/")
public class UserController {

  private final UserAccountService userService;

  public UserController(UserAccountService userService) {
    this.userService = userService;
  }

  @PostMapping(path = "user")
  public ResponseEntity register(@RequestBody @Valid RegistrationRequest request) {
    return userService.saveNewUser(request);
  }

  @PutMapping(path = "user")
  public ResponseEntity editUser(@RequestBody @Valid RegistrationRequest request) {
    return userService.saveUser(request);
  }
}
