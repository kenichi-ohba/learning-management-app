import React, { useState, useEffect } from "react";
import apiClient from '../api/apiClient';
import { GOAL_STATUS, GOAL_TYPE, GOAL_TYPE_JP, GOAL_STATUS_JP  } from "../constants/goalConstants";

function GoalForm({ onCreateGoal, initialData, onUpdateComplete, isEditMode = false  }) {
  const [description, setDescription] = useState("");
  const [targetDate, setTargetDate] = useState("");
  const [goalType, setGoalType] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState('');
  const [status, setStatus] = useState(GOAL_STATUS.PENDING)

  useEffect(() => {
    if(isEditMode && initialData) {
      console.log("GoalForm: Stting initial data for edit:", initialData);
      setDescription(initialData.description || '');
      setTargetDate(initialData.targetDate || '');
      setGoalType(initialData.goalType || '');
      setStatus(initialData.status || GOAL_STATUS.PENDING);
    } else {
      setDescription('');
      setTargetDate('');
      setGoalType('');
      setStatus(GOAL_STATUS.PENDING);
    }
  }, [initialData, isEditMode]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setIsLoading(true);
    setError(null);
    setSuccessMessage('');

    const goalData = {
      description,
      targetDate: targetDate || null,
      goalType: goalType || null,
      status: status
    };
    console.log("GoalForm: Submitting data:", goalData);
    try {
      let response
      if (isEditMode && initialData?.goalId) { // --- 編集モードの処理 ---
        console.log(`GoalForm: Sending PUT request to /goals/${initialData.goalId}`);
        // ★ 更新 API (PUT) を呼び出す ★
        response = await apiClient.put(`/goals/${initialData.goalId}`, goalData);
        setSuccessMessage('目標を更新しました！');
        // ★ 親コンポーネントに更新完了を通知 ★
        if (onUpdateComplete) {
          onUpdateComplete(response.data); // 更新後のデータを渡す
        }
      } else {
        console.log("GoalForm: Sending POST request to /goals");
        response = await apiClient.post('/goals', goalData);
        setSuccessMessage('新しい目標を追加しました');
        setDescription('');
        setTargetDate('');
        setGoalType('');
        setStatus(GOAL_STATUS.PENDING);

        if (onCreateGoal) {
          onCreateGoal(response.data);
        }
      }
      console.log("GoalForm: API call successful", response.data);
    } catch (err) {
      console.error(`GoalForm: 目標の${isEditMode ? '更新' : '追加'}に失敗しました:`, err);
      setError(`目標の${isEditMode ? '更新' : '追加'}に失敗しました。`);
    } finally {
      setIsLoading(false);
    }

  };

  return (
    <div className="bg-white p-6 rounded-lg shadow mb-8">
    {/* isEditMode に応じてタイトルを変更 */}
    <h2 className="text-xl font-semibold mb-4">{isEditMode ? '目標を編集' : '新しい目標を追加'}</h2>
    {error && <p style={{ color: 'red' }}>{error}</p>}
    {!isEditMode && successMessage && <p style={{ color: 'green' }}>{successMessage}</p>}
    <form onSubmit={handleSubmit}>
      <div className="mb-4">
        <label htmlFor="goalDescription" className="block text-sm font-medium text-gray-700 mb-1">目標内容 <span className="text-red-500">*</span></label>
        <textarea id="goalDescription" rows="3" className="..." value={description} onChange={(e) => setDescription(e.target.value)} required disabled={isLoading} />
      </div>
      <div className="mb-4">
        <label htmlFor="targetDate" className="block text-sm font-medium text-gray-700 mb-1">目標期日 <span className="text-xs text-gray-500">(任意)</span></label>
        <input type="date" id="targetDate" className="..." value={targetDate} onChange={(e) => setTargetDate(e.target.value)} disabled={isLoading} />
      </div>
      <div className="mb-4">
        <label htmlFor="goalType" className="block text-sm font-medium text-gray-700 mb-1">目標の種類 <span className="text-xs text-gray-500">(任意)</span></label>
        <select id="goalType" className="..." value={goalType} onChange={(e) => setGoalType(e.target.value)} disabled={isLoading}>
          <option value="">選択してください</option>
          {Object.entries(GOAL_TYPE).map(([key, value]) => (
            <option key={key} value={value}>{GOAL_TYPE_JP[value] || value}</option>
          ))}
        </select>
      </div>
      <div className="mb-4">
        <label htmlFor="goalStatus" className="block text-sm font-medium text-gray-700 mb-1">状態</label>
        <select id="goalStatus" className="..." value={status} onChange={(e) => setStatus(e.target.value)} disabled={isLoading}>
          {Object.entries(GOAL_STATUS).map(([key, value]) => (
            <option key={key} value={value}>{GOAL_STATUS_JP[value] || value}</option>
          ))}
        </select>
      </div>
      <div>
        <button type="submit" className="..." disabled={isLoading}>
          {isLoading ? (isEditMode ? '更新中...' : '追加中...') : (isEditMode ? '更新する' : '目標を追加する')}
        </button>
      </div>
    </form>
  </div>
  );
}
export default GoalForm;