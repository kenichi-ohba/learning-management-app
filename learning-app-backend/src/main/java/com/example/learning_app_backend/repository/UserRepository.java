package com.example.learning_app_backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.learning_app_backend.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  
  // ユーザー名でユーザーを検索するメソッド(Spring Data JPA が自動で実装を生成)
  //Optional<User>は、ユーザーが見つかる場合ト見つからない場合があることを示す  
  Optional<User> findByUsername(String Username);

  // メールアドレスでユーザーを検索するメソッド
  Optional<User> findByEmail(String email);
  
  // ユーザー名が存在するかどうかチェックするメソッド（重複チェック用）
  boolean existsByUsername(String username);

  // メールアドレスがそんざいするかどうかをチェックするメソッド(重複チェック用)
  boolean existsByEmail(String email);

}

