package pusher.net.domain.service;

import io.vavr.Tuple2;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pusher.net.domain.dto.AuthDto;
import pusher.net.domain.utils.SessionUtils;

import java.util.Collections;

@Service
public class AuthService {
    private final SessionUtils sessionUtils;

    @Autowired
    public AuthService(SessionUtils sessionUtils) {
        this.sessionUtils = sessionUtils;
    }

    public AuthDto authenticate(String login, String password) {
        RestTemplate restTemplate = new RestTemplate();
        String plainCredentials = login + ":" + password;
        String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> httpRequest = new HttpEntity<>(headers);
        ResponseEntity<AuthDto> responseEntity = restTemplate.exchange("http://localhost:8080/person/login", HttpMethod.POST, httpRequest, AuthDto.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            sessionUtils.getSession().setAttribute(sessionUtils.getSessionId(), headers);
            AuthDto body = responseEntity.getBody();
            System.out.println(body);
            if (body != null) {
                return body;
            }
        }
        return null;
    }

    public HttpHeaders getAuthHeaders() {
        HttpHeaders headers = (HttpHeaders) sessionUtils.getSession().getAttribute(sessionUtils.getSessionId());
        return headers;
    }

    public HttpHeaders getJsonSupportHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public enum AuthStatus{
        AUTHENTIFICATED,
        NOT_AUTHENTIFICATED,
    }
}
