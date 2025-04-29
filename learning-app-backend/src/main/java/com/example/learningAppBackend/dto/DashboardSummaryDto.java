package com.example.learningAppBackend.dto;

import java.util.List;
import lombok.Data;

@Data
public class DashboardSummaryDto {

  /** 今週 (月曜〜日曜) の合計学習時間 (分) */
  private Integer totalDurationThisWeek;

  /** 今週 (月曜〜日曜) の記録件数 (分) */
  private Long recordCountThisWeek;

  /** 最新の学習記録(DTO のリスト) */
  private List<LearningRecordDto> recentRecords;

  private Long pendingGoalsTodayCount;

  private Long pendingGoalsThisWeekCount;

}
