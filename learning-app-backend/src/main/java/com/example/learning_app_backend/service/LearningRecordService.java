package com.example.learning_app_backend.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.learning_app_backend.dto.LearningRecordDto;
import com.example.learning_app_backend.entity.LearningRecord;
import com.example.learning_app_backend.repository.LearningRecordRepository;

@Service // ① このクラスが Service であることを示す (省略可能な場合もある)
public class LearningRecordService {

  private final LearningRecordRepository learningRecordRepository;

  @Autowired // ② コンストラクタインジェクション
  public LearningRecordService(LearningRecordRepository learningRecordRepository) {
    this.learningRecordRepository = learningRecordRepository;
  }

  // 全件取得メソッド
  @Transactional(readOnly = true)// 読み取り専用トランザクション
  public List<LearningRecordDto> getAllRecords() {
        List<LearningRecord> records = learningRecordRepository.findAll();
        // Entity のリストを DTO のリストに変換して返す
        return records.stream()
                      .map(this::convertToDto) // 下で定義する変換メソッドを使用
                      .collect(Collectors.toList());
    }
    // 新規登録メソッド
    @Transactional // 書き込みを含むトランザクション
    public LearningRecordDto createRecord(LearningRecordDto learningRecordDto) {
        LearningRecord learningRecord = convertToEntity(learningRecordDto); // DTO を Entity に変換

        // ★★★ userId を設定する必要がある ★★★
        // 本来は Spring Security などでログインユーザーの ID を取得して設定する
        // ここでは仮に固定値を入れる (例: 1L)
        learningRecord.setUserId(1L); // TODO: 認証情報から取得するように修正

        LearningRecord savedRecord = learningRecordRepository.save(learningRecord); // DB に保存
        return convertToDto(savedRecord); // 保存された Entity を DTO に変換して返す
    }

    // --- DTO と Entity の相互変換メソッド ---

    // Entity から DTO へ変換
    private LearningRecordDto convertToDto(LearningRecord entity) {
        LearningRecordDto dto = new LearningRecordDto();
        dto.setRecordId(entity.getRecordId());
        // dto.setUserId(entity.getUserId()); // 必要なら userId も DTO に含める
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

    // DTO から Entity へ変換 (登録・更新用)
    private LearningRecord convertToEntity(LearningRecordDto dto) {
        LearningRecord entity = new LearningRecord();
        // ID は DB で自動採番されるので、DTO からはセットしない (更新の場合はセットする)
        // entity.setUserId(dto.getUserId()); // userId は別途設定するのでここでは不要
        entity.setRecordDate(dto.getRecordDate());
        entity.setDurationMinutes(dto.getDurationMinutes());
        entity.setCompletedTasks(dto.getCompletedTasks());
        entity.setLearnedContent(dto.getLearnedContent());
        entity.setUnderstoodPoints(dto.getUnderstoodPoints());
        entity.setIssues(dto.getIssues());
        entity.setNextActions(dto.getNextActions());
        entity.setTextAchievementLevel(dto.getTextAchievementLevel());
        // createdAt, updatedAt は自動設定されるので不要
        return entity;
    }
  
  
}
