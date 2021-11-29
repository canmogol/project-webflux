# Spring WebFlux

Why was Spring WebFlux created?

Part of the answer is the need for a non-blocking web stack to handle concurrency 
with a small number of threads and scale with fewer hardware resources.

**How to Compile**

You can compile the project only by running the `mvnw` command line script.

On *nix environment you may run the `mvnw` shell script,
```bash
./mvnw clean install
```

On Windows environment you may run the `mvnw.cmd` command.
```bash
mvnw.cmd clean install
```

**How to Configure**

You need to update the database connection configuration 
in the `application.properties` file and set the necessary values.

Also, you need to connect to the PostgreSQL database and 
execute the `schema.sql` file's content. This will create a table
called `book` and also will create a notification for insert, update and 
delete events.

**How to Use**

You can use the `Spring-WebFlux.postman_collection.json` Postman collection 
to test the REST endpoints.

Also, you can use the `curl` to listen to the server-send events.

```bash
curl -N -v http://localhost:8080/api/books/book-stream
```

**How to Run**

You can run the `ProjectWebfluxApplication` directly from your IDE,
or you can use the `mvnw`.

```bash
./mvnw spring-boot:run
```
