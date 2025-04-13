package com.example.learning_app_backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.learning_app_backend.dto.LearningRecordDto;
import com.example.learning_app_backend.entity.LearningRecord;
import com.example.learning_app_backend.repository.LearningRecordRepository;
import jakarta.persistence.EntityNotFoundException;

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
    /**
     * IDを指定して学習記録を1件取得する
     * @param recordId 取得したい学習記録のID
     * @return 見つかった学習記録のDTO
     * @throws EntityNotFoundException 指定されたIDの記録が見つからない場合
     */
    @Transactional(readOnly = true)
    public LearningRecordDto getRecordById(Long recordId) {
        Optional<LearningRecord> optionalRecord = learningRecordRepository.findById(recordId);
        LearningRecord record = optionalRecord.orElseThrow(() ->
            new EntityNotFoundException("指定されたIDの学習記録が見つかりません:" + recordId)
        );
        return convertToDto(record);
    }

    /**
     * 指定されたIDの学習記録を更新する
     * @param recordId 更新したい学習記録のID
     * @param learningRecordDto 更新内容を含むDTO
     * @return 更新後の学習記録のDTO
     * @throws EntityNotFoundException 指定されたIDの記録が見つからない場合
     */
    @Transactional
    public LearningRecordDto updateRecord(Long recordId, LearningRecordDto learningRecordDto) {
        LearningRecord existingRecord  = learningRecordRepository.findById(recordId)
            .orElseThrow(() -> new EntityNotFoundException("更新対象の記録が見つかりません:" + recordId));
        
            // DTO の値で　Entity を更新（更新するフィールドを選択）
            existingRecord.setRecordDate(learningRecordDto.getRecordDate());
            existingRecord.setDurationMinutes(learningRecordDto.getDurationMinutes());
            existingRecord.setCompletedTasks(learningRecordDto.getCompletedTasks());
            existingRecord.setLearnedContent(learningRecordDto.getLearnedContent());
            existingRecord.setUnderstoodPoints(learningRecordDto.getUnderstoodPoints());
            existingRecord.setIssues(learningRecordDto.getIssues());
            existingRecord.setNextActions(learningRecordDto.getNextActions());
            existingRecord.setTextAchievementLevel(learningRecordDto.getTextAchievementLevel());
            // 注意: userId や createdAt は通常更新しない

            LearningRecord updatedRecord = learningRecordRepository.save(existingRecord);
            return convertToDto(updatedRecord); // 既存の変換メソッドを使用
    }   

    /**
     * 指定されたIDの学習記録を削除する
     * @param recordId 削除したい学習記録のID
     * @throws EntityNotFoundException 指定されたIDの記録が見つからない場合 (任意)
     */
    @Transactional
    public void deleteRecord(Long recordId) {
        // 存在確認
        if (!learningRecordRepository.existsById(recordId)) {
            throw new EntityNotFoundException("削除対象の記録が見つかりません: " + recordId);
        }
        learningRecordRepository.deleteById(recordId);
    }

    
    
                    
  
  
}
