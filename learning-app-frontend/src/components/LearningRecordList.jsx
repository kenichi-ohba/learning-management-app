import React, {useState, useEffect} from "react";
import axios from 'axios';

function LearningRecordList({onEdit, onDelete}) {
  // 学習記録のリストを保持するための state
  const [records, setRecords] =useState([]);
  // データ読み込み中かを示す state
  const [isLoading, setIsLoading] = useState(true);
  // エラーメッセージを保持するための state
  const [error, setError] = useState(null);

  // コンポーネントが最初にマウントされたときにデータを取得する
  useEffect(() => {
    const fetchRecords = async () => {
      setIsLoading(true); // ローディング開始
      setError(null); // エラーをリセット
      try {
        //バックエンドの全件取得APIを呼び出す
        const response = await axios.get('http://localhost:8080/api/learning-records');
        console.log('APIから取得したデータ:', response.data); // ← これを追加
        setRecords(response.data);// 取得したデータを state にセット
      } catch(err) {
        console.error("学習記録の取得に失敗しました:", err);
        setError("データの読み込みに失敗しました。"); // エラーメッセージをセット 
      } finally {
        setIsLoading(false); //ローディング終了
      }
    };

    fetchRecords(); //データ取得関数を実行

},[]); //空の依存配列 [] を指定して初回のみ実行

//ローディング中の表示
if (isLoading) {
  return <p>読み込み中...</p>
}

// エラー発生時の表示
if (error) {
  return <p style={{color: 'red'}}>{error}</p>;
}

//学習記録リストの表示
return (
  <div>
    <h2>学習記録一覧</h2>
    {records.length === 0 ? (
      <p>学習記録がありません。</p>
    ) : (
      <ul style={{listStyle: 'none', padding: 0}}>
        {/* records 配列の各要素を record として繰り返し処理 */}
        {records.map((record) => (
          // key 属性には一意な値を指定する (recordId が適切)
          <li key={record.recordId}>
            <div><strong>日付:</strong> {record.recordDate}</div>
            <div><strong> 時間:</strong> {record.durationMinutes}分</div>
            <div><strong> 内容:</strong> {record.learnedContent}</div>
            {/* 必要に応じて他の情報も表示 */}
            <div>
              <button onClick={() => onEdit(record.recordId)} style={{ marginRight: '5px'}}>編集</button>
              <button onClick={() => onDelete(record.recordId)}>削除</button>
            </div>
          </li>
        ))}
      </ul>
    )}
  </div>
);
}

export default LearningRecordList;