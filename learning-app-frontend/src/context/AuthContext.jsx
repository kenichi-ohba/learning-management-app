import React, { createContext, useState, useContext, useEffect } from "react";
//import apiClient from "../api/apiClient";

//コンテキストオブジェクトを作成・エクスポート
const AuthContext = createContext(null);

// AuthProvider コンポーネントを作成・エクスポート
export function AuthProvider({ children }) {
  //ログイン状態を管理する state (初期値は false)
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  // ログインユーザー情報を管理する state (初期値は null)
  const [currentUser, setCurrentUser] = useState(null);
  //ローディング状態(アプリ起動時に localStorage を確認する間)
  const [isLoading, setIsLoading] = useState(true);
  const [authToken, setAuthToken] = useState(null);

  // アプリ起動時に localStorage から認証情報を復元する処理
  useEffect(() => {
    console.log("AuthProvider: Initializing authentication state...");
    setIsLoading(true);
    try {
      const token = localStorage.getItem("authToken");
      const storedUser = localStorage.getItem("authUser");

      if (token && storedUser) {
        console.log("AuthProvider: Token and user info found in localStorage.");
        const userData = JSON.parse(storedUser);
        setAuthToken(token);
        setCurrentUser(userData);
        setIsLoggedIn(true);
        console.log("AuthProvider: Authentication state restored.", userData);
      } else {
        console.log(
          "AuthProvider: No token or user info found in localStorage."
        );
        localStorage.removeItem("suthToken");
        localStorage.removeItem("authUser");
        setIsLoggedIn(false);
        setAuthToken(null);
      }
    } catch (error) {
      console.error(
        "AuthProvider: Error restoring authentication state:",
        error
      );
      localStorage.removeItem("authToken");
      localStorage.removeItem("authUser");
      setIsLoggedIn(false);
      setCurrentUser(null);
      setAuthToken(null);
    } finally {
      setIsLoading(false);
      console.log('AuthProvider: Initialization complete.')
    }

  }, []);

  // ログイン処理関数
  const login = (userData, token) => {
    console.log('AuthProvider: login called', userData, token);
    // ★ JWT を localStorage に保存
    localStorage.setItem("authToken", token);
    // ★ ユーザー情報も localStorage に保存 (文字列として保存)
    localStorage.setItem("authUser", JSON.stringify(userData));
    setAuthToken(token);
    setCurrentUser(userData);
    setIsLoggedIn(true);
   console.log('AuthProvider: User logged in and data saved.');
  };

  const logout = () => {
    console.log("Logout function called");
    // ★ localStorage から JWT とユーザー情報を削除
    localStorage.removeItem("authToken");
    localStorage.removeItem("authUser");
    setAuthToken(null);
    setCurrentUser(null);
    setIsLoggedIn(false);

    console.log('AuthProvider: User logged out and data removed.');
  };

  // Provider に渡す値 (state と関数をまとめたオブジェクト)
  const value = {
    isLoggedIn,
    currentUser,
    authToken,
    isLoading,
    login,
    logout,
  };

  if (isLoading) {
    return <div>Loading authentication...</div>; // または null やスピナーコンポーネント
  }

  // AuthContext.provider で children を囲み、value を提供する
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};
