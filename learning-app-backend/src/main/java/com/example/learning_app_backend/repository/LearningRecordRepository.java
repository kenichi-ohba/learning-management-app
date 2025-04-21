package com.example.learning_app_backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.learning_app_backend.entity.LearningRecord;
import java.util.List;
import java.util.Optional;


@Repository // ① このインターフェースが Repository であることを示す (省略可能な場合もある)
public interface LearningRecordRepository extends JpaRepository<LearningRecord, Long> { // ② JpaRepository を継承

    List<LearningRecord> findByUserId(Long userId);

    Optional<LearningRecord> findByRecordIdAndUserId(Long recordId, Long userId);

    boolean existsByRecordIdAndUserId(Long recordId, Long userId);
  
}
