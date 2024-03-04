package logic.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class RefreshWindowEventSource {
    private final static Logger log = LoggerFactory.getLogger(RefreshWindowEventSource.class);

    private final static List<RefreshWindowEventListener> listeners = new ArrayList<>();

    public static void addEventListener(RefreshWindowEventListener listener) {
        listeners.add(listener);
    }

    public static void refresh(JFrame frame) {
        log.info("Data Refresh");

        frame.setVisible(false);
        listeners.forEach(RefreshWindowEventListener::listen);
    }
}
