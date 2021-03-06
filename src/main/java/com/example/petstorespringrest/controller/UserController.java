package com.example.petstorespringrest.controller;

import com.example.petstorespringrest.entity.User;
import com.example.petstorespringrest.error.UserIsExitsException;
import com.example.petstorespringrest.error.ValidationException;
import com.example.petstorespringrest.repository.UserRepository;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Api(tags = "user", description = "Operations about user")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ApiOperation(value = "Create user", notes = "This can only be done by the logged in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PostMapping(produces = "application/json")
    public void save(@Valid @RequestBody @ApiParam(value = "Created user object") User user, BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasErrors()) {
                throw new ValidationException("parameters not valid");
            }
        }

        if (userRepository.getUserByUsername(user.getUsername()).isPresent()) {
            throw new UserIsExitsException("user is exits");
        } else {
            userRepository.save(user);
        }
    }

    @ApiOperation(value = "Get user by user name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid username supplied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping(value = "/{username}", produces = "application/json")
    public ResponseEntity<User> getUserByUsername(@PathVariable("username")
                                                  @ApiParam(value = "The name that needs to be fetched. Use user1 for testing.", example = "username") String username) throws ValidationException {
        if (userRepository.getUserByUsername(username).isEmpty()) {
            throw new ValidationException("parameters not valid");
        }

        Optional<User> userByUsername = userRepository.getUserByUsername(username);
        return ResponseEntity.ok(userByUsername.get());
    }

    @ApiOperation(value = "Update user", notes = "This can only be done by the logged in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid user supplied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping(value = "/{username}", produces = "application/json")
    public void updateUserByUsername(@PathVariable("username")
                                     @ApiParam(value = "name that need to be updated", example = "username") String username,
                                     @ApiParam(value = "Updated user object") @RequestBody User user) throws ValidationException {
        if (username == null | userRepository.getUserByUsername(username).isEmpty()) {
            throw new ValidationException("parameters not valid");
        }
        Optional<User> userByUsername = userRepository.getUserByUsername(username);
        User updateUser = new User();
        if (userByUsername.isPresent()) {
            updateUser = userRepository.save(user);
        }
    }

    @ApiOperation(value = "Delete user", notes = "This can only be done by the logged in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid user supplied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping(value = "/{username}", produces = "application/json")
    public void deleteUserByUsername(@PathVariable("username")
                                     @ApiParam(value = "The name that needs to be deleted", example = "username") String username) throws ValidationException {
        if (username == null | userRepository.getUserByUsername(username).isEmpty()) {
            throw new ValidationException("parameters not valid");
        }

        Optional<User> userByUsername = userRepository.getUserByUsername(username);
        userByUsername.ifPresent(userRepository::delete);
    }

    @ApiOperation(value = "Creates list of users with given input array")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PostMapping(value = "/createWithArray", produces = "application/json")
    public void createWithArray(@RequestBody @ApiParam(value = "List of user object") User[] users) {
        List<User> userList = new ArrayList<>();

        for (int i = 0; i < users.length; i++) {
            userList.add(userRepository.save(users[i]));
        }
    }

    @ApiOperation(value = "Creates list of users with given input array")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PostMapping(value = "/createWithList", produces = "application/json")
    public void createWithList(@RequestBody @ApiParam(value = "List of user object") List<User> userList) {
        userRepository.saveAll(userList);
    }
}
