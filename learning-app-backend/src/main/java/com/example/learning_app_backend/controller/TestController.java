package com.example.learning_app_backend.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;


@RestController //このクラスがREST APIのコントローラーであることを示す
@RequestMapping("/api/test") //　このコントローラーの基本パス
// React開発サーバーからのアクセスを許可
//  ポート番号はReact(Viteの開発サーバー)起動時に確認出来る
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class TestController {
  
  // /api/test/hello へのGETリクエストを処理
  @GetMapping("/hello")
  public Map <String, String> sayHello() {
    //⑤ {"message": "Hello from Spring Boot!"} というJSONを返す

      return Map.of("message", "Hello from Spring Boot!");
  }
  
}
