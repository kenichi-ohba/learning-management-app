package com.example.learningAppBackend.dto;

import java.time.LocalDate;
import com.example.learningAppBackend.enumeration.GoalStatus;
import com.example.learningAppBackend.enumeration.GoalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalDto {
  private Long goalId;

  private String description;

  private LocalDate targetDate;

  private GoalStatus status;

  private GoalType goalType;

}
