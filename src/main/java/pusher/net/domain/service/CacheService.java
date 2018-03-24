package pusher.net.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pusher.net.Pusher;
import pusher.net.domain.dto.UsersLoginEmailsDto;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CacheService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Pusher.class);
    private static Map<String, HttpHeaders> cacheHeaders = new ConcurrentHashMap<>();
    private static Map<String, String> cacheLoginEmail = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.set("Authorization", "Basic YXdkYXdkOjEyMzMyMQ==");
        HttpEntity<?> requestHttp = new HttpEntity<>(authHeaders);
        ResponseEntity<UsersLoginEmailsDto> responseEntity = restTemplate.exchange("http://localhost:8080/getAllRegistrationData", HttpMethod.GET, requestHttp, UsersLoginEmailsDto.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            UsersLoginEmailsDto body = responseEntity.getBody();
            if (body != null) {
                body.getPool().forEach(loginEmailWrapper -> cacheLoginEmail.put(loginEmailWrapper.getLogin(), loginEmailWrapper.getEmail()));
            }
        } else {
            LOGGER.error("Cannot load registrate data status code is:  {}", responseEntity.getStatusCodeValue());
        }
        System.out.println(cacheHeaders);
        System.out.println(cacheLoginEmail);
    }

    public List<String> getAllLogins() {
        List<String> allLogins = cacheLoginEmail.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
        return allLogins;
    }

    public List<String> getAllEmail() {
        List<String> allEmails = cacheLoginEmail.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
        return allEmails;
    }

    void addToCache(String sessionId, HttpHeaders httpHeaders) {
        cacheHeaders.put(sessionId, httpHeaders);
    }

    public HttpHeaders getFromCache(String sessionId) {
        return cacheHeaders.get(sessionId);
    }
}
