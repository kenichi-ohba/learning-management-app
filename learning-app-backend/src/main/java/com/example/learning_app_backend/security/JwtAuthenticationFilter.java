package com.example.learning_app_backend.security;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

     try {
            // 1. リクエストヘッダーから JWT を取得
            String jwt = parseJwt(request);

            // 2. JWT が存在し、有効か検証
            if (jwt != null && jwtUtils.validateToken(jwt)) {
                // 3. トークンからユーザー名を取得
                String username = jwtUtils.getUsernameFromToken(jwt);

                // 4. ユーザー名から UserDetails を取得 (DB アクセス)
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 5. UserDetails から認証トークンを作成
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()); // パスワードは不要、権限をセット

                // 6. 認証トークンにリクエストの詳細情報を設定
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // ★★★ 7. SecurityContextHolder に認証情報を設定 ★★★
                // これにより、このリクエストの間、ユーザーは認証済みとして扱われる
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("JWT 認証成功: {}", username);
            }
        } catch (Exception e) {
            logger.error("JWT 認証フィルターでエラーが発生しました: {}", e.getMessage());
        }

        // 8. 次のフィルターへ処理を渡す
        filterChain.doFilter(request, response);
    }

    // リクエストヘッダーから "Bearer " トークンを抽出するヘルパーメソッド
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            // "Bearer " の後にあるトークン部分を返す (7文字目以降)
            return headerAuth.substring(7);
        }
        // Bearer トークンが見つからない場合は null を返す
        return null;
    }
  }
