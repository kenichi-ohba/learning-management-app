# 学習管理アプリ 開発ステップ一覧 (更新版)

## はじめに

このドキュメントは、学習管理アプリの開発プロセス全体を概観するためのステップ一覧です。完了したステップ、これからのステップ、および各ステップの推定難易度を示します。

## 前提技術スタック

- **バックエンド:** Java 21, Spring Boot 3.x, Spring Data JPA, Spring Security
- **フロントエンド:** React (Vite), JavaScript, HTML, CSS, Axios
- **データベース:** PostgreSQL (Docker Compose で管理)
- **インフラ:** Ubuntu (WSL 2), Docker Desktop
- **開発ツール:** VSCode, Git, SDKMAN, nvm, VSCode REST Client

## 開発ステップ

| No.  | フェーズ         | ステップ内容                                       | 状況     | 推定難易度 | 備考 (読み方など)                                            |
| :--- | :--------------- | :------------------------------------------------- | :------- | :--------- | :------------------------------------------------------------- |
| 0.1  | 計画・設計       | 要件定義                                           | ✅完了   | 中         | 機能、画面仕様の明確化                                         |
| 0.2  | 計画・設計       | 技術選定                                           | ✅完了   | 低         | 使用する言語、フレームワーク等の決定                           |
| 0.3  | 計画・設計       | DB 設計・ER 図作成                                 | ✅完了   | 中         | テーブル構造、リレーションの設計                               |
| 1.1  | 環境構築         | 開発環境準備 (OS, IDE, JDK, Docker, Node.js)     | ✅完了   | 中         | 各ツールのインストールと設定                                   |
| 1.2  | 環境構築         | Spring Boot プロジェクト作成                     | ✅完了   | 低         | Spring Initializr 使用                                         |
| 1.3  | 環境構築         | React プロジェクト作成                           | ✅完了   | 低         | Vite 使用                                                      |
| 1.4  | 環境構築         | Docker Compose で PostgreSQL 環境構築            | ✅完了   | 中         | `docker-compose.yml` 作成、コンテナ起動                         |
| 2.1  | バックエンド基本 | Spring Boot: DB 接続設定 (`application.properties`) | ✅完了   | 低         | JDBC URL, ユーザー名, パスワード等の設定                      |
| 2.2  | バックエンド基本 | Spring Boot: Entity 作成 (`LearningRecord`, `User`) | ✅完了   | 中         | JPA アノテーション、テーブルマッピング                           |
| 2.3  | バックエンド基本 | Spring Boot: Repository 作成 (`LearningRecordRepo`, `UserRepo`) | ✅完了   | 低         | `JpaRepository` 継承                                         |
| 2.4  | バックエンド基本 | Spring Boot: DTO 作成 (`LearningRecordDto`, `UserRegDto`, `LoginReqDto`, `LoginResDto`) | ✅完了   | 低         | API 用のデータ転送オブジェクト                                 |
| 2.5  | バックエンド基本 | Spring Boot: Service 作成 (CRUD, User登録, UserDetailsService) | ✅完了   | 中         | ビジネスロジック、Repository 呼び出し、DTO 変換、パスワードハッシュ化 |
| 2.6  | バックエンド基本 | Spring Boot: Controller 作成 (CRUD API, 登録/ログイン API) | ✅完了   | 中         | `@RestController`, `@RequestMapping`, Service 呼び出し        |
| 2.7  | バックエンド基本 | Spring Boot: Security 基本設定 (CORS, CSRF disable, Beans) | ✅完了   | 中〜高     | `SecurityConfig`, CORS Bean, PasswordEncoder, AuthManager      |
| 3.1  | 基本連携・テスト | API テスト (CRUD, 登録, ログイン(JWT返却))       | ✅完了   | 中         | REST Client 等での動作確認                                   |
| 3.2  | 基本連携・テスト | React: 基本的な API 呼び出し実装                 | ✅完了   | 中         | Axios, `useEffect`, `useState` を使用した疎通確認           |
| 3.3  | 基本連携・テスト | CORS, DB接続, Git, Security 等の問題解決         | ✅完了   | 高         | エラー解決のトラブルシューティング                             |
| 4.1  | コア機能 (CRUD)  | React: 学習記録 CRUD 画面実装 & API 連携         | ✅完了   | 中〜高     | フォーム、一覧、編集・削除ボタン、自動更新                   |
| 5.1  | 認証機能 (JWT)   | バックエンド: JWT 依存関係追加 & 設定            | ✅完了   | 低         | `pom.xml`, `application.properties` (`jwt.secret` 等)         |
| 5.2  | 認証機能 (JWT)   | バックエンド: JWT Utils 作成                     | ✅完了   | 中         | トークン生成・検証ヘルパー (`JwtUtils.java`)                   |
| 5.3  | 認証機能         | React: React Router 導入 & 基本ルート設定        | ✅完了   | 中         | ページコンポーネント作成、`App.jsx` でのルーティング設定       |
| 5.4  | 認証機能         | React: 登録/Login 画面作成 & API 連携            | ✅完了   | 中〜高     | フォーム作成、API 呼び出し、基本的なページ遷移               |
| 5.5  | 認証機能         | React: Context API で認証状態管理              | ✅完了   | 中〜高     | `AuthContext`, `AuthProvider` (状態復元, login/logout)       |
| 5.6  | 認証機能         | React: ナビゲーション表示切替 & ログアウト実装   | ✅完了   | 中         | `useAuth`, 条件レンダリング, `logout()` 呼び出し             |
| 5.7  | 認証機能         | React: ルーティングガード (`PrivateRoute`) 実装    | ✅完了   | 中         | 未ログイン時のページアクセス制限                               |
| ---  | ---              | ---                                                | ---      | ---        | ---                                                            |
| 5.8  | **認証機能 (JWT)** | **バックエンド: SecurityConfig - STATELESS 設定** | **未完了** | 中         | セッション管理を無効化                                         |
| 5.9  | **認証機能 (JWT)** | **バックエンド: JwtAuthenticationFilter 実装** | **未完了** | **高** | リクエストヘッダーから JWT を検証し認証情報をセットするフィルター |
| 5.10 | **認証機能 (JWT)** | **バックエンド: フィルターチェーンへの追加** | **未完了** | 中         | `SecurityConfig` で `addFilterBefore`                          |
| 5.11 | **認証機能 (JWT)** | **フロントエンド: axios インターセプター実装** | **未完了** | 中〜高     | リクエストヘッダーに自動で JWT を付与 (`apiClient`)            |
| 5.12 | **認証機能 (JWT)** | **フロントエンド: AuthContext JWT 処理** | **未完了** | 中         | `login` でトークン保存、起動時復元を JWT ベースに              |
| 5.13 | **認証機能 (JWT)** | **バックエンド/全体: JWT 認証動作確認** | **未完了** | 中〜高     | JWT で保護された API にアクセスできるか最終確認                |
| 6.1  | 追加機能         | Top ページ (バックエンド: 集計 API)              | 未完了   | 中         | データ集計、JPA クエリ                                         |
| 6.2  | 追加機能         | Top ページ (フロントエンド: 表示, グラフ連携)    | 未完了   | 中〜高     | API 連携、グラフライブラリ                                       |
| 6.3  | 追加機能         | 目標管理 (バックエンド API)                      | 未完了   | 中         | CRUD 応用 (`goals` テーブル)                                   |
| 6.4  | 追加機能         | 目標管理 (フロントエンド UI)                     | 未完了   | 中         | フォーム処理                                                   |
| 6.5  | 追加機能         | カレンダー表示 (バックエンド API)                  | 未完了   | 中         | 日付処理、データ集計、達成度計算                               |
| 6.6  | 追加機能         | カレンダー表示 (フロントエンド: ライブラリ連携)    | 未完了   | 中〜高     | カレンダーライブラリ、イベント処理、データ表示                 |
| 6.7  | 追加機能         | **AI 提案機能 (バックエンド: 外部 API 連携)** | 未完了   | **高** | Gemini API 等, 非同期, Prompt 設計, API Key 管理              |
| 6.8  | 追加機能         | AI 提案機能 (フロントエンド: 表示)               | 未完了   | 低〜中     | モーダル表示等                                                 |
| 6.9  | 追加機能         | **グループ機能 (バックエンド: データ構造, API)** | 未完了   | **高** | 複数テーブル連携, 権限管理, 招待/申請ロジックなど複雑       |
| 6.10 | 追加機能         | **グループ機能 (フロントエンド: 画面, API)** | 未完了   | **高** | 表示情報が多く状態管理/UI が複雑化しやすい                     |
| 7.1  | 改善・テスト・Deploy | UI/UX 全体改善 (CSS, レスポンス, Error処理)    | 未完了   | 中         | デザイン調整, User Experience 向上                              |
| 7.2  | 改善・テスト・Deploy | テストコード実装 (バックエンド/フロントエンド)     | 未完了   | 中〜高     | JUnit, Mockito, Jest 等                                        |
| 7.3  | 改善・テスト・Deploy | デプロイ準備 (Dockerfile, Compose調整)         | 未完了   | 中〜高     | Docker, ビルドプロセス, 環境変数                                |

