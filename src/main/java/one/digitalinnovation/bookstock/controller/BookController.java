package one.digitalinnovation.bookstock.controller;

import lombok.AllArgsConstructor;
import one.digitalinnovation.bookstock.dto.BookDTO;
import one.digitalinnovation.bookstock.dto.QuantityDTO;
import one.digitalinnovation.bookstock.exception.BookAlreadyRegisteredException;
import one.digitalinnovation.bookstock.exception.BookNotFoundException;
import one.digitalinnovation.bookstock.exception.BookStockExceededException;
import one.digitalinnovation.bookstock.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BookController implements BookControllerDocs{

    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO createBook(@RequestBody @Valid BookDTO bookDTO) throws BookAlreadyRegisteredException{
        return bookService.createBook(bookDTO);
    }

    @GetMapping("/{name}")
    public BookDTO findByName(@PathVariable String name) throws BookNotFoundException{
        return bookService.findByName(name);
    }

    @GetMapping
    public List<BookDTO> listBooks(){
        return bookService.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws BookNotFoundException{
        bookService.deleteById(id);
    }

    @PatchMapping("/{id}/increment")
    public BookDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws BookStockExceededException, BookNotFoundException {
        return bookService.increment(id, quantityDTO.getQuantity());
    }
}
