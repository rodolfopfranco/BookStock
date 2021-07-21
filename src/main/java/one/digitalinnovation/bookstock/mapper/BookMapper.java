package one.digitalinnovation.bookstock.mapper;

import one.digitalinnovation.bookstock.dto.BookDTO;
import one.digitalinnovation.bookstock.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    Book toModel(BookDTO bookDTO);

    BookDTO toDTO(Book book);
}
