package id.ac.ub.filkom.perpustakaan.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  UserDetailsService users(PasswordEncoder p) {
    return new InMemoryUserDetailsManager(
        User.withUsername("A001").password(p.encode("anggota123")).roles("ANGGOTA").build(),
        User.withUsername("A002").password(p.encode("anggota123")).roles("ANGGOTA").build(),
        User.withUsername("pustakawan")
            .password(p.encode("pustaka123"))
            .roles("PUSTAKAWAN")
            .build());
  }

  @Bean
  SecurityFilterChain filter(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(
            a ->
                a.requestMatchers("/style.css", "/error")
                    .permitAll()
                    .requestMatchers("/stok", "/stok/**")
                    .hasRole("PUSTAKAWAN")
                    .requestMatchers("/pinjam", "/reservasi", "/reservasi/**")
                    .hasRole("ANGGOTA")
                    .anyRequest()
                    .authenticated())
        .formLogin(f -> f.defaultSuccessUrl("/", true))
        .logout(l -> l.logoutSuccessUrl("/login?logout"))
        .build();
  }
}
