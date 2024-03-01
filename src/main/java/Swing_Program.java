import start.StartWindow;
import start.logic.MemberService;
import start.presentation.MemberController;

import javax.swing.*;

public class Swing_Program {
    public static void main(String[] args) throws Exception {
        MemberService memberService = new MemberService();
        MemberController memberController = new MemberController(memberService);

        SwingUtilities.invokeLater(() -> {
            new StartWindow(memberController).setVisible(true);
        });
    }
}
