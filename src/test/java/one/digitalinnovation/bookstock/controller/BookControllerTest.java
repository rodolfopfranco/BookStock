package one.digitalinnovation.bookstock.controller;

import one.digitalinnovation.bookstock.builder.BookDTOBuilder;
import one.digitalinnovation.bookstock.dto.BookDTO;
import one.digitalinnovation.bookstock.dto.QuantityDTO;
import one.digitalinnovation.bookstock.exception.BookNotFoundException;
import one.digitalinnovation.bookstock.exception.BookStockExceededException;
import one.digitalinnovation.bookstock.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static one.digitalinnovation.bookstock.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    private static final String BOOK_API_URL_PATH = "/api/v1/books";
    private static final long VALID_BOOK_ID = 1L;
    private static final long INVALID_BOOK_ID = 2l;
    private static final String BOOK_API_SUBPATH_INCREMENT_URL = "/increment";
    private static final String BOOK_API_SUBPATH_DECREMENT_URL = "/decrement";

    private MockMvc mockMvc;
    @Mock
    private BookService bookService;
    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s,locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenBookIsCreated() throws Exception {
        //POST tests on the API
        //Given:
        BookDTO bookDTO = BookDTOBuilder.builder().build().toBookDTO();
        //When:
        Mockito.when(bookService.createBook(bookDTO)).thenReturn(bookDTO);
        //Then:
        mockMvc.perform(post(BOOK_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(bookDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name",is(bookDTO.getName())))
                .andExpect(jsonPath("$.author",is(bookDTO.getAuthor())))
                .andExpect(jsonPath("$.genre",is(bookDTO.getGenre().toString())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldsThenErrorIsReturned() throws Exception {
        //Given:
        BookDTO bookDTO = BookDTOBuilder.builder().build().toBookDTO();
        bookDTO.setAuthor(null);
        //Then:
        mockMvc.perform(post(BOOK_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(bookDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidNameThenReturnsOkStatus() throws Exception {
        //Given:
        BookDTO bookDTO = BookDTOBuilder.builder().build().toBookDTO();
        //When:
        when(bookService.findByName(bookDTO.getName())).thenReturn(bookDTO);
        //Then:
        mockMvc.perform(MockMvcRequestBuilders.get(BOOK_API_URL_PATH+"/"+bookDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name",is(bookDTO.getName())))
                .andExpect(jsonPath("$.author",is(bookDTO.getAuthor())))
                .andExpect(jsonPath("$.genre",is(bookDTO.getGenre().toString())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredNameThenReturnsNotFoundStatus() throws Exception {
        //Given:
        BookDTO bookDTO = BookDTOBuilder.builder().build().toBookDTO();
        //When:
        when(bookService.findByName(bookDTO.getName())).thenThrow(BookNotFoundException.class);
        //Then:
        mockMvc.perform(MockMvcRequestBuilders.get(BOOK_API_URL_PATH+"/"+bookDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETBookListIsCalledThenReturnsOkStatus() throws Exception {
        //Given:
        BookDTO bookDTO = BookDTOBuilder.builder().build().toBookDTO();
        //When:
        when(bookService.listAll()).thenReturn(Collections.singletonList(bookDTO));
        //Then:
        //$[0]. is a Hamcrest way of getting list data
        mockMvc.perform(MockMvcRequestBuilders.get(BOOK_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name",is(bookDTO.getName())))
                .andExpect(jsonPath("$[0].author",is(bookDTO.getAuthor())))
                .andExpect(jsonPath("$[0].genre",is(bookDTO.getGenre().toString())));
    }

    @Test
    void whenGETBookListIsCalledWithoutBooksThenReturnsOkStatus() throws Exception {
        //Given:
        BookDTO bookDTO = BookDTOBuilder.builder().build().toBookDTO();
        //When:
        when(bookService.listAll()).thenReturn(Collections.singletonList(bookDTO));
        //Then:
        mockMvc.perform(MockMvcRequestBuilders.get(BOOK_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name",is(bookDTO.getName())))
                .andExpect(jsonPath("$[0].author",is(bookDTO.getAuthor())))
                .andExpect(jsonPath("$[0].genre",is(bookDTO.getGenre().toString())));
    }

    @Test
    void whenDELETEIsCalledWithValidIdThenReturnsNoContentStatus() throws Exception {
        //Given:
        BookDTO bookDTO = BookDTOBuilder.builder().build().toBookDTO();
        //When:
        doNothing().when(bookService).deleteById(bookDTO.getId());
        //Then:
        mockMvc.perform(MockMvcRequestBuilders.delete(BOOK_API_URL_PATH+"/"+bookDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETEIsCalledWithInvalidIdThenReturnsNoContentStatus() throws Exception {
        //When:
        doThrow(BookNotFoundException.class).when(bookService).deleteById(INVALID_BOOK_ID);
        //Then:
        mockMvc.perform(MockMvcRequestBuilders.delete(BOOK_API_URL_PATH+"/"+INVALID_BOOK_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPATCHIsCalledToIncrementDiscountThenOKstatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder()
                .quantity(10)
                .build();

        BookDTO bookDTO = BookDTOBuilder.builder().build().toBookDTO();
        bookDTO.setQuantity(bookDTO.getQuantity() + quantityDTO.getQuantity());

        when(bookService.increment(VALID_BOOK_ID, quantityDTO.getQuantity())).thenReturn(bookDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch(BOOK_API_URL_PATH + "/" + VALID_BOOK_ID
                + BOOK_API_SUBPATH_INCREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(bookDTO.getName())))
                .andExpect(jsonPath("$.author", is(bookDTO.getAuthor())))
                .andExpect(jsonPath("$.genre", is(bookDTO.getGenre().toString())))
                .andExpect(jsonPath("$.quantity", is(bookDTO.getQuantity())));
    }
}