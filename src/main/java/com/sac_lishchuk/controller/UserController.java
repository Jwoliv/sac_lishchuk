package com.sac_lishchuk.controller;

import com.sac_lishchuk.service.UserService;
import com.sac_lishchuk.shared.dto.UserDto;
import com.sac_lishchuk.shared.request.ChangePasswordRequest;
import com.sac_lishchuk.shared.request.CreateUserRequest;
import com.sac_lishchuk.shared.response.SuccessChangedPasswordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/by-id/{id}")
    public UserDto getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/by-email/{email}")
    public UserDto getUserByEmail(@PathVariable("email") String email) {
        return userService.getUserByEmail(email);
    }

    @PostMapping
    public UserDto saveUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @PatchMapping("/change-password")
    public SuccessChangedPasswordResponse changePassword(@RequestBody ChangePasswordRequest request) {
        return userService.changePassword(request);
    }

}
