package com.example.learning_app_backend.dto;

// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserRegistrationDto {

  // @NotBlank
  // @Size(min = 3, max = 50)// 文字数制限
  private String username;
  
  // @NotBlank
  // @Size(min = 8)
  private String password;// ★注意: ここは生のパスワードを受け取る
  
  // @NotBlank
  // @Email //Email形式チェック
  private String email;// ★注意: ここは生のパスワードを受け取る
  
  
}
