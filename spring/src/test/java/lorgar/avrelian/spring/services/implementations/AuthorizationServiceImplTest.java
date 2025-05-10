package lorgar.avrelian.spring.services.implementations;

import lorgar.avrelian.spring.dao.Login;
import lorgar.avrelian.spring.dto.LoginDTO;
import lorgar.avrelian.spring.dto.RegisterDTO;
import lorgar.avrelian.spring.mappers.LoginMapper;
import lorgar.avrelian.spring.models.Role;
import lorgar.avrelian.spring.repository.LoginRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static lorgar.avrelian.spring.constants.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@DisplayName(value = "Test of AuthorizationServiceImpl")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthorizationServiceImplTest {
    @Mock
    private JpaUserDetailsServiceImpl jpaUserDetailsService;
    @Mock
    private LoginRepository loginRepository;
    @Mock
    private LoginMapper loginMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthorizationServiceImpl authorizationService;

    @Test
    @DisplayName(value = "login(BasicAuthDTO basicAuthDTO): return Login")
    @Order(1)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void login1() {
        when(loginRepository.findByLoginEqualsIgnoreCase(eq(OWNER_AUTH.getLogin())))
                .thenReturn(Optional.of(OWNER));
        when(passwordEncoder.matches(OWNER_AUTH.getPassword(), OWNER.getPassword()))
                .thenReturn(true);
        //
        Login actualLogin = authorizationService.login(OWNER_AUTH);
        assertNotNull(actualLogin);
        Assertions.assertDoesNotThrow(() -> authorizationService.login(OWNER_AUTH));
        assertInstanceOf(Login.class, actualLogin);
        assertEquals(OWNER, actualLogin);
    }

    @Test
    @DisplayName(value = "login(BasicAuthDTO basicAuthDTO): return null")
    @Order(2)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void login2() {
        when(loginRepository.findByLoginEqualsIgnoreCase(eq(ADMIN_AUTH.getLogin())))
                .thenReturn(Optional.ofNullable(null));
        //
        Login actualLogin = authorizationService.login(ADMIN_AUTH);
        assertNull(actualLogin);
        Assertions.assertDoesNotThrow(() -> authorizationService.login(ADMIN_AUTH));
    }

    @Test
    @DisplayName(value = "login(BasicAuthDTO basicAuthDTO): return null")
    @Order(3)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void login3() {
        when(loginRepository.findByLoginEqualsIgnoreCase(eq(USER_AUTH.getLogin())))
                .thenReturn(Optional.of(USER));
        when(passwordEncoder.matches(USER_AUTH.getPassword(), USER.getPassword()))
                .thenReturn(false);
        //
        Login actualLogin = authorizationService.login(USER_AUTH);
        assertNull(actualLogin);
        Assertions.assertDoesNotThrow(() -> authorizationService.login(USER_AUTH));
    }

    @Test
    @DisplayName(value = "register(RegisterDTO registerDTO): return Login")
    @Order(4)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void register1() {
        when(loginRepository.findByLoginEqualsIgnoreCase(eq(OWNER_REGISTER.getLogin())))
                .thenReturn(Optional.ofNullable(null));
        when(loginMapper.registerDTOToLogin(eq(OWNER_REGISTER)))
                .thenReturn(OWNER);
        when(passwordEncoder.encode(eq(OWNER.getPassword())))
                .thenReturn(OWNER.getPassword());
        when(loginRepository.count())
                .thenReturn(0L);
        when(loginRepository.save(OWNER))
                .thenReturn(OWNER);
        //
        Login actualLogin = authorizationService.register(OWNER_REGISTER);
        assertNotNull(actualLogin);
        Assertions.assertDoesNotThrow(() -> authorizationService.register(OWNER_REGISTER));
        assertInstanceOf(Login.class, actualLogin);
        assertEquals(OWNER, actualLogin);
    }

    @Test
    @DisplayName(value = "register(RegisterDTO registerDTO): return Login")
    @Order(5)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void register2() {
        when(loginRepository.findByLoginEqualsIgnoreCase(eq(ADMIN_REGISTER.getLogin())))
                .thenReturn(Optional.ofNullable(null));
        when(loginMapper.registerDTOToLogin(eq(ADMIN_REGISTER)))
                .thenReturn(ADMIN);
        when(passwordEncoder.encode(eq(ADMIN.getPassword())))
                .thenReturn(ADMIN.getPassword());
        when(loginRepository.count())
                .thenReturn(1L);
        Login admin = ADMIN;
        admin.setRole(Role.ROLE_USER);
        when(loginRepository.save(admin))
                .thenReturn(admin);
        //
        Login actualLogin = authorizationService.register(ADMIN_REGISTER);
        assertNotNull(actualLogin);
        Assertions.assertDoesNotThrow(() -> authorizationService.register(ADMIN_REGISTER));
        assertInstanceOf(Login.class, actualLogin);
        assertEquals(admin, actualLogin);
    }

    @Test
    @DisplayName(value = "register(RegisterDTO registerDTO): return null")
    @Order(6)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void register3() {
        when(loginRepository.findByLoginEqualsIgnoreCase(eq(USER_REGISTER.getLogin())))
                .thenReturn(Optional.ofNullable(null));
        when(loginMapper.registerDTOToLogin(eq(USER_REGISTER)))
                .thenReturn(USER);
        when(passwordEncoder.encode(eq(USER.getPassword())))
                .thenReturn(USER.getPassword());
        when(loginRepository.count())
                .thenReturn(2L);
        when(loginRepository.save(USER))
                .thenReturn(null);
        //
        Login actualLogin = authorizationService.register(USER_REGISTER);
        assertNull(actualLogin);
        Assertions.assertDoesNotThrow(() -> authorizationService.register(USER_REGISTER));
    }

    @Test
    @DisplayName(value = "register(RegisterDTO registerDTO): return null")
    @Order(7)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void register4() {
        when(loginRepository.findByLoginEqualsIgnoreCase(eq(OWNER_REGISTER.getLogin())))
                .thenReturn(Optional.of(OWNER));
        //
        Login actualLogin = authorizationService.register(OWNER_REGISTER);
        assertNull(actualLogin);
        Assertions.assertDoesNotThrow(() -> authorizationService.register(OWNER_REGISTER));
    }

    @Test
    @DisplayName(value = "register(RegisterDTO registerDTO): return null")
    @Order(8)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void register5() {
        RegisterDTO admin = ADMIN_REGISTER;
        admin.setPasswordConfirmation("321");
        when(loginRepository.findByLoginEqualsIgnoreCase(eq(admin.getLogin())))
                .thenReturn(Optional.ofNullable(null));
        //
        Login actualLogin = authorizationService.register(admin);
        assertNull(actualLogin);
        Assertions.assertDoesNotThrow(() -> authorizationService.register(admin));
    }

    @Test
    @DisplayName(value = "getAllUsers(): return List<LoginDTO>")
    @Order(9)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getAllUsers1() {
        when(loginRepository.findAll())
                .thenReturn(LOGINS);
        when(loginMapper.loginListToLoginDTOList(eq(LOGINS)))
                .thenReturn(LOGIN_DTO_LIST);
        //
        Collection<LoginDTO> actualCollection = authorizationService.getAllUsers();
        assertNotNull(actualCollection);
        Assertions.assertDoesNotThrow(() -> authorizationService.getAllUsers());
        assertInstanceOf(Collection.class, actualCollection);
        assertIterableEquals(LOGIN_DTO_LIST, actualCollection);
    }

    @Test
    @DisplayName(value = "getAllUsers(): return List<>")
    @Order(10)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getAllUsers2() {
        when(loginRepository.findAll())
                .thenReturn(new ArrayList<>());
        when(loginMapper.loginListToLoginDTOList(eq(Collections.EMPTY_LIST)))
                .thenReturn(new ArrayList<>());
        //
        Collection<LoginDTO> actualCollection = authorizationService.getAllUsers();
        assertNotNull(actualCollection);
        Assertions.assertDoesNotThrow(() -> authorizationService.getAllUsers());
        assertInstanceOf(Collection.class, actualCollection);
        assertIterableEquals(Collections.EMPTY_LIST, actualCollection);
    }

    @Test
    @DisplayName(value = "getAllUsers(int page, int size): return List<LoginDTO>")
    @Order(11)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getAllUsers3() {
        int page = 1;
        int size = 3;
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Login> loginPage = new PageImpl<>(LOGINS, pageRequest, LOGINS.size());
        when(loginRepository.findAll(pageRequest))
                .thenReturn(loginPage);
        when(loginMapper.loginListToLoginDTOList(eq(LOGINS)))
                .thenReturn(LOGIN_DTO_LIST);
        //
        Collection<LoginDTO> actualCollection = authorizationService.getAllUsers(page, size);
        assertNotNull(actualCollection);
        Assertions.assertDoesNotThrow(() -> authorizationService.getAllUsers(page, size));
        assertInstanceOf(Collection.class, actualCollection);
        assertIterableEquals(LOGIN_DTO_LIST, actualCollection);
    }

    @Test
    @DisplayName(value = "getAllUsers(int page, int size): return List<>")
    @Order(12)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getAllUsers4() {
        int page = 1;
        int size = 3;
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Login> loginPage = new PageImpl<>(new ArrayList<>(), pageRequest, 0L);
        when(loginRepository.findAll(pageRequest))
                .thenReturn(loginPage);
        when(loginMapper.loginListToLoginDTOList(eq(Collections.EMPTY_LIST)))
                .thenReturn(new ArrayList<>());
        //
        Collection<LoginDTO> actualCollection = authorizationService.getAllUsers(page, size);
        assertNotNull(actualCollection);
        Assertions.assertDoesNotThrow(() -> authorizationService.getAllUsers(page, size));
        assertInstanceOf(Collection.class, actualCollection);
        assertIterableEquals(Collections.EMPTY_LIST, actualCollection);
    }

    @Test
    @DisplayName(value = "setRole(UserDetails userDetails, long id, Role role): return LoginDTO")
    @Order(13)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void setRole1() {
        when(loginRepository.findById(eq(USER.getId())))
                .thenReturn(Optional.of(USER));
        when(loginRepository.save(eq(USER)))
                .thenReturn(USER);
        when(loginMapper.loginToLoginDTO(eq(USER)))
                .thenReturn(USER_LOGIN_DTO);
        //
        LoginDTO actualLogin = authorizationService.setRole(ADMIN_DETAILS, USER.getId(), Role.ROLE_USER);
        assertNotNull(actualLogin);
        Assertions.assertDoesNotThrow(() -> authorizationService.setRole(ADMIN_DETAILS, USER.getId(), Role.ROLE_USER));
        assertInstanceOf(LoginDTO.class, actualLogin);
        assertEquals(USER_LOGIN_DTO, actualLogin);
    }

    @Test
    @DisplayName(value = "setRole(UserDetails userDetails, long id, Role role): throw IllegalArgumentException")
    @Order(14)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void setRole2() {
        assertThrows(IllegalArgumentException.class, () -> authorizationService.setRole(ADMIN_DETAILS, USER.getId(), Role.ROLE_OWNER));
    }

    @Test
    @DisplayName(value = "setRole(UserDetails userDetails, long id, Role role): throw IllegalArgumentException")
    @Order(15)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void setRole3() {
        when(loginRepository.findById(eq(USER.getId())))
                .thenReturn(Optional.ofNullable(null));
        //
        assertThrows(IllegalArgumentException.class, () -> authorizationService.setRole(ADMIN_DETAILS, USER.getId(), Role.ROLE_USER));
    }

    @Test
    @DisplayName(value = "setRole(UserDetails userDetails, long id, Role role): return null")
    @Order(16)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void setRole4() {
        when(loginRepository.findById(eq(USER.getId())))
                .thenReturn(Optional.of(USER));
        when(loginRepository.save(eq(USER)))
                .thenReturn(null);
        //
        LoginDTO actualLogin = authorizationService.setRole(ADMIN_DETAILS, USER.getId(), Role.ROLE_USER);
        assertNull(actualLogin);
        Assertions.assertDoesNotThrow(() -> authorizationService.setRole(ADMIN_DETAILS, USER.getId(), Role.ROLE_USER));
    }

    @Test
    @DisplayName(value = "setPassword(UserDetails userDetails, long id, String password): return LoginDTO")
    @Order(17)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void setPassword1() {
        when(loginRepository.findById(eq(USER.getId())))
                .thenReturn(Optional.of(USER));
        when(loginRepository.findByLoginEqualsIgnoreCase(eq(OWNER_DETAILS.getUsername())))
                .thenReturn(Optional.of(OWNER));
        when(passwordEncoder.encode(eq(USER.getPassword())))
                .thenReturn(USER.getPassword());
        when(loginRepository.save(eq(USER)))
                .thenReturn(USER);
        when(loginMapper.loginToLoginDTO(eq(USER)))
                .thenReturn(USER_LOGIN_DTO);
        //
        LoginDTO actualLogin = authorizationService.setPassword(OWNER_DETAILS, USER.getId(), USER.getPassword());
        assertNotNull(actualLogin);
        Assertions.assertDoesNotThrow(() -> authorizationService.setPassword(OWNER_DETAILS, USER.getId(), USER.getPassword()));
        assertInstanceOf(LoginDTO.class, actualLogin);
        assertEquals(USER_LOGIN_DTO, actualLogin);
    }

    @Test
    @DisplayName(value = "setPassword(UserDetails userDetails, long id, String password): throw IllegalArgumentException")
    @Order(18)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void setPassword2() {
        when(loginRepository.findById(eq(USER.getId())))
                .thenReturn(Optional.ofNullable(null));
        when(loginRepository.findByLoginEqualsIgnoreCase(eq(OWNER_DETAILS.getUsername())))
                .thenReturn(Optional.of(OWNER));
        //
        assertThrows(IllegalArgumentException.class, () -> authorizationService.setPassword(OWNER_DETAILS, USER.getId(), USER.getPassword()));
    }

    @Test
    @DisplayName(value = "setPassword(UserDetails userDetails, long id, String password): return LoginDTO")
    @Order(19)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void setPassword3() {
        when(loginRepository.findById(eq(USER.getId())))
                .thenReturn(Optional.of(USER));
        when(loginRepository.findByLoginEqualsIgnoreCase(eq(USER_DETAILS.getUsername())))
                .thenReturn(Optional.of(USER));
        when(passwordEncoder.encode(eq(USER.getPassword())))
                .thenReturn(USER.getPassword());
        when(loginRepository.save(eq(USER)))
                .thenReturn(USER);
        when(loginMapper.loginToLoginDTO(eq(USER)))
                .thenReturn(USER_LOGIN_DTO);
        //
        LoginDTO actualLogin = authorizationService.setPassword(USER_DETAILS, USER.getId(), USER.getPassword());
        assertNotNull(actualLogin);
        Assertions.assertDoesNotThrow(() -> authorizationService.setPassword(USER_DETAILS, USER.getId(), USER.getPassword()));
        assertInstanceOf(LoginDTO.class, actualLogin);
        assertEquals(USER_LOGIN_DTO, actualLogin);
    }

    @Test
    @DisplayName(value = "setPassword(UserDetails userDetails, long id, String password): throw IllegalArgumentException")
    @Order(20)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void setPassword4() {
        when(loginRepository.findById(eq(ADMIN.getId())))
                .thenReturn(Optional.of(ADMIN));
        when(loginRepository.findByLoginEqualsIgnoreCase(eq(USER_DETAILS.getUsername())))
                .thenReturn(Optional.of(USER));
        //
        assertThrows(IllegalArgumentException.class, () -> authorizationService.setPassword(USER_DETAILS, ADMIN.getId(), ADMIN.getPassword()));
    }

    @Test
    @DisplayName(value = "setPassword(UserDetails userDetails, long id, String password): return null")
    @Order(21)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void setPassword5() {
        when(loginRepository.findById(eq(USER.getId())))
                .thenReturn(Optional.of(USER));
        when(loginRepository.findByLoginEqualsIgnoreCase(eq(OWNER_DETAILS.getUsername())))
                .thenReturn(Optional.of(OWNER));
        when(passwordEncoder.encode(eq(USER.getPassword())))
                .thenReturn(USER.getPassword());
        when(loginRepository.save(eq(USER)))
                .thenReturn(null);
        //
        LoginDTO actualLogin = authorizationService.setPassword(OWNER_DETAILS, USER.getId(), USER.getPassword());
        assertNull(actualLogin);
        Assertions.assertDoesNotThrow(() -> authorizationService.setPassword(OWNER_DETAILS, USER.getId(), USER.getPassword()));
    }

    @Test
    @DisplayName(value = "delete(UserDetails userDetails, long id): return LoginDTO")
    @Order(22)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void delete1() {
        when(loginRepository.findByLoginEqualsIgnoreCase(eq(OWNER.getLogin())))
                .thenReturn(Optional.of(OWNER));
        when(loginRepository.findById(eq(USER.getId())))
                .thenReturn(Optional.of(USER));
        when(loginMapper.loginToLoginDTO(eq(USER)))
                .thenReturn(USER_LOGIN_DTO);
        //
        LoginDTO actualLogin = authorizationService.delete(OWNER_DETAILS, USER.getId());
        assertNotNull(actualLogin);
        Assertions.assertDoesNotThrow(() -> authorizationService.delete(OWNER_DETAILS, USER.getId()));
        assertInstanceOf(LoginDTO.class, actualLogin);
        assertEquals(USER_LOGIN_DTO, actualLogin);
    }

    @Test
    @DisplayName(value = "delete(UserDetails userDetails, long id): throw IllegalArgumentException")
    @Order(23)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void delete2() {
        assertThrows(IllegalArgumentException.class, () -> authorizationService.delete(OWNER_DETAILS, OWNER.getId()));
    }

    @Test
    @DisplayName(value = "delete(UserDetails userDetails, long id): return LoginDTO")
    @Order(24)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void delete3() {
        when(loginRepository.findByLoginEqualsIgnoreCase(eq(OWNER.getLogin())))
                .thenReturn(Optional.of(OWNER));
        when(loginRepository.findById(eq(USER.getId())))
                .thenReturn(Optional.ofNullable(null));
        //
        assertThrows(IllegalArgumentException.class, () -> authorizationService.delete(OWNER_DETAILS, USER.getId()));
    }

    @Test
    @DisplayName(value = "delete(UserDetails userDetails, long id): return null")
    @Order(25)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void delete4() {
        when(loginRepository.findByLoginEqualsIgnoreCase(eq(ADMIN.getLogin())))
                .thenReturn(Optional.of(ADMIN));
        when(loginRepository.findById(eq(USER.getId())))
                .thenReturn(Optional.of(USER));
        //
        LoginDTO actualLogin = authorizationService.delete(ADMIN_DETAILS, USER.getId());
        assertNull(actualLogin);
        Assertions.assertDoesNotThrow(() -> authorizationService.delete(ADMIN_DETAILS, USER.getId()));
    }
}