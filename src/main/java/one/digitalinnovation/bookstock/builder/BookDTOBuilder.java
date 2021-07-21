package one.digitalinnovation.bookstock.builder;

import lombok.Builder;
import one.digitalinnovation.bookstock.dto.BookDTO;
import one.digitalinnovation.bookstock.enums.BookGenre;

@Builder
public class BookDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Genji Monogatari";

    @Builder.Default
    private String author = "Murasaki Shikibu";

    @Builder.Default
    private int max = 30;

    @Builder.Default
    private int quantity = 5;

    @Builder.Default
    private BookGenre genre = BookGenre.ROMANCE;

    public BookDTO toBookDTO(){
        return new BookDTO(
                id,
                name,
                author,
                max,
                quantity,
                genre);
    }
}
