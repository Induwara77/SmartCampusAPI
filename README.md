# Smart Campus API

**Module:** 5COSC022W – Client-Server Architectures  
**Technology:** JAX-RS (Jersey 2.32) + Apache Tomcat 9  
**Base URL:** `http://localhost:8080/api/v1`

---

## How to Build and Run

1. Clone the repository
```
git clone https://github.com/Induwara77/SmartCampusAPI.git
```

2. Open the project in **Apache NetBeans**

3. Right-click the project → **Clean and Build**

4. Right-click the project → **Run** (deploys to Apache Tomcat 9)

5. Server starts at `http://localhost:8080`

---

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1` | Discovery endpoint |
| GET | `/api/v1/rooms` | Get all rooms |
| POST | `/api/v1/rooms` | Create a room |
| GET | `/api/v1/rooms/{id}` | Get a room |
| DELETE | `/api/v1/rooms/{id}` | Delete a room |
| GET | `/api/v1/sensors` | Get all sensors |
| GET | `/api/v1/sensors?type=CO2` | Filter sensors by type |
| POST | `/api/v1/sensors` | Create a sensor |
| GET | `/api/v1/sensors/{id}` | Get a sensor |
| GET | `/api/v1/sensors/{id}/readings` | Get sensor readings |
| POST | `/api/v1/sensors/{id}/readings` | Add a reading |

---

## Sample curl Commands

**1. Get API Info**
```bash
curl -X GET http://localhost:8080/api/v1
```

**2. Create a Room**
```bash
curl -X POST http://localhost:8080/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"LIB-301","name":"Library Quiet Study","capacity":50}'
```

**3. Create a Sensor**
```bash
curl -X POST http://localhost:8080/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","currentValue":22.5,"roomId":"LIB-301"}'
```

**4. Filter Sensors by Type**
```bash
curl -X GET "http://localhost:8080/api/v1/sensors?type=Temperature"
```

**5. Add a Sensor Reading**
```bash
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value":25.3}'
```

---

## Report – Question Answers

### Part 1.1 – JAX-RS Resource Lifecycle
By default, JAX-RS creates a **new instance** of a resource class for every incoming HTTP request. This means instance variables are reset on every request. To share data across requests, this project uses a **static DataStore class** with static HashMaps, so all resource instances share the same data. This prevents data loss between requests.

### Part 1.2 – HATEOAS
HATEOAS means the API response includes links to related resources. This helps client developers navigate the API without reading documentation. If URLs change, clients follow the links instead of hardcoded paths, reducing coupling between client and server.

### Part 2.1 – ID Only vs Full Object
Returning only IDs forces the client to make extra requests for each item (N+1 problem), increasing server load. Returning full objects gives the client everything in one request, which is more efficient for most use cases. This API returns full objects by default.

### Part 2.2 – DELETE Idempotency
Yes, DELETE is idempotent. The first call removes the room and returns `200 OK`. If the same DELETE is sent again, the room is already gone so the server returns `404 Not Found`. The server state is the same after both calls — the room does not exist. No extra side effects occur.

### Part 3.1 – @Consumes Mismatch
If a client sends data with `Content-Type: text/plain` to a method annotated with `@Consumes(APPLICATION_JSON)`, JAX-RS automatically returns **HTTP 415 Unsupported Media Type**. The resource method is never called. This protects the API from malformed input.

### Part 3.2 – QueryParam vs PathParam
Using `@QueryParam` for filtering (e.g., `?type=CO2`) is better because query parameters are optional and composable. You can combine multiple filters easily (e.g., `?type=CO2&status=ACTIVE`). Path-based filtering (e.g., `/sensors/type/CO2`) implies a separate resource exists, which is semantically incorrect for filtering.

### Part 4.1 – Sub-Resource Locator Pattern
The sub-resource locator pattern separates concerns into different classes. `SensorResource` handles sensor operations and delegates reading logic to `SensorReadingResource`. This keeps each class small and focused. In large APIs, putting all nested routes in one class makes it hard to maintain and understand.

### Part 5.1 – HTTP 422 vs 404
HTTP 404 means the URL endpoint was not found. HTTP 422 means the endpoint was found but the request body contains invalid data. When a `roomId` inside a JSON payload does not exist, the endpoint itself is working fine — only the referenced data is wrong. So 422 is more accurate and helps clients understand they need to fix their request body, not their URL.

### Part 5.2 – Stack Trace Security Risks
Exposing stack traces reveals internal package names, class names, library versions, and file paths. Attackers can use this to find known vulnerabilities in specific library versions. This project uses a `GlobalExceptionMapper` that catches all unexpected errors and returns a safe generic `500 Internal Server Error` message instead.
