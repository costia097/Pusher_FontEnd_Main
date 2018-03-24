package pusher.net.domain.models;

import org.springframework.stereotype.Component;

@Component
public class RegistrationFormModel {

    public RegistrationFormModel() {
    }

    public RegistrationFormModel(String firstName, String lastName, String login, String email, String gender, String password, String repeatPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.email = email;
        this.gender = gender;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    private String firstName;
    private String lastName;
    private String login;
    private String email;
    private String gender;
    private String password;
    private String repeatPassword;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
