import React from "react";
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext'; 

function PrivateRoute({ children }) {
  const { isLoggedIn, isLoadingAuth } = useAuth();
  const location = useLocation();

  // ★ 認証情報を localStorage から読み込んでいる間のローディング表示 ★
  //    これがないと、一瞬ログインページが表示されてしまう可能性がある
  if (isLoadingAuth) {
    return <div>認証情報を確認中...</div>
  }
  // ★ ログインしていない場合はログインページへリダイレクト ★
  if (!isLoggedIn) {
    // `<Navigate>` コンポーネントを使ってリダイレクト
    // `to="/login"`: リダイレクト先のパス
    // `replace`: ブラウザの履歴に現在のページを残さないようにする (戻るボタンで戻れないように)
    // `state={{ from: location }}`: (任意) リダイレクト元の情報を渡す (ログイン後に元のページに戻す場合などに使う) 
    return <Navigate to="/login" replace state={{ from: location}} />;
  }

  return children;
}

export default PrivateRoute;