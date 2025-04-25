package com.example.learningAppBackend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.learningAppBackend.entity.Goal;
import com.example.learningAppBackend.enumeration.GoalStatus;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long>{

  // --- Spring Data JPA が自動生成するメソッド ---

  /**
   * 指定されたユーザーIDに紐づく全ての目標を取得します。 期日の昇順でソートします。
   * 
   * @param userId ユーザーID
   * @return Goal Entity のリスト
   */
  List<Goal> findByUserIdOrderByTargetDateAsc(Long userId);

  /**
   * 指定されたユーザーIDとステータスに紐づく目標を取得します。 期日の昇順でソートします。
   * 
   * @param userId ユーザーID
   * @param status 目標の状態 (GoalStatus Enum)
   * @return Goal Entity のリスト
   */
  List<Goal> findByUserIdAndStatusOrderByTargetDateAsc(Long userId, GoalStatus status);


  /**
   * 指定されたユーザーIDと目標IDに紐づく目標を取得します。 (更新・削除時の所有権チェック用)
   * 
   * @param goalId 目標ID
   * @param userId ユーザーID
   * @return Optional<Goal>
   */
  Optional<Goal> findByGoalIdAndUserId(Long goalId, Long userId);

  /**
   * 指定されたユーザーIDと目標IDに紐づく目標が存在するかチェックします。 (削除時の存在・所有権チェック用)
   * 
   * @param goalId 目標ID
   * @param userId ユーザーID
   * @return 存在すれば true
   */
  boolean existsByGoalIdAndUserId(Long goalId, Long userId);

  /**
   * 指定されたユーザーIDと期日に紐づく目標を取得します。 (「今日の目標」などを取得する場合に利用可能)
   * 
   * @param userId ユーザーID
   * @param targetDate 期日
   * @return Goal Entity のリスト
   */
  List<Goal> findByUserIdAndTargetDate(Long userId, LocalDate targetDate);

}
