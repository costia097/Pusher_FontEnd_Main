package pusher.net.domain.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

@SpringUI(path = "/forgotPasswordSend")
@Theme("mytheme")
public class ForgotPasswordPageSendSecond extends UI {
    @Override
    protected void init(VaadinRequest request) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label sendLabe = new Label("On your email" + getSession().getAttribute("email") + "has been send messadge with next instruction");
        Button backToMailnPage = new Button("Back to main page");
        backToMailnPage.addClickListener(event -> {
            getPage().setLocation("/");
        });
        horizontalLayout.addComponent(backToMailnPage);
        horizontalLayout.addComponent(sendLabe);
        setContent(horizontalLayout);
    }
}
