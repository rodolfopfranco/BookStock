package one.digitalinnovation.bookstock.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import one.digitalinnovation.bookstock.dto.BookDTO;
import one.digitalinnovation.bookstock.exception.BookAlreadyRegisteredException;
import one.digitalinnovation.bookstock.exception.BookNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Api("Manages book stock")
public interface BookControllerDocs {
    @ApiOperation(value = "Book creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success! book created!"),
            @ApiResponse(code = 400, message = "Missing required fields or wrong field range value")
    })
    BookDTO createBook(BookDTO bookDTO) throws BookAlreadyRegisteredException;

    @ApiOperation(value = "Returns book found by a name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success! Book found!"),
            @ApiResponse(code = 404, message = "Book not found with given name")
    })
    BookDTO findByName(@PathVariable String name) throws BookNotFoundException;

    @ApiOperation(value = "Returns a list of all books registered in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all books registered in the system"),
    })
    List<BookDTO> listBooks();

    @ApiOperation(value = "Delete a book by a given valid Id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Success! Book deleted in the system"),
            @ApiResponse(code = 404, message = "Book with given id not found.")
    })
    void deleteById(@PathVariable Long id) throws BookNotFoundException;
}
