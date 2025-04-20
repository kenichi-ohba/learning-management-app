import React, { useState} from "react";
import apiClient from '../api/axiosInstance';
import { useNavigate  } from "react-router-dom";// ページ遷移用フック

function RegisterPage() {
  // フォームの入力値を管理する state
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  // API 通信の状態を管理する state
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const navigate = useNavigate(); // useNavigate フックを取得

  const handleSubmit = async (event) => {
    event.preventDefault(); // デフォルトのフォーム送信を抑制
    setIsLoading(true);     // ローディング開始
    setError(null);         // エラークリア
  

  const registrationData = { username, email, password };

  try {
    // バックエンドの登録 API (POST /api/users/register)を呼び出し
    await apiClient.post('http://localhost:8080/api/users/register', registrationData)

    // 登録成功時の処理
    alert('ユーザー登録が完了しました！ログインページに移動します。');
    navigate('/login'); //ログインページへ以降

  } catch (err) {
    // 登録失敗時の処理
    console.error("登録エラー:", err);
    if (err.response && err.response.data) {
      //バックエンドからエラーメッセージが返ってきた場合(重複エラー)
      setError(err.response.data);
    } else {
      // その他のエラー(ネットワークエラーなど)
      setError("登録中にエラーが発生しました。")
    }
  } finally {
    // 成功・失敗に関わらずローディング終了
    setIsLoading(false);
  }
  };
  return(
    <div>
      <h1>ユーザー登録</h1>
      <form onSubmit={handleSubmit}>
        {/* エラーメッセージ表示 */}
        {error && <p style={{color: 'red' }}>{error}</p>}

        <div>
          <label htmlFor="username">ユーザー名</label>
          <input 
            type="text"
            id="username"
            value={username}
            // 入力されるたびに username state を更新
            onChange={(e) => setUsername(e.target.value)}
            required// HTML5 の必須入力チェック
          />
        </div>
        <div>
          <label htmlFor="email">メールアドレス</label>
          <input
          type="email"
          id="email"
          value={email} 
          onChange={(e) => setEmail(e.target.value)}
          required
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
            minLength={8}          
          />
        </div>
        <button type="submit" disabled={isLoading}>
          {isLoading ? '登録中...' : '登録する'}
        </button>
      </form>
    </div>
  )
}


export default RegisterPage;