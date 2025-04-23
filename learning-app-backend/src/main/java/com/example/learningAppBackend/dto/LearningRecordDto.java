package com.example.learningAppBackend.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Lombok: Getter, Setter, equals, hashCode, toString を自動生成
@NoArgsConstructor // Lombok: 引数なしコンストラクタを自動生成
@AllArgsConstructor // Lombok: 全てのフィールドを引数に持つコンストラクタを自動生成
public class LearningRecordDto {

  private Long recordId;
  
  // userId はリクエストには含めずｍ、レスポンスに含めることが多い
  // 今回はシンプルにするため、レスポンスにも含めない例とする
  // private Long userId;
  
  private LocalDate recordDate;
  private Integer durationMinutes;
  private Integer completedTasks;
  private String learnedContent;
  private String understoodPoints;
  private String issues;
  private String nextActions;
  private Integer textAchievementLevel;



  // aiSuggestion, createdAt, updatedAt レスポンスを返すことが多い
  // 今回はシンプルにする為に省略
  // private String aiSuggestion;
  // private LocalDate createdAt;
  // private LocalDate updatedAt;

  // 注意: 登録API(POST)ではrecoedIdは通常クライアントからおくらないので　null になる想定
  //      レスポンスでは DB に保存された際に採番された recordId が入る。
}
