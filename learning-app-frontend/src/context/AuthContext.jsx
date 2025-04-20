import React, { createContext, useState, useContext, useEffect} from "react";

//コンテキストオブジェクトを作成・エクスポート
const AuthContext = createContext(null);

// AuthProvider コンポーネントを作成・エクスポート
export function AuthProvider({ children}) {
  //ログイン状態を管理する state (初期値は false)
  const [isLoggedIn, setIsloggedIn] = useState(false);
  // ログインユーザー情報を管理する state (初期値は null)
  const [currentUser, setCurrentUser] = useState(null);
  //ローディング状態(アプリ起動時に localStorage を確認する間)
  const [isLoading, setIsLoading] = useState(true);

  // アプリ起動時に localStorage から認証情報を復元する処理
  useEffect(() => {
    try {
      const storedUser = localStorage.getItem('authUser'); //'authUser' というキーで保存された情報を取得
      if (storedUser) {
        const userData = JSON.parse(storedUser); //JSON 文字列をオブジェクトに戻す
        setCurrentUser(userData);// ユーザー情報を State にセット
        setIsloggedIn(true);     // ログイン状態を true にセット
      }
    } catch (error) {
      console.error("localStorege からユーザー情報復元エラー:", error);
      localStorage.removeItem('authUser');
    } finally {
      setIsLoading(false); //ローディング完了
    }
  },[]);

  // ログイン処理関数
  const login = (userData) => {
    setIsloggedIn(true);
    setCurrentUser(userData);
    //localStorege にユーザー情報を保存 (オブジェクトをJSON文字列に変換して保存)
    localStorage.setItem('authUser', JSON.stringify(userData));
    console.log("ログイン情報を保存しました:", userData);
  };

  const logout = () => {
    setIsloggedIn(false);
    setCurrentUser(null);
    // localStorage からユーザー情報を削除
    localStorage.removeItem('authUser');
    console.log("ログアウトし、保存情報を削除しました。");
    //TODO: ログアウトのAPIを後で追加
  };

  // Provider に渡す値 (state と関数をまとめたオブジェクト)
  const value = {
    isLoggedIn,
    currentUser,
    isLoadingAuth: isLoading,
    login,
    logout,
  };

  // AuthContext.provider で children を囲み、value を提供する
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;

}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}