package com.example.learning_app_backend.config;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

// import static org.springframework.security.config.Customizer.withDefaults; // withDefaults は使わないので不要な場合も

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ↓↓↓ 詳細な CORS 設定 Bean を使うように指定 ↓↓↓
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // OPTIONS はすべて許可
                .requestMatchers("/api/test/hello").permitAll()
                // ↓↓↓ ユーザー登録 API (POST) を認証なしで許可 ↓↓↓
                .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                // ログイン API も認証なしで許可
                .requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()
                .requestMatchers("/api/learning-records/**").authenticated() // ← これはまだテスト用に残しておくか検討
                .anyRequest().authenticated()
            );
            

        return http.build();
    }

    // ↓↓↓ 詳細な CORS 設定を行う Bean を定義 ↓↓↓
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // ★★★ 許可するオリジンをリストで指定 ★★★
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://127.0.0.1:5173"));
        // 許可する HTTP メソッドを指定 (OPTIONS も含めるのが重要)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 許可するリクエストヘッダーを指定 ("*" はすべて許可)
        configuration.setAllowedHeaders(List.of("*"));
        // Cookie などの認証情報を含むリクエストを許可するか (今は false でも良いかも)
        configuration.setAllowCredentials(true); // 必要に応じて true に

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // すべてのパス ("/**") に対して上記設定を適用
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //BCrypt という協力なハッシュアルゴリズムを使用する Encoder を返す
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager を Bean として公開するメソッドを追加
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    
}