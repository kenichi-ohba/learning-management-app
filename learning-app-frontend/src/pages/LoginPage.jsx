import React, { useState } from "react";
import apiClient from "../api/apiClient";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function LoginPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  // ナビゲーションフックと認証コンテキストの login 関数を取得
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (event) => {
    event.preventDefault(); // デフォルトのフォーム送信をキャンセル
    setIsLoading(true); // ローディング開始
    setError(null); // エラーメッセージをリセット
    const loginData = { username, password }; // 送信するデータ

    try {
      // バックエンドのログイン API (POST /api/users/login)を呼び出し
      const response = await apiClient.post("/users/login", loginData);

      const {
        token,
        username: responseUsername,
        email,
        userId,
      } = response.data;

      if (token && responseUsername) {
        const userData = { userId, username: responseUsername, email };
        login(userData, token); // 認証状態を更新し、情報を保存

        console.log("Login successful:", response.data);
        alert("ログインしました");
        navigate("/records");
      } else {
        console.error(
          "LoginPage: Login response missing token or username:",
          response.data
        );
        setError("ログインに失敗しました。レスポンスデータが不正です。");
      }
    } catch (err) {
      // ログイン失敗時の処理
      console.error("LoginPage: Login API error:", err);
      if (err.response && err.response.data) {
        const errorMessage =
          typeof err.response.data === "string"
            ? err.response.data
            : err.response.data.message || JSON.stringify(err.response.data); // message プロパティを優先
        setError(`ログインエラー: ${errorMessage}`);
      } else if (err.request) {
        setError("サーバーに接続できませんでした。");
      } else {
        setError("ログイン中に予期しないエラーが発生しました。");
      }
    } finally {
      // ローディング終了
      setIsLoading(false);
    }
  };

  return (
    <div>
      <h1>ログイン</h1>
      <form onSubmit={handleSubmit}>
        {/* エラーメッセージ表示 */}
        {error && <p style={{ color: "red" }}>{error}</p>}
        <div>
          <label htmlFor="username">ユーザー名</label>
          <input
            type="text"
            id="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
            disabled={isLoading} // ローディング中は無効化
          />
        </div>
        <div>
          <label htmlFor="password">パスワード</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            disabled={isLoading} // ローディング中は無効化
          />
        </div>
        <button type="submit" disabled={isLoading}>
          {isLoading ? "ログイン中..." : "ログイン"}
        </button>
      </form>
      <p style={{ marginTop: "1em" }}>
        アカウントをお持ちでないですか？<Link to="/register">ユーザー登録</Link>
      </p>
    </div>
  );
}

export default LoginPage;
