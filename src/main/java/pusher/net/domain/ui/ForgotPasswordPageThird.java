package pusher.net.domain.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Binder;
import com.vaadin.data.BindingValidationStatus;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pusher.net.domain.dto.UpdatePasswordFormDto;
import pusher.net.domain.service.AuthService;

@SpringUI(path = "/updatePassword")
@Theme("mytheme")
public class ForgotPasswordPageThird extends UI {
    private final RestTemplate restTemplate;
    private final AuthService authService;

    @Autowired
    public ForgotPasswordPageThird(RestTemplate restTemplate, AuthService authService) {
        this.restTemplate = restTemplate;
        this.authService = authService;
    }

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout verticalLayout = new VerticalLayout();
        PasswordField newPassFld = new PasswordField("Write here you new password: ");
        PasswordField newPassRepeatFld = new PasswordField("Repeat please: ");
        Button submitBtn = new Button("Submit");

        Binder.Binding<UpdatePasswordFormDto, String> updatePassFormValidator = new Binder<UpdatePasswordFormDto>()
                .forField(newPassFld)
                .withValidator(s -> !s.isEmpty() && !s.contains(" "), "failed")
                .bind(UpdatePasswordFormDto::getNewPass, UpdatePasswordFormDto::setNewPass);

        Binder.Binding<UpdatePasswordFormDto, String> updatePassRepeatFormValidator = new Binder<UpdatePasswordFormDto>()
                .forField(newPassRepeatFld)
                .withValidator(s -> !s.isEmpty() && !s.contains(" "), "failed")
                .withValidator(s -> s.equals(newPassFld.getValue()), "not equals")
                .bind(UpdatePasswordFormDto::getRepeatPass, UpdatePasswordFormDto::setRepeatPass);

        submitBtn.addClickListener(event -> {
            if (updatePassFormValidator.validate().getStatus().equals(BindingValidationStatus.Status.OK)
                    && updatePassRepeatFormValidator.validate().getStatus().equals(BindingValidationStatus.Status.OK)) {
                UpdatePasswordFormDto updatePasswordFormDto = new UpdatePasswordFormDto();
                updatePasswordFormDto.setNewPass(newPassFld.getValue());
                updatePasswordFormDto.setEmail(getSession().getAttribute("email").toString());
                HttpEntity<?> requestEntity = new HttpEntity<>(updatePasswordFormDto);
                ResponseEntity<Void> responseEntity = restTemplate.postForEntity("http://localhost:8080/person/forgotPassword/update", requestEntity, Void.class);
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    getPage().setLocation("/updatePasswordSuc");
                }
            }
        });
        verticalLayout.addComponent(newPassFld);
        verticalLayout.addComponent(newPassRepeatFld);
        verticalLayout.addComponent(submitBtn);
        setContent(verticalLayout);
    }
}
