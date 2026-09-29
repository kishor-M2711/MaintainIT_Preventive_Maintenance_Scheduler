# MaintainIt — Preventive Maintenance Scheduler for Equipment

Spring Boot backend based on the supplied project brief.

## What it does
- Registers machines with maintenance intervals in hours or days.
- Logs machine usage hours.
- Auto-generates a preventive maintenance task when the machine reaches 80% of its interval.
- Does not generate another OPEN task while one is already open for that machine.
- Lets a technician complete a task with notes.
- Completion resets the usage baseline and maintenance date.
- Lists open and overdue tasks.
- Includes validation and a global exception handler.

## Stack
Java 17, Spring Boot 4.1.1, Spring Web, Spring Data JPA, Validation, MySQL, Maven.

## Run
1. Create database: `CREATE DATABASE maintainit;`
2. Edit `src/main/resources/application.properties` and replace `YOUR_MYSQL_PASSWORD`.
3. In the folder containing `pom.xml`, run:

```powershell
mvn clean
mvn spring-boot:run
```

Server: `http://localhost:8080`

## Main APIs
- POST/GET/PUT/DELETE `/api/machines`
- POST/GET `/api/technicians`
- POST/GET `/api/usage-logs/machine/{machineId}`
- GET `/api/maintenance-tasks`
- GET `/api/maintenance-tasks/open`
- GET `/api/maintenance-tasks/overdue`
- GET `/api/maintenance-tasks/machine/{machineId}`
- POST `/api/maintenance-tasks`
- PUT `/api/maintenance-tasks/{taskId}/complete`

## Sample machine JSON
```json
{
  "name": "CNC Machine",
  "model": "CNC-X100",
  "maintenanceIntervalType": "HOURS",
  "maintenanceIntervalValue": 100,
  "currentUsageHours": 0,
  "usageHoursAtLastMaintenance": 0
}
```

## Sample technician
```json
{ "name": "Arun Technician", "email": "arun@example.com", "phone": "9876543210" }
```

## Sample usage log
POST `/api/usage-logs/machine/1`
```json
{ "hoursUsed": 85 }
```
At 85 hours on a 100-hour interval, an OPEN preventive-maintenance task is automatically created.

## Complete task
PUT `/api/maintenance-tasks/1/complete`
```json
{ "technicianId": 1, "notes": "Hydraulic oil replaced and machine inspected." }
```

## Project structure
```text
maintainit/
├── pom.xml
├── database.sql
├── README.md
└── src/main/java/com/example/maintainit/
    ├── controller
    ├── entity
    ├── exception
    ├── repository
    └── service
```


## Web UI

The project includes a simple dark-green and mint web UI under:

`src/main/resources/static/`

After starting the application, open:

`http://localhost:8080/`

The UI connects to the REST APIs in the same Spring Boot application.
