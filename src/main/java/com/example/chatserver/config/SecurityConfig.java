package com.example.chatserver.config;

import com.example.chatserver.config.jwt.TokenProvider;
import com.example.chatserver.config.TokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity  // URL 요청에 대한 Spring Security 동작 활성화 (URL 요청에 대한 인가, 인증 처리)
@RequiredArgsConstructor
public class SecurityConfig {
    // JWT 토큰 생성 및 검증에 사용할 컴포넌트를 주입받음
    private final TokenProvider tokenProvider;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. 요청 인가 설정
                // 현재는 모든 URL("/**") 에 대해 접근을 허용.
                // 만약 API 테스트 시 인증( 폼 로그인 )을 반드시 사용하려면, 아래 permitALL() 을 수정해야 함.
            .authorizeHttpRequests(  // 요청 인가 여부 결정을 위한 조건 판단
                (authorizeHttpRequests) ->
                    authorizeHttpRequests.requestMatchers(
                            // TODO : 모든 요청을 허용 하는 대신, 로그인 하지 않으면 특정 페이지에 접근 할 수 없도록 수정 할 필요가 있음.
                        new AntPathRequestMatcher("/**")
//                        , new AntPathRequestMatcher("/**", HttpMethod.POST.name())
//                        , new AntPathRequestMatcher("/**", HttpMethod.PUT.name())
//                        , new AntPathRequestMatcher("/**", HttpMethod.PATCH.name())
//                        , new AntPathRequestMatcher("/**", HttpMethod.DELETE.name())
                    ).permitAll()
//                    authorizeHttpRequests
//                    .requestMatchers(
//                        // Apache Ant 스타일 패턴을 사용해 URL 매칭 정의
//                        new AntPathRequestMatcher(
//                            "/"              // 메인 페이지 비회원 접속 허용
//                        ),
//                        new AntPathRequestMatcher(
//                            "/users/login"   // 로그인 URL 비회원 접속 허용
//                        ),
//                        new AntPathRequestMatcher(
//                            "/csrf-token"   // CSRF 토큰 발급 URL 비회원 접속 허용
//                        ),
//                        new AntPathRequestMatcher(
//                            "/products-temp/**", HttpMethod.GET.name()
//                        )
//                    ).permitAll()
//                    .requestMatchers(
//                        new AntPathRequestMatcher(
//                            "/products-temp/**"
//                        )
//                    ).hasAnyRole("SUPER_ADMIN", "ADMIN")
//                    .anyRequest().authenticated()  // 나머지 모든 URL 에 회원 로그인 요구
            )
//            .csrf(csrf -> csrf
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//            )
            // 2. CSRF 설정
                // /api/** 및 /users/login 경로는 CSRF 보호를 무시함.
                // (폼 기반 로그인이나 API 테스트 시 CSRF 토큰 문제를 피하기 위함)
            .csrf(
                (csrf) -> csrf.ignoringRequestMatchers(
                    new AntPathRequestMatcher("/api/**")
                    , new AntPathRequestMatcher("/api/auth/login")
                )
            )
//                (csrf) ->
//                    csrf.ignoringRequestMatchers(
//                        // 필요 시 특정 페이지 CSRF 토큰 무시 설정
//                        new AntPathRequestMatcher("/h2-console/**")
//                        // , new AntPathRequestMatcher("/login")
//                        // , new AntPathRequestMatcher("/logout")
//                        // , new AntPathRequestMatcher("/signup")
//                    )
//            )
            // 3. 헤더 설정
            // X-Frame-Options 헤더를 SAMEORIGIN 으로 설정하여, 동일 도메인 내에서 iframe 사용을 허용함.
            .headers(
                (headers) ->
                    headers.addHeaderWriter(
                        new XFrameOptionsHeaderWriter(
                            // X-Frame-Options 는 웹 페이지 내에서 다른 웹 페이지 표시 허용 여부 제어
                            XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN  // 동일 도메인 내에서 표시 허용
                        )
                    )
            )
            .formLogin(
                (formLogin) ->
                    formLogin  // Controller 에 PostMapping URL 바인딩이 없어도
                               // POST 요청을 아래 라인에서 수신하고 인증 처리
                            .loginPage("/api/auth/login")  // 로그인 페이지 URL 지정
                            .defaultSuccessUrl("/", true)  // 로그인 성공 후 이동할 페이지 (강제 이동)
                            .failureUrl("/api/auth/login?error=true")  // 로그인 실패 시 이동할 페이지
//                AbstractHttpConfigurer::disable
            )
            .logout(
                (logout) ->
                    logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/api/auth/logout"))
                        .logoutSuccessUrl("/")       // 로그아웃 성공 후 메인 페이지로 이동
                        .invalidateHttpSession(true) // 세션 무효화
            )
//            .sessionManagement(
//                (sessionConfig) -> {
//                    sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                }
//            )
//            .addFilterBefore(
//                tokenAuthenticationFilter(),  // 토큰을 username, password 검사보다 먼저 검사한다.
//                UsernamePasswordAuthenticationFilter.class
//            )
        ;
        return http.build();
    }

    // passwordEncoder 빈 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }
}

//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private JWTAuthenticationFilter jwtAuthenticationFilter;
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeRequests()
//                .dispatcherTypeMatchers("/api/auth/**").permitAll() // 인증 관련 엔드포인트는 모두 열어둠
//                .anyRequest().authenticated()
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//    }
//}
