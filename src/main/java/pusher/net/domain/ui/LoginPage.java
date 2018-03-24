package pusher.net.domain.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Binder;
import com.vaadin.data.BindingValidationStatus;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pusher.net.domain.dto.AuthDto;
import pusher.net.domain.models.LoginFormModel;
import pusher.net.domain.service.AuthService;
import pusher.net.domain.utils.SessionUtils;


@SpringUI(path = "/login")
@Theme("mytheme")
public class LoginPage extends UI {
    private final AuthService authService;
    private final SessionUtils sessionUtils;
    @Autowired
    public LoginPage(AuthService authService, SessionUtils sessionUtils) {
        this.authService = authService;
        this.sessionUtils = sessionUtils;
    }

    @Override
    protected void init(VaadinRequest request) {
        WrappedSession session = sessionUtils.getSession();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        initElements(horizontalLayout, request);
        setContent(horizontalLayout);
    }

    private void initElements(AbstractLayout layout, VaadinRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        TextField loginFld = new TextField("Login");
        PasswordField passwFld = new PasswordField("Password");
        Button loginBtn = new Button("Login");
        Button forgotPassBtn = new Button("Forgot password");
        forgotPassBtn.addClickListener(event -> {
            getPage().setLocation("/forgotPassword");
        });

        Binder.Binding<LoginFormModel, String> loginValidator = new Binder<LoginFormModel>().forField(loginFld)
                .withValidator(s -> !s.isEmpty() && !s.contains(" "), "failed")
                .bind(LoginFormModel::getLogin, LoginFormModel::setLogin);

        Binder.Binding<LoginFormModel, String> passwordValidator = new Binder<LoginFormModel>().forField(passwFld)
                .withValidator(s -> !s.isEmpty() && !s.contains(" "), "failed")
                .bind(LoginFormModel::getPassword, LoginFormModel::setPassword);

        loginBtn.addClickListener(event -> {
            if (loginValidator.validate().getStatus().equals(BindingValidationStatus.Status.OK)
                    && passwordValidator.validate().getStatus().equals(BindingValidationStatus.Status.OK)) {
                String login = loginFld.getValue();
                String password = passwFld.getValue();
                AuthDto response = authService.authenticate(login, password);
                Notification notification = new Notification("Hello " + response.getName());
                notification.show(Page.getCurrent());
                VaadinSession session = getSession();
                session.setAttribute("name", response.getName());
                session.setAttribute("roles", response.getRoles());
                getPage().setLocation("/");
            }
        });
        Button backBtn = new Button("Go to main page");
        backBtn.addClickListener(event -> {
            getPage().setLocation("/");
        });
        layout.addComponent(forgotPassBtn);
        layout.addComponent(loginFld);
        layout.addComponent(passwFld);
        layout.addComponent(loginBtn);
        layout.addComponent(backBtn);
    }
}
