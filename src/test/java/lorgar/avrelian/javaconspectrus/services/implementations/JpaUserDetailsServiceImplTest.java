package lorgar.avrelian.javaconspectrus.services.implementations;

import lorgar.avrelian.javaconspectrus.constants.Constants;
import lorgar.avrelian.javaconspectrus.repository.LoginRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static lorgar.avrelian.javaconspectrus.constants.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@DisplayName(value = "Test of JpaUserDetailsServiceImpl")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JpaUserDetailsServiceImplTest {
    @Mock
    private LoginRepository loginRepository;
    @InjectMocks
    private JpaUserDetailsServiceImpl jpaUserDetailsServiceImpl;

    @Test
    @DisplayName(value = "loadUserByUsername(String username): return UserDetails")
    @Order(1)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void loadUserByUsername1() {
        when(loginRepository.findByLoginEqualsIgnoreCase(eq(OWNER.getLogin())))
                .thenReturn(Optional.of(OWNER));
        //
        UserDetails actualUser = jpaUserDetailsServiceImpl.loadUserByUsername(OWNER.getLogin());
        assertEquals(OWNER_DETAILS, actualUser);
        assertInstanceOf(UserDetails.class, actualUser);
        assertDoesNotThrow(() -> jpaUserDetailsServiceImpl.loadUserByUsername(OWNER.getLogin()));
    }

    @Test
    @DisplayName(value = "loadUserByUsername(String username): throw UsernameNotFoundException")
    @Order(2)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void loadUserByUsername2() {
        when(loginRepository.findByLoginEqualsIgnoreCase(eq(USER.getLogin())))
                .thenReturn(Optional.ofNullable(null));
        //
        assertThrows(UsernameNotFoundException.class, () -> jpaUserDetailsServiceImpl.loadUserByUsername(USER.getLogin()));
    }
}