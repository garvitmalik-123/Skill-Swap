<div align="center">

# 🤝 SkillSwap

### *A peer-to-peer learning and skill-exchange platform where users can both teach and learn, create courses, exchange skills, earn SkillPoints, and build reputation.*

![Status](https://img.shields.io/badge/backend-Phase%201--22%20Complete-brightgreen)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F)
![MongoDB](https://img.shields.io/badge/MongoDB-Atlas%20%2F%20Local-47A248)
![License](https://img.shields.io/badge/license-Educational-blue)

</div>

---

## 👨‍💻 Author

**Garvit Malik**  •  **Shivika**

---

## 📌 About the Project

**SkillSwap** is a full-stack peer-to-peer learning platform that reimagines online education as a two-way exchange rather than a one-way marketplace.

Most e-learning platforms lock people into a fixed role — you're either an instructor selling courses or a student buying them. SkillSwap removes that wall entirely. Every account is **unified**: the same person can publish a course on Java in the morning and book a UI/UX session as a learner in the evening, all without switching roles or accounts.

At the center of the platform is an internal currency called **SkillPoints**, which lets members pay for learning with their own knowledge instead of always reaching for a credit card — teach a class, help someone out, or contribute to the community, and you earn points you can spend elsewhere on the platform. Alongside SkillPoints, SkillSwap also supports a **skill-matching engine** that pairs people whose "I can teach X" complements someone else's "I want to learn X," turning the platform into a genuine two-way exchange rather than just a course catalog.

The backend is built as a production-style Spring Boot + MongoDB REST API, with JWT-based authentication, role-based authorization, atomic and idempotent SkillPoint/wallet transactions, and full test coverage — designed to be robust enough to plug a real frontend into and eventually deploy.

Unlike traditional learning platforms, a single normal user account can act as both a **learner and a teacher**.

**Users can:**

| | |
|---|---|
| 👤 | Create and manage their profiles |
| 🎓 | Add skills they can teach & skills they want to learn |
| 📚 | Create and enroll in courses |
| 📈 | Track learning progress |
| 💎 | Earn and spend SkillPoints |
| 🔁 | Exchange skills directly with other users |
| 📅 | Book skill sessions |
| ⭐ | Review courses and users |
| 🔔 | Receive notifications |
| 🏆 | Build their reputation |

The backend provides secure REST APIs, business logic, authentication, authorization, MongoDB persistence, course management, SkillPoints, wallet records, payments, skill exchange, reviews, notifications, and Admin operations.

---

## 🎯 Project Goals

- Build a real-world peer-to-peer learning platform
- Allow users to both teach and learn using one account
- Provide skill-based matching and exchange
- Support free, SkillPoint-based, and paid courses
- Implement secure JWT authentication
- Build an auditable SkillPoint and wallet system
- Provide scalable REST APIs
- Practice professional Git/GitHub collaboration
- Build a modern React frontend after completing the backend
- Deploy the complete application

---

## 🛠️ Tech Stack

<table>
<tr>
<td valign="top">

**Backend**
- Java 21
- Spring Boot 3.x
- Spring Web / REST
- Spring Security
- JWT Authentication
- Spring Data MongoDB
- MongoDB Atlas / Local MongoDB
- Maven
- Bean Validation
- Lombok

</td>
<td valign="top">

**Frontend**
- React
- Vite
- JavaScript
- Axios
- React Router
- Tailwind CSS

</td>
<td valign="top">

**API & Testing**
- Swagger / OpenAPI
- Postman
- JUnit
- Mockito

**DevOps / Tools**
- Git & GitHub
- Docker
- MongoDB Atlas
- Optional MinIO / Cloud Storage

</td>
</tr>
</table>

> The backend specification defines Java 21, Spring Boot 3.x, Spring Security, JWT, Spring Data MongoDB, Maven, Bean Validation, and Git/GitHub as the core technology stack.

---

## 🏗️ System Architecture

```text
                         ┌───────────────────┐
                         │   React Frontend  │
                         └─────────┬─────────┘
                                   │
                              HTTP / JSON
                                   │
                         ┌─────────▼─────────┐
                         │   Spring Boot     │
                         │    REST API       │
                         └─────────┬─────────┘
                                   │
                ┌──────────────────┼──────────────────┐
                │                  │                  │
        ┌───────▼───────┐  ┌──────▼──────┐  ┌──────▼──────┐
        │ Spring Security│  │ Controllers │  │   Services  │
        │     + JWT      │  │             │  │             │
        └───────────────┘  └─────────────┘  └──────┬──────┘
                                                   │
                                            ┌──────▼──────┐
                                            │ Repositories│
                                            └──────┬──────┘
                                                   │
                                            ┌──────▼──────┐
                                            │   MongoDB   │
                                            └─────────────┘
```

> The backend follows a **Controller → Service → Repository → MongoDB** architecture, with DTOs used for API requests and responses instead of exposing database documents directly.

---

## 👥 Team Collaboration

This project is developed by **2 team members** using GitHub for version control, branch management, Pull Requests, code reviews, and issue tracking.

### Branch Strategy

```text
main
 │
 └── develop
      │
      ├── feature/project-setup      ✅ merged
      ├── feature/auth-service       ✅ merged
      ├── feature/profile-skills     ✅ merged
      ├── feature/categories         ✅ merged
      ├── feature/courses            ✅ merged
      ├── feature/course-content     ✅ merged
      ├── feature/course-discovery   ✅ merged
      ├── feature/enrollment         ✅ merged
      ├── feature/skillpoints        ✅ merged
      ├── feature/wallet-payment     ✅ merged
      ├── feature/reviews            ✅ merged
      ├── feature/skill-exchange     ✅ merged
      ├── feature/skill-sessions     ✅ merged
      ├── feature/wishlist           ✅ merged
      ├── feature/notifications      ✅ merged
      ├── feature/admin              ✅ merged
      ├── feature/moderation         ✅ merged
      ├── feature/messaging          ✅ merged
      ├── feature/testing            ✅ merged
      └── feature/swagger-docs       ✅ merged
```

### Git Rules

- `main` → stable production-ready code
- `develop` → integration branch
- `feature/*` → individual feature development
- No direct push to `main`
- Features are merged through Pull Requests
- Pull Requests should be reviewed before merging
- Pull from `develop` before starting new work

---

## 🗺️ Development Roadmap

The project follows a **backend-first development strategy**, based on the SkillSwap Backend PRD.

> ### 📍 Current Status
> **Phases 1–22 complete.** The entire backend — authentication, profiles, courses, enrollment, certificates, SkillPoints, wallet & payments, reviews, skill exchange, sessions, wishlist, notifications, admin, moderation, messaging, testing, and Swagger documentation — is done. Next up: **Frontend Development (Phase 23)**.

<details>
<summary><b>Phase 1 — Project Initialization ⚙️ ✅</b></summary>

- [x] Create GitHub repository
- [x] Add teammate as collaborator
- [x] Create `develop` branch
- [x] Create Spring Boot project
- [x] Configure Maven
- [x] Add dependencies
- [x] Create package structure
- [x] Configure environment variables
- [x] Create `.gitignore`

**Branch:** `feature/project-setup`
</details>

<details>
<summary><b>Phase 2 — MongoDB Configuration 🗄️ ✅</b></summary>

- [x] Configure MongoDB
- [x] Create database
- [x] Configure MongoDB connection
- [x] Configure MongoDB repositories
- [x] Add indexes where required
- [x] Test database connection

**Branch:** `feature/project-setup`

MongoDB is the primary application database, with indexes planned for frequently searched fields and common access patterns.
</details>

<details>
<summary><b>Phase 3 — Common Backend Infrastructure 🧩 ✅</b></summary>

- [x] DTO structure
- [x] Common API response
- [x] Error response structure
- [x] Global exception handler
- [x] Validation
- [x] Custom exceptions
- [x] Common configuration
- [x] Logging

**Branch:** `feature/project-setup`
</details>

<details>
<summary><b>Phase 4 — Authentication & Authorization 🔐 ✅</b></summary>

- [x] User registration
- [x] User login
- [x] BCrypt password hashing
- [x] JWT access token
- [x] JWT validation
- [x] Spring Security configuration
- [x] Role-based authorization
- [x] Logout strategy
- [x] Forgot password
- [x] Reset password
- [x] `/me` endpoint

**Roles:** `USER`, `ADMIN` — a single `USER` account can both create courses and enroll in courses; a separate `STUDENT` or `INSTRUCTOR` role is not required.

**Branch:** `feature/auth-service`

**APIs:**
```text
POST /api/auth/register
POST /api/auth/login
POST /api/auth/forgot-password
POST /api/auth/reset-password
GET  /api/auth/me
```
</details>

<details>
<summary><b>Phase 5 — User Profile & Skills 👤 ✅</b></summary>

**User Profile:** profile, update profile, profile image reference, bio, location, experience level, account status, timestamps — all ✅

**Skill Management:** create/manage skills, teaching skills, learning interests, remove skills, prevent duplicates, categories, levels — all ✅

The PRD defines user relationships with skills using `CAN_TEACH` and `WANTS_TO_LEARN`.

**Branch:** `feature/profile-skills`

**APIs:**
```text
GET    /api/users/{id}
PUT    /api/users/me
GET    /api/users/me/skills
POST   /api/users/me/skills
DELETE /api/users/me/skills/{skillId}
GET    /api/skills
GET    /api/categories
```
</details>

<details>
<summary><b>Phase 6 — Course Management 📚 ✅</b></summary>

**Course Types:** `FREE` · `SKILLPOINT` · `PAID`

All CRUD, description, category, skills, difficulty, language, duration, learning objectives, prerequisites, thumbnail, status, publish/archive — ✅

**Course Lifecycle:**
```text
DRAFT → PUBLISHED → ARCHIVED
```
Only published courses can be enrolled in, and only the course creator can modify their own course.

**Branch:** `feature/courses`

**APIs:**
```text
POST   /api/courses
GET    /api/courses
GET    /api/courses/{id}
PUT    /api/courses/{id}
DELETE /api/courses/{id}
POST   /api/courses/{id}/publish
POST   /api/courses/{id}/archive
```
</details>

<details>
<summary><b>Phase 7 — Course Lessons & Resources 📖 ✅</b></summary>

Create/update/delete lessons, ordering, content, video reference, PDF/resource reference, published status — ✅
Optional quizzes/assignments — ⬜ (not yet implemented)

> Large files should use object storage such as MinIO or cloud storage instead of being stored directly inside MongoDB.

**Branch:** `feature/course-content`

**APIs:**
```text
POST   /api/courses/{courseId}/lessons
GET    /api/courses/{courseId}/lessons
PUT    /api/courses/{courseId}/lessons/{lessonId}
DELETE /api/courses/{courseId}/lessons/{lessonId}
PUT    /api/courses/{courseId}/lessons/{lessonId}/reorder
POST   /api/courses/{courseId}/lessons/{lessonId}/publish
```
</details>

<details>
<summary><b>Phase 8 — Course Discovery 🔎 ✅</b></summary>

Search by keyword, skill, category, creator; filter by type, price, rating, difficulty, language, duration; sorting; pagination — all ✅

**Branch:** `feature/course-discovery`

**APIs:**
```text
GET /api/courses/search
```
</details>

<details>
<summary><b>Phase 9 — Enrollment & Learning Progress 🎓 ✅</b></summary>

**Enrollment:** free/SkillPoint/paid enrollment, duplicate prevention, status, timestamps — ✅

**Learning Progress:** completed lessons, progress calculation, completion detection, quiz/assignment results, certificate eligibility — ✅

> For SkillPoint and paid courses, the backend verifies the required transaction before granting course access.

**Branch:** `feature/enrollment`
</details>

<details>
<summary><b>Phase 10 — Certificates 🏆 ✅</b></summary>

Certificate ID generation, metadata storage, user/course association, completion date, optional PDF, optional verification — all ✅

**Branch:** `feature/certificates`
</details>

<details>
<summary><b>Phase 11 — SkillPoints 💎 ✅</b></summary>

**Earning:** Teaching Reward · Course Contribution · Community Contribution · Challenges/Rewards

**Spending:** SkillPoint Course Enrollment · Eligible Skill Sessions · Platform Activities

Balance, credit/debit transactions, history, negative-balance prevention, atomic updates, idempotency — all ✅

> Every SkillPoint change is recorded as a transaction rather than simply overwriting the balance.

**Branch:** `feature/skillpoints`

**APIs:**
```text
GET /api/me/skillpoints
GET /api/me/skillpoints/transactions
```
</details>

<details>
<summary><b>Phase 12 — Wallet, Orders & Payments 💰 ✅</b></summary>

**Wallet:** creator wallet, pending/available earnings, transactions, platform commission — ✅
**Orders:** create order, unique order ID, status, history — ✅
**Payments:** create/verify payment, success/failure/cancellation handling, duplicate-callback prevention, sandbox integration — ✅

> The PRD requires server-side payment verification and enrollment only after successful payment.

**Branch:** `feature/wallet-payment`

**APIs:**
```text
POST /api/orders
POST /api/orders/payment-callback
GET  /api/orders/{orderNumber}
```
</details>

<details>
<summary><b>Phase 13 — Reviews & Ratings ⭐ ✅</b></summary>

Course & session reviews, 1–5 rating, written review, unauthorized/duplicate review prevention, review reporting, rating aggregation — all ✅

**Branch:** `feature/reviews`

**APIs:**
```text
POST   /api/courses/{id}/reviews
GET    /api/courses/{id}/reviews
PUT    /api/reviews/{id}
DELETE /api/reviews/{id}
```
</details>

<details>
<summary><b>Phase 14 — Skill Exchange & Matching 🤝 ✅</b></summary>

```text
User A                          User B
CAN_TEACH: Java                 CAN_TEACH: UI/UX
WANTS_TO_LEARN: UI/UX           WANTS_TO_LEARN: Java
              └──────► Potential Match 🤝 ◄──────┘
```

Find compatible users, rule-based matching, compatibility calculation, send/accept/reject requests, complete exchange, post-exchange review — all ✅

> The MVP uses rule-based matching; optional factors include experience, language, availability, and rating. Cancel-request support is planned as a follow-up refinement.

**Branch:** `feature/skill-exchange`

**APIs:**
```text
GET  /api/skill-exchange/matches
POST /api/skill-exchange/requests
GET  /api/skill-exchange/requests
PUT  /api/skill-exchange/requests/{id}/accept
PUT  /api/skill-exchange/requests/{id}/reject
PUT  /api/skill-exchange/{id}/complete
```
</details>

<details>
<summary><b>Phase 15 — Skill Sessions 📅 ✅</b></summary>

Create session, description, duration, availability, price/SkillPoint cost, browse, book, payment/SkillPoint verification, booking tracking, complete, review — all ✅

**Branch:** `feature/skill-sessions`
</details>

<details>
<summary><b>Phase 16 — Wishlist ❤️ ✅</b></summary>

Add/remove course, view wishlist, duplicate-entry prevention — all ✅

**Branch:** `feature/wishlist`
</details>

<details>
<summary><b>Phase 17 — Notifications 🔔 ✅</b></summary>

**Events:** enrollment, new learner, course completion, new review, SkillPoints earned/spent, payment received, exchange request/response, session booking, admin actions

Create, get, read, mark-all-as-read, server-side read/unread status — all ✅

**Branch:** `feature/notifications`

**APIs:**
```text
GET /api/notifications
PUT /api/notifications/{id}/read
PUT /api/notifications/read-all
```
</details>

<details>
<summary><b>Phase 18 — Admin Management 🛡️ ✅</b></summary>

Manage/suspend/reactivate users, manage courses & skills/categories, monitor transactions, manage reports, moderate content, platform statistics, course approval/rejection — all ✅

**Branch:** `feature/admin`

**APIs:**
```text
GET /api/admin/users
PUT /api/admin/users/{id}/suspend
GET /api/admin/courses
PUT /api/admin/courses/{id}/approve
PUT /api/admin/courses/{id}/reject
GET /api/admin/reports
PUT /api/admin/reports/{id}/resolve
GET /api/admin/transactions
GET /api/admin/analytics
```
</details>

<details>
<summary><b>Phase 19 — Reports & Moderation 🚨 ✅</b></summary>

Users can report a **User, Course, Review, Session,** or **Content**, capturing reporter, target, reason, description, status, and timestamps. Admin actions are auditable.

**Branch:** `feature/moderation`
</details>

<details>
<summary><b>Phase 20 — Advanced Messaging 💬 ✅</b></summary>

Create/retrieve conversations, send/retrieve messages, unread count, conversation authorization, WebSocket support — all ✅

**Branch:** `feature/messaging`
</details>

<details>
<summary><b>Phase 21 — Testing 🧪 ✅</b></summary>

**Unit Testing:** service, controller, repository, security tests — all ✅
**Integration Testing:** auth, course, enrollment, SkillPoint, payment, skill exchange, and admin authorization workflows — all ✅

> The PRD specifically requires testing for authentication, enrollment models, SkillPoints, payment idempotency, skill exchange, and Admin authorization.

**Branch:** `feature/testing`
</details>

<details open>
<summary><b>Phase 22 — Swagger / OpenAPI 📚 ✅</b></summary>

All REST APIs documented and grouped by tag.

**Swagger UI:** `http://localhost:8080/swagger-ui/index.html`

**API Groups:**
```text
Authentication ✅   Users ✅        Skills ✅          Categories ✅
Courses ✅          Enrollment ✅   Progress ✅        SkillPoints ✅
Wallet ✅           Orders ✅       Payments ✅        Reviews ✅
Skill Exchange ✅   Notifications ✅   Admin ✅
```

**Branch:** `feature/swagger-docs`
</details>

<details>
<summary><b>Phase 23 — 🎨 Frontend Development</b> <i>(next up)</i></summary>

**Backend complete hone ke baad frontend start hoga.**

```text
frontend/
├── Authentication      ├── Learning Dashboard
├── Home                ├── SkillPoints
├── Profile             ├── Wallet
├── Skills              ├── Skill Matching
├── Courses             ├── Skill Exchange
├── Course Details      ├── Notifications
├── Enrollment          ├── Reviews
                         └── Admin Dashboard
```

**Frontend Branches:**
```text
feature/frontend-auth        feature/frontend-skillpoints
feature/frontend-profile     feature/frontend-exchange
feature/frontend-skills      feature/frontend-admin
feature/frontend-courses
feature/frontend-learning
```
</details>

<details>
<summary><b>Phase 24 — 🔗 Frontend & Backend Integration</b></summary>

- [ ] Configure Axios
- [ ] Connect authentication APIs & JWT handling
- [ ] Connect profile / skills / course / enrollment APIs
- [ ] Connect SkillPoint / wallet / payment APIs
- [ ] Connect matching / exchange / notification / Admin APIs
- [ ] Handle API errors
- [ ] Add loading states
</details>

<details>
<summary><b>Phase 25 — 🐳 Docker & Deployment</b></summary>

**Backend:** Dockerfile, environment variables, production configuration — ⬜

**Database:** MongoDB Atlas ✅ · Production indexes ⬜ · Secure credentials ✅

**Frontend:** Production build, environment configuration, deployment — ⬜

**Final Architecture:**
```text
React Frontend → Spring Boot API → MongoDB / Atlas
```
</details>

---

## 📂 Backend Project Structure

```text
backend/
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── skillswap/
        │           └── backend/
        │               ├── config/
        │               ├── controller/
        │               ├── dto/
        │               │   ├── request/
        │               │   └── response/
        │               ├── entity/
        │               ├── repository/
        │               ├── service/
        │               │   └── impl/
        │               ├── security/
        │               ├── exception/
        │               └── SkillswapBackendApplication.java
        │
        └── resources/
            └── application.properties
```

---

## 🗄️ MongoDB Collections

```text
users ✅                    certificates ✅              wallets ✅
skills ✅                   reviews ✅                   wallet_transactions ✅
user_skills ✅              skill_exchange_requests ✅   skillpoint_transactions ✅
categories ✅               skill_sessions ✅            orders ✅
courses ✅                                               payments ✅
course_lessons ✅           wishlists ✅
course_enrollments ✅       notifications ✅
course_progress ✅          reports ✅
                            messages ✅ / conversations ✅
```

---

## 🔄 Git Workflow

```bash
# 1. Start from develop
git checkout develop
git pull origin develop

# 2. Create a feature branch
git checkout -b feature/feature-name

# 3. Work on the feature
git status

# 4. Commit
git add .
git commit -m "feat: add feature"

# 5. Push
git push -u origin feature/feature-name
```

```text
feature/feature-name → Pull Request → develop → (after testing) → Pull Request → main
```

---

## 📝 Commit Convention

```text
feat: add user registration
feat: implement JWT authentication
feat: add skill management
feat: implement course CRUD
feat: add course enrollment
feat: implement SkillPoint transactions
feat: add review and rating system
feat: add skill matching and exchange requests

fix: resolve JWT validation issue
fix: prevent duplicate enrollment

test: add authentication tests
test: add skill exchange tests

refactor: improve matching service
docs: update API documentation
```

---

## 📊 Project Milestones

| Milestone | Status | | Milestone | Status |
|---|:---:|---|---|:---:|
| GitHub Setup | ✅ | | Skill Matching | ✅ |
| Spring Boot Setup | ✅ | | Skill Exchange | ✅ |
| MongoDB Setup | ✅ | | Skill Sessions | ✅ |
| Common Configuration | ✅ | | Wishlist | ✅ |
| Authentication | ✅ | | Notifications | ✅ |
| User Profile | ✅ | | Admin | ✅ |
| Skill Management | ✅ | | Moderation | ✅ |
| Categories | ✅ | | Messaging | ✅ |
| Course Management | ✅ | | Testing | ✅ |
| Course Content | ✅ | | Swagger | ✅ |
| Course Discovery | ✅ | | **Frontend** | ⬜ |
| Enrollment | ✅ | | **Integration** | ⬜ |
| Learning Progress | ✅ | | **Docker** | ⬜ |
| Certificates | ✅ | | **Deployment** | ⬜ |
| SkillPoints | ✅ | | | |
| Wallet | ✅ | | | |
| Orders & Payments | ✅ | | | |
| Reviews | ✅ | | | |

**Backend: 22/22 phases complete 🎉 &nbsp;|&nbsp; Overall: 22/25 phases complete**

---

## 🚀 Complete User Flow

```text
Register → Login → Create Profile → Add Teaching/Learning Skills
    → Explore / Create Courses → Enroll → Learn & Progress → Certificate

           SKILL EXCHANGE
Find Match → Send Request → Accept/Reject → Exchange Skills → Complete → Review
```

---

## 🌟 Advanced Features *(post-MVP)*

Real payment gateway · Creator payouts · One-to-one session booking · Real-time WebSocket messaging · MinIO object storage · AI course recommendations · AI skill-gap analysis · Advanced analytics · Gamification · Badges · Audit logs · Advanced moderation

---

## ✅ Backend MVP — Complete

```text
✓ Spring Boot setup              ✓ Wallet & Earnings
✓ MongoDB                        ✓ Reviews & Ratings
✓ JWT Authentication             ✓ Skill Matching / Exchange
✓ Unified User Profile           ✓ Wishlist
✓ Skill Management               ✓ Notifications
✓ Course CRUD                    ✓ Admin Management
✓ FREE / SKILLPOINT / PAID       ✓ Reports & Moderation
✓ Course Search & Filtering      ✓ Advanced Messaging
✓ Enrollment                     ✓ Testing
✓ Learning Progress              ✓ Validation & Exception Handling
✓ Certificates                   ✓ Swagger / OpenAPI
✓ SkillPoints
```

> This corresponds to the full backend MVP scope defined in the Backend PRD (Phases 1–22).

---

## 📄 License

This project is developed for **educational, portfolio, and collaborative software-development purposes**.
