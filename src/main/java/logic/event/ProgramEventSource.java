package logic.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ProgramEventSource {
    private final static Logger log = LoggerFactory.getLogger(ProgramEventSource.class);

    private final static List<ProgramEventListener> listeners = new ArrayList<>();

    public static void addEventListener(ProgramEventListener listener) {
        listeners.add(listener);
    }

    public static void refresh(JFrame frame) {
        log.info("Data Refresh");

        listeners.forEach(ProgramEventListener::listen);
        frame.setVisible(false);
    }

    public static void developerInfo() {
        JFrame developerInfoFrame = new JFrame();

        developerInfoFrame.setTitle("개발자 정보");
        developerInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        developerInfoFrame.setLayout(new BorderLayout());
        developerInfoFrame.setSize(500, 500);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - developerInfoFrame.getWidth()) / 2;
        int y = (screenSize.height - developerInfoFrame.getHeight()) / 2;
        developerInfoFrame.setLocation(x, y);

        JPanel centerInfo = new JPanel();
        centerInfo.setLayout(new BoxLayout(centerInfo, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel("유영우");
        nameLabel.setFont(new Font(nameLabel.getName(), Font.BOLD, 30));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel gitHubLink = new JLabel("<html><a href=\"https://github.com/heukwu\">GitHub(HEUKWU)</a></html>");
        gitHubLink.setAlignmentX(Component.CENTER_ALIGNMENT);

        gitHubLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gitHubLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/heukwu"));
                } catch (Exception exception) {
                    throw new IllegalArgumentException("URL 오류");
                }
            }
        });

        centerInfo.add(nameLabel);
        centerInfo.add(gitHubLink);

        developerInfoFrame.add(centerInfo, BorderLayout.CENTER);

        developerInfoFrame.setVisible(true);
    }
}
