package pusher.net.domain.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

@SpringUI(path = "/updatePasswordSuc")
@Theme("mytheme")
public class ForgotPasswordPageFour extends UI {

    @Override
    protected void init(VaadinRequest request) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label infoLabel = new Label("Now log in use you ew password");
        Button button = new Button("Go to login page");
        button.addClickListener(event -> {
            getPage().setLocation("/login");
        });
        horizontalLayout.addComponent(infoLabel);
        horizontalLayout.addComponent(button);
        setContent(horizontalLayout);
    }
}
