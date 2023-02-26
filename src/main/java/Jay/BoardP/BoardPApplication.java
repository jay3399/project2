package Jay.BoardP;

import Jay.BoardP.controller.dto.User;
import java.util.Optional;
import org.hibernate.annotations.Filter;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableBatchProcessing
@EnableCaching
public class BoardPApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardPApplication.class, args);
	}


//	@Bean
//	public AuditorAware<String> auditorProvider() {
//		return () -> Optional.ofNullable(SecurityContextHolder.getContext())
//			.map(securityContext -> securityContext.getAuthentication())
//			.filter(authentication -> authentication.isAuthenticated())
//			.map(authentication1 -> authentication1.getPrincipal())
//			.map(obj -> User.class.cast(obj))
//			.map(user -> user.getUsername());
//	}
}
