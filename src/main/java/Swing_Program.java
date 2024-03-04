import logic.MemberService;
import presentation.MemberController;
import start.StartWindow;

import javax.swing.*;

public class Swing_Program {
    private static final MemberController memberController = new MemberController(new MemberService());

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StartWindow(memberController).setVisible(true);
            }
        });
    }
}
