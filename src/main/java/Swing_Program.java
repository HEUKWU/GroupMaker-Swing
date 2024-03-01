import start.StartWindow;
import start.logic.MemberService;
import start.logic.event.RefreshWindowEventSource;
import start.presentation.MemberController;

import javax.swing.*;

public class Swing_Program {
    private static final MemberService memberService = new MemberService();
    private static final MemberController memberController = new MemberController(memberService);

    public static void main(String[] args) {
        RefreshWindowEventSource.addEventListener(() -> new StartWindow(memberController).setVisible(true));
        SwingUtilities.invokeLater(() -> new StartWindow(memberController).setVisible(true));
    }
}
