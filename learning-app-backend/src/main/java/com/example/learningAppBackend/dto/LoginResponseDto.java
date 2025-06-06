package com.example.learningAppBackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
  
  private String token;
  private String username;
  private String email;
  private Long userId;
}
