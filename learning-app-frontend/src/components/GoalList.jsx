import React from "react";
import { GOAL_STATUS, GOAL_STATUS_JP  } from "../constants/goalConstants";

function GoalList({ goals, onEdit, onDelete, onUpdateStatus }) {
const getStatusBadge = (status) => {
  switch (status) {
      case GOAL_STATUS.PENDING: // Enum 定数で比較
        return <span className="text-xs font-semibold inline-block py-1 px-2 uppercase rounded text-yellow-600 bg-yellow-200 last:mr-0 mr-1">未達成</span>;
      case GOAL_STATUS.IN_PROGRESS: // Enum 定数で比較
        return <span className="text-xs font-semibold inline-block py-1 px-2 uppercase rounded text-blue-600 bg-blue-200 last:mr-0 mr-1">進行中</span>;
      case GOAL_STATUS.COMPLETED: // Enum 定数で比較
        return <span className="text-xs font-semibold inline-block py-1 px-2 uppercase rounded text-green-600 bg-green-200 last:mr-0 mr-1">達成済</span>;
      default:
        return <span className="text-xs font-semibold inline-block py-1 px-2 uppercase rounded text-gray-600 bg-gray-200 last:mr-0 mr-1">不明</span>;
  }
}
  return (
    <div className="bg-white p-6 rounded-lg shadow mt-8">
      <h2 className="text-xl font-semibold mb-4">現在の目標一覧</h2>
      {/* 目標リストが空か、要素がなければメッセージ表示 */}
      {!goals || goals.length === 0 ? (
        <p className="text-gray-500">登録されている目標はありません。</p>
      ) : (
        <ul className="space-y-4">
          {/* goals 配列を map でループして各目標を表示 */}
          {goals.map((goal) => (
            <li key={goal.goalId} className="border border-gray-200 p-4 rounded-md flex flex-col sm:flex-row justify-between sm:items-center hover:bg-gray-50 transition duration-150 ease-in-out">
              {/* 目標情報 */}
              <div className="mb-3 sm:mb-0">
                <p className={`font-medium ${goal.status === GOAL_STATUS.COMPLETED ? 'line-through text-gray-500' : ''}`}>
                  {goal.description}
                </p>
                {/* 期日があれば表示 */}
                {goal.targetDate && (
                  <p className="text-sm text-gray-500">期日: {goal.targetDate}</p>
                )}
                {/* 状態バッジを表示 */}
                <div className="mt-1">
                  {getStatusBadge(goal.status)}
                </div>
                {/* 必要なら種類も表示 */}
                {/* {goal.goalType && <p className="text-xs text-gray-400">種類: {goal.goalType}</p>} */}
              </div>
              {/* アクションボタン */}
              <div className="flex space-x-2 flex-shrink-0">
                {/* 達成/未達成ボタン (状態に応じて切り替え) */}
                {goal.status !== GOAL_STATUS.COMPLETED ? (
                  <button
                    // ★★★ 将来的に onUpdateStatus 関数を呼び出すようにする ★★★
                    onClick={() => onUpdateStatus(goal.goalId, GOAL_STATUS.COMPLETED)}
                    className="px-3 py-1 text-sm bg-green-500 text-white rounded hover:bg-green-600 transition duration-150 ease-in-out"
                    title="達成済みにする"
                  >
                    <i className="fas fa-check mr-1"></i> 達成
                  </button>
                ) : (
                  <button
                    // ★★★ 将来的に onUpdateStatus 関数を呼び出すようにする ★★★
                    onClick={() => onUpdateStatus(goal.goalId, GOAL_STATUS.PENDING)}
                    className="px-3 py-1 text-sm bg-gray-500 text-white rounded hover:bg-gray-600 transition duration-150 ease-in-out"
                    title="未達成に戻す"
                  >
                    <i className="fas fa-undo mr-1"></i> 未達成
                  </button>
                )}
                 {/* 編集ボタン (編集機能は後で実装) */}
                 <button
                    onClick={() => onEdit(goal.goalId)}
                    className="px-3 py-1 text-sm bg-yellow-500 text-white rounded hover:bg-yellow-600 transition duration-150 ease-in-out"
                    title="編集"
                 >
                    <i className="fas fa-edit">編集</i>
                 </button>
                {/* 削除ボタン */}
                <button
                  onClick={() => onDelete(goal.goalId)} // 親から渡された onDelete を呼び出す
                  className="px-3 py-1 text-sm bg-red-500 text-white rounded hover:bg-red-600 transition duration-150 ease-in-out"
                  title="削除"
                >
                  <i className="fas fa-trash">削除</i>
                </button>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default GoalList;