package com.example.learningAppBackend.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.learningAppBackend.dto.DashboardSummaryDto;
import com.example.learningAppBackend.dto.LearningRecordDto;
import com.example.learningAppBackend.entity.LearningRecord;
import com.example.learningAppBackend.entity.User;
import com.example.learningAppBackend.repository.LearningRecordRepository;
import com.example.learningAppBackend.repository.UserRepository;

@Service
public class DashboardService {
  private final LearningRecordRepository learningRecordRepository;
  private final UserRepository userRepository;

  public DashboardService(LearningRecordRepository learningRecordRepository,
      UserRepository userRepository) {
    this.learningRecordRepository = learningRecordRepository;
    this.userRepository = userRepository;
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()
        || "anonymousUser".equals(authentication.getPrincipal())) {
      throw new IllegalStateException("ユーザーが認証されていません。APIへのアクセスが許可されていない可能性があります。");
    }
    String username = authentication.getName();
    User currentUser = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("ログイン中のユーザーが見つかりません: " + username));
    return currentUser.getUserId();
  }

  /**
   * ダッシュボード用のサマリーデータを取得する
   * 
   * @return DashboardSummaryDto
   */
  @Transactional(readOnly = true)
  public DashboardSummaryDto geDashboardSummary() {
    Long currentUserId = getCurrentUserId();
    LocalDate today = LocalDate.now();

    // 今週 (月曜～日曜) の開始日と終了日を計算
    LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

    // Respository メソッドを呼び出してデータを取得
    Integer totalDuration = learningRecordRepository.sumDurationMinutesForUserBetween(currentUserId,startOfWeek, endOfWeek);
    Long recordCount = learningRecordRepository.countRecordsForUserBetween(currentUserId, startOfWeek, endOfWeek);

    Pageable top3 = PageRequest.of(0, 3); // 0ページ目 (最初のページ) を 3 件取
    List<LearningRecord> recentEntities =
        learningRecordRepository.findByUserIdOrderByRecordDateDescRecordIdDesc(currentUserId, top3);

    // 最新記録リストを DTO リストに変換 (このクラス内に変換ロジックを記述する例)
    List<LearningRecordDto> recentDtos = recentEntities.stream().map(this::convertToDto) // ↓で定義する変換メソッドを使用
        .collect(Collectors.toList());

    // 結果を DTO に詰める
    DashboardSummaryDto summary = new DashboardSummaryDto();
    summary.setTotalDurationThisWeek(totalDuration);
    summary.setRecordCountThisWeek(recordCount);
    summary.setRecentRecords(recentDtos);

    return summary;
  }


  // --- Entity から DTO への変換メソッド (このクラス内に定義する例) ---
  private LearningRecordDto convertToDto(LearningRecord entity) {
    if (entity == null) return null;
    LearningRecordDto dto = new LearningRecordDto();
    dto.setRecordId(entity.getRecordId());
    // dto.setUserId(entity.getUserId()); // 必要に応じて
    dto.setRecordDate(entity.getRecordDate());
    dto.setDurationMinutes(entity.getDurationMinutes());
    dto.setCompletedTasks(entity.getCompletedTasks());
    dto.setLearnedContent(entity.getLearnedContent());
    dto.setUnderstoodPoints(entity.getUnderstoodPoints());
    dto.setIssues(entity.getIssues());
    dto.setNextActions(entity.getNextActions());
    dto.setTextAchievementLevel(entity.getTextAchievementLevel());
    // 必要なら createdAt, updatedAt などもセット
    return dto;
}
}
