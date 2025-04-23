package com.example.learningAppBackend.entity;

import jakarta.persistence.*; // JPA アノテーション用
import lombok.Data; // Lombok アノテーション (Getter, Setter, toString など)
import lombok.NoArgsConstructor; // Lombok: 引数なしコンストラクタ
import lombok.AllArgsConstructor; // Lombok: 全引数コンストラクタ
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp; // Hibernate 固有アノテーション
import org.hibernate.annotations.UpdateTimestamp;   // Hibernate 固有アノテーション


@Data // Lombok: Getter, Setter, equals, hashCode, toString を自動生成
@NoArgsConstructor // Lombok: 引数なしコンストラクタを自動生成
@AllArgsConstructor // Lombok: 全てのフィールドを引数に持つコンストラクタを自動生成
@Entity // ① このクラスが JPA エンティティであることを示す
@Table(name = "learning_records") //　対応するテーブル名を指定
public class LearningRecord {

    @Id //主キーであることを示す
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB側で自動裁判(PostgreSQLのSERIALなど)
    private Long recordId; //　主キー(カラム名: record_id)

    // ユーザーID(今回はまずLong型で。あとで　User Entityとの関連付けも可能
    @Column(nullable = false) //NOT NULL 誓約
    private Long userId; // (カラム名: user_id)


    @Column(nullable = false)
    private LocalDate recordDate;// 学習日(カラム名: record_date)

    @Column(nullable = false)
    private Integer durationMinutes;// 学習時間(分)　(カラム名: duration_minnutes)

    private Integer completedTasks;// 完了タスク数(カラム名: completed_tasks)

    @Column(nullable = false, columnDefinition = "TEXT")// 内容は長くなる可能性があるので　TEXT 型を指定
    private String learnedContent; // 学習内容(カラム名: learned_content)

    @Column(columnDefinition = "TEXT")
    private String understoodPoints; // 理解できたこと(カラム名:understood_point)

    @Column(columnDefinition = "TEXT")
    private String issues; // 理解できなかったこと(カラム名:issues)

    @Column(columnDefinition = "TEXT")
    private String nextActions; // 次の行動(カラム名:next_actions)

    private Integer textAchievementLevel; // テキスト目標達成度 (カラム名: text_achievement_level)

    @Column(columnDefinition = "TEXT")
    private String aiSuggestion;// AIからの提案 (カラム名: ai_suggestion)

    @CreationTimestamp //　レコード作成時に現在日時を自動設定(Hibernate)
    @Column(nullable = false, updatable = false) // 作成後は更新しない
    private LocalDateTime createdAt; // 作成日時(カラム名: created_at)

    @UpdateTimestamp //　レコード更新時に現在日時を自動設定(Hibernate)
    @Column(nullable = false)
    private LocalDateTime updatedAt; // 更新日時(カラム名: updated_at)

    
    




}
