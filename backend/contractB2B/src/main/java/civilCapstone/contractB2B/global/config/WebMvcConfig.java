package civilCapstone.contractB2B.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
// CORS 설정
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "ws://localhost:3000", "http://3.34.76.221:80", "ws://3.34.76.221:80", "http://3.34.76.221", "ws://http://3.34.76.221")
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}
