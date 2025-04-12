import React, { useState } from 'react'
import LearningRecordForm from './components/LearningRecordForm.jsx'
import LearningRecordList from './components/LearningRecordList.jsx'  
//import axios from 'axios'
import './App.css'

function App() {
  // List を再読み込みさせるための state (単純な例)
  // From でとうろくせいこうしたら値を更新して List を再読み込みさせる
  const [listkey, setListKey] = useState(0);
  
  // Form 空の通知を受け取る関数
  const handleRecordCreated = (newRecord) => {
    console.log('新しい学習記録が登録されました:', newRecord);
    // Listを強制的に再マウントさせるために key を変更
    setListKey(prevKey => prevKey + 1);
  };


  return (
    <>
      <h1>学習管理アプリ</h1>

      {/* 学習記録入力フォームを表示 */}
      {/* onRecordCreated プロパティとして関数を渡す */}
      <LearningRecordForm onRecordCreated={handleRecordCreated} />

      <hr />
      {/* 学習記録リストを表示 */}
      {/* key を渡す事で、 key が変わるとコンポーネントが再生成される */}
      <LearningRecordList key={listkey} />
      

    </>
  )
}

export default App
