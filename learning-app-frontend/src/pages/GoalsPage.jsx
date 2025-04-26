import React, { useEffect, useState } from "react";
import apiClient from "../api/apiClient";
import GoalList from "../components/GoalList";
import GoalForm from "../components/GoalForm";
import { GOAL_STATUS } from "../constants/goalConstants";

function GoalsPage() {
  const [goals, setGoals] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [listKey, setListKey] = useState(Date.now());
  const [isEditing, setIsEditing] = useState(false);
  const [editingGoalId, setEditingGoalId] = useState(null); // 編集中 ID
  const [editingGoalData, setEditingGoalData] = useState(null); // 編集中データ

  useEffect(() => {
    const fetchGoals = async () => {
      setIsLoading(true);
      setError(null);
      console.log("GoalsPage: Fetching goals...");
      try {
        const response = await apiClient.get("/goals");
        setGoals(response.data);
      } catch (err) {
        console.error("GoalsPage: 目標の取得に失敗しました:", err);
        setError("目標データの読み込みに失敗しました。");
        setGoals([]);
      } finally {
        setIsLoading(false);
      }
    };
    fetchGoals();
  }, [listKey]);

  // 目標登録関数
  const handleCreateGoal = async (newGoalData) => {
    console.log("goalsPage: Creating new goal:", newGoalData);
    try {
      await apiClient.post("/goals", newGoalData);
      alert("新しい目標を追加しました！");
      setListKey(Date.now()); // リストを再読み込み
    } catch (err) {
      console.error("GoalsPage: 目標の追加に失敗しました:", err);
      alert("目標の追加に失敗しました。");
    }
  };

  // 削除関数
  const handleDeleteGoal = async (goalId) => {
    if (window.confirm(`ID: ${goalId} 目標を本当に削除しますか？`)) {
      try {
        await apiClient.delete(`/goals/${goalId}`);
        alert("目標を削除しました。");
        setListKey(Date.now());
      } catch (err) {
        console.error("GoalsPage: 目標の削除に失敗しました:", err);
        alert("目標の削除にしっぱいしました");
      }
    }
  };

  // 編集処理関数 ---
  const handleEditGoal = async (goalId) => {
    console.log(`GoalsPage: Edit goal ID: ${goalId}`);
    setError(null); // 他のエラーをクリア
    setIsEditing(true);
    setEditingGoalId(goalId);

    // 編集対象の最新データをAPIから取得する
    try {
      // ★ GET /api/goals/{id} を呼び出す ★
      const response = await apiClient.get(`/goals/${goalId}`);
      setEditingGoalData(response.data); // 取得したデータを state に保存
    } catch (err) {
      console.error("GoalsPage: 編集対象データの取得に失敗:", err);
      alert("編集データの取得に失敗しました。");
      // エラーなら編集モードを解除
      setIsEditing(false);
      setEditingGoalId(null);
      setEditingGoalData(null);
    }
  };

  // 状態更新処理関数 ---
  const handleUpdateStatus = async (goalId, newStatus) => {
    console.log(`GoalsPage: Updating status for ID: ${goalId} to ${newStatus}`);
    // 更新対象の目標データを現在のリストから探す
    const targetGoal = goals.find(goal => goal.goalId === goalId);
    if (!targetGoal) {
      console.error("GoalsPage: 更新対象の目標が見つかりません:", goalId);
      alert("更新対象の目標が見つかりませんでした。");
      return;
    }

    // ★ 更新用のデータを作成 (他の情報は既存のものを維持し、status のみ変更) ★
    const updatedGoalData = {
      ...targetGoal, // 既存のデータをコピー
      status: newStatus // status だけ新しい値で上書き
    };
    delete updatedGoalData.goalId; // ID は URL で送るのでボディには不要

    try {
      // バックエンドの更新 API (PUT /api/goals/{id}) を呼び出す
      await apiClient.put(`/goals/${goalId}`, updatedGoalData);
      alert('目標の状態を更新しました。');
      setListKey(Date.now()); // ★ 成功したらリストを再読み込み
    } catch (err) {
      console.error("GoalsPage: 目標の状態更新に失敗しました:", err);
      alert('目標の状態更新に失敗しました。');
    }
  };
     // 編集フォームで更新が完了したときの処理関数
   const handleUpdateComplete = () => {
    console.log('GoalsPage: 更新が完了しました');
    setIsEditing(false); // 編集モードを解除
    setEditingGoalId(null); // 編集対象情報をクリア
    setEditingGoalData(null);
    setListKey(Date.now()); // リストを更新
  };
  return (
    <div>
      <h1>目標管理</h1>
      {!isEditing && (
        <GoalForm onCreateGoal={handleCreateGoal} />
      )}

      {/* --- ↓↓↓ 編集モードの場合に編集用フォームを表示 ↓↓↓ --- */}
      {isEditing && editingGoalData && (
        <div className="mt-8 border-t pt-6"> {/* 少し見た目を調整 */}
          <h2 className="text-xl font-semibold mb-4">
            目標を編集 (ID: {editingGoalId})
          </h2>
          <GoalForm
            // key を渡して編集対象が変わったらフォームを再生成 (重要)
            key={editingGoalId}
            // 編集対象の初期データとして渡す
            initialData={editingGoalData}
            // 更新完了時に呼び出す関数を渡す
            onUpdateComplete={handleUpdateComplete}
            // 編集モードであることを示すフラグ
            isEditMode={true}
          />
          <button
            onClick={() => { // キャンセルボタン
              setIsEditing(false);
              setEditingGoalId(null);
              setEditingGoalData(null);
            }}
            className="mt-4 px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600"
          >
            編集をキャンセル
          </button>
        </div>
      )}

      {isLoading && <p>目標を読み込み中...</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}
      {!isLoading && !error && (
        <GoalList
          goals={goals}
          onEdit={handleEditGoal}
          onDelete={handleDeleteGoal}
          onUpdateStatus={handleUpdateStatus}
        />
      )}
    </div>
  );
}
export default GoalsPage;
