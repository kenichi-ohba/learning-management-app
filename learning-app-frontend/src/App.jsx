import { useEffect, useState } from 'react'
import axios from 'axios'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

function App() {
  const [count, setCount] = useState(0)
  // APIから取得したメッセージを保持する為のstate
  const [backendMessage, setBackendMessage] = useState(''); //初期値は空文字列

  //
  useEffect(() => {
    // Spring boot のAPIエンドポイント
    const apiUrl = 'http://localhost:8080/api/test/hello';

    console.log('API呼び出しを開始します:' ,apiUrl);//ログ出力


    //axiosを使って GET リクエストを送信
   axios.get(apiUrl)
   .then(response => {
    // 通信が成功した場合
    console.log('APIからのレスポンス：', response.data);//ログ出力
    // レスポンスデータの中の messageを stateにセット
    setBackendMessage(response.data.message);
  })
  .catch(error => {
    //通信が失敗した場合
    console.error('API通信エラー：', error);
    if (error.response) {
    //サーバーからのエラーレスポンスがある場合
      console.error('レスポンスデータ：', error.response.data);
    } else if (error.request) {
      //リクエストは送信されたがレスポンスがない場合
      console.error('サーバーからの応答がありません：');
    } else {
      // リクエスト設定時のエラー
      console.error('エラー：', error.message);
    }
    setBackendMessage('メッセージの取得に失敗しました。');
  });
  // 第２引数にから配列'[]'を渡すと、このuseEffect は最初の1回だけ実行される
  }, []);
  return (
    <>
      <div>
        <a href="https://vite.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </div>
      <h1>学習管理アプリ</h1>
      <div className="card">
        <button onClick={() => setCount((count) => count + 1)}>
          count is {count}
        </button>
        <p>
          Edit <code>src/App.jsx</code> and save to test HMR
        </p>
      </div>
      <p className="read-the-docs">
        バックエンドからのメッセージ: {backendMessage || '読み込み中...'}
      </p>
    </>
  )
}

export default App
