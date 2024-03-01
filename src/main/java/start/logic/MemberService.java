package start.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import start.state.Member;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MemberService {
    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);
    private static final String DATA_FILE = "src/main/resources/data.txt";

    public List<Member> readMembers() throws Exception {
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
            throw e;
        }
    }

    public void removeData(String nameToRemove) throws IOException {
        File originalFile = new File(DATA_FILE);
        File tempFile = new File("temp.txt");
        try (
                BufferedReader reader = new BufferedReader(new FileReader(originalFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String[] data = currentLine.split(",");
                if (data.length == 2) {
                    String name = data[0];
                    if (!name.equals(nameToRemove)) {
                        writer.write(currentLine + System.lineSeparator());
                    }
                }
            }
            writer.close();
            reader.close();
            if (!originalFile.delete()) {
                logger.warn("originalFile({})이 삭제되지 않았습니다.", DATA_FILE);
            }
            if (!tempFile.renameTo(originalFile)) {
                logger.warn("tempFile을 originalFile({})로 대체하는 데 실패했습니다.", DATA_FILE);
            }
        } catch (IOException e) {
            logger.error("멤버 정보를 삭제하는 데 실패했습니다.", e);
            throw e;
        }
    }
}
