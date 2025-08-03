package city.better.authservice.controllers;

import city.better.authservice.dtos.JwtValueDto;
import city.better.authservice.dtos.LoginRequestDto;
import city.better.authservice.dtos.RegisterRequestDto;
import city.better.authservice.dtos.UserInfoDto;
import city.better.authservice.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("register")
    public ResponseEntity<JwtValueDto> register(@Valid
                                                @RequestBody
                                                RegisterRequestDto request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("login")
    public ResponseEntity<JwtValueDto> login(@Valid
                                             @RequestBody
                                             LoginRequestDto request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @GetMapping("validate")
    public ResponseEntity<UserInfoDto> validate(@RequestHeader("Authorization") String authorizationHeader) {
        var token = authorizationHeader
                .replace("Bearer ", "")
                .trim();
        var userInfo = authService.validateAndExtract(token);

        return ResponseEntity.ok(userInfo);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PatchMapping("verify-employee/{id}")
    public ResponseEntity<Void> verifyEmployee(@PathVariable Long id) {
        authService.verifyEmployee(id);

        return ResponseEntity.ok().build();
    }
}
