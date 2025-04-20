package com.example.learning_app_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.learning_app_backend.dto.LoginRequestDto;
import com.example.learning_app_backend.dto.UserRegistrationDto;
import com.example.learning_app_backend.entity.User;
import com.example.learning_app_backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController // REST API の Controller
@RequestMapping("/api/users") // 基本バス
public class UserController {

  private final UserService userService;
  private final AuthenticationManager authenticationManager;


  public UserController(UserService userService, AuthenticationManager authenticationManager) {
    this.userService = userService;
    this.authenticationManager = authenticationManager;
  }

  // ↓↓↓ ユーザー登録 API エンドポイントを実装
  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
    // `@Valid`: DTO のバリデーションを有効化(要 spring-boot-starter-validation)
    // `@RequestBody`: リクエストボディの JSON を DTO にマッピング

    try {
      // UserService の登録メソッドを呼び出す
      User registeredUser = userService.registerUser(registrationDto);

      // 成功レスポンス: パスワードハッシュを含まない DTO を返すのが望ましい
      // UserResponseDto responseDto = convertToUserResponseDto(registeredUser); // 変換メソッドを用意
      // return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

      // (シンプルな例: とりあえず登録された Entity を返す場合 - 非推奨)
      // 注意: このままだとパスワードハッシュがレスポンスに含まれる可能性がある
      return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);

    } catch (IllegalStateException e) {
      // ユーザー名や Email の重複エラーなど
      // エラーメッセージをレスポンスボディとして返す
      return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // 409 Conflict
    } catch (Exception e) {
      // その他の予期せぬエラー
      // 本番では詳細なエラー内容はログに出力し、ユーザーには汎用的なメッセージを返す
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ユーザー登録中にエラーが発生しました。"); // 500
                                                                                                  // Internal
                                                                                                  // Server
                                                                                                  // Error
    }
    // TODO: バリデーションエラー発生時のハンドリング (@Valid が機能した場合)
    // これは @RestControllerAdvice を使ったグローバル例外ハンドリングで対応するのが一般的
  }


  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto loginRequestDto,
      HttpServletRequest request) {
    try {
      // AuthenticationManager で認証を実行
      Authentication authentication = authenticationManager.authenticate(
          // UsernamePasswordAuthenticationToken は認証リクエストの標準的な入れ物
          new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(),
              loginRequestDto.getPassword()));

      // 認証に成功した場合 SecurityContext に認証情報を設定
      SecurityContextHolder.getContext().setAuthentication(authentication);

      HttpSession session = request.getSession(true); // true でセッションがなければ新規作成
      session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
          SecurityContextHolder.getContext());
      System.out.println("ログイン成功、セッションを作成/更新しました. Session ID: " + session.getId()); // 確認用ログ

      // 認証成功のレスポンスを返す
      // 本来はこのタイミングで JWT トークンなどを生成して返すことが多い
      // 今回はまず成功下ことと認証情報を返すシンプルな例
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      return ResponseEntity.ok("ログイン成功！ User:" + userDetails.getUsername());
    } catch (AuthenticationException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("ログインに失敗しました: ユーザー名またはパスワードが正しくありません。");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ログイン処理週にエラーが発生しました");
    }
  }

}
