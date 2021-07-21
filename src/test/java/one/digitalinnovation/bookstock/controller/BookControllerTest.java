package one.digitalinnovation.bookstock.controller;

import one.digitalinnovation.bookstock.builder.BookDTOBuilder;
import one.digitalinnovation.bookstock.dto.BookDTO;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static one.digitalinnovation.bookstock.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
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
        //Testes POST on the API

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
}