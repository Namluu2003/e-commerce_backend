# DATN Backend — Spring Boot (API)

Phần **Backend** của đồ án tốt nghiệp (DATN) — trang web bán giày thời trang, xây dựng bằng **Spring Boot**.

## ✨ Công nghệ

- **Java 17**
- **Spring Boot 3.3.6** (Spring Web, Spring Data JPA, Spring Security, OAuth2)
- **MySQL 8.0** — cơ sở dữ liệu
- **Redis 7.2** — cache / phiên (session)
- **Lombok**
- Bảo mật: **JWT** (token), **Google OAuth2**, ZaloPay (sandbox)

## 📁 Cấu trúc thư mục

```
src/main/java/com/poly/app/
├── domain/                  # Nghiệp vụ theo phân hệ
│   ├── admin/               # Quản trị (thống kê, voucher, khuyến mãi...)
│   ├── auth/                # Xác thực & phân quyền
│   ├── client/              # Phía người dùng (giỏ hàng, đơn hàng...)
│   ├── model/               # Entity: Customer, Staff, ...
│   └── ...
├── infrastructure/          # Hạ tầng: config, security, email, util
│   ├── config/              # CORS, WebSocket config
│   ├── security/            # JWT, Spring Security
│   ├── email/               # Gửi email
│   └── util/
└── DatnBackendSpringApplication.java
src/main/resources/
└── application.properties   # Cấu hình (DB, JWT, mail, ZaloPay...)
```

## ✅ Yêu cầu cài đặt

- **JDK 17**
- **Maven** 3.9+ (hoặc dùng `./mvnw`)
- **MySQL 8.0** (đang chạy trên `localhost:3306`)
- **Redis** (tuỳ chọn, dùng cho cache/session)

## 🚀 Chạy dự án

### Cách 1 — Chạy trực tiếp (Maven)

```bash
# Windows
mvnw.cmd clean package -DskipTests
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw clean package -DskipTests
./mvnw spring-boot:run
```

Backend chạy tại: **http://localhost:8080**

### Cách 2 — Chạy bằng Docker (đầy đủ MySQL + Redis + App)

```bash
# Đảm bảo Docker đã cài, rồi trong thư mục gốc dự án:
docker compose up -d --build
```

Docker Compose khởi động 3 thành phần:
| Thành phần | Bên trong | Cổng Host |
|-----------|-----------|-----------|
| `app` (Spring Boot) | 8080 | `8080` |
| `db` (MySQL 8.0) | 3306 | `3307` |
| `redis` (Redis 7.2) | 6379 | `6379` |

## ⚙️ Cấu hình (application.properties)

Các giá trị cần thiết nằm trong `src/main/resources/application.properties`:

| Cấu hình                                              | Mô tả                                                 |
| ----------------------------------------------------- | ----------------------------------------------------- |
| `spring.datasource.url`                               | URL kết nối MySQL                                     |
| `spring.datasource.username/password`                 | Tài khoản DB                                          |
| `security.jwt.secret-key` / `expiration`              | Khoá & thời hạn JWT                                   |
| `client.domain`                                       | Domain frontend cho phép (vd `http://localhost:3000`) |
| `spring.mail.*`                                       | Cấu hình SMTP gửi email                               |
| `zalopay.*`                                           | Cấu hình ZaloPay (sandbox)                            |
| `spring.security.oauth2.client.registration.google.*` | Google OAuth2                                         |

> ⚠️ **Bảo mật:** Không commit các giá trị bí mật thật (JWT key, mật khẩu SMTP, API key,...). Khi deploy lên cloud nên đưa vào **biến môi trường** (`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `SECURITY_JWT_SECRET_KEY`, `SPRING_MAIL_USERNAME`, `SPRING_MAIL_PASSWORD`, `ZALOPAY_KEY1`, `ZALOPAY_KEY2`, `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`, `CLIENT_DOMAIN`...).

## 📡 Gọi API

- Server chạy mặc định tại: `http://localhost:8080`
- Một số endpoint cần header: `Authorization: Bearer <token>`
- CORS đang cho phép mọi origin (`allowedOrigins("*")`).

## 🔗 Repo

- Backend: `https://github.com/Namluu2003/e-commerce_backend`
