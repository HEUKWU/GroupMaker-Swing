package start;

import logic.event.RefreshWindowEventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentation.MemberController;
import state.Member;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StartWindow extends JFrame {
    private final Logger log = LoggerFactory.getLogger(StartWindow.class);

    private final MemberController memberController;
    private static final String DATA_FILE = "src/main/resources/data.txt";

    public StartWindow(MemberController memberController) {
        this.memberController = memberController;

        setTitle("그룹 생성기");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(500, 1300);

        // 인원 현황
        JPanel memberCountStatusPanel = paintMemberCountStatusPanel();
        add(memberCountStatusPanel, BorderLayout.NORTH);

        // 인원 목록
        JScrollPane scrollPane = refreshNameListPanel(memberController);
        add(scrollPane, BorderLayout.WEST);

        // 인원 추가, 그룹 생성 버튼
        JPanel rightScreenPanel = new JPanel(new BorderLayout());
        add(rightScreenPanel, BorderLayout.EAST);

        JPanel groupListPanel = new JPanel();
        groupListPanel.setLayout(new BoxLayout(groupListPanel, BoxLayout.Y_AXIS));
        rightScreenPanel.add(groupListPanel, BorderLayout.NORTH);

        JPanel addMemberAndGroupingPanel = paintAddMemberAndGroupingPanel(groupListPanel);
        rightScreenPanel.add(addMemberAndGroupingPanel, BorderLayout.SOUTH);
    }

    private JPanel paintMemberCountStatusPanel() {
        List<Member> members = memberController.getMembers();
        int totalMemberCount = members.size();
        long checkedMemberCount = members.stream().filter(Member::isChecked).count();

        JPanel memberCountStatusPanel = new JPanel();
        memberCountStatusPanel.setLayout(new BoxLayout(memberCountStatusPanel, BoxLayout.Y_AXIS));
        memberCountStatusPanel.setBounds(100, 100, 100, 100);

        JLabel totalCountLabel = new JLabel("총인원 : " + totalMemberCount);
        memberCountStatusPanel.add(totalCountLabel);

        JLabel checkedCountLabel = new JLabel("참여 인원 : " + checkedMemberCount);
        memberCountStatusPanel.add(checkedCountLabel);

        return memberCountStatusPanel;
    }

    private JScrollPane refreshNameListPanel(MemberController memberController) {
        List<Member> members = memberController.getMembers();

        JPanel nameListPanel = new JPanel();
        nameListPanel.setLayout(new BoxLayout(nameListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(nameListPanel);
        scrollPane.setVerticalScrollBarPolicy(scrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        members.forEach(member -> {
            JPanel namePanel = new JPanel();

            JCheckBox checkBox = new JCheckBox(member.name(), member.isChecked());
            checkBox.addItemListener(new CheckBoxListener());

            JButton deleteButton = new JButton("삭제");
            deleteButton.addActionListener(e -> {
                removeData(member.name());
            });

            namePanel.add(checkBox);
            namePanel.add(deleteButton);

            nameListPanel.add(namePanel);
        });

        return scrollPane;
    }

    private void addPersonName(String name) {
        if (name.isEmpty() || name.equals(" ")) {
            log.error("이름에는 빈문자열이 입력될 수 없습니다.");
            throw new IllegalArgumentException("이름에는 빈문자열이 입력될 수 없습니다.");
        }

        saveData(name);

        RefreshWindowEventSource.refresh(this);
    }

    private class CheckBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox checkBox = (JCheckBox) e.getSource();
            String name = checkBox.getText();
            boolean isChecked = checkBox.isSelected();

            updateData(name, isChecked);
        }

    }
    private void updateData(String name, boolean isChecked) {
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
                    boolean currentChecked = Boolean.parseBoolean(data[1]);
                    if (currentName.equals(name)) {
                        // 해당 이름의 라인을 수정하여 새로운 체크 여부를 반영
                        writer.write(name + "," + isChecked + System.lineSeparator());
                    } else {
                        // 해당 이름이 아닌 다른 라인은 그대로 유지
                        writer.write(currentLine + System.lineSeparator());
                    }
                }
            }
            writer.close();
            reader.close();
            inputFile.delete();
            tempFile.renameTo(inputFile);

            RefreshWindowEventSource.refresh(this);
        } catch (IOException e) {
            log.error("파일 갱신에 실패했습니다.");
            throw new IllegalArgumentException("파일 갱신에 실패했습니다.");
        }
    }

    private void saveData(String name) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE, true))) {
            writer.println(name + "," + true);
        } catch (IOException e) {
            log.error("데이터를 저장하는 데 실패했습니다.");
            throw new IllegalArgumentException("데이터를 저장하는 데 실패했습니다.");
        }
    }

    private void removeData(String nameToRemove) {
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

            RefreshWindowEventSource.refresh(this);
        } catch (IOException e) {
            log.error("파일을 삭제하는 데 실패했습니다.");
            throw new IllegalArgumentException("파일을 삭제하는 데 실패했습니다.");
        }
    }

    private JPanel paintAddMemberAndGroupingPanel(JPanel groupListPanel) {
        JPanel addPersonPanel = new JPanel();
        JTextField nameField = new JTextField(15);

        JButton addPersonButton = new JButton("인원 추가");
        addPersonButton.addActionListener(e -> {
            addPersonName(nameField.getText());
            nameField.setText("");
        });

        addPersonPanel.add(nameField);
        addPersonPanel.add(addPersonButton);

        JPanel createGroupPanel = new JPanel();
        JTextField groupCountField = new JTextField(15);

        JButton createGroupButton = new JButton("그룹 생성");
        createGroupButton.addActionListener(e -> {
            int groupCount;
            List<String> members = memberController.getMembers()
                    .stream()
                    .filter(Member::isChecked)
                    .map(Member::name)
                    .collect(Collectors.toCollection(ArrayList::new));
            try {
                groupCount = Integer.parseInt(groupCountField.getText());
            } catch (Exception exception) {
                log.error("올바른 그룹 개수가 입력되지 않았습니다.");
                throw new IllegalArgumentException("올바른 그룹 개수가 입력되지 않았습니다.");
            }

            if (groupCount <= 0 || groupCount > members.size()) {
                log.error("올바른 그룹 개수가 입력되지 않았습니다.");
                throw new IllegalArgumentException("올바른 그룹 개수가 입력되지 않았습니다.");
            }

            divideGroups(groupListPanel, groupCount, members);
        });

        createGroupPanel.add(groupCountField);
        createGroupPanel.add(createGroupButton);

        JPanel rightBottomPanel = new JPanel();
        rightBottomPanel.setLayout(new BoxLayout(rightBottomPanel, BoxLayout.Y_AXIS));

        rightBottomPanel.add(addPersonPanel);
        rightBottomPanel.add(createGroupPanel);

        return rightBottomPanel;
    }

    private void divideGroups(JPanel groupListPanel, int groupCount, List<String> members) {
        groupListPanel.removeAll();
        groupListPanel.revalidate();
        groupListPanel.repaint();

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

        for (int i = 0; i < groups.size(); i++) {
            JList<String> list = new JList<>(groups.get(i).toArray(new String[0]));
            groupListPanel.add(new JLabel("그룹" + (i + 1)));
            groupListPanel.add(list);
        }
    }
}
