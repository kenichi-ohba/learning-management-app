package com.example.learning_app_backend.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.learning_app_backend.dto.LearningRecordDto;
import com.example.learning_app_backend.entity.LearningRecord;
import com.example.learning_app_backend.entity.User;
import com.example.learning_app_backend.repository.LearningRecordRepository;
import com.example.learning_app_backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

@Service // ① このクラスが Service であることを示す (省略可能な場合もある)
public class LearningRecordService {

    private final LearningRecordRepository learningRecordRepository;
    private final UserRepository userRepository; // ← UserRepository を追加


    public LearningRecordService(LearningRecordRepository learningRecordRepository,
            UserRepository userRepository) {
        this.learningRecordRepository = learningRecordRepository;
        this.userRepository = userRepository;
    }

    private Long getCurrentUserId() {
        org.springframework.security.core.Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            // 認証されていない場合の処理 (例外をスローするなど)
            throw new IllegalStateException("ユーザーが認証されていません。");
        }
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));
        return currentUser.getUserId();
    }

    // 全件取得メソッド
    @Transactional(readOnly = true) // 読み取り専用トランザクション
    public List<LearningRecordDto> getAllRecords() {
        Long currentUserId = getCurrentUserId();
        List<LearningRecord> records = learningRecordRepository.findByUserId(currentUserId);
        // Entity のリストを DTO のリストに変換して返す
        return records.stream().map(this::convertToDto) // 下で定義する変換メソッドを使用
                .collect(Collectors.toList());
    }

    // 新規登録メソッド
    @Transactional // 書き込みを含むトランザクション
    public LearningRecordDto createRecord(LearningRecordDto learningRecordDto) {
        Long currentUserId = getCurrentUserId();
        LearningRecord learningRecord = convertToEntity(learningRecordDto); // DTO を Entity に変換
        learningRecord.setUserId(currentUserId);
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
     * 
     * @param recordId 取得したい学習記録のID
     * @return 見つかった学習記録のDTO
     * @throws EntityNotFoundException 指定されたIDの記録が見つからない場合
     */
    @Transactional(readOnly = true)
    public LearningRecordDto getRecordById(Long recordId) {
        Long currentUserId = getCurrentUserId();
        LearningRecord record = learningRecordRepository.findByRecordIdAndUserId(recordId, currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("指定されたIDの学習記録が見つかりません:" + recordId));
        return convertToDto(record);
    }

    /**
     * 指定されたIDの学習記録を更新する
     * 
     * @param recordId 更新したい学習記録のID
     * @param learningRecordDto 更新内容を含むDTO
     * @return 更新後の学習記録のDTO
     * @throws EntityNotFoundException 指定されたIDの記録が見つからない場合
     */
    @Transactional
    public LearningRecordDto updateRecord(Long recordId, LearningRecordDto learningRecordDto) {
        Long currentUserId = getCurrentUserId();
        LearningRecord existingRecord = learningRecordRepository.findByRecordIdAndUserId(recordId, currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("更新対象の記録が見つかりません:" + recordId));

        // DTO の値で Entity を更新（更新するフィールドを選択）
        existingRecord.setRecordDate(learningRecordDto.getRecordDate());
        existingRecord.setDurationMinutes(learningRecordDto.getDurationMinutes());
        existingRecord.setCompletedTasks(learningRecordDto.getCompletedTasks());
        existingRecord.setLearnedContent(learningRecordDto.getLearnedContent());
        existingRecord.setUnderstoodPoints(learningRecordDto.getUnderstoodPoints());
        existingRecord.setIssues(learningRecordDto.getIssues());
        existingRecord.setNextActions(learningRecordDto.getNextActions());
        existingRecord.setTextAchievementLevel(learningRecordDto.getTextAchievementLevel());

        LearningRecord updatedRecord = learningRecordRepository.save(existingRecord);
        return convertToDto(updatedRecord); // 既存の変換メソッドを使用
    }

    /**
     * 指定されたIDの学習記録を削除する
     * 
     * @param recordId 削除したい学習記録のID
     * @throws EntityNotFoundException 指定されたIDの記録が見つからない場合 (任意)
     */
    @Transactional
    public void deleteRecord(Long recordId) {
        Long currentUserId = getCurrentUserId();
        // 存在確認
        if (!learningRecordRepository.existsByRecordIdAndUserId(recordId, currentUserId)) {
            throw new EntityNotFoundException("削除対象の記録が見つかりません: " + recordId);
        }
        learningRecordRepository.deleteById(recordId);
    }



}
