# 🤝 SkillSwap

> **A peer-to-peer learning and skill-exchange platform where users can both teach and learn, create courses, exchange skills, earn SkillPoints, and build reputation.**

---

## 👨‍💻 Author

**Garvit Malik**

**Shivika**

---

## 📌 About the Project

**SkillSwap** is a full-stack peer-to-peer learning platform.

Unlike traditional learning platforms, a single normal user account can act as both a **learner and a teacher**.

Users can:

* Create and manage their profiles
* Add skills they can teach
* Add skills they want to learn
* Create courses
* Enroll in courses
* Track learning progress
* Earn and spend SkillPoints
* Exchange skills directly with other users
* Book skill sessions
* Send and receive skill-exchange requests
* Review courses and users
* Receive notifications
* Build their reputation

The backend provides secure REST APIs, business logic, authentication, authorization, MongoDB persistence, course management, SkillPoints, wallet records, payments, skill exchange, reviews, notifications, and Admin operations.

---

# 🎯 Project Goals

* Build a real-world peer-to-peer learning platform
* Allow users to both teach and learn using one account
* Provide skill-based matching and exchange
* Support free, SkillPoint-based, and paid courses
* Implement secure JWT authentication
* Build an auditable SkillPoint and wallet system
* Provide scalable REST APIs
* Practice professional Git/GitHub collaboration
* Build a modern React frontend after completing the backend
* Deploy the complete application

---

# 🛠️ Tech Stack

## Backend

* Java 21
* Spring Boot 3.x
* Spring Web / REST
* Spring Security
* JWT Authentication
* Spring Data MongoDB
* MongoDB Atlas / Local MongoDB
* Maven
* Bean Validation
* Lombok

## Frontend

* React
* Vite
* JavaScript
* Axios
* React Router
* Tailwind CSS

## API & Testing

* Swagger / OpenAPI
* Postman
* JUnit
* Mockito

## DevOps / Tools

* Git
* GitHub
* Docker
* MongoDB Atlas
* Optional MinIO / Cloud Object Storage

The backend specification defines Java 17, Spring Boot 3.x, Spring Security, JWT, Spring Data MongoDB, Maven, Bean Validation, and Git/GitHub as the core technology stack.

---

# 🏗️ System Architecture

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

The backend follows a **Controller → Service → Repository → MongoDB** architecture, with DTOs used for API requests and responses instead of exposing database documents directly.

---

# 👥 Team Collaboration

This project is developed by **2 team members**.

GitHub will be used for:

* Version control
* Branch management
* Pull Requests
* Code reviews
* Issue tracking
* Team collaboration

### Branch Strategy

```text
main
 │
 └── develop
      │
      ├── feature/project-setup
      ├── feature/auth
      ├── feature/profile-skills
      ├── feature/categories
      ├── feature/courses
      ├── feature/enrollment
      ├── feature/skillpoints
      ├── feature/wallet-payment
      ├── feature/reviews
      ├── feature/skill-exchange
      ├── feature/notifications
      ├── feature/admin
      └── feature/testing-docs
```

### Git Rules

* `main` → stable production-ready code
* `develop` → integration branch
* `feature/*` → individual feature development
* No direct push to `main`
* Features are merged through Pull Requests
* Pull Requests should be reviewed before merging
* Pull from `develop` before starting new work

---

# 🗺️ Development Roadmap

The project follows a **backend-first development strategy**. The backend development order is based on the SkillSwap Backend PRD.

---

## Phase 1 — Project Initialization ⚙️

### Tasks

* [ ] Create GitHub repository
* [ ] Add teammate as collaborator
* [ ] Create `develop` branch
* [ ] Create Spring Boot project
* [ ] Configure Maven
* [ ] Add dependencies
* [ ] Create package structure
* [ ] Configure environment variables
* [ ] Create `.gitignore`

### Branch

```text
feature/project-setup
```

---

# Phase 2 — MongoDB Configuration 🗄️

### Tasks

* [ ] Configure MongoDB
* [ ] Create database
* [ ] Configure MongoDB connection
* [ ] Configure MongoDB repositories
* [ ] Add indexes where required
* [ ] Test database connection

### Branch

```text
feature/project-setup
```

MongoDB is the primary application database, with indexes planned for frequently searched fields and common access patterns.

---

# Phase 3 — Common Backend Infrastructure 🧩

### Tasks

* [ ] DTO structure
* [ ] Common API response
* [ ] Error response structure
* [ ] Global exception handler
* [ ] Validation
* [ ] Custom exceptions
* [ ] Common configuration
* [ ] Logging

### Branch

```text
feature/project-setup
```

---

# Phase 4 — Authentication & Authorization 🔐

### Features

* [ ] User registration
* [ ] User login
* [ ] BCrypt password hashing
* [ ] JWT access token
* [ ] JWT validation
* [ ] Spring Security configuration
* [ ] Role-based authorization
* [ ] Logout strategy
* [ ] Forgot password
* [ ] Reset password
* [ ] `/me` endpoint

### Roles

```text
USER
ADMIN
```

A single `USER` account can both create courses and enroll in courses; a separate `STUDENT` or `INSTRUCTOR` role is not required.

### Branch

```text
feature/auth
```

### APIs

```text
POST /api/auth/register
POST /api/auth/login
POST /api/auth/forgot-password
POST /api/auth/reset-password
GET  /api/auth/me
```

---

# Phase 5 — User Profile & Skills 👤

## User Profile

* [ ] User profile
* [ ] Update profile
* [ ] Profile image reference
* [ ] Bio
* [ ] Location
* [ ] Experience level
* [ ] Account status
* [ ] Timestamps

## Skill Management

* [ ] Create/manage skills
* [ ] Add teaching skills
* [ ] Add learning interests
* [ ] Remove skills
* [ ] Prevent duplicate relationships
* [ ] Skill categories
* [ ] Skill levels

The PRD defines user relationships with skills using `CAN_TEACH` and `WANTS_TO_LEARN`.

### Branch

```text
feature/profile-skills
```

### APIs

```text
GET    /api/users/{id}
PUT    /api/users/me
GET    /api/users/me/skills
POST   /api/users/me/skills
DELETE /api/users/me/skills/{skillId}

GET    /api/skills
GET    /api/categories
```

---

# Phase 6 — Course Management 📚

Users can create courses that other users can learn from.

### Course Types

```text
FREE
SKILLPOINT
PAID
```

### Features

* [ ] Create course
* [ ] Update course
* [ ] Delete course
* [ ] Course description
* [ ] Category
* [ ] Skills
* [ ] Difficulty
* [ ] Language
* [ ] Duration
* [ ] Learning objectives
* [ ] Prerequisites
* [ ] Thumbnail reference
* [ ] Course status
* [ ] Publish course
* [ ] Archive course

### Course Lifecycle

```text
DRAFT
  ↓
PUBLISHED
  ↓
ARCHIVED
```

Only published courses can be enrolled in, and only the course creator can modify their own course.

### Branch

```text
feature/courses
```

### APIs

```text
POST   /api/courses
GET    /api/courses
GET    /api/courses/{id}
PUT    /api/courses/{id}
DELETE /api/courses/{id}

POST /api/courses/{id}/publish
POST /api/courses/{id}/archive
```

---

# Phase 7 — Course Lessons & Resources 📖

### Features

* [ ] Create lessons
* [ ] Update lessons
* [ ] Delete lessons
* [ ] Lesson ordering
* [ ] Lesson content
* [ ] Video reference
* [ ] PDF/resource reference
* [ ] Published status
* [ ] Optional quizzes/assignments

Large files should use object storage such as MinIO or cloud storage instead of being stored directly inside MongoDB.

### Branch

```text
feature/course-content
```

---

# Phase 8 — Course Discovery 🔎

### Features

* [ ] Search courses
* [ ] Search by skill
* [ ] Search by category
* [ ] Search by creator
* [ ] Filter by course type
* [ ] Filter by price
* [ ] Filter by rating
* [ ] Filter by difficulty
* [ ] Filter by language
* [ ] Filter by duration
* [ ] Sorting
* [ ] Pagination

### Branch

```text
feature/course-discovery
```

The backend specification explicitly requires search, filtering, sorting, and pagination for course discovery.

---

# Phase 9 — Enrollment & Learning Progress 🎓

## Enrollment

* [ ] Free course enrollment
* [ ] SkillPoint course enrollment
* [ ] Paid course enrollment
* [ ] Prevent duplicate enrollment
* [ ] Enrollment status
* [ ] Enrollment timestamps

## Learning Progress

* [ ] Track completed lessons
* [ ] Calculate progress
* [ ] Detect course completion
* [ ] Track quiz/assignment results
* [ ] Certificate eligibility

### Branch

```text
feature/enrollment
```

For SkillPoint and paid courses, the backend must verify the required transaction before granting course access.

---

# Phase 10 — Certificates 🏆

### Features

* [ ] Generate certificate ID
* [ ] Store certificate metadata
* [ ] Associate certificate with user/course
* [ ] Store completion date
* [ ] Optional PDF certificate
* [ ] Optional certificate verification

### Branch

```text
feature/certificates
```

---

# Phase 11 — SkillPoints 💎

SkillPoints are the internal reward currency of SkillSwap.

### Earning

```text
Teaching Reward
Course Contribution
Community Contribution
Challenges / Rewards
```

### Spending

```text
SkillPoint Course Enrollment
Eligible Skill Sessions
Platform Activities
```

### Features

* [ ] SkillPoint balance
* [ ] Credit transaction
* [ ] Debit transaction
* [ ] Transaction history
* [ ] Prevent negative balance
* [ ] Atomic balance updates
* [ ] Idempotency protection

Every SkillPoint change must be recorded as a transaction rather than simply overwriting the balance.

### Branch

```text
feature/skillpoints
```

### APIs

```text
GET /api/me/skillpoints
GET /api/me/skillpoints/transactions
```

---

# Phase 12 — Wallet, Orders & Payments 💰

## Wallet

* [ ] Creator wallet
* [ ] Pending earnings
* [ ] Available earnings
* [ ] Wallet transactions
* [ ] Platform commission

## Orders

* [ ] Create order
* [ ] Unique order ID
* [ ] Order status
* [ ] Order history

## Payments

* [ ] Create payment
* [ ] Verify payment
* [ ] Handle successful payment
* [ ] Handle failed payment
* [ ] Handle cancelled payment
* [ ] Prevent duplicate payment callbacks
* [ ] Test/sandbox payment integration

### Branch

```text
feature/wallet-payment
```

The PRD requires server-side payment verification and enrollment only after successful payment.

---

# Phase 13 — Reviews & Ratings ⭐

### Features

* [ ] Course reviews
* [ ] Session reviews
* [ ] Rating from 1–5
* [ ] Written review
* [ ] Prevent unauthorized reviews
* [ ] Prevent duplicate reviews
* [ ] Review reporting
* [ ] Rating aggregation

### Branch

```text
feature/reviews
```

### APIs

```text
POST   /api/courses/{id}/reviews
GET    /api/courses/{id}/reviews
PUT    /api/reviews/{id}
DELETE /api/reviews/{id}
```

---

# Phase 14 — Skill Exchange & Matching 🤝

This is one of the core features of SkillSwap.

### Example

```text
User A

CAN_TEACH:
Java

WANTS_TO_LEARN:
UI/UX


User B

CAN_TEACH:
UI/UX

WANTS_TO_LEARN:
Java

          ↓

   Potential Match 🤝
```

### Features

* [ ] Find compatible users
* [ ] Rule-based matching
* [ ] Match teaching skills with learning interests
* [ ] Match reciprocal requirements
* [ ] Calculate compatibility
* [ ] Send exchange request
* [ ] Accept request
* [ ] Reject request
* [ ] Cancel request
* [ ] Complete exchange
* [ ] Review after completion

The MVP uses rule-based matching; optional factors include experience, language, availability, and rating.

### Branch

```text
feature/skill-exchange
```

### APIs

```text
GET  /api/skill-exchange/matches
POST /api/skill-exchange/requests
GET  /api/skill-exchange/requests

PUT /api/skill-exchange/requests/{id}/accept
PUT /api/skill-exchange/requests/{id}/reject

PUT /api/skill-exchange/{id}/complete
```

---

# Phase 15 — Skill Sessions 📅

### Features

* [ ] Create skill session
* [ ] Set skill
* [ ] Session description
* [ ] Duration
* [ ] Availability
* [ ] Price / SkillPoint cost
* [ ] Browse sessions
* [ ] Book session
* [ ] Verify payment/SkillPoints
* [ ] Track booking
* [ ] Complete session
* [ ] Review session

### Branch

```text
feature/skill-sessions
```

---

# Phase 16 — Wishlist ❤️

### Features

* [ ] Add course to wishlist
* [ ] Remove course
* [ ] View wishlist
* [ ] Prevent duplicate wishlist entries

### Branch

```text
feature/wishlist
```

---

# Phase 17 — Notifications 🔔

Notifications will be generated for important platform events.

### Events

* Course enrollment
* New learner enrollment
* Course completion
* New review
* SkillPoints earned/spent
* Payment received
* Skill exchange request
* Exchange response
* Session booking
* Admin actions

### Features

* [ ] Create notification
* [ ] Get notifications
* [ ] Read notification
* [ ] Mark all as read
* [ ] Server-side read/unread status

### Branch

```text
feature/notifications
```

### APIs

```text
GET /api/notifications

PUT /api/notifications/{id}/read
PUT /api/notifications/read-all
```

---

# Phase 18 — Admin Management 🛡️

### Admin Features

* [ ] Manage users
* [ ] Suspend users
* [ ] Reactivate users
* [ ] Manage courses
* [ ] Manage skills/categories
* [ ] Monitor transactions
* [ ] Manage reports
* [ ] Moderate content
* [ ] View platform statistics
* [ ] Course approval/rejection

### Branch

```text
feature/admin
```

### APIs

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

---

# Phase 19 — Reports & Moderation 🚨

### Features

Users can report:

```text
User
Course
Review
Session
Content
```

### Report Data

* Reporter
* Target
* Reason
* Description
* Status
* Timestamps

### Branch

```text
feature/moderation
```

Admin actions should be auditable.

---

# Phase 20 — Advanced Messaging 💬

Messaging is an advanced feature.

### Features

* [ ] Create conversations
* [ ] Retrieve conversations
* [ ] Send messages
* [ ] Retrieve messages
* [ ] Unread message count
* [ ] Conversation authorization
* [ ] WebSocket support

### Branch

```text
feature/messaging
```

---

# Phase 21 — Testing 🧪

## Unit Testing

* [ ] Service tests
* [ ] Controller tests
* [ ] Repository tests
* [ ] Security tests

## Integration Testing

* [ ] Authentication workflow
* [ ] Course workflow
* [ ] Enrollment workflow
* [ ] SkillPoint transactions
* [ ] Payment workflow
* [ ] Skill exchange workflow
* [ ] Admin authorization

The PRD specifically requires testing for authentication, enrollment models, SkillPoints, payment idempotency, skill exchange, and Admin authorization.

### Branch

```text
feature/testing
```

---

# Phase 22 — Swagger / OpenAPI 📚

Document all REST APIs.

### Swagger

```text
http://localhost:8080/swagger-ui/index.html
```

### API Groups

```text
Authentication
Users
Skills
Categories
Courses
Enrollment
Progress
SkillPoints
Wallet
Orders
Payments
Reviews
Skill Exchange
Notifications
Admin
```

---

# 🎨 Phase 23 — Frontend Development

**Backend complete hone ke baad frontend start hoga.**

### Frontend Modules

```text
frontend/
│
├── Authentication
├── Home
├── Profile
├── Skills
├── Courses
├── Course Details
├── Enrollment
├── Learning Dashboard
├── SkillPoints
├── Wallet
├── Skill Matching
├── Skill Exchange
├── Notifications
├── Reviews
└── Admin Dashboard
```

### Frontend Branches

```text
feature/frontend-auth
feature/frontend-profile
feature/frontend-skills
feature/frontend-courses
feature/frontend-learning
feature/frontend-skillpoints
feature/frontend-exchange
feature/frontend-admin
```

---

# 🔗 Phase 24 — Frontend & Backend Integration

### Tasks

* [ ] Configure Axios
* [ ] Connect authentication APIs
* [ ] JWT handling
* [ ] Connect profile APIs
* [ ] Connect skills APIs
* [ ] Connect course APIs
* [ ] Connect enrollment APIs
* [ ] Connect SkillPoint APIs
* [ ] Connect wallet/payment APIs
* [ ] Connect matching APIs
* [ ] Connect exchange APIs
* [ ] Connect notification APIs
* [ ] Connect Admin APIs
* [ ] Handle API errors
* [ ] Add loading states

---

# 🐳 Phase 25 — Docker & Deployment

### Backend

* [ ] Dockerfile
* [ ] Environment variables
* [ ] Production configuration

### Database

* [ ] MongoDB Atlas
* [ ] Production indexes
* [ ] Secure credentials

### Frontend

* [ ] Production build
* [ ] Environment configuration
* [ ] Deploy frontend

### Final Architecture

```text
                    ┌──────────────────┐
                    │ React Frontend   │
                    └────────┬─────────┘
                             │
                             ▼
                    ┌──────────────────┐
                    │ Spring Boot API  │
                    └────────┬─────────┘
                             │
                             ▼
                    ┌──────────────────┐
                    │ MongoDB / Atlas  │
                    └──────────────────┘
```

---

# 📂 Backend Project Structure

```text
backend/
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── skillswap/
        │           ├── config/
        │           ├── controller/
        │           ├── dto/
        │           ├── entity/
        │           ├── repository/
        │           ├── service/
        │           ├── security/
        │           ├── exception/
        │           └── SkillSwapApplication.java
        │
        └── resources/
            ├── application.properties
            └── application-dev.properties
```

---

# 🗄️ MongoDB Collections

The backend PRD proposes collections including:

```text
users
skills
user_skills
categories

courses
course_lessons
course_enrollments
course_progress

certificates
reviews

skill_exchanges
skill_exchange_requests
skill_sessions

wallets
wallet_transactions
skillpoint_transactions

orders
payments
wishlists

notifications
reports

messages
conversations
```

---

# 🔄 Git Workflow

### 1. Start from develop

```bash
git checkout develop
git pull origin develop
```

### 2. Create feature branch

```bash
git checkout -b feature/feature-name
```

### 3. Work on the feature

```bash
git status
```

### 4. Commit

```bash
git add .
git commit -m "feat: add feature"
```

### 5. Push

```bash
git push -u origin feature/feature-name
```

### 6. Create Pull Request

```text
feature/feature-name
        ↓
   Pull Request
        ↓
      develop
```

### 7. After testing

```text
develop
   ↓
Pull Request
   ↓
 main
```

---

# 📝 Commit Convention

Use clear conventional commits:

```text
feat: add user registration
feat: implement JWT authentication
feat: add skill management
feat: implement course CRUD
feat: add course enrollment
feat: implement SkillPoint transactions

fix: resolve JWT validation issue
fix: prevent duplicate enrollment

test: add authentication tests
test: add skill exchange tests

refactor: improve matching service
docs: update API documentation
```

---

# 📊 Project Milestones

| Milestone            | Status |
| -------------------- | ------ |
| GitHub Setup         | ⬜      |
| Spring Boot Setup    | ⬜      |
| MongoDB Setup        | ⬜      |
| Common Configuration | ⬜      |
| Authentication       | ⬜      |
| User Profile         | ⬜      |
| Skill Management     | ⬜      |
| Categories           | ⬜      |
| Course Management    | ⬜      |
| Course Content       | ⬜      |
| Course Discovery     | ⬜      |
| Enrollment           | ⬜      |
| Learning Progress    | ⬜      |
| Certificates         | ⬜      |
| SkillPoints          | ⬜      |
| Wallet               | ⬜      |
| Orders & Payments    | ⬜      |
| Reviews              | ⬜      |
| Skill Matching       | ⬜      |
| Skill Exchange       | ⬜      |
| Skill Sessions       | ⬜      |
| Wishlist             | ⬜      |
| Notifications        | ⬜      |
| Admin                | ⬜      |
| Moderation           | ⬜      |
| Testing              | ⬜      |
| Swagger              | ⬜      |
| Frontend             | ⬜      |
| Integration          | ⬜      |
| Docker               | ⬜      |
| Deployment           | ⬜      |

---

# 🚀 Complete User Flow

```text
                    ┌──────────────┐
                    │    Register  │
                    └──────┬───────┘
                           ↓
                    ┌──────────────┐
                    │     Login    │
                    └──────┬───────┘
                           ↓
                    ┌──────────────┐
                    │ Create Profile│
                    └──────┬───────┘
                           ↓
                 ┌────────────────────┐
                 │ Add Teaching/Learn │
                 │      Skills        │
                 └──────────┬─────────┘
                            ↓
              ┌──────────────────────────┐
              │    Explore / Create      │
              │         Courses          │
              └────────────┬─────────────┘
                           ↓
                    ┌──────────────┐
                    │    Enroll    │
                    └──────┬───────┘
                           ↓
                  ┌─────────────────┐
                  │ Learn & Progress│
                  └────────┬────────┘
                           ↓
                    ┌──────────────┐
                    │  Certificate │
                    └──────────────┘


       ┌────────────────────────────────────────┐
       │           SKILL EXCHANGE               │
       └────────────────────┬───────────────────┘
                            ↓
                     Find Match
                            ↓
                  Send Exchange Request
                            ↓
                     Accept / Reject
                            ↓
                    Exchange Skills
                            ↓
                       Complete
                            ↓
                       Review
```

---

# 🌟 Advanced Features

The following features can be added after the core MVP:

* Real payment gateway
* Creator payouts
* One-to-one session booking
* Real-time WebSocket messaging
* MinIO object storage
* AI course recommendations
* AI skill-gap analysis
* Advanced analytics
* Gamification
* Badges
* Audit logs
* Advanced moderation

These are classified as advanced backend features in the PRD.

---

# ✅ Backend MVP

The first complete backend milestone will include:

```text
✓ Spring Boot setup
✓ MongoDB
✓ JWT Authentication
✓ Unified User Profile
✓ Skill Management
✓ Course CRUD
✓ FREE / SKILLPOINT / PAID Courses
✓ Course Search & Filtering
✓ Enrollment
✓ Learning Progress
✓ SkillPoints
✓ Wallet & Earnings
✓ Reviews & Ratings
✓ Skill Matching / Exchange
✓ Notifications
✓ Admin APIs
✓ Validation & Exception Handling
✓ Swagger / OpenAPI
```

This corresponds to the MVP scope defined in the Backend PRD.

---

# 📄 License

This project is developed for **educational, portfolio, and collaborative software-development purposes**.
