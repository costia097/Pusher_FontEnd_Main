package pusher.net.domain.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Binder;
import com.vaadin.data.BindingValidationStatus;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pusher.net.domain.dto.ForgotFormDto;
import pusher.net.domain.service.CacheService;

@SpringUI(path = "/forgotPassword")
@Theme("mytheme")
public class ForgotPasswordPageSendFirst extends UI {
    private final RestTemplate restTemplate;
    private final CacheService cacheService;

    @Autowired
    public ForgotPasswordPageSendFirst(RestTemplate restTemplate, CacheService cacheService) {
        this.restTemplate = restTemplate;
        this.cacheService = cacheService;
    }

    @Override
    protected void init(VaadinRequest request) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        TextField forgotTxF = new TextField("Enter here your email: ");
        Button forgotBtn = new Button("Submit");

        Binder.Binding<ForgotFormDto, String> forgotFormValidator = new Binder<ForgotFormDto>()
                .forField(forgotTxF)
                .withValidator(new EmailValidator("error"))
                .withValidator(s -> cacheService.getAllEmail().contains(forgotTxF.getValue()),"User with this email doesn't exist")
                .bind(ForgotFormDto::getEmail, ForgotFormDto::setEmail);

        forgotBtn.addClickListener(event -> {
            if (forgotFormValidator.validate().getStatus().equals(BindingValidationStatus.Status.OK)) {
                String email = forgotTxF.getValue();
                ResponseEntity<Void> responseEntity = restTemplate.getForEntity("http://localhost:8080/person/forgotPassword?email={email}", Void.class, email);
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    getSession().setAttribute("email", forgotTxF.getValue());
                    getPage().setLocation("/forgotPasswordSend");
                }
            }
        });

        horizontalLayout.addComponent(forgotTxF);
        horizontalLayout.addComponent(forgotBtn);
        setContent(horizontalLayout);
    }
}
