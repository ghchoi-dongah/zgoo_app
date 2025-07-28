package zgoo.app.controller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.http.HttpServletResponse;
import zgoo.app.util.CustomLoginSuccessHandler;
import zgoo.app.util.SHA256Encoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/**").permitAll())
                .formLogin(form -> form
                    .loginPage("/member/login")
                    .defaultSuccessUrl("/main", true)
                    // .successHandler(new CustomLoginSuccessHandler())
                    .successHandler((request, response, authentication) -> {
                        response.setStatus(HttpServletResponse.SC_OK);
                    })
                    .failureHandler((request, response, exception) -> {
                        String memLoginId = request.getParameter("memLoginId");
                        String password = request.getParameter("password");
                        request.getSession().setAttribute("memLoginId", memLoginId);
                        request.getSession().setAttribute("password", password);
                        request.getSession().setAttribute("loginError", true);
                        // response.sendRedirect("/member/login");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    })
                    .usernameParameter("memLoginId")
                    .passwordParameter("password")
                    .permitAll())
                .logout(logout -> logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/full-menu")
                    .invalidateHttpSession(true))
                .csrf(AbstractHttpConfigurer::disable)
        ;
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new SHA256Encoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
