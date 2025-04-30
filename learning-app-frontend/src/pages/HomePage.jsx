import React, { useEffect, useState } from "react";
import apiClient from "../api/apiClient";
import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

// 関数コンポーネントを定義
function HomePage() {
  const [summaryData, setSummaryData] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const { currentUser } = useAuth();

  useEffect(() => {
    const fetchDashboardSummary = async () => {
      setIsLoading(true);
      setError(null);
      console.log("HomePage: Fetching dashboard summary...");

      try {
        const response = await apiClient.get("/dashboard/summary");
        console.log(
          "HomePage: Dashboard summary data received:",
          response.data
        );
        setSummaryData(response.data);
      } catch (err) {
        console.error(
          "HomePage: ダッシュボードデータの取得に失敗しました:",
          err
        );
        if (err.response) {
          setError(
            `データの読み込みに失敗しました (Status: ${err.response.status})。再ログインが必要かもしれません。`
          );
        } else {
          setError("データの読み込み宙に予期しないエラーが発生しました。");
        }
        setSummaryData(null);
      } finally {
        setIsLoading(false);
      }
    };

    fetchDashboardSummary();
  }, []);

  if (isLoading) {
    return <div>ダッシュボードを読み込み中...</div>;
  }

  if (error) {
    return <dev style={{ color: "red" }}>エラー: {error} </dev>;
  }

  if (!summaryData) {
    return <dev>ダッシュボードデータを表示出来ませんでした。</dev>;
  }

  return (
    <div>
      {/* ユーザーへの挨拶 (任意) */}
      {currentUser && (
        <h1 className="text-2xl font-semibold mb-4">
          こんにちは、{currentUser.username} さん！
        </h1>
      )}

      {/* --- サマリーカード --- */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        {/* カード 1: 今週の合計時間 */}
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-lg font-semibold mb-2 text-gray-500">
            今週の合計学習時間
          </h2>
          <p className="text-3xl font-bold text-blue-600">
            {/* 分を時間と分に変換して表示 (例) */}
            {Math.floor((summaryData.totalDurationThisWeek || 0) / 60)}
            <span className="text-xl">時間</span>{" "}
            {(summaryData.totalDurationThisWeek || 0) % 60}
            <span className="text-xl">分</span>
          </p>
        </div>
        {/* カード 2: 今週の記録数 */}
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-lg font-semibold mb-2 text-gray-500">
            今週の記録数
          </h2>
          <p className="text-3xl font-bold text-green-600">
            {summaryData.recordCountThisWeek || 0}
            <span className="text-xl">件</span>
          </p>
        </div>
        {/* カード 3: 今日期日の未達成目標 */}
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-lg font-semibold mb-2 text-gray-500">
            今日の未達成の目標
          </h2>
          <p className="text-3xl font-bold text-yellow-600">
            {summaryData.pendingGoalsTodayCount !== null
              ? summaryData.pendingGoalsTodayCount
              : "?"}
            <span className="text-xl">件</span>
          </p>
          <Link
            to="/goals"
            className="text-sm text-blue-600 hover:underline mt-2 inline-block"
          >
            目標管理へ &rarr;
          </Link>
        </div>
        {/* カード 4: 今週期日の未達成目標 */}
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-lg font-semibold mb-2 text-gray-500">
            今週の未達成の目標
          </h2>
          <p className="text-3xl font-bold text-yellow-600">
            {summaryData.pendingGoalsThisWeekCount !== null
              ? summaryData.pendingGoalsThisWeekCount
              : "?"}
            <span className="text-xl">件</span>
          </p>
          <Link
            to="/goals"
            className="text-sm text-blue-600 hover:underline mt-2 inline-block"
          >
            目標管理へ &rarr;
          </Link>
        </div>
      </div>
      {/* --- ↑↑↑ サマリーカードここまで ↑↑↑ --- */}

      {/* --- 最近の学習記録 --- */}
      <div className="bg-white p-6 rounded-lg shadow mb-8">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-semibold">最近の学習記録</h2>
          <Link to="/records" className="text-sm text-blue-600 hover:underline">
            すべて表示
          </Link>
        </div>
        <div className="space-y-4">
          {summaryData.recentRecords && summaryData.recentRecords.length > 0 ? (
            summaryData.recentRecords.map((record) => (
              <div
                key={record.recordId}
                className="border-b pb-3 last:border-b-0"
              >
                <p className="text-sm text-gray-500">
                  {record.recordDate}
                  <span className="ml-2 text-gray-400">
                    ({record.durationMinutes}分)
                  </span>
                </p>
                <p className="font-medium mt-1">{record.learnedContent}</p>
                {/* 必要なら詳細へのリンクなどを追加 */}
                {/* <Link to={`/records/${record.recordId}`}>詳細</Link> */}
              </div>
            ))
          ) : (
            <p className="text-gray-500">最近の学習記録はありません。</p>
          )}
        </div>
        {/* 新規記録追加ボタン (RecordsPage へのリンク) */}
        <Link to="/records">
          <button className="mt-6 px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 text-sm">
            <i className="fas fa-plus mr-1"></i> 新しい記録を追加/確認
          </button>
        </Link>
      </div>

      {/* --- (任意) グラフ表示エリア (次のステップで実装) --- */}
      {/*
    <div className="bg-white p-6 rounded-lg shadow">
      <h2 className="text-xl font-semibold mb-4">学習時間の推移 (今週)</h2>
      <div className="h-64 flex items-center justify-center text-gray-400 italic">
        [ここにグラフが表示されます]
      </div>
    </div>
    */}
    </div>
  );
}

export default HomePage;
