import React from "react";
import { Routes, Route, Link, useNavigate } from "react-router-dom";
import "./App.css";
import { useAuth } from "./context/AuthContext";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import RecordsPage from "./pages/RecordsPage";
import PrivateRoute from "./components/PrivateRoute";
import GoalsPage from './pages/GoalsPage';

function App() {
  // useAuth フックで isLoggedIn と logout を取得
  const { isLoggedIn, logout, currentUser } = useAuth();
  const navIgate = useNavigate(); // ページ遷移用

  const handleLogout = () => {
    logout(); // AuthContext の logout 関数を呼び出し (state更新 & localStorage削除)
    navIgate("/login");
  };

  return (
    <>
      <nav>
        {isLoggedIn && currentUser ? (
          <span>こんにちは、{currentUser.username} さん</span> // currentUser の中身に注意
        ) : null}
        <ul>
          <li>
            <Link to="/">ホーム</Link>
          </li>
          {isLoggedIn && <li><Link to="/records">学習記録</Link></li>}
          {isLoggedIn && <li><Link to="/goals">目標管理</Link></li>}
          {isLoggedIn ? (
            <li>
              <button onClick={handleLogout}>ログアウト</button>
            </li>
          ) : (
            <>
              <li>
                <Link to="/login">ログイン</Link>
              </li>
              <li>
                <Link to="/register">登録</Link>
              </li>
            </>
          )}
        </ul>
      </nav>
      <hr />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route
          path="/records"
          element={
            <PrivateRoute>
              <RecordsPage />
            </PrivateRoute>
          }
        />
        <Route
          path="/goals"
          element={
            <PrivateRoute>
              <GoalsPage/>
            </PrivateRoute>
          }
        />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
      </Routes>
    </>
  );
}
export default App;
