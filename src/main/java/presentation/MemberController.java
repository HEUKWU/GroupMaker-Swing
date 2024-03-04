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

    public void updateData(String name, boolean isChecked) {
        memberService.updateData(name, isChecked);
    }

    public void removeMember(String nameToRemove) {
        memberService.removeMember(nameToRemove);
    }

    public void saveData(String name) {
        memberService.saveData(name);
    }

    public List<List<String>> createGroups(String count) {
        return memberService.createGroups(count);
    }
}
