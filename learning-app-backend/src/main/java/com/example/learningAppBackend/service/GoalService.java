package com.example.learningAppBackend.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.learningAppBackend.dto.GoalDto;
import com.example.learningAppBackend.entity.Goal;
import com.example.learningAppBackend.entity.User;
import com.example.learningAppBackend.enumeration.GoalStatus;
import com.example.learningAppBackend.repository.GoalRepository;
import com.example.learningAppBackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class GoalService {

  private final GoalRepository goalRepository;
  private final UserRepository userRepository;

  public GoalService(GoalRepository goalRepository, UserRepository userRepository) {
    this.goalRepository = goalRepository;
    this.userRepository = userRepository;
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()
        || "anonymousUser".equals(authentication.getPrincipal())) {
      throw new IllegalStateException("ユーザーが認証されていません。");
    }
    String username = authentication.getName();
    User currentUser = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("ログイン中のユーザーが見つかりません: " + username));
    return currentUser.getUserId();
  }

  @Transactional(readOnly = true)
  public List<GoalDto> getAllGoalsForCurrentUser() {
    Long currentUserId = getCurrentUserId();
    List<Goal> goals = goalRepository.findByUserIdOrderByTargetDateAsc(currentUserId); // Repository
                                                                                       // メソッドを使用
    return goals.stream().map(this::convertToDto).collect(Collectors.toList());
  }

  /**
   * IDを指定してログインユーザーの目標を1件取得する
   * 
   * @param goalId 取得したい目標のID
   * @return 見つかった目標のDTO
   * @throws EntityNotFoundException 指定IDの目標が見つからないか、アクセス権がない場合
   */
  @Transactional(readOnly = true)
  public GoalDto getGoalByIdForCurrentUser(Long goalId) {
    Long currentUserId = getCurrentUserId();
    Goal goal = goalRepository.findByGoalIdAndUserId(goalId, currentUserId) // 所有権チェック
        .orElseThrow(
            () -> new EntityNotFoundException("指定されたIDの目標が見つからないか、アクセス権がありません: " + goalId));
    return convertToDto(goal);
  }

  /**
   * ログインユーザーの新しい目標を作成する
   * 
   * @param goalDto 作成する目標の情報を含むDTO
   * @return 作成された目標のDTO
   */
  @Transactional
  public GoalDto createGoalForCurrentUser(GoalDto goalDto) {
    Long currentUserId = getCurrentUserId();
    Goal goal = convertToEntity(goalDto);
    goal.setUserId(currentUserId); // ログインユーザーのIDを設定
    // status は Entity のデフォルト値 (PENDING) が使われる想定
    Goal savedGoal = goalRepository.save(goal);
    return convertToDto(savedGoal);
  }

  /**
   * ログインユーザーの目標を更新する
   * 
   * @param goalId 更新したい目標のID
   * @param goalDto 更新内容を含むDTO
   * @return 更新後の目標のDTO
   * @throws EntityNotFoundException 指定IDの目標が見つからないか、アクセス権がない場合
   */
  @Transactional
  public GoalDto updateGoalForCurrentUser(Long goalId, GoalDto goalDto) {
    Long currentUserId = getCurrentUserId();
    Goal existingGoal = goalRepository.findByGoalIdAndUserId(goalId, currentUserId) // 所有権チェック
        .orElseThrow(() -> new EntityNotFoundException("更新対象の目標が見つからないか、アクセス権がありません: " + goalId));

    // DTO の内容で Entity を更新
    existingGoal.setDescription(goalDto.getDescription());
    existingGoal.setTargetDate(goalDto.getTargetDate()); // null も許可
    existingGoal.setStatus(goalDto.getStatus()); // 状態も更新可能にする
    existingGoal.setGoalType(goalDto.getGoalType()); // 種類も更新可能にする

    Goal updatedGoal = goalRepository.save(existingGoal);
    return convertToDto(updatedGoal);
  }

  /**
   * ログインユーザーの目標を削除する
   * 
   * @param goalId 削除したい目標のID
   * @throws EntityNotFoundException 指定IDの目標が見つからないか、アクセス権がない場合
   */
  @Transactional
  public void deleteGoalForCurrentUser(Long goalId) {
    Long currentUserId = getCurrentUserId();
    // 存在確認と所有権チェックを兼ねる
    if (!goalRepository.existsByGoalIdAndUserId(goalId, currentUserId)) {
      throw new EntityNotFoundException("削除対象の目標が見つからないか、アクセス権がありません: " + goalId);
    }
    goalRepository.deleteById(goalId);
  }


  // --- Entity から DTO への変換メソッド ---
  private GoalDto convertToDto(Goal entity) {
    if (entity == null)
      return null;
    GoalDto dto = new GoalDto();
    dto.setGoalId(entity.getGoalId());
    dto.setDescription(entity.getDescription());
    dto.setTargetDate(entity.getTargetDate());
    dto.setStatus(entity.getStatus());
    dto.setGoalType(entity.getGoalType());
    return dto;
  }

  // --- DTO から Entity への変換メソッド ---
  private Goal convertToEntity(GoalDto dto) {
    if (dto == null)
      return null;
    Goal entity = new Goal();
    // goalId は新規作成時には null、更新時には Service 側で取得した Entity にセットする
    // userId は Service 側でログインユーザーの ID をセットする
    entity.setDescription(dto.getDescription());
    entity.setTargetDate(dto.getTargetDate());
    entity.setStatus(dto.getStatus() != null ? dto.getStatus() : GoalStatus.PENDING); // デフォルトはPENDING
    entity.setGoalType(dto.getGoalType());
    // createdAt, updatedAt は自動設定
    return entity;
  }
}
