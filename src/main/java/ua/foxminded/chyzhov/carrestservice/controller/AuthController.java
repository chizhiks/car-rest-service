package ua.foxminded.chyzhov.carrestservice.controller;

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
public class AuthController {

    @Value("${client-id}")
    private String clientId;

    @Value("${resource-url}")
    private String resourceUrl;

    @Value("${grant-type}")
    private String grantType;

    @PostMapping("/auth")
    public String auth(@RequestBody AuthDto authDto) {
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
