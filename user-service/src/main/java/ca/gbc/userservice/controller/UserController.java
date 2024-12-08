package ca.gbc.userservice.controller;

import ca.gbc.userservice.model.User;
import ca.gbc.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Endpoint to create an user",
            description = "Endpoint to create an user"
    )
    // Create a new user
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.saveUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @Operation(
            summary = "Endpoint to get an user by ID",
            description = "Endpoint to get an user by ID"
    )

    // Get a user by ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Endpoint to get an user by username",
            description = "Endpoint to get an user by username"
    )
    @GetMapping("/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.findUserByUsername(username);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Endpoint to get an user by email",
            description = "Endpoint to get an user by email"
    )
    @GetMapping("/email/{userEmail}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String userEmail) {
        User user = userService.findUserByUserEmail(userEmail);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Endpoint to get all users",
            description = "Endpoint to get all users"
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(
            summary = "Endpoint to delete user by ID",
            description = "Endpoint to delete user by ID"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Endpoint to check user validity ",
            description = "Endpoint to check user validity"
    )

    @GetMapping("/isValid")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> userIsValid(@RequestParam String userId) {
        Optional<User>  userOp= userService.findUserById(Long.valueOf(userId));
        return new ResponseEntity<>(userOp.isPresent(), HttpStatus.OK);
    }

    @Operation(
            summary = "Endpoint to check if user is staff",
            description = "Endpoint to check  if user is staff"
    )
    @GetMapping("/isUserStaff")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> isUserStaff(@RequestParam String userId) {
        Optional<User>  userOp= userService.findUserById(Long.valueOf(userId));
        boolean isStaff = false;
        if(userOp.isPresent()) {
            User user = userOp.get();
            if(user.getUserRole().equals("staff")) {
                isStaff = true;
            }
        }
        return new ResponseEntity<>(isStaff, HttpStatus.OK);
    }
}