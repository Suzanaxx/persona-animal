package com.animal.persona;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Dovoli vse poti
                .allowedOrigins("http://localhost:5173") // Frontend URL
                .allowedMethods("*") // Dovoli GET, POST, itd.
                .allowedHeaders("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**") // URL poti za slike
                .addResourceLocations("classpath:/static/images/"); // Mapa znotraj projekta
    }
}
