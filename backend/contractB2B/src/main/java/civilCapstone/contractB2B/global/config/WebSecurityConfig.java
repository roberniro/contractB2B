package civilCapstone.contractB2B.global.config;

import civilCapstone.contractB2B.global.service.JwtAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Slf4j
// Spring Security 설정, JWT 인증 필터 추가, CORS 설정, CSRF 비활성화, 세션 비활성화, 인증 불필요한 요청 허용
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/user").permitAll()
                .antMatchers(HttpMethod.POST, "/user/auth").permitAll()
                .antMatchers("/chatting").permitAll()
                .antMatchers("/chatting/**").permitAll()
                .anyRequest().authenticated();
        http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);
    }
}