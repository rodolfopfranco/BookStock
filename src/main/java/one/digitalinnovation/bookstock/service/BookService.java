package one.digitalinnovation.bookstock.service;

import lombok.AllArgsConstructor;
import one.digitalinnovation.bookstock.dto.BookDTO;
import one.digitalinnovation.bookstock.entity.Book;
import one.digitalinnovation.bookstock.exception.BookAlreadyRegisteredException;
import one.digitalinnovation.bookstock.exception.BookNotFoundException;
import one.digitalinnovation.bookstock.mapper.BookMapper;
import one.digitalinnovation.bookstock.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
//Autowired to help with constructor injection:
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper = BookMapper.INSTANCE;

    public BookDTO createBook(BookDTO bookDTO) throws BookAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(bookDTO.getName());
        Book book = bookMapper.toModel(bookDTO);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDTO(savedBook);
    }

    public BookDTO findByName(String name) throws BookNotFoundException {
        Book foundBook = bookRepository.findByName(name)
                .orElseThrow(() -> new BookNotFoundException(name));
        return bookMapper.toDTO(foundBook);
    }

    public List<BookDTO> listAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws BookNotFoundException {
        verifyIfExists(id);
        bookRepository.deleteById(id);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws BookAlreadyRegisteredException{
        Optional<Book> optSavedBook = bookRepository.findByName(name);
        if(optSavedBook.isPresent()){
            throw new BookAlreadyRegisteredException(name);
        }
    }

    private Book verifyIfExists(Long id) throws BookNotFoundException {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }
}
