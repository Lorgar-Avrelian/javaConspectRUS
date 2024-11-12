package lorgar.avrelian.javaconspectrus.mappers;

import lorgar.avrelian.javaconspectrus.dao.Login;
import lorgar.avrelian.javaconspectrus.dto.LoginDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LoginMapper {
    LoginDTO loginToLoginDTO(Login login);
    Login loginDTOToLogin(LoginDTO loginDTO);
    List<LoginDTO> loginDTOListToLoginDTOList(List<Login> loginList);
}
