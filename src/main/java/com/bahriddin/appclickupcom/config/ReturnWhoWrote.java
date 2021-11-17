package com.bahriddin.appclickupcom.config;

import com.bahriddin.appclickupcom.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class ReturnWhoWrote {
    @Bean
    AuditorAware<User> auditorAware() {
        return new WhoWrote();
    }
}
