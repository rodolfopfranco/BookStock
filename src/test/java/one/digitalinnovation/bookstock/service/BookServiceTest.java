package one.digitalinnovation.bookstock.service;

import one.digitalinnovation.bookstock.builder.BookDTOBuilder;
import one.digitalinnovation.bookstock.dto.BookDTO;
import one.digitalinnovation.bookstock.entity.Book;
import one.digitalinnovation.bookstock.exception.BookAlreadyRegisteredException;
import one.digitalinnovation.bookstock.mapper.BookMapper;
import one.digitalinnovation.bookstock.repository.BookRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        BookDTO bookDTO = BookDTOBuilder.builder().build().toBookDTO();
        Book expectedSavedBook = bookMapper.toModel(bookDTO);

        //When statements
        Mockito.when((bookRepository.findByName(bookDTO.getName()))).thenReturn(Optional.empty());
        Mockito.when(bookRepository.save(expectedSavedBook)).thenReturn(expectedSavedBook);

        //then
        BookDTO createdBookDTO = bookService.createBook(bookDTO);

        //Using Hamcrest:
        assertThat(createdBookDTO.getId(), is(equalTo(bookDTO.getId())));
        assertThat(createdBookDTO.getName(), is(equalTo(bookDTO.getName())));
        assertThat(createdBookDTO.getQuantity(), is(equalTo(bookDTO.getQuantity())));

        //Using Jupiter:
        assertEquals(bookDTO.getId(), createdBookDTO.getId());
        assertEquals(bookDTO.getName(), createdBookDTO.getName());
    }

    @Test
    void whenAlreadyRegisteredGivenBookThenExceptionSThrown() throws BookAlreadyRegisteredException {
        //Create a mock object:
        BookDTO expectedBookDTO = BookDTOBuilder.builder().build().toBookDTO();
        Book duplicatedBook = bookMapper.toModel(expectedBookDTO);

        //When
        when(bookRepository.findByName((expectedBookDTO.getName()))).thenReturn(Optional.of(duplicatedBook));

        //Create another book of the same name and tries to get the error:
        assertThrows(BookAlreadyRegisteredException.class, () -> bookService.createBook(expectedBookDTO));
    }


}