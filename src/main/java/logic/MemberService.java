package logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.Member;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MemberService {
    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);
    private static final String DATA_FILE = "src/main/resources/data.txt";

    public List<Member> readMembers() {
        List<Member> members = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 2) {
                    String name = data[0];
                    boolean isChecked = Boolean.parseBoolean(data[1]);
                    Member member = new Member(name, isChecked);
                    members.add(member);
                } else {
                    throw new IllegalArgumentException("잘못된 데이터가 들어있습니다.");
                }
            }
            return members;
        } catch (Exception e) {
            logger.error("멤버 정보를 읽어오는 데에 에러가 발생했습니다.", e);
            throw new RuntimeException(e);
        }
    }
}
