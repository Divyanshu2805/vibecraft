# Tech Stack

- **Language:** Java 25
- **Framework:** Spring Boot 4.1.0
  - Spring Web MVC (`spring-boot-starter-webmvc`)
  - Spring Data JPA (`spring-boot-starter-data-jpa`)
- **Database:** PostgreSQL — datasource configured in `application.yaml`, `ddl-auto: create`
- **Build tool:** Maven (via Maven Wrapper)
- **Other libraries:** Lombok, MapStruct 1.6.3 (compile-time entity↔DTO mapping, `mapper` package), Bean Validation (`spring-boot-starter-validation`, for `@NotBlank`/`@Email`/`@Size`/`@NotNull`/`@Valid` on request DTOs)
- **Testing:** Spring Boot Test, JUnit 5 (`spring-boot-starter-webmvc-test`, `spring-boot-starter-data-jpa-test`)
