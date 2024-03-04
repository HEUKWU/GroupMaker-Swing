package logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.Member;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MemberService {
    private static final Logger log = LoggerFactory.getLogger(MemberService.class);
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
            log.error("멤버 정보를 읽어오는 데에 에러가 발생했습니다.", e);
            throw new RuntimeException(e);
        }
    }

    public void updateData(String name, boolean isChecked) {
        try {
            File inputFile = new File(DATA_FILE);
            File tempFile = new File("temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String[] data = currentLine.split(",");
                if (data.length == 2) {
                    String currentName = data[0];
                    if (currentName.equals(name)) {
                        writer.write(name + "," + isChecked + System.lineSeparator());
                    } else {
                        writer.write(currentLine + System.lineSeparator());
                    }
                }
            }
            writer.close();
            reader.close();
            inputFile.delete();
            tempFile.renameTo(inputFile);
        } catch (IOException e) {
            log.error("파일 갱신에 실패했습니다.");
            throw new IllegalArgumentException("파일 갱신에 실패했습니다.");
        }
    }

    public void removeMember(String nameToRemove) {
        try {
            File inputFile = new File(DATA_FILE);
            File tempFile = new File("temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

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
            inputFile.delete();
            tempFile.renameTo(inputFile);
        } catch (IOException e) {
            log.error("파일을 삭제하는 데 실패했습니다.");
            throw new IllegalArgumentException("파일을 삭제하는 데 실패했습니다.");
        }
    }

    public void saveData(String name) {
        if (name.isEmpty() || name.equals(" ")) {
            log.error("이름에는 빈문자열이 입력될 수 없습니다.");
            throw new IllegalArgumentException("이름에는 빈문자열이 입력될 수 없습니다.");
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE, true))) {
            writer.println(name + "," + true);
        } catch (IOException e) {
            log.error("데이터를 저장하는 데 실패했습니다.");
            throw new IllegalArgumentException("데이터를 저장하는 데 실패했습니다.");
        }
    }

    public List<List<String>> createGroups(String count) {
        int groupCount;
        List<String> members = readMembers()
                .stream()
                .filter(Member::isChecked)
                .map(Member::name)
                .collect(Collectors.toCollection(ArrayList::new));
        try {
            groupCount = Integer.parseInt(count);
        } catch (Exception exception) {
            log.error("올바른 그룹 개수가 입력되지 않았습니다.");
            throw new IllegalArgumentException("올바른 그룹 개수가 입력되지 않았습니다.");
        }

        if (groupCount <= 0 || groupCount > members.size()) {
            log.error("올바른 그룹 개수가 입력되지 않았습니다.");
            throw new IllegalArgumentException("올바른 그룹 개수가 입력되지 않았습니다.");
        }

        Collections.shuffle(members);

        List<List<String>> groups = new ArrayList<>();
        for (int i = 0; i < groupCount; i++) {
            groups.add(new ArrayList<>());
        }

        int currentGroup = 0;
        for (String member : members) {
            groups.get(currentGroup).add(member);
            currentGroup = (currentGroup + 1) % groupCount;
        }

        return groups;
    }
}
