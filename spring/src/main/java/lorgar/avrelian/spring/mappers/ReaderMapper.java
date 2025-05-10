package lorgar.avrelian.spring.mappers;

import lorgar.avrelian.spring.dto.NewReaderDTO;
import lorgar.avrelian.spring.dto.ReaderNoBooksDTO;
import lorgar.avrelian.spring.models.Reader;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.Collection;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReaderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "newReaderDTO.name")
    @Mapping(target = "secondName", source = "newReaderDTO.secondName")
    @Mapping(target = "surname", source = "newReaderDTO.surname")
    @Mapping(target = "personalNumber", source = "newReaderDTO.personalNumber")
    Reader newReaderDTOtoReader(NewReaderDTO newReaderDTO);

    @Mapping(target = "id", source = "reader.id")
    @Mapping(target = "name", source = "reader.name")
    @Mapping(target = "secondName", source = "reader.secondName")
    @Mapping(target = "surname", source = "reader.surname")
    @Mapping(target = "personalNumber", source = "reader.personalNumber")
    ReaderNoBooksDTO readerToNoBooksDTO(Reader reader);

    Collection<ReaderNoBooksDTO> readersToNoBooksDTOs(Collection<Reader> readers);
}