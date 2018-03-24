package pusher.net.domain.utils;

import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;
import org.springframework.stereotype.Component;

@Component
public class SessionUtils {

    public String getSessionId() {
        return VaadinSession.getCurrent().getSession().getId();
    }

    public WrappedSession getSession() {
        return VaadinSession.getCurrent().getSession();
    }
}
