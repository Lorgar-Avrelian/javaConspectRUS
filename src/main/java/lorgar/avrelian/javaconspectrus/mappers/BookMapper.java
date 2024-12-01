package lorgar.avrelian.javaconspectrus.mappers;

import lorgar.avrelian.javaconspectrus.dto.BookDTO;
import lorgar.avrelian.javaconspectrus.dto.NewBookDTO;
import lorgar.avrelian.javaconspectrus.models.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {
    @Mapping(target = "reader", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", source = "newBookDTO.authorFIO")
    Book newBookDTOtoBook(NewBookDTO newBookDTO);

    default Collection<BookDTO> booksListToBookDTOList(Collection<Book> books) {
        Collection<BookDTO> booksDTO = new ArrayList<>();
        for (Book book : books) {
            booksDTO.add(bookToBookDTO(book));
        }
        return booksDTO.stream()
                       .sorted(Comparator.comparing(BookDTO::getId))
                       .toList();
    }

    BookDTO bookToBookDTO(Book book);

    default Book bookDTOToBook(BookDTO bookDTO) {
        if (bookDTO == null) {
            return null;
        }
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setYear(bookDTO.getYear());
        return book;
    }
}