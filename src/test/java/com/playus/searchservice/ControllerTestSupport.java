package com.playus.searchservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playus.searchservice.domain.common.feign.client.UserFeignClient;
import com.playus.searchservice.domain.post.controller.PostSearchController;
import com.playus.searchservice.domain.post.service.PostSearchService;
import com.playus.searchservice.global.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(controllers = {
        PostSearchController.class
})
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected PostSearchService postSearchService;

    @MockitoBean
    protected JwtUtil jwtUtil;

    @MockitoBean
    protected UserFeignClient userFeignClient;

    @TestConfiguration
    static class TestSecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable);
            http.formLogin(AbstractHttpConfigurer::disable);
            http.httpBasic(AbstractHttpConfigurer::disable);
            http.authorizeHttpRequests(auth -> auth
                    .anyRequest().hasAuthority("USER")
            );

            // 세션 관리: Stateless
            http.sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

            return http.build();
        }
    }
}
