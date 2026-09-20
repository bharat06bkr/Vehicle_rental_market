# Vehicle Rental Marketplace - Full-Stack Web Application

A peer-to-peer **Vehicle Rental Marketplace** web application built using **Spring Boot 3 (Java 17)**, **Spring Data JPA & Hibernate**, **MySQL**, **Spring Security with JWT**, and a **Vanilla HTML5/CSS3/JavaScript (Fetch API)** frontend.

Designed specifically for **Java Backend Developer / SDE interviews, campus placement preparation, and technical portfolio demonstrations**.

---

## Technical Architecture & Core Concept

```text
[ Vanilla Frontend: HTML5 / CSS3 / JS Fetch API ]
                       │
             HTTP REST Requests (JSON)
                       │
                       ▼
         [ Controller Layer (@RestController) ]
                       │
          [ Service Layer (@Service) ] ── (DTOs & Jakarta Validation)
                       │
         [ Repository Layer (@Repository) ]
                       │
       [ JPA Layer (Spring Data JPA Specifications) ]
                       │
         [ ORM Engine (Hibernate 6 Implementation) ]
                       │
             [ JDBC Driver (MySQL Connector) ]
                       │
           [ Database Engine (MySQL / H2) ]
```

---

## 1. Deep Dive: Data Access Pipeline

In this project, data flows through 5 distinct technical abstractions:

```text
Java Entity ──> JPA ──> Hibernate ──> JDBC ──> MySQL
```

1. **Java Entity**: A POJO (e.g., `Vehicle.java`) annotated with `@Entity`, `@Table`, `@ManyToOne`, `@OneToMany`. It represents domain data in object-oriented memory.
2. **JPA (Jakarta Persistence API)**: Standard Java specification defining annotations and interfaces (`EntityManager`, `@Column`, `@JoinColumn`). JPA specifies *what* data mapping looks like without implementing the SQL translation itself.
3. **Hibernate**: The ORM framework implementing JPA. Hibernate parses entity mappings, tracks object dirty states, manages the persistence context (L1 cache), and generates dialect-specific SQL (JPQL/HQL to MySQL SQL).
4. **JDBC (Java Database Connectivity)**: Low-level Java driver (`com.mysql.cj.jdbc.Driver`). Opens TCP network sockets to MySQL on port 3306, binds parameterized SQL queries (`PreparedStatement`), and maps returned SQL `ResultSet` rows back to Hibernate objects.
5. **MySQL**: Relational Database Management System storing data on disk in tabular formats with ACID transactional integrity, foreign key constraints, and B-Tree indexes.

---

## 2. Key Business Rules & State Logic

### Dual-Status Vehicle Design
A vehicle contains two distinct status enumerations:
- **`approvalStatus`** (`PENDING`, `APPROVED`, `REJECTED`): Administrative validity. Dictates whether the listing complies with platform rules. Controlled exclusively by `ADMIN`.
- **`availabilityStatus`** (`AVAILABLE`, `BOOKED`, `RENTED`, `MAINTENANCE`, `INACTIVE`): Operational/temporal readiness. Dictates whether the car is physically free right now. Controlled by `OWNER` & the Booking Engine.

*Why Separate?* An approved car can be currently out on a trip (`APPROVED` + `RENTED`). Conversely, a newly added car cannot be rented by customers even if physically free (`PENDING` + `AVAILABLE`).

---

### Date-Overlap Prevention Algorithm
To prevent double bookings, `BookingRepository.java` evaluates requested rental dates against all active bookings (`PENDING`, `CONFIRMED`, `ACTIVE`):

$$\text{Overlap} \iff (\text{RequestedPickup} \le \text{ExistingReturn}) \land (\text{RequestedReturn} \ge \text{ExistingPickup})$$

```sql
SELECT COUNT(b) > 0 FROM Booking b 
WHERE b.vehicle.id = :vehicleId 
  AND b.status IN ('PENDING', 'CONFIRMED', 'ACTIVE')
  AND (b.pickupDate <= :returnDate AND b.returnDate >= :pickupDate)
```

---

### Strict Booking State Machine

```text
               ┌──────────> REJECTED (Owner Rejects)
               │
PENDING ───────┼──────────> CANCELLED (Customer Cancels)
               │
               ▼
           CONFIRMED ─────> CANCELLED (Customer Cancels)
               │
               ▼
            ACTIVE (Pickup Inspection Handover Recorded)
               │
               ▼
           COMPLETED (Return Inspection Recorded) ──> Eligible for Review
```

---

## 3. REST API Reference

### Auth Endpoints
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/auth/register` | Public | Register new Customer or Owner |
| `POST` | `/api/auth/login` | Public | Authenticate user & return JWT token |

### Public Vehicle Discovery
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/vehicles` | Public | Search approved & available vehicles with filters |
| `GET` | `/api/vehicles/{id}` | Public | Get detailed vehicle specifications & average rating |
| `GET` | `/api/vehicles/{id}/reviews` | Public | Get customer reviews for a vehicle |

### Customer Operations
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/customer/bookings` | CUSTOMER | Create booking request (backend computes total price) |
| `GET` | `/api/customer/bookings` | CUSTOMER | List customer rental bookings |
| `PUT` | `/api/customer/bookings/{id}/cancel` | CUSTOMER | Cancel pending or confirmed booking |
| `POST` | `/api/customer/reviews` | CUSTOMER | Submit rating & review for completed booking |

### Owner Operations
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/owner/vehicles` | OWNER | List new vehicle (starts as PENDING_APPROVAL) |
| `GET` | `/api/owner/vehicles` | OWNER | List owner's vehicle listings & approval status |
| `GET` | `/api/owner/bookings` | OWNER | View incoming booking requests |
| `PUT` | `/api/owner/bookings/{id}/accept` | OWNER | Accept booking request |
| `PUT` | `/api/owner/bookings/{id}/reject` | OWNER | Reject booking request |
| `POST` | `/api/owner/bookings/{id}/pickup` | OWNER | Record pickup odometer & mark status ACTIVE |
| `POST` | `/api/owner/bookings/{id}/return` | OWNER | Record return inspection & mark COMPLETED |

### Admin Operations
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/admin/users` | ADMIN | View all platform users |
| `PUT` | `/api/admin/owners/{id}/status` | ADMIN | Approve, reject, or suspend owner accounts |
| `GET` | `/api/admin/vehicles/pending` | ADMIN | View pending vehicle listings |
| `PUT` | `/api/admin/vehicles/{id}/approve` | ADMIN | Approve vehicle listing |
| `PUT` | `/api/admin/vehicles/{id}/reject` | ADMIN | Reject vehicle listing with reason |
| `GET` | `/api/admin/dashboard` | ADMIN | Fetch platform oversight statistics |

---

## 4. How to Run & Switch Databases (H2 vs MySQL)

The application uses **Spring Profiles** allowing seamless switching between zero-config **H2 In-Memory Database** (with `/h2-console` enabled) and **MySQL Database**.

### Option A: Run with H2 In-Memory Database + H2 Web Console (Default)
In `src/main/resources/application.properties`:
```properties
spring.profiles.active=h2
```
When running in `h2` mode:
- No database installation required (runs zero-config in RAM).
- Access the interactive **H2 Web Console** in your browser:
  - **URL**: `http://localhost:8080/h2-console`
  - **JDBC URL**: `jdbc:h2:mem:vehiclerental_db`
  - **User**: `sa`
  - **Password**: *(leave blank)*

---

### Option B: Run with MySQL Database
1. Open MySQL Workbench or terminal and create the database:
   ```sql
   CREATE DATABASE vehiclerental_db;
   ```
2. In `src/main/resources/application.properties`, switch profile to `mysql`:
   ```properties
   spring.profiles.active=mysql
   ```
3. Update `src/main/resources/application-mysql.properties` if your local MySQL credentials differ from `root`/`root`.

---

### Launching the Application
```bash
mvn clean package
java -jar target/vehicle-rental-marketplace-1.0.0.jar
```
Or pass the active profile directly via CLI flag:
```bash
# Run with H2 Profile
java -jar target/vehicle-rental-marketplace-1.0.0.jar --spring.profiles.active=h2

# Run with MySQL Profile
java -jar target/vehicle-rental-marketplace-1.0.0.jar --spring.profiles.active=mysql
```

Access the application in your browser: `http://localhost:8080`

### Default Demo Logins (Auto-Seeded on Startup)
- **Admin**: `admin@vehiclerental.com` / `admin123`
- **Approved Owner**: `owner@vehiclerental.com` / `owner123`
- **Customer**: `customer@vehiclerental.com` / `customer123`

---

## 5. Git Commit Workflow Recommendation

```text
git init
git add .
git commit -m "Initial Spring Boot setup with Maven dependencies"
git commit -m "Configure MySQL database connection and Hibernate JPA properties"
git commit -m "Create domain Entities, Enums, and Repository interfaces"
git commit -m "Implement Spring Security 6 with JWT authentication & BCrypt password hashing"
git commit -m "Implement Admin module for owner verification, vehicle approval, and stats"
git commit -m "Implement Owner module for listing vehicles, booking accept/reject, pickup & return handover"
git commit -m "Implement Customer module with date-overlap booking validation and server price calculation"
git commit -m "Implement Review system for completed bookings"
git commit -m "Build Vanilla HTML5/CSS3/JS Fetch API frontend interfaces"
```
