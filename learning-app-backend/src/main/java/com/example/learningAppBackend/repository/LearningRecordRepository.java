package com.example.learningAppBackend.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.learningAppBackend.entity.LearningRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;



@Repository // ① このインターフェースが Repository であることを示す (省略可能な場合もある)
public interface LearningRecordRepository extends JpaRepository<LearningRecord, Long> { // ② JpaRepository を継承

    List<LearningRecord> findByUserId(Long userId);

    Optional<LearningRecord> findByRecordIdAndUserId(Long recordId, Long userId);
    /**
     * 
     * @param recordId
     * @param userId
     * @return
     */
    boolean existsByRecordIdAndUserId(Long recordId, Long userId);

    /**
     * 指定ユーザーIDの、指定期間内の合計学習時間（分）を取得します。
     * 記録がない場合は 0 を返します。
     * @param userId
     * @param startDate
     * @param endDate
     * @return 合計学習時間 (分)
     */
    @Query("SELECT COALESCE(SUM(lr.durationMinutes), 0) FROM LearningRecord lr WHERE lr.userId = :userId AND lr.recordDate >= :startDate AND lr.recordDate <= :endDate")
    Integer sumDurationMinutesForUserBetween(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    /**
     * 指定ユーザーIDの、指定期間内の記録件数を取得します。
     * @param userId
     * @param startDate
     * @param endDate
     * @return 記録件数
     */
    @Query("SELECT COUNT(lr) FROM LearninggRecord lr.userId = :userId AND lr.recordDate >= :startDate AND lr.recordDate <= endDate")
    Long countRevordsForUserBetween(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 指定ユーザーIDの最新の学習記録を指定された件数だけ取得します。
     * 日付の降順、次にIDの降順でソートします。
     * @param userId ユーザーID
     * @param pageable ページネーション情報 (取得件数、ソート順などを含む)
     * @return 学習記録 Entity のリスト
     */
    List<LearningRecord> findByUserIdOrderByRecordDateDescRecordIdDesc(Long userId, Pageable pageable);
}
