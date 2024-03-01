import start.StartWindow;
import start.logic.MemberService;
import start.presentation.DataCache;
import start.state.Member;

import javax.swing.*;
import java.util.List;

public class Swing_Program {
    public static void main(String[] args) throws Exception {
        MemberService memberService = new MemberService();

        List<Member> members = memberService.readMembers();
        // 다른 데이터들 불러와서 set, set 하지 않고, init(members, 다른 데이터, 다른 데이터1 ...) 이렇게 한다.
        DataCache.getInstance().setMembers(members);

        SwingUtilities.invokeLater(() -> {
            new StartWindow().setVisible(true);
        });
    }
}
