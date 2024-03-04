import logic.MemberService;
import logic.event.RefreshWindowEventSource;
import presentation.MemberController;
import start.StartWindow;

import javax.swing.*;

public class Swing_Program {
    private static final MemberController memberController = new MemberController(new MemberService());

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StartWindow(memberController).setVisible(true));
        RefreshWindowEventSource.addEventListener(() -> new StartWindow(memberController).setVisible(true));
    }
}
