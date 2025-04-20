import React, {useState} from "react";
import apiClient from '../api/axiosInstance';
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function LoginPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (event) => {
    event.preventDefault()
    setIsLoading(true);
    setError(null);
    const loginData = {username, password};

    try {
      // バックエンドのログイン API (POST /api/users/login)を呼び出し
      const response = await apiClient.post('http://localhost:8080/api/users/login', loginData);

      // ログイン成功時の処理
      console.log('ログイン成功レスポンス:', response.data);
      login({ username: username });

      alert('ログインしました！');
      navigate('/records');// 学習記録ページへ遷移      
    } catch (err) {
      // ログイン失敗時の処理
      console.error("ログインエラー:", err);
      if(err.response && err.response.data) {
        //バックエンドからエラーメッセージが返ってきた場合
        setError(err.response.data);
      } else {
        setError("ログイン中にエラーが発生しました。");
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div>
      <h1>ログイン</h1>
      <form onSubmit={handleSubmit}>
        {error && <p style={{ color: 'red'}}>{error}</p>}
        <div>
          <label htmlFor="username">ユーザー名</label>
          <input
            type="text"
            id="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
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
        />
        </div>
        <button type="submit" disabled={isLoading}>
          {isLoading ? 'ログイン中...' : 'ログイン'}
        </button>
      </form>
      <p style={{marginTop: '1em'}}>
        アカウントをお持ちでないですか？<Link to="/register">ユーザー登録</Link>
      </p>
    </div>
  )
}

export default LoginPage;