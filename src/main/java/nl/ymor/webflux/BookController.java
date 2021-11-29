package nl.ymor.webflux;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.r2dbc.postgresql.api.Notification;
import io.r2dbc.postgresql.api.PostgresqlConnection;
import io.r2dbc.postgresql.api.PostgresqlResult;
import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/books")
public class BookController {

    private final BookRepository bookRepository;

    private PostgresqlConnection connection;

    public BookController(BookRepository bookRepository,
                          ConnectionFactory connectionFactory) {
        this.bookRepository = bookRepository;
        connection = Mono.from(connectionFactory.create())
                .cast(PostgresqlConnection.class)
                .block();
    }

    @GetMapping
    public Flux<Book> getAll() {
        return bookRepository.findAll();
    }

    @PostMapping
    public Mono<Book> create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @PutMapping
    public Mono<Book> update(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @DeleteMapping("/all")
    public Mono<Void> deleteAll() {
        return bookRepository.deleteAll();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable(value = "id") Long id) {
        // bookRepository.deleteById(id).block(); // this will block if we want to be sure the book is deleted
        return bookRepository.deleteById(id);
    }

    @PostConstruct
    private void postConstruct() {
        connection.createStatement("LISTEN book_notification").execute()
                .flatMap(PostgresqlResult::getRowsUpdated).subscribe();
    }

    @GetMapping(value = "/book-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> getStream() {
        return connection.getNotifications()
                .map(Notification::getParameter);
    }

}
