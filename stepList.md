# 学習管理アプリ 開発ステップ一覧

## はじめに

このドキュメントは、学習管理アプリの開発プロセス全体を概観するためのステップ一覧です。完了したステップ、これからのステップ、および各ステップの推定難易度を示します。

## 前提技術スタック

- **バックエンド:** Java 21, Spring Boot 3.x, Spring Data JPA, Spring Security
- **フロントエンド:** React (Vite), JavaScript, HTML, CSS, Axios
- **データベース:** PostgreSQL (Docker Compose で管理)
- **インフラ:** Ubuntu (WSL 2), Docker Desktop
- **開発ツール:** VSCode, Git, SDKMAN, nvm, VSCode REST Client (または Postman/Insomnia)

## 開発ステップ

| No.  | フェーズ             | ステップ内容                                        | 状況    | 推定難易度 | 備考                                                   |
| :--: | :------------------- | :-------------------------------------------------- | :------ | :--------- | :----------------------------------------------------- |
| 0.1  | 計画・設計           | 要件定義                                            | ✅ 完了 | 中         | 機能、画面仕様の明確化                                 |
| 0.2  | 計画・設計           | 技術選定                                            | ✅ 完了 | 低         | 使用する言語、フレームワーク等の決定                   |
| 0.3  | 計画・設計           | DB 設計・ER 図作成                                  | ✅ 完了 | 中         | テーブル構造、リレーションの設計                       |
| 1.1  | 環境構築             | 開発環境準備 (OS, IDE, JDK, Docker, Node.js)        | ✅ 完了 | 中         | 各ツールのインストールと設定                           |
| 1.2  | 環境構築             | Spring Boot プロジェクト作成                        | ✅ 完了 | 低         | Spring Initializr または VSCode 拡張機能を使用         |
| 1.3  | 環境構築             | React プロジェクト作成                              | ✅ 完了 | 低         | Vite または Create React App を使用                    |
| 1.4  | 環境構築             | Docker Compose で PostgreSQL 環境構築               | ✅ 完了 | 中         | `docker-compose.yml` 作成、コンテナ起動                |
| 2.1  | バックエンド基本     | Spring Boot: DB 接続設定 (`application.properties`) | ✅ 完了 | 低         | JDBC URL, ユーザー名, パスワード等の設定               |
| 2.2  | バックエンド基本     | Spring Boot: Entity 作成 (`LearningRecord`)         | ✅ 完了 | 中         | JPA アノテーション、テーブルマッピング                 |
| 2.3  | バックエンド基本     | Spring Boot: Repository 作成 (`LearningRecordRepo`) | ✅ 完了 | 低         | `JpaRepository` 継承                                   |
| 2.4  | バックエンド基本     | Spring Boot: DTO 作成 (`LearningRecordDto`)         | ✅ 完了 | 低         | API 用のデータ転送オブジェクト                         |
| 2.5  | バックエンド基本     | Spring Boot: Service 作成 (登録/取得)               | ✅ 完了 | 中         | ビジネスロジック、Repository 呼び出し、DTO 変換        |
| 2.6  | バックエンド基本     | Spring Boot: Controller 作成 (登録/取得 API)        | ✅ 完了 | 中         | `@RestController`, `@RequestMapping`, Service 呼び出し |
| 2.7  | バックエンド基本     | Spring Boot: Security 基本設定 (CORS, permitAll)    | ✅ 完了 | 中〜高     | `SecurityConfig`, CORS Bean, テスト用アクセス許可      |
| 3.1  | 基本連携・テスト     | API テスト (POST/GET)                               | ✅ 完了 | 低〜中     | REST Client 等での動作確認                             |
| 3.2  | 基本連携・テスト     | React: 基本的な API 呼び出し実装                    | ✅ 完了 | 中         | `axios`, `useEffect`, `useState` を使用した疎通確認    |
| 3.3  | 基本連携・テスト     | CORS 等の問題解決                                   | ✅ 完了 | 高         | エラー解決のトラブルシューティング                     |
| 4.1  | コア機能 (CRUD)      | React: 学習記録入力フォーム作成                     | ✅ 完了  | 中         | React フォームコンポーネント作成                       |
| 4.2  | コア機能 (CRUD)      | React: 登録 API (POST) 連携                         | ✅ 完了  | 中         | フォーム送信、`axios.post`                             |
| 4.3  | コア機能 (CRUD)      | React: 学習記録一覧表示画面作成                     | ✅ 完了  | 中         | データリスト表示コンポーネント                         |
| 4.4  | コア機能 (CRUD)      | React: 一覧取得 API (GET) 連携                      | ✅ 完了  | 中         | `useEffect`, `axios.get`, 状態更新                     |
| 4.5  | コア機能 (CRUD)      | バックエンド: 更新 API (PUT) 実装                   | ✅ 完了  | 中         | Service, Controller (ID 指定、データ更新)              |
| 4.6  | コア機能 (CRUD)      | バックエンド: 削除 API (DELETE) 実装                | ✅ 完了  | 中         | Service, Controller (ID 指定、データ削除)              |
| 4.7  | コア機能 (CRUD)      | バックエンド: 詳細取得 API (GET by ID) 実装         | ✅ 完了  | 中         | Service, Controller (ID 指定、データ取得)              |
| 4.8  | コア機能 (CRUD)      | React: 更新・削除・詳細表示機能 実装                | ✅ 完了  | 中〜高     | 編集フォーム、削除確認、モーダル表示など               |
| 5.1  | 認証機能             | バックエンド: User Entity/Repo/Service 実装         | 未完了  | 中         | ユーザー情報管理                                       |
| 5.2  | 認証機能             | **バックエンド: Security 設定詳細化**               | 未完了  | **高**     | PasswordEncoder, UserDetailsService, JWT/Session 等    |
| 5.3  | 認証機能             | バックエンド: 登録/Login/Reset API 実装             | 未完了  | 中         | 認証ロジック                                           |
| 5.4  | 認証機能             | React: 登録/Login 画面作成、API 連携                | 未完了  | 中〜高     | フォーム、API 呼び出し                                 |
| 5.5  | 認証機能             | **React: Token/Session 処理, 認証状態管理**         | 未完了  | **高**     | LocalStorage/Context/Redux, Header 付与, Routing Guard |
| 5.6  | 認証機能             | バックエンド: API アクセス制御修正                  | 未完了  | 中         | `permitAll` から `authenticated()` 等へ                |
| 6.1  | 追加機能             | Top ページ (バックエンド: 集計 API)                 | 未完了  | 中         | データ集計、JPA クエリ                                 |
| 6.2  | 追加機能             | Top ページ (フロントエンド: 表示, グラフ連携)       | 未完了  | 中〜高     | API 連携、グラフライブラリ                             |
| 6.3  | 追加機能             | 目標管理 (バックエンド API)                         | 未完了  | 中         | CRUD 応用                                              |
| 6.4  | 追加機能             | 目標管理 (フロントエンド UI)                        | 未完了  | 中         | フォーム処理                                           |
| 6.5  | 追加機能             | カレンダー表示 (バックエンド API)                   | 未完了  | 中         | 日付処理、データ集計、達成度計算                       |
| 6.6  | 追加機能             | カレンダー表示 (フロントエンド: ライブラリ連携)     | 未完了  | 中〜高     | カレンダーライブラリ、イベント処理、データ表示         |
| 6.7  | 追加機能             | **AI 提案機能 (バックエンド: 外部 API 連携)**       | 未完了  | **高**     | 外部 API, 非同期, Prompt 設計, API Key 管理            |
| 6.8  | 追加機能             | AI 提案機能 (フロントエンド: 表示)                  | 未完了  | 低〜中     | モーダル表示等                                         |
| 6.9  | 追加機能             | **グループ機能 (バックエンド: データ構造, API)**    | 未完了  | **高**     | 複数テーブル連携, 権限管理, 招待/申請ロジックなど複雑  |
| 6.10 | 追加機能             | **グループ機能 (フロントエンド: 画面, API)**        | 未完了  | **高**     | 表示情報が多く状態管理/UI が複雑化しやすい             |
| 7.1  | 改善・テスト・Deploy | UI/UX 全体改善 (CSS, レスポンス, Error 処理)        | 未完了  | 中         | デザイン調整, User Experience 向上                     |
| 7.2  | 改善・テスト・Deploy | テストコード実装 (バックエンド/フロントエンド)      | 未完了  | 中〜高     | テスト概念, Framework 学習                             |
| 7.3  | 改善・テスト・Deploy | デプロイ準備 (Dockerfile, Compose 調整)             | 未完了  | 中〜高     | Docker, ビルドプロセス, 環境変数                       |
