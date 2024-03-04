import logic.MemberService;
import logic.event.ProgramEventSource;
import presentation.MemberController;
import start.StartWindow;

import javax.swing.*;

public class Swing_Program {
    private static final MemberController memberController = new MemberController(new MemberService());

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StartWindow(memberController).setVisible(true));
        ProgramEventSource.addEventListener(() -> new StartWindow(memberController).setVisible(true));

        Thread.setDefaultUncaughtExceptionHandler((t, e) ->
                JOptionPane.showMessageDialog(null,
                        e.getMessage(),
                        "알림",
                        JOptionPane.ERROR_MESSAGE));
    }
}
