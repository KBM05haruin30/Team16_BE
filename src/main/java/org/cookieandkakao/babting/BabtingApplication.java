package org.cookieandkakao.babting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@EnableConfigurationProperties
@ConfigurationPropertiesScan
@PropertySource("classpath:application-kakao-key.properties")
@SpringBootApplication
public class BabtingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BabtingApplication.class, args);
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

}
