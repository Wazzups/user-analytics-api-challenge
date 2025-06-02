# Challenge: Performance and analytics via API

This repository contains the solution for the “Performance and Data Analysis via API” challenge implemented in Java with Spring Boot. 

Challenge: [Challenge.md](./CHALLENGE.md) 

## 1. Objective

Build a REST API that accepts up to 100,000 users in JSON, stores them in memory, and exposes endpoints to:
- Filter "superusers"(score >= 900 and active = true)
- Retrieve the top 5 countries with the most superusers
- Group statistics by Team (total members, leaders, completed projects, % active)
- Retrieve how many logins occurred per day, with an optional minimum-logins filters
- Provide and evaluation endpoint

## 2. Architecture Overview
1. **Controller Layer**
   - Receive HTTP requests and delegate to the corresponding service.
   - For file uploads ('multipart/form-data), the controller accepts a 'MultipartFile'

2. **Service layer**
    - **User service**:Business logic to import users into in-memory storage, filter superusers. group by country, calculate team statistics, count logins per day
    - **Evaluation service**:Programmatically calls each endpoint (using RestTemplate)

3. **Repository Layer / In-memory storage**
   - Stores all users in a ÇoncurrentHashMap<UUID, User>'to ensure thread-safety and O(1) access.
   
4. **Interceptor for Timing**
   - Implements 'HandlerInterceptor'. In 'preHandle()' it stores 'startTime'and 'requestTimeStamp'. In 'postHandle()', it calculates elapsed time and adds these headers to the response.
   
5. **Models (POJOs)**
    - **'User'**, **'Team'**, **'Project'**, **'LogEntry'**, **'TeamInsight'**, **'EvaluationResult'**

## 3. Implementation Decisions
1. **Using 'ConcurrentHashMap' instead of using a DB in memory like H2** 
    - Avoid creating JPA entities, DDL scripts, managing a datasource, etc.
    - Enables filtering and grouping directly with Java Streams in pure memory.

2. **Timing via Interceptor**

3. **OpenAPI Documentation**

---

## 4. How to Run locally

### Prerequisites 

- Java 11+
- Maven 3.6+
- (Optional) 'curl', Postman or Insomnia


```bash
git clone https://github.com/your-username/user-analytics-api.git
cd user-analytics-api

# Compile and package
mvn clean package

# By default app runs on http://localhost:8080.
mvn spring-boot:run
```

### Postman collection [userApi.json](./usersApi.postman_collection.json)


## 5. Bugs
- The `postHandle` of `TimingInterceptor` normally runs before Spring MVC writes the JSON, allowing header injection.
- The response is committed too early, so the header aren't being injected.
- Solution: a `TimingFilter` that wraps the response to guarantee header injection before any `flushBuffer()`
### 