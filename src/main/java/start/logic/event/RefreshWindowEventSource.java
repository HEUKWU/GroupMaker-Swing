package start.logic.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RefreshWindowEventSource {
    private final static Logger logger = LoggerFactory.getLogger(RefreshWindowEventSource.class);

    private static final List<RefreshWindowEventListener> listeners = new ArrayList<>();

    public static void addEventListener(RefreshWindowEventListener listener) {
        listeners.add(listener);
    }

    public static void refresh() {
        logger.info("데이터 refresh");
        listeners.forEach(RefreshWindowEventListener::listen);
    }
}
