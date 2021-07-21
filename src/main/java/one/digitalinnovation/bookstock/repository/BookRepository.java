package one.digitalinnovation.bookstock.repository;

import one.digitalinnovation.bookstock.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByName(String name);
}
