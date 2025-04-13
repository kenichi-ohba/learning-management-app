import React, { useState } from 'react'
import LearningRecordForm from './components/LearningRecordForm.jsx'
import LearningRecordList from './components/LearningRecordList.jsx'  
//import axios from 'axios'
import './App.css'

function App() {
  //この state の値が変わると learningRecordList コンポーネントが再マウントされる
  const [listkey, setListKey] = useState(Date.now());
  
  // LearningRecordForm 呼び出される関数
  const handleRecordCreated = (newRecord) => {
    console.log('App: 新しい記録が登録されたのでリストを更新します', newRecord);
    // listKey の値を更新する事で、LearningRecordList コンポーネントが再マウントされる
    setListKey(Date.now()); //  現在時刻などで値を強制的に変更
  };


  return (
    <>
      <h1>学習管理アプリ</h1>

      {/* 学習記録入力フォームを表示 */}
      {/* onRecordCreated プロパティとして関数を渡す */}
      <LearningRecordForm onRecordCreated={handleRecordCreated} />

      <hr />
      {/* 学習記録リストを表示 */}
      {/* key プロパティに listKey state を渡す */}
      <LearningRecordList key={listkey} />
      

    </>
  )
}

export default App
