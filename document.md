# 学習管理アプリ 使用技術・機能サマリー

## はじめに

このドキュメントは、現在開発中の学習管理アプリケーションで使用されている主要な技術、ライブラリ、フレームワーク、および実装済みの主な機能や概念をまとめたものです。新しいチャットセッションを開始する際などの状況把握にご利用ください。

## バックエンド (Spring Boot)

* **言語:** Java 21
* **フレームワーク:** Spring Boot 3.x
* **主要 Spring モジュール:**
    * `spring-boot-starter-web`: REST API 開発 (Tomcat 組み込み)
    * `spring-boot-starter-data-jpa`: データベースアクセス (JPA, Hibernate)
    * `spring-boot-starter-security`: 認証・認可
    * `spring-boot-starter-validation`: 入力値検証
* **データベース連携:**
    * JPA (Java Persistence API) / Hibernate (ハイバネート): ORM (Object-Relational Mapping)
    * `JpaRepository` (ジェイピーエーリポジトリ): データアクセス用のインターフェース (Spring Data JPA)
    * Entity (エンティティ): データベーステーブルに対応する Java クラス (`@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column` など)
    * `application.properties`: DB 接続情報、JPA/Hibernate 設定 (`ddl-auto=update` など)
* **API 実装:**
    * `@RestController`: REST API を提供する Controller (コントローラー) クラス
    * `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`: HTTP メソッドと URL パスへのマッピング
    * `@PathVariable`: URL パスから値を取得
    * `@RequestBody`: リクエストボディ (JSON) を Java オブジェクト (DTO) に変換
    * `ResponseEntity`: HTTP ステータスコードとレスポンスボディを制御
    * DTO (Data Transfer Object): API のリクエスト/レスポンスで使用するデータ構造 (`dto` パッケージ)
* **ビジネスロジック:**
    * Service (サービス) 層 (`@Service`): ビジネスロジックを実装
    * `@Transactional`: トランザクション管理
* **セキュリティ (Spring Security):**
    * `SecurityConfig.java`: セキュリティ設定クラス (`@Configuration`, `@EnableWebSecurity`)
    * `SecurityFilterChain` Bean: HTTP セキュリティ設定 (フィルターチェーン)
    * CORS (Cross-Origin Resource Sharing) 設定: `CorsConfigurationSource` Bean, `http.cors()`
    * CSRF (Cross-Site Request Forgery) 保護: 無効化 (`http.csrf().disable()`)
    * アクセス制御: `http.authorizeHttpRequests()`, `.requestMatchers()`, `permitAll()`, `authenticated()`
    * `PasswordEncoder` (パスワードエンコーダ) Bean: `BCryptPasswordEncoder` (ビークリプト) を使用
    * `AuthenticationManager` (オーセンティケーションマネージャー) Bean: 認証処理の実行
    * `UserDetailsService` (ユーザーディテールサービス): `UserService` で実装し、ユーザー情報を DB からロード (`loadUserByUsername`)
    * **JWT (JSON Web Token) 認証 (実装中):**
        * 依存関係: `jjwt-api`, `jjwt-impl`, `jjwt-jackson` (`pom.xml`)
        * 設定: `jwt.secret`, `jwt.expiration-ms` (`application.properties`)
        * ユーティリティ: `JwtUtils.java` (トークン生成・検証)
        * ログイン API (`/api/users/login`): 認証成功時に JWT をレスポンスボディで返すように修正済み
        * **(これから実装):** `SecurityConfig` での `STATELESS` 設定、`JwtAuthenticationFilter` の実装と追加
* **ビルドツール:** Maven (メーヴン) (`pom.xml`, `./mvnw`)
* **その他ライブラリ:**
    * Lombok (ロンボック): 定型コード削減 (`@Data`, `@NoArgsConstructor` など)
    * `spring-boot-devtools`: 開発中の自動リロードなど
    * PostgreSQL Driver (ポストグレスキューエル・ドライバー)
    * `spring-boot-configuration-processor`: 設定プロパティメタデータ生成

## フロントエンド (React)

* **環境:** Vite (ヴィート)
* **言語:** JavaScript (JSX 記法)
* **ライブラリ:**
    * React (リアクト) 18.x: UI 構築ライブラリ
    * `react-router-dom` (リアクト・ルーター・ドム) v6: ページ遷移・ルーティング管理 (`BrowserRouter`, `Routes`, `Route`, `Link`, `useNavigate`)
    * `axios` (アクシオス): HTTP クライアント (API 通信)
* **状態管理:**
    * `useState` (ユーズステート) フック: コンポーネントローカルな状態管理
    * `useEffect` (ユーズエフェクト) フック: 副作用（API 呼び出しなど）の管理
    * React Context API (リアクト・コンテキスト・エーピーアイ): グローバルな認証状態管理 (`AuthContext`, `AuthProvider`, `useContext`/`useAuth`)
* **コンポーネント設計:**
    * `pages` フォルダ: ページ単位のコンポーネント
    * `components` フォルダ: 再利用可能な UI 部品
* **認証連携:**
    * `localStorage` (ローカルストレージ): 認証情報（ユーザー名など、将来的に JWT）の永続化
    * `axios` インスタンス (`apiClient`): `baseURL` 設定 (**`withCredentials: true` は JWT 移行で削除予定**)
    * `PrivateRoute` コンポーネント: ログイン必須ページのアクセス制御（ルーティングガード）
* **パッケージ管理:** npm (エヌピーエム)

## データベース

* **種類:** PostgreSQL (ポストグレスキューエル) (バージョン 15)
* **管理:** Docker Compose (ドッカー・コンポーズ) (`docker-compose.yml`)

## 開発ツール・環境

* **OS:** Ubuntu (ウブントゥ) (WSL 2 - Windows Subsystem for Linux 2)
* **IDE:** Visual Studio Code (ビジュアルスタジオコード / VSCode)
* **バージョン管理:** Git (ギット), GitHub (ギットハブ)
* **Java 環境:** SDKMAN (エスディーケイマン)
* **Node.js 環境:** nvm (ノード・バージョン・マネージャー)
* **API テスト:** VSCode REST Client (ブイエスコード・レスト・クライアント)
* **DB クライアント:** VSCode PostgreSQL Explorer (ブイエスコード・ポストグレスキューエル・エクスプローラー)
* **AI 支援:** Gemini Code Assist (ジェミニ・コードアシスト)
