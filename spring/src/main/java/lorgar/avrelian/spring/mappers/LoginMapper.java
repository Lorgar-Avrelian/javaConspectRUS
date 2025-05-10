package lorgar.avrelian.spring.mappers;

import lorgar.avrelian.spring.dao.Login;
import lorgar.avrelian.spring.dto.LoginDTO;
import lorgar.avrelian.spring.dto.RegisterDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LoginMapper {
    LoginDTO loginToLoginDTO(Login login);

    List<LoginDTO> loginListToLoginDTOList(List<Login> loginList);

    default Login registerDTOToLogin(RegisterDTO registerDTO) {
        Login login = new Login();
        login.setLogin(registerDTO.getLogin());
        login.setPassword(registerDTO.getPassword());
        return login;
    }
}