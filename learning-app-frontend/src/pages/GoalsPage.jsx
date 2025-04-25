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
  // const [isEditing, setIsEditing] = useState(false);
  // const [editingRecordId, setEditingRecordId] = useState(null);
  // const [editingRecordData, setEditingRecordData] = useState(null);

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

  // --- (将来追加) 編集処理関数 ---
  const handleEditGoal = (goalId) => {
    console.log(`GoalsPage: Edit goal ID: ${goalId} (Not implemented yet)`);
    // 編集フォーム表示のロジックをここに追加
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
    // 不要な情報は削除 (バックエンドの DTO に合わせる)
    delete updatedGoalData.goalId; // ID は URL で送るのでボディには不要
    // delete updatedGoalData.userId; // userId も通常は不要

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
  return (
    <div>
      <h1>目標管理</h1>

      <GoalForm onCreateGoal={handleCreateGoal} />

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
