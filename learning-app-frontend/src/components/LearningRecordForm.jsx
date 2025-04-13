import React, { useState, useEffect } from "react";
import axios from "axios";

// 親コンポーネントに登録成功を通知するための関数を受け取る
function LearningRecordForm({
  onRecordCreated,
  initialData,
  onUpdateComplete,
  isEditMode = false,
}) {
  // フォームの各入力値を保持する為の state
  const [recordDate, setRecordDate] = useState("");
  const [durationMinutes, setDurationMinutes] = useState("");
  const [learnedContent, setLearnedContent] = useState("");
  // 他のフィールド用の state も同様に追加する (例: completedTasks など)
  // const [completedTasks, setCompletedTasks] = useState('');
  // const [understoodPoints, setUnderstoodPoints] = useState('');
  // const [issues, setIssues] = useState('');
  // const [nextActions, setNextActions] = useState('');
  // const [textAchievementLevel, setTextAchievementLevel] = useState('');

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState("");

  useEffect(() => {
    // initialDate が存在する場合 (編集モード)
    if (initialData) {
      // Date オブジェクトやフォーマットの違いに注意が必要な場合がある
      setRecordDate(initialData.recordDate || "");
      setDurationMinutes(initialData.durationMinutes || "");
      setLearnedContent(initialData.learnedContent || "");
    } else {
      // initialDateがない場合(新規登録モード) はフォームをクリア
      setRecordDate("");
      setDurationMinutes("");
      setLearnedContent("");
    }
  }, [initialData]); // initialData が変更されたときに実行

  // フォーム送信時の処理
  const handleSubmit = async (event) => {
    event.preventDefault(); // デフォルトのフォーム送信動作をキャンセル
    setIsSubmitting(true);
    setError(null);
    setSuccessMessage("");

    // 送信するデータオブジェクトを作成
    const recordData = {
      recordDate,
      // derationMinutes は数値なので変換(未入力の場合は null など考慮)
      durationMinutes: durationMinutes ? parseInt(durationMinutes, 10) : null,
      learnedContent,
      // 他のフィールドも同様に追加
      // completedTasks: completedTasks ? parseInt(completedTasks, 10) : null,
      // understoodPoints,
      // issues,
      // nextActions,
      // textAchievementLevel: textAchievementLevel ? parseInt(textAchievementLevel, 10) : null,
    };

    try {
      let response;
      if (isEditMode && initialData?.recordId) {
        // 更新 API を呼び出す
        response = await axios.put(
          `http://localhost:8080/api/learning-records/${initialData.recordId}`,
          recordData
        );
        setSuccessMessage("学習記録を更新しました！");
        // 登録成功を親コンポーネントに通知
        if (onUpdateComplete) {
          onUpdateComplete(response.data); // 作成されたデータを渡す
        }
      } else {
        //登録 API (POST) を呼び出す
        response = await axios.post(
          "http://localhost:8080/api/learning-records",
          recordData
        );
        setSuccessMessage("学習記録を登録しました！");
        // フォームクリア (編集モードではクリアしないことが多い)
        setRecordDate("");
        setDurationMinutes("");
        setLearnedContent("");
        if (onRecordCreated) {
          onRecordCreated(response.data);
        }
      }
    } catch (err) {
      console.error(
        `学習記録の${isEditMode ? "更新" : "登録"}に失敗しました:`,
        err
      );
      setError(
        `${
          isEditMode ? "更新" : "登録"
        }に失敗しました。入力内容を確認してください。`
      );
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div>
      <h2>学習記録を入力</h2>
      {error && <p style={{ color: "red" }}>{error}</p>}
      {successMessage && <p style={{ color: "green" }}>{successMessage}</p>}
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="recordDate">日付</label>
          <input
            type="date"
            id="recordDate"
            value={recordDate}
            onChange={(e) => setRecordDate(e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor="durationMinutes">学習時間（分）</label>
          <input
            type="number"
            id="durationMinutes"
            value={durationMinutes}
            onChange={(e) => setDurationMinutes(e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor="learnedContent">学習内容:</label>
          <textarea
            id="learnedContent"
            value={learnedContent}
            onChange={(e) => setLearnedContent(e.target.value)} // 正しい更新関数を使用
            required
          />
        </div>
        {/* 他の入力フィールドも同様に追加 */}
        {/* ... */}
        <button type="submit" disabled={isSubmitting}>
          {isSubmitting
            ? isEditMode
              ? "更新中..."
              : "登録中..."
            : isEditMode
            ? "更新する"
            : "登録する"}
        </button>
      </form>
    </div>
  );
}
export default LearningRecordForm;
