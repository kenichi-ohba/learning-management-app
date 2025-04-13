package com.example.learning_app_backend.controller;

import org.springframework.web.bind.annotation.RestController;
import com.example.learning_app_backend.dto.LearningRecordDto;
import com.example.learning_app_backend.entity.LearningRecord;
import com.example.learning_app_backend.service.LearningRecordService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController // REST API の Contoroller
@RequestMapping("/api/learning-records") // この Contoroller の基本パス
// クラスレベルでのCORS設定(securityConfigでも設定しているが念のため個別にも設定)
// SecurityConfig で設定済みなら不要な場合もある
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class LearningRecordController {

  private final LearningRecordService learningRecordService;

  // コンストラクタインジェクションで Service を注入
  @Autowired
  public LearningRecordController(LearningRecordService learningRecordService) {
    this.learningRecordService = learningRecordService;
  }

  // 全件取得 API (GET /api/leaning-records)
  @GetMapping
  public ResponseEntity<List<LearningRecordDto>> getAllLearningRecords() {
    List<LearningRecordDto> records = learningRecordService.getAllRecords();
    return ResponseEntity.ok(records); // HTTP 200 OK と DTO リストを返す
  }

  // 新規登録 API (POST /api/learning-records)
  @PostMapping
  public ResponseEntity<LearningRecordDto> createLearningRecoed(
      @RequestBody LearningRecordDto learningRecordDto) {
    // @RequestBody でリクエストボディの JSON を DTO にマッピング
    LearningRecordDto createRecord = learningRecordService.createRecord(learningRecordDto);
    // HTTP 201 Created と 作成された DTO を返す
    return new ResponseEntity<>(createRecord, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<LearningRecordDto> getLearningRecordById(@PathVariable Long id) {
    // Service を呼び出して DTO を受け取る
    LearningRecordDto recordDto = learningRecordService.getRecordById(id);
    // Service で見つからない場合は例外がスローされる想定
    // 成功した場合は 200 OK と DTO を返す
    return ResponseEntity.ok(recordDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<LearningRecordDto> updateLearningRecord(@PathVariable Long id,
      @RequestBody LearningRecordDto learningRecordDto) {
    LearningRecordDto updatedRecord = learningRecordService.updateRecord(id, learningRecordDto); // Service呼び出し
    return ResponseEntity.ok(updatedRecord);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLearningRecord(@PathVariable Long id) {
    learningRecordService.deleteRecord(id); // Service呼び出し
    return ResponseEntity.noContent().build(); // HTTP 204 No Content を返す (成功を示す)
  }

}
