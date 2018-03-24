package pusher.net.domain.dto;

import java.util.ArrayList;
import java.util.List;

public class UsersLoginEmailsDto {
    private List<LoginEmailWrapper> pool = new ArrayList<>();

    public UsersLoginEmailsDto() {
    }

    public UsersLoginEmailsDto(List<LoginEmailWrapper> pool) {
        this.pool = pool;
    }

    public List<LoginEmailWrapper> getPool() {
        return pool;
    }

    public void setPool(List<LoginEmailWrapper> pool) {
        this.pool = pool;
    }

    public static class LoginEmailWrapper {
        private String login;
        private String email;

        public LoginEmailWrapper() {
        }

        public LoginEmailWrapper(String login, String email) {
            this.login = login;
            this.email = email;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
