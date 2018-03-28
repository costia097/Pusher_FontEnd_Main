package pusher.net.domain.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Binder;
import com.vaadin.data.BindingValidationStatus;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pusher.net.domain.models.RegistrationFormModel;
import pusher.net.domain.service.AuthService;
import pusher.net.domain.service.CacheService;

import java.util.Arrays;
import java.util.List;

@SpringUI(path = "/registration")
@Theme("mytheme")
public class RegistrationPageFirst extends UI {
    private final AuthService authService;
    private String[] choosenGender = new String[1];
    private final RestTemplate restTemplate;
    private final CacheService cacheService;

    @Autowired
    public RegistrationPageFirst(AuthService authService, RestTemplate restTemplate, CacheService cacheService) {
        this.authService = authService;
        this.restTemplate = restTemplate;
        this.cacheService = cacheService;
    }

    @Override
    protected void init(VaadinRequest request) {
        Label title = new Label("Registration form");
        FormLayout formLayout = new FormLayout();
        TextField firstNameTxF = new TextField("FirstName");
        TextField lastNameTxF = new TextField("LastName");
        TextField loginTxf = new TextField("Login");
        TextField emailTxF = new TextField("Email");
        PasswordField passwordTxF = new PasswordField("Password");
        PasswordField repeatPasswordTxF = new PasswordField("RepeatPassword");
        List<String> genderValue = Arrays.asList("Male", "Female");
        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>("Select option: ", genderValue);
        radioButtonGroup.addValueChangeListener(event -> {
            choosenGender[0] = event.getValue();
        });
        Button submitBtn = new Button("RegistrationNow");

        Binder.Binding<RegistrationFormModel, String> firstNameValidator = new Binder<RegistrationFormModel>()
                .forField(firstNameTxF)
                .withValidator(s -> !s.isEmpty() && !s.contains(" "), "failed")
                .bind(RegistrationFormModel::getFirstName, RegistrationFormModel::setLastName);

        Binder.Binding<RegistrationFormModel, String> lastNameValidator = new Binder<RegistrationFormModel>()
                .forField(lastNameTxF)
                .withValidator(s -> !s.isEmpty() && !s.contains(" "), "failed")
                .bind(RegistrationFormModel::getLastName, RegistrationFormModel::setLastName);

        Binder.Binding<RegistrationFormModel, String> loginValidator = new Binder<RegistrationFormModel>()
                .forField(loginTxf)
                .withValidator(s -> !s.isEmpty() && !s.contains(" "), "failed")
                .withValidator(s -> !cacheService.getAllLogins().contains(s), "dooblicate")
                .bind(RegistrationFormModel::getLogin, RegistrationFormModel::setLogin);

        Binder.Binding<RegistrationFormModel, String> emailValidator = new Binder<RegistrationFormModel>()
                .forField(emailTxF)
                .withValidator(new EmailValidator("error"))
                .withValidator(s -> !cacheService.getAllEmail().contains(s), "dooblicate")
                .bind(RegistrationFormModel::getEmail, RegistrationFormModel::setEmail);

        Binder.Binding<RegistrationFormModel, String> passwordValidator = new Binder<RegistrationFormModel>()
                .forField(passwordTxF)
                .withValidator(s -> !s.isEmpty() && !s.contains(" "), "failed")
                .bind(RegistrationFormModel::getPassword, RegistrationFormModel::setPassword);

        Binder.Binding<RegistrationFormModel, String> repeatPasswordValidator = new Binder<RegistrationFormModel>()
                .forField(repeatPasswordTxF)
                .withValidator(s -> !s.isEmpty() && !s.contains(" "), "failed")
                .withValidator(s -> passwordTxF.getValue().equals(repeatPasswordTxF.getValue()), "not repeat")
                .bind(RegistrationFormModel::getRepeatPassword, RegistrationFormModel::setRepeatPassword);

        submitBtn.addClickListener(event -> {
            if ((firstNameValidator.validate().getStatus().equals(BindingValidationStatus.Status.OK))
                    && (lastNameValidator.validate().getStatus().equals(BindingValidationStatus.Status.OK))
                    && (loginValidator.validate().getStatus().equals(BindingValidationStatus.Status.OK))
                    && (emailValidator.validate().getStatus().equals(BindingValidationStatus.Status.OK))
                    &&  (passwordValidator.validate().getStatus().equals(BindingValidationStatus.Status.OK))
                    && (repeatPasswordValidator.validate().getStatus().equals(BindingValidationStatus.Status.OK)))
            {
                String firstName = firstNameTxF.getValue();
                String lastName = lastNameTxF.getValue();
                String login = loginTxf.getValue();
                String email = emailTxF.getValue();
                String gender = choosenGender[0];
                String password = passwordTxF.getValue();
                RegistrationFormModel registrationFormModel = new RegistrationFormModel(firstName, lastName, login, email, gender, password, null);
                HttpEntity<?> requestEntity = new HttpEntity<>(registrationFormModel, authService.getJsonSupportHeader());
                ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8080/person/registration", requestEntity, String.class);
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    String body = responseEntity.getBody();
                    System.out.println(body);
                    VaadinSession session = getSession();
                    session.setAttribute("email", email);
                    HttpHeaders emailHeaders = authService.getJsonSupportHeader();
                    emailHeaders.set("email", email);
                    emailHeaders.set("name", firstName);
                    HttpEntity<?> emailRequest = new HttpEntity<>(emailHeaders);
                    ResponseEntity<String> responseEntity1 = restTemplate.postForEntity("http://localhost:8080/person/sendEmail", emailRequest, String.class);
                    getPage().setLocation("/emailSend");
                    cacheService.addLoginEmailToCache(login, email);
                }
            }
        });
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Button goBackBtn = new Button("Go to main page");
        goBackBtn.addClickListener(event -> {
            getPage().setLocation("/");
        });

        horizontalLayout.addComponent(goBackBtn);
        horizontalLayout.addComponent(submitBtn);

        formLayout.addComponent(title);
        formLayout.addComponent(firstNameTxF);
        formLayout.addComponent(lastNameTxF);
        formLayout.addComponent(loginTxf);
        formLayout.addComponent(emailTxF);
        formLayout.addComponent(radioButtonGroup);
        formLayout.addComponent(passwordTxF);
        formLayout.addComponent(repeatPasswordTxF);
        formLayout.addComponent(horizontalLayout);
        setContent(formLayout);
    }
}
