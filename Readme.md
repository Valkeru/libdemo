# Library Management System

A RESTful web application for library management. The system covers the core lifecycle of a library: managing authors, books, and physical book instances, as well as tracking book borrowings.

## Domain model

Library entities are:  
* **Author**
* **Book**
* **Cycle** is several books with a common setting but differs in a plot (like Chronicles of Midkemia by Piers Anthony)
* **Series** is several books with a common setting and characters (like Song of Ice and Fire by George Martin or The Stainless Steel Rat by Harry Harrison)
* **User**
* **BookInstance** describes a particular book copy in the library
* **Token** is JWT token representation, uses for authorization process
* **BookLending** is a representation for book reservation, borrowing and return process
 

## Key Features

- **Authentication & Authorization**: JWT-based authentication with fine-grained Role-Based (RBAC) and Permission-Based (PBAC) access control.
- **Full-Text Search**: Fast book and author search powered by Elasticsearch.
- **Caching**: Performance optimization using Redis.
- **Data Seeding**: Automatic population of test data with the `develop` profile.
- **Logging**: Centralized log collection pipeline with Logstash and Elasticsearch.

## Technologies & Frameworks

- **Spring Boot 4**
- **PostgreSQL**
- **Elasticsearch**
- **Logstash**
- **Kibana**
- **Redis**
- **JWT**

---

## Quick Start

### Running with Docker

```bash
docker-compose up -d
```
Once started, the application and its ecosystem services are accessible:

| Service    | URL                                             |
|------------|-------------------------------------------------|
| Swagger UI | http://localhost:8080/api/swagger-ui/index.html |
| Kibana     | http://localhost:5601                           |

## First Launch

Some test data is created on first application launch if the `develop` Spring profile is set. Authors, books, and book instances are generated automatically, so basic functionality is available by default.

### Initializer Configuration
You can tune the generated author dataset size using the following environment variables:

| Environment Variable                | Description                               | Default value |
|-------------------------------------|-------------------------------------------|---------------|
| DATA_INITIALIZER_AUTHOR_BATCH_SIZE  | Number of authors saved in a single batch | 100           |
| DATA_INITIALIZER_AUTHOR_BATCH_COUNT | Total number of author batches to insert  | 100           |

> **Total authors generated**: `DATA_INITIALIZER_AUTHOR_BATCH_SIZE * DATA_INITIALIZER_AUTHOR_BATCH_COUNT` (10,000 by default).   
**Performance Note**: The maximum recommended batch size is 1,000. Larger values may cause performance issues.

### Default Users
The following pre-configured accounts are created during initialization:

| Username          | Password |
|-------------------|----------|
| admin             | password |
| manager           | password |
| default_librarian | password |
| default_user      | password |

---

## Access Control

Application has permission-based and role-based access control. Permission is generally used for access control, but some actions are restricted by roles if it is required by business-model (only a librarian should be able to issue a book to reader, for instance).  
Implemented permissions are:
* AUTHOR_CREATE
* AUTHOR_UPDATE
* AUTHOR_DELETE
* CYCLE_CREATE
* CYCLE_UPDATE
* CYCLE_DELETE
* SERIES_CREATE
* SERIES_UPDATE
* SERIES_DELETE
* BOOK_CREATE
* BOOK_UPDATE
* BOOK_DELETE
* BOOK_INSTANCE_CREATE
* BOOK_INSTANCE_UPDATE
* BOOK_INSTANCE_VIEW
* BOOK_INSTANCE_DELETE
* BOOK_INSTANCE_INVENTORY_NUMBER_EDIT
* SERVICE_INDEXING
* SERVICE_LIBRARY_CARD_CREATE
* SERVICE_LIBRARY_CARD_VIEW
* SERVICE_BOOK_LENDING_CANCEL
* SERVICE_BOOK_LENDING_RETURN

Users roles are:  

| Name      | Description                                    |
|-----------|------------------------------------------------|
| ADMIN     | Application administration role                |
| LIBRARIAN | Role for librarians                            |
| MANAGER   | Library manager able to fill catalog           |
| USER      | General user able to only view library catalog |
| READER    | User able to lease books                       |

Users with `ADMIN` role have full permission-based access granted. However, actions restricted by role-based authorization (such as borrowing books) are prohibited for admin accounts by design.
New permissions added to the system are granted to the `ROLE_ADMIN` automatically, manual intervention is not required.

### Permission Grouping

For permissions granting purpose ( [**updateRolePermissions** method](src/main/java/ru/valkeru/libdemo/web/controller/security/SecurityController.java) ), some permissions are grouped into CRUD aliases to simplify granting access.
For example, to grant full book management permissions, an admin can send an explicit list:
```json
[
  "BOOK_CREATE",
  "BOOK_UPDATE",
  "BOOK_DELETE"
]
```
Or simply pass the BOOK_CRUD permission alias:
```json
[
"BOOK_CRUD"
]
```
BOOK_CRUD is expanded into the set of atomic permissions stored in a database.
If necessary, it is possible to combine multiple composite permissions into broader sets:
```
    MANAGEMENT_PERMISSION_SET(
        AUTHOR_CRUD,
        CYCLE_CRUD,
        SERIES_CRUD,
        BOOK_CRUD
    )
```
The permission set persisted in the database will be:
```json
[
  "CYCLE_CREATE",
  "BOOK_UPDATE",
  "AUTHOR_CREATE",
  "AUTHOR_UPDATE",
  "BOOK_DELETE",
  "CYCLE_DELETE",
  "BOOK_CREATE",
  "SERIES_CREATE",
  "CYCLE_UPDATE",
  "SERIES_DELETE",
  "AUTHOR_DELETE",
  "SERIES_UPDATE"
]
```
