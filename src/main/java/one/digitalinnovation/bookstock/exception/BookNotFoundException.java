package one.digitalinnovation.bookstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotFoundException extends Exception{
    public BookNotFoundException(String bookName){
        super(String.format("Book with name %s not found in the system.", bookName));
    }

    public BookNotFoundException(Long id){
        super(String.format("Book with id %s not found in the system.", id));
    }
}
