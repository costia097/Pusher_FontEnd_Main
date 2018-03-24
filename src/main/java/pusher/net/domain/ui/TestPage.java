package pusher.net.domain.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

@SpringUI(path = "/home")
public class TestPage extends UI {
    @Override
    protected void init(VaadinRequest request) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Button button = new Button("AAA");
        horizontalLayout.addComponent(button);
        setContent(horizontalLayout);
        WrappedSession session = VaadinSession.getCurrent().getSession();
    }
}
