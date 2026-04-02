# Tech Stack

- **Language:** Java 25
- **Framework:** Spring Boot 4.1.0
  - Spring Web MVC (`spring-boot-starter-webmvc`)
  - Spring Data JPA (`spring-boot-starter-data-jpa`)
- **Database:** PostgreSQL (driver included, not yet configured/connected)
- **Build tool:** Maven (via Maven Wrapper)
- **Other libraries:** Lombok, Bean Validation (`spring-boot-starter-validation`, for `@NotBlank`/`@Email`/`@Size`/`@NotNull`/`@Valid` on request DTOs)
- **Testing:** Spring Boot Test, JUnit 5 (`spring-boot-starter-webmvc-test`, `spring-boot-starter-data-jpa-test`)
