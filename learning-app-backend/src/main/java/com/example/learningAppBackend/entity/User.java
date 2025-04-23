package com.example.learningAppBackend.entity;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")// 対応するテーブル名を指定 (クラス名と違う場合や明示したい場合)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // DB で自動採番
  private Long userId; // ログインID

  @Column(unique = true, nullable = false, length = 50) // ユニーク誓約, Not Null, 長さ制限
  private String username;

  @Column(nullable = false)
  private String passwordHash;// ★ハッシュ化されたパスワードを保存 (フィールド名を password にしない)

  @Column(unique = true, nullable = false)
  private String email;

  @CreationTimestamp // レコード作成日時を自動設定
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp // レコード更新日時を自動設定
  @Column(nullable = false)
  private LocalDateTime updatedAt;
}
