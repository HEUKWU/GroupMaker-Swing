package presentation;

import logic.MemberService;
import state.Member;

import java.util.List;

public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    public List<Member> getMembers() {
        return memberService.readMembers();
    }
}
