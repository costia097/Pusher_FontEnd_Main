package pusher.net.domain.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pusher.net.domain.service.AuthService;
import pusher.net.domain.service.CacheService;
import pusher.net.domain.utils.SessionUtils;

@SpringUI(path = "/")
@Theme("mytheme")
public class MainPage extends UI{

    private final CacheService cacheService;

    private final SessionUtils sessionUtils;

    private final AuthService authService;

    private final TestPage testPage;

    private final RestTemplate restTemplate;


    @Autowired
    public MainPage(CacheService cacheService, SessionUtils sessionUtils, AuthService authService, TestPage testPage, RestTemplate restTemplate) {
        this.cacheService = cacheService;
        this.sessionUtils = sessionUtils;
        this.authService = authService;
        this.testPage = testPage;
        this.restTemplate = restTemplate;
    }

    @Override
    protected void init(VaadinRequest request) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        MenuBar menuBar = new MenuBar();
        MenuBar.MenuItem itemHome = menuBar.addItem("Home", null, selectedItem -> {
            getPage().setLocation("/home");
        });
        MenuBar.MenuItem itemLogin = menuBar.addItem("Login", null, selectedItem -> {
            getPage().setLocation("/login");
        });
        MenuBar.MenuItem itemRegistration = menuBar.addItem("Registation", null, selectedItem -> {
            getPage().setLocation("/registration");
        });
        MenuBar.MenuItem itemDashbord = menuBar.addItem("Dashbord", null, selectedItem -> {

        });

        Label textField = new Label();
        Button logoutBtn = new Button("Logout");
        logoutBtn.addClickListener(event -> {
            HttpHeaders authHeaders = authService.getAuthHeaders();
            HttpEntity<?> httpRequest = new HttpEntity<>(authHeaders);
            ResponseEntity<Void> responseEntity = restTemplate.postForEntity("http://localhost:8080/person/logout", httpRequest, Void.class);
            System.out.println(responseEntity);
            getSession().getSession().invalidate();
            getPage().setLocation("/");
        });
        logoutBtn.setVisible(false);
        VaadinSession session = getSession();

        String name = (String) session.getAttribute("name");
        if (name != null) {
            textField.setValue("Hello, " + name);
            logoutBtn.setVisible(true);
        }
        VerticalLayout verticalLayout = new VerticalLayout();

        Button button1 = new Button("1");
        Button button2 = new Button("2");
        Button button3 = new Button("3");
        Button button4 = new Button("4");

        verticalLayout.addComponent(button1);
        verticalLayout.addComponent(button2);
        verticalLayout.addComponent(button3);
        verticalLayout.addComponent(button4);

        horizontalLayout.addComponent(menuBar);
        horizontalLayout.addComponent(verticalLayout);
        horizontalLayout.addComponent(textField);
        horizontalLayout.addComponent(logoutBtn);
        setContent(horizontalLayout);
    }
}
