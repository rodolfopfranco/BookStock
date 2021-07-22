package one.digitalinnovation.bookstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookStockExceededException extends Exception {

    public BookStockExceededException(Long id, int quantityToIncrement) {
        super(String.format("Can't increment given quantity (%s)! Exceeds ID %s Book's stock capacity.", quantityToIncrement, id));
    }
}