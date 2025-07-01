package ktlib.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import ktlib.BookApplication;
import ktlib.domain.GivenBestseller;
import ktlib.domain.RegistedBook;
import lombok.Data;

@Entity
@Table(name = "Book_table")
@Data
//<<< DDD / Aggregate Root
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookId;

    private Date registrationDate;

    private Date publicationDate;

    private Long numberOfSubscribers;

    private Long publicationId;

    private Long manuscriptId;

    private Long authorId;

    private String status;

    public static BookRepository repository() {
        BookRepository bookRepository = BookApplication.applicationContext.getBean(
            BookRepository.class
        );
        return bookRepository;
    }

    //<<< Clean Arch / Port Method
    public static void registBook(PreparedPublish preparedPublish) {
        //implement business logic here:

        /** Example 1:  new item 
        Book book = new Book();
        repository().save(book);

        RegistedBook registedBook = new RegistedBook(book);
        registedBook.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        // if preparedPublish.llmGptId exists, use it
        
        // ObjectMapper mapper = new ObjectMapper();
        // Map<, Object> publishMap = mapper.convertValue(preparedPublish.getLlmGptId(), Map.class);

        repository().findById(preparedPublish.get???()).ifPresent(book->{
            
            book // do something
            repository().save(book);

            RegistedBook registedBook = new RegistedBook(book);
            registedBook.publishAfterCommit();

         });
        */

        Book book = new Book();

        // Map event data to entity fields
        book.setRegistrationDate(preparedPublish.getPublicationDate());
        book.setPublicationDate(preparedPublish.getPublicationDate());
        book.setNumberOfSubscribers(0L);
        book.setPublicationId(preparedPublish.getPublicationId());
        book.setManuscriptId(preparedPublish.getManuscriptId());
        book.setStatus("open");

        // Persist and emit domain event
        repository().save(book);
        RegistedBook registedBook = new RegistedBook(book);
        registedBook.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void giveBestseller(BookSubscribed bookSubscribed) {
        
        repository().findById(bookSubscribed.getBookId()).ifPresent(book -> {

            Long current = book.getNumberOfSubscribers();
            if (current == null) current = 0L;
            book.setNumberOfSubscribers(current + 1);

            if (book.getNumberOfSubscribers() >= 5 && !"Bestseller".equals(book.getStatus())) {
                book.setStatus("Bestseller");

                GivenBestseller event = new GivenBestseller(book);
                event.setBookId(book.getBookId());
                event.setAuthorId(book.getAuthorId());
                event.setNumberOfSubscribers(book.getNumberOfSubscribers());
                event.setPublicationId(book.getPublicationId());
                event.publishAfterCommit();
            }

            repository().save(book);
        });
    }
}
