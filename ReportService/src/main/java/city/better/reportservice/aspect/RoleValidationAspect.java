package city.better.reportservice.aspect;

import city.better.reportservice.annotation.RoleRequired;
import city.better.reportservice.dto.AuthResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import city.better.reportservice.enums.Role;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleValidationAspect {

    private final RestTemplate restTemplate;

    @Around("@annotation(roleRequired)")
    public Object validateUserRole(ProceedingJoinPoint joinPoint, RoleRequired roleRequired) throws Throwable {
        HttpEntity<Void> entity = getVoidHttpEntity();

        try {
            ResponseEntity<AuthResponseDto> response = restTemplate.exchange(
                    "http://auth-service:8080/auth/validate",
                    HttpMethod.GET,
                    entity,
                    AuthResponseDto.class
            );

            AuthResponseDto userInfo = response.getBody();
            if (userInfo == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Failed to retrieve user data");
            }

            Role actualRole = userInfo.role();
            Role expectedRole = roleRequired.value();

            if (!expectedRole.equals(actualRole)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized role");
            }

            return joinPoint.proceed();

        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Authorization error");
        }
    }

    private static HttpEntity<Void> getVoidHttpEntity() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No request context found");
        }

        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "JWT token missing");
        }

        token = token.substring(7);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return new HttpEntity<>(headers);
    }
}
