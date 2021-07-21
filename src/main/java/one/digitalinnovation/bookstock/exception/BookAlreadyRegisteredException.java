package one.digitalinnovation.bookstock.exception;

public class BookAlreadyRegisteredException extends Exception{

    public BookAlreadyRegisteredException(String bookName) {
        super(String.format("Book with name %s is already registered in the system.",bookName));
    }
}
