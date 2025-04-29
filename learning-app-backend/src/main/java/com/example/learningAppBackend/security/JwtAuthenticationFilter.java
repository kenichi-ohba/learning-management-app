package com.example.learningAppBackend.security;

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
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        logger.info(">>> JwtAuthenticationFilter: URI = {}", request.getRequestURI()); // ★実行確認用

        try {
            // 1. リクエストヘッダーから JWT を取得
            String jwt = parseJwt(request);
            // ★★★ parseJwt の結果をログに出力 ★★★
            logger.debug("Parsed JWT from header: {}", jwt);
            // 2. JWT が存在し、有効か検証
            if (jwt != null && jwtUtils.validateToken(jwt)) {
                logger.debug("JWT is valid"); // ★検証成功したか
                // 3. トークンからユーザー名を取得
                String username = jwtUtils.getUsernameFromToken(jwt);
                logger.debug("Username from token: {}", username); // ★ユーザー名が取れたか

                // 4. ユーザー名から UserDetails を取得 (DB アクセス)
                //    SecurityContextHolder に認証情報がまだない場合のみ DB アクセスを行うのが効率的
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    // ★★★ UserDetails 取得結果をログに出力 ★★★
                    logger.debug("UserDetails loaded: {}", userDetails != null ? userDetails.getUsername() : "null");

                    // 5. UserDetails から認証トークンを作成
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()); // パスワードは不要、権限をセット

                    // 6. 認証トークンにリクエストの詳細情報を設定
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // ★★★ 7. SecurityContextHolder に認証情報を設定 ★★★
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    // ★★★ 認証情報セット成功ログ ★★★
                    logger.info("<<< JwtAuthenticationFilter: Authentication set for user: {}", username);
                } else {
                    logger.debug("Username from token is null or SecurityContext already contains Authentication.");
                }
            } else {
                // ★★★ トークンがないか無効だった場合のログ ★★★
                if (jwt == null) {
                    logger.debug("JWT is null (No Bearer token found in Authorization header or invalid format)");
                } else {
                    // validateToken が false を返した場合 (validateToken 内のエラーログも確認)
                    logger.warn("JWT validation failed for token (check previous logs for specific reason like expiration or signature)");
                }
                 // 認証情報がセットされないので、匿名扱いになる
                 // SecurityContextHolder.clearContext(); // 念のためクリアしても良い
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
        logger.trace("Could not find Bearer string in Authorization header, will ignore the header."); // TRACEレベルログ
        return null;
    }
}
