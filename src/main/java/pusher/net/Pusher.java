package pusher.net;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Pusher {
	public static void main(String[] args) {
		SpringApplication.run(Pusher.class, args);
	}
}
