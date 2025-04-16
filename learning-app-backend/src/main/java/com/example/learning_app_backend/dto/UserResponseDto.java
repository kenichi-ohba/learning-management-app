package com.example.learning_app_backend.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserResponseDto {
  private Long userId;
  private String username;
  private String email;
  private LocalDateTime createdAt;
  // passwordHash は含めない
}
