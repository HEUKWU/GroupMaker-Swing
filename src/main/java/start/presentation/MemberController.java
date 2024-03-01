package start.presentation;

import start.logic.MemberService;
import start.state.Member;

import java.util.List;

public class MemberController {
    // 캐싱
    private final List<Member> members;
    private final MemberService memberService;

    public MemberController(MemberService memberService) throws Exception {
        this.memberService = memberService;
        members = memberService.readMembers();
    }

    public List<Member> getMembers() {
        return members;
    }
}
