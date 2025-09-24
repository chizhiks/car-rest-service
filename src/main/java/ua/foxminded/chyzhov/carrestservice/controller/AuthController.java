package ua.foxminded.chyzhov.carrestservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ua.foxminded.chyzhov.carrestservice.dto.AuthDto;

@Setter
@RestController
@Tag(name = "authentication", description = "API for user authentication. Provides JWT token generation.")
public class AuthController {

    @Value("${client-id}")
    private String clientId;

    @Value("${resource-url}")
    private String resourceUrl;

    @Value("${grant-type}")
    private String grantType;

    @Operation(
            summary = "Authentication user",
            description = "Authentication user credentials and return JWT access token"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                               "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJfTVF...",
                                               "expires_in": 300,
                                               "refresh_expires_in": 1800,
                                               "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI...",
                                               "token_type": "Bearer",
                                               "not-before-policy": 0,
                                               "session_state": "f8d7e892-7c8b-4a5d-9c3f-2e4b6a8f1d3c",
                                               "scope": "openid"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid credentials or request format",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication failed - invalid username or password",
                    content = @Content()
            )
    })
    @PostMapping("/auth")
    public String auth(
            @Parameter(description = "User credentials", required = true)
            @RequestBody AuthDto authDto) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var body = "client_id=" + clientId +
                "&username=" + authDto.login() +
                "&password=" + authDto.password() +
                "&grant_type=" + grantType;

        var requestEntity = new HttpEntity<>(body, headers);
        var restTemplate = new RestTemplate();

        var response = restTemplate.exchange(resourceUrl, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }

        return null;
    }
}
