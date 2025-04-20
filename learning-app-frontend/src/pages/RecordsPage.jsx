import React, {useState} from "react";
import apiClient from '../api/axiosInstance';
import LearningRecordForm from "../components/LearningRecordForm";
import LearningRecordList from "../components/LearningRecordList";


function RecordsPage() {
  //この state の値が変わると learningRecordList コンポーネントが再マウントされる
  const [listKey, setListKey] = useState(Date.now());
  // 編集フォームを表示するかどうかのフラグ
  const [isEditing, setIsEditing] = useState(false);
  // 現在編集中の記録の ID (なければ null)
  const [editingRecordId, setEditingRecordId] = useState(null);
  // 現在編集中の記録 ID (なければ null)
  const [editingRecordData, setEditingRecordData] = useState(null);

  // LearningRecordForm 呼び出される関数
  const handleRecordCreated = (newRecord) => {
    console.log("App: 新しい記録が登録されたのでリストを更新します", newRecord);
    // listKey の値を更新する事で、LearningRecordList コンポーネントが再マウントされる
    setListKey(Date.now()); //  現在時刻などで値を強制的に変更
  };

  //  削除ボタンが押されたときの処理関数
  const handleDelete = async (recordId) => {
    // 削除確認ダイアログを表示
    if (window.confirm(`ID: ${recordId} の記録を本当に削除しますか？`)) {
      try {
        // バックエンドの削除APIを呼び出す
        await apiClient.delete(
          `http://localhost:8080/api/learning-records/${recordId}`
        );
        alert("削除しました。");
        // 削除成功したらリストを更新
        setListKey(Date.now());
      } catch (err) {
        console.error("削除に失敗しました:", err);
        alert("削除に失敗しました。");
      }
    }
  };

  // 編集ボタンが押されたときの処理関数を追加
  const handleEdit = async (recordId) => {
    console.log(`App: ID: ${recordId} の記録を編集します`);
    setIsEditing(true); // 編集モードにする
    setEditingRecordId(recordId); // 編集対象の ID を保持

    try {
      const response = await apiClient.get(
        `http://localhost:8080/api/learning-records/${recordId}`
      );
      setEditingRecordData(response.data); // 取得したデータを state に保存
    } catch (err) {
      console.error("編集データの取得に失敗:", err);
      alert("編集データの取得に失敗しました。");
      setIsEditing(false);
      setEditingRecordId(null);
      setEditingRecordData(null);
    }
  };

  //--- ↓↓↓ 編集フォームで更新が完了したときの処理関数を追加
  const handleUpdateComplete = () => {
    console.log("App: 更新が完了しました");
    setIsEditing(false);
    setEditingRecordId(null);
    setEditingRecordData(null);
    setListKey(Date.now());
  };

  return (
    <div>
      <h2>学習記録</h2>

      {/* 編集モードデない場合に新規登録フォームを表示 */}

      {!isEditing && (<LearningRecordForm onRecordCreated={handleRecordCreated}/>)}

      {/* 編集モードの場合に編集フォームを表示 */}
      {isEditing && editingRecordData && (
        <div>
        <hr />
        <h2>学習記録を編集(ID: {editingRecordId})</h2>
        <LearningRecordForm
          //key を渡して編集対象が変わったらフォームを再生成させる
          key={editingRecordId}
          // 編集対象の初期データとして渡す
          initialData={editingRecordData}
          // 更新完了に呼び出す関数を渡す
          onUpdateComplete={handleUpdateComplete}
          //編集モードであることを示すフラグ
          isEditMode={true}
        />
        <button
          onClick={() => {
            setIsEditing(false);
            setEditingRecordId(null);
            setEditingRecordData(null);
          }}
          style={{ marginTop: "10px" }}
        >
          編集をキャンセル
        </button>
      </div>
      )}

      <hr />

       {/* 学習記録リストを表示 */}
      {/* LearningRecordList に onEdit と onDelete を渡す */}
      <LearningRecordList
       key={listKey} 
       onEdit={handleEdit}
       onDelete={handleDelete}
       />
      
    </div>
  );
}

export default RecordsPage;
