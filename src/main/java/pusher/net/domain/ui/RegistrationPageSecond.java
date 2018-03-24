package pusher.net.domain.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

@SpringUI(path = "/emailSend")
@Theme("mytheme")
public class RegistrationPageSecond extends UI {
    @Override
    protected void init(VaadinRequest request) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Button goBackBtn = new Button("Go to main page");
        goBackBtn.addClickListener(event -> {
            getPage().setLocation("/");
        });
        VaadinSession session = getSession();
        String email = (String) session.getAttribute("email");
        Label infolLabel = new Label("On your email " + email + " has been send email. Please check and confirm it");
        horizontalLayout.addComponent(infolLabel);
        horizontalLayout.addComponent(goBackBtn);
        setContent(horizontalLayout);
        if (email != null) {
            infolLabel.setVisible(true);
        } else {
            infolLabel.setVisible(false);
        }
    }
}
