package com.example.learningAppBackend.controller;

import com.example.learningAppBackend.dto.GoalDto;
import com.example.learningAppBackend.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/goals")
public class GoalController {

  private final GoalService goalService;

  private GoalController(GoalService goalService) {
    this.goalService = goalService;
  }


  /**
   * ログインユーザーのすべての目標を取得する API
   * 
   * @return GoalDto のリストを含む ResponseEntity
   */
  @GetMapping
  public ResponseEntity<List<GoalDto>> getAllGoals() {
    List<GoalDto> goals = goalService.getAllGoalsForCurrentUser();
    return ResponseEntity.ok(goals);
  }

  /**
   * ログインユーザーの特定の目標を取得する API
   * 
   * @param id 取得したい目標の ID
   * @return GoalDto を含む ResponseEntity (見つからない場合は 404 を返すように Service 側で例外処理推奨)
   */
  @GetMapping("/{id}")
  public ResponseEntity<GoalDto> getGoalById(@PathVariable Long id) {
    GoalDto goal = goalService.getGoalByIdForCurrentUser(id);
    return ResponseEntity.ok(goal);
  }

  /**
   * ログインユーザーの新しい目標を作成する API
   * @param goalDto リクエストボディから受け取る目標データ
   * @return  作成された GoalDto を含む ResponseEntity
   */
  @PostMapping
  public ResponseEntity<GoalDto> createGoal(@Valid @RequestBody GoalDto goalDto) {
    GoalDto createGoal = goalService.createGoalForCurrentUser(goalDto);
    return new ResponseEntity<>(createGoal, HttpStatus.CREATED);
  }

  /**
     * ログインユーザーの目標を更新する API
     * @param id 更新したい目標の ID
     * @param goalDto リクエストボディから受け取る更新データ
     * @return 更新後の GoalDto を含む ResponseEntity (200 OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<GoalDto> updateGoal(@PathVariable Long id, @Valid @RequestBody GoalDto goalDto) {
        GoalDto updatedGoal = goalService.updateGoalForCurrentUser(id, goalDto);
        return ResponseEntity.ok(updatedGoal); // 200 OK と更新後データ
    }

    /**
     * ログインユーザーの目標を削除する API
     * @param id 削除したい目標の ID
     * @return ボディなしの ResponseEntity (204 No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        goalService.deleteGoalForCurrentUser(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}
