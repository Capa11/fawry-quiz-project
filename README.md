# Fawry Quiz Project

Spring Boot quiz API. Requires JDK 21, PostgreSQL, and Google OAuth credentials for Google sign-in.

## Run

1. Create an empty PostgreSQL database. Hibernate creates/updates the tables on startup using the existing project configuration.
2. Copy `.env.example` to `.env` in the project root and replace every example value with your own credentials. The database URL includes your database username and password; URL-encode special characters in those values.
3. Set `JWT_SECRET` to a random Base64-encoded signing key containing at least 32 bytes. In PowerShell you can generate one with:

   ```powershell
   $key = New-Object byte[] 32
   [System.Security.Cryptography.RandomNumberGenerator]::Fill($key)
   [Convert]::ToBase64String($key)
   ```

4. For Google sign-in, configure a Google OAuth web client with redirect URI `http://localhost:8080/login/oauth2/code/google` and enter its client ID and client secret in `.env`.
5. From the project root run:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   On Linux/macOS: `sh ./mvnw spring-boot:run`. Set `JAVA_HOME` to your JDK 21 directory if Maven cannot locate Java.

The application runs on port 8080. API documentation is available at `http://localhost:8080/swagger-ui/index.html`; the static reference is `docs/api/index.html`. The configured frontend URL is `http://localhost:3000`.

## Credentials

Spring imports the root `.env` automatically as a Java properties file. Use plain `NAME=value` entries without shell quotes or `export`; Java properties escaping applies. You can also supply the same names as operating-system environment variables, which take precedence over the file.

`.env` is ignored by Git and is outside the packaged application. Run from the project root so Spring can find it. Only `.env.example`, with placeholders, belongs in the repository. Your instructor should use their own database, JWT key, and Google OAuth credentials. Never share the local `.env` or a database dump containing real users.

Configuration import reference: [Spring Boot externalized configuration](https://docs.spring.io/spring-boot/reference/features/external-config.html).
