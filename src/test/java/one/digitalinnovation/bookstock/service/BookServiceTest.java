package one.digitalinnovation.bookstock.service;

import one.digitalinnovation.bookstock.builder.BookDTOBuilder;
import one.digitalinnovation.bookstock.dto.BookDTO;
import one.digitalinnovation.bookstock.entity.Book;
import one.digitalinnovation.bookstock.exception.BookAlreadyRegisteredException;
import one.digitalinnovation.bookstock.mapper.BookMapper;
import one.digitalinnovation.bookstock.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

//ExtendWith tells to use mockito:
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    private static final long INVALID_BOOK_ID = 1L;

    @Mock
    private BookRepository bookRepository;

    private BookMapper bookMapper = BookMapper.INSTANCE;

    @InjectMocks
    private BookService bookService;

    @Test
    void whenBookInformedThenItShouldBeCreated() throws BookAlreadyRegisteredException {
        //Create a mock object:
        BookDTO bookDTO = BookDTOBuilder.builder()
                .build()
                .toBookDTO();
        Book expectedSavedBook = bookMapper.toModel(bookDTO);

        //When statements
        Mockito.when((bookRepository.findByName(bookDTO.getName()))).thenReturn(Optional.empty());
        Mockito.when(bookRepository.save(expectedSavedBook)).thenReturn(expectedSavedBook);

        //then
        BookDTO createdBookDTO = bookService.createBook(bookDTO);

        assertEquals(bookDTO.getId(), createdBookDTO.getId());
    }
}