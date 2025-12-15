package project_java_ajun.platform_rental_barang.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //Spring Security tidak akan membuat dan menyimpan session alias stateless
                .authorizeHttpRequests(auth -> auth
                   //Public Endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/admins/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/pembayaran/webhook").permitAll()

                   //Admin Endpoints
                        .requestMatchers("/api/admins/*").hasRole("ADMIN")

                   //User EndPoints
                        .requestMatchers(HttpMethod.GET,"/api/users/*").hasAnyRole("RENTER","OWNER","ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/users/**").hasAnyRole("RENTER","OWNER")
                        .requestMatchers(HttpMethod.DELETE,"/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/users").hasRole("ADMIN")

                  //Barang Endpoints
                        .requestMatchers(HttpMethod.POST,"/api/barangs/create").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PUT,"/api/barangs/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.GET,"/api/barangs").hasAnyRole("ADMIN","RENTER")
                        .requestMatchers(HttpMethod.GET,"/api/barangs/*").hasRole("OWNER")
                        .requestMatchers(HttpMethod.GET,"/api/barangs/filter").hasAnyRole("ADMIN","RENTER")
                        .requestMatchers(HttpMethod.DELETE,"/api/barangs/**").hasAnyRole("OWNER","ADMIN")

                  //Pemesanan Endpoints
                        .requestMatchers(HttpMethod.POST, "/api/pemesanan").hasRole("RENTER")
                        .requestMatchers(HttpMethod.PUT, "/api/pemesanan/*/cancel").hasRole("RENTER")
                        .requestMatchers(HttpMethod.PUT, "/api/pemesanan/*/approved").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/pemesanan/*/returned").hasRole("OWNER")
                        .requestMatchers(HttpMethod.GET, "/api/pemesanan").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/pemesanan/*").hasRole("RENTER")

                  //Pembayaran Endpoints
                        .requestMatchers(HttpMethod.POST, "/api/pembayaran").hasRole("RENTER")
                        .requestMatchers(HttpMethod.GET, "/api/pembayaran/*").hasAnyRole("RENTER","OWNER")
                        .requestMatchers(HttpMethod.GET, "/api/pembayaran").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                //register jwt filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            CustomUserDetailsService customUserDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
