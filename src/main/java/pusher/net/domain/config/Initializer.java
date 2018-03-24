package pusher.net.domain.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pusher.net.domain.service.CacheService;

@Component
public class Initializer {
    @Autowired
    private CacheService cacheService;
}
