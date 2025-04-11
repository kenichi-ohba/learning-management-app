package com.example.learning_app_backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.learning_app_backend.entity.LearningRecord;
//import java.util.List; 

@Repository // ① このインターフェースが Repository であることを示す (省略可能な場合もある)
public interface LearningRecordRepository extends JpaRepository<LearningRecord, Long> { // ② JpaRepository を継承

    // ③ 基本的な CRUD メソッド (save, findById, findAll, deleteById など) は自動で提供される

    // ④ 必要であれば、カスタム検索メソッドを追加できる (例)
    // findBy + フィールド名 で、そのフィールドによる検索メソッドを自動生成
    // List<LearningRecord> findByUserIdOrderByRecordDateDesc(Long userId);
  
}
