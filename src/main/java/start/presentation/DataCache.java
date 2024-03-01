package start.presentation;

import start.state.Member;

import java.util.ArrayList;
import java.util.List;

public class DataCache {
    private static DataCache INSTANCE;

    private DataCache() {}

    public static DataCache getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataCache();
        }
        return INSTANCE;
    }

    // 상태
    private List<Member> members = new ArrayList<>();

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public List<Member> getMembers() {
        return members;
    }
}
