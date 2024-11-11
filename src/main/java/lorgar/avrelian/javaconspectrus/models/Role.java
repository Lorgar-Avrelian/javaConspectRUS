package lorgar.avrelian.javaconspectrus.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN, OWNER;

    @Override
    public String getAuthority() {
        return name();
    }
}
