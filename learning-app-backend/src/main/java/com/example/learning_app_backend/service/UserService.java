package com.example.learning_app_backend.service;

import java.util.Collections;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.learning_app_backend.dto.UserRegistrationDto;
import com.example.learning_app_backend.entity.User;
import com.example.learning_app_backend.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
  
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }
  
  
  /**
   * 新規ユーザー登録する
   * @param registrationDto 登録情報を含むDTO
   * @return 登録されたユーザー情報(パスワードは含まないDTO推奨)
   * @throws IllegalStateException ユーザー名またはEmailが既に存在する場合
   */
  @Transactional
  public User registerUser(UserRegistrationDto registrationDto) {
    // ユーザー名の重複チェック
    if (userRepository.existsByUsername(registrationDto.getUsername())) {
      throw new IllegalStateException("エラー: このユーザー名は既に使用されています。");
    }

    // Emailの重複チェック
    if (userRepository.existsByEmail(registrationDto.getEmail())) {
      throw new IllegalStateException("エラー: このメールアドレスは既に使用されています。");
    }

    // 新しい User Entity　を作成
    User newUser = new User();
    newUser.setUsername(registrationDto.getUsername());
    newUser.setEmail(registrationDto.getEmail());

     // ★★★ パスワードをハッシュ化して設定 ★★★
     newUser.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));

     return userRepository.save(newUser);
  }

  
  /**
   * ユーザー名に基づいてユーザー情報をロードする(Spring Security が利用)
   * @param username
   * @return UserDetails　オブジェクト (ユーザー名、ハッシュ化ますワード、権限含む)
   * @throws UsernameNotFoundException ユーザーが見つからない場合
  */

  @Override
  @Transactional(readOnly  = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // UserRepository を使ってユーザー名でユーザーを検索
    Optional<User> UserOptional = userRepository.findByUsername(username);

    // ユーザーが見つからなけらば例外をスロー
    User user = UserOptional.orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません" + username));

    // Spring Security が使える UserDetails オブジェクトを作成して返す
    // 第1引数: ユーザー名
    // 第2引数: データベースから取得したハッシュ化済みパスワード
    // 第3引数: 権限(Role) のリスト(今回はからリスト)
    return new org.springframework.security.core.userdetails.User(
      user.getUsername(),
      user.getPasswordHash(),
      Collections.emptyList());

}
}
