import React, { useState } from "react";
// ↓↓↓ 目標種類の定数をインポート (任意) ↓↓↓
import { GOAL_TYPE, GOAL_TYPE_JP } from "../constants/goalConstants";

function GoalForm({ onCreateGoal }) {
  const [description, setDescription] = useState("");
  const [targetDate, setTargetDate] = useState("");
  const [goalType, setGoalType] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError(null);
    setIsLoading(true);

    const newGoalDate = {
      description,
      targetDate: targetDate || null,
      goalType: goalType || null,
    };

    try {
      await onCreateGoal(newGoalDate);

      setDescription("");
      setTargetDate(null);
      setGoalType(null);
    } catch (err) {
      console.error("GoalForm: Error during goal creation:", err);
      setError("目標の追加に失敗しました。");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="bg-white p-6 rounded-lg shadow mb-8">
      <h2 className="text-x1 font-semibold mb-4">新しい目標を追加</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <form onSubmit={handleSubmit}>
        <label
          htmlFor="goalDescription"
          className="block text-sm font-medium text-gray-700 mb-1"
        >
          目標管理<span className="text-red-500">*</span>
        </label>
        <textarea
          id="goalDescription"
          rows="3"
          className="w-full px-3 py-2 border-gray-300 rounded-md shadow-sm focus:outLine-none focus:ring-blue focus:border-blue-500"
          placeholder="例:公式ドキュメントを読む"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          required
          disabled={isLoading}
        />
        <div className="mb-4">
          <label
            htmlFor="targetDate"
            className="block text-sm font-medium text-gray-700 mb-1"
          >
            目標期日 <span className="text-xs text-gray-500">(任意)</span>
          </label>
          <input
            type="date"
            id="targetDate"
            className="w-full sm:w-auto px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
            value={targetDate}
            onChange={(e) => setTargetDate(e.target.value)}
            disabled={isLoading}
          />
        </div>
        <div className="mb-4">
          <label htmlFor="goalType" className="block text-sm font-medium text-gray-700 mb-1">目標の種類 <span className="text-xs text-gray-500">(任意)</span></label>
          <select
            id="goalType"
            className="w-full sm:w-auto px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 bg-white"
            value={goalType}
            onChange={(e) => setGoalType(e.target.value)}
            disabled={isLoading}
          >
            <option value="">選択してください</option>
            {/* 定義した GOAL_TYPE を使って選択肢を生成 */}
            {Object.entries(GOAL_TYPE).map(([key, value]) => (
              <option key={key} value={value}>{GOAL_TYPE_JP[value] || value}</option> // 日本語表示。なければキー名
            ))}
          </select>
        </div>
        <div>
          <button
            type="submit"
            className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50"
            disabled={isLoading}
          >
            {isLoading ? '追加中...' : '目標を追加する'}
          </button>
        </div>
      </form>
    </div>
  );
}
export default GoalForm;