package start;

import start.presentation.DataCache;
import start.state.Member;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.List;

public class StartWindow extends JFrame {

    private static final String DATA_FILE = "src/main/resources/data.txt";

    public StartWindow() {
        setTitle("그룹 생성기");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(500, 1300);

        JPanel memberCountStatus = paintMemberCountStatusPanel();
        add(memberCountStatus, BorderLayout.NORTH);

        JScrollPane scrollPane = paintNameListPanel();
        add(scrollPane, BorderLayout.WEST);

        JPanel rightScreenPanel = new JPanel(new BorderLayout());

        JPanel groupListPanel = new JPanel();
        groupListPanel.setLayout(new BoxLayout(groupListPanel, BoxLayout.Y_AXIS));
        rightScreenPanel.add(groupListPanel, BorderLayout.NORTH);

        JPanel addMemberAndGroupingPanel = paintAddMemberAndGroupingPanel();
        rightScreenPanel.add(addMemberAndGroupingPanel, BorderLayout.SOUTH);
        add(rightScreenPanel, BorderLayout.EAST);
    }

    private static JPanel paintMemberCountStatusPanel() {
        List<Member> members = DataCache.getInstance().getMembers();
        int totalMemberCount = members.size();
        long checkedMemberCount = members.stream().filter(Member::isChecked).count();

        JPanel memberCountStatus = new JPanel();
        memberCountStatus.setLayout(new BoxLayout(memberCountStatus, BoxLayout.Y_AXIS));
        memberCountStatus.setBounds(100, 100, 100, 100);

        JLabel totalCountLabel = new JLabel("총인원 : " + totalMemberCount);
        memberCountStatus.add(totalCountLabel);

        JLabel checkedCountLabel = new JLabel("참여 인원 : " + checkedMemberCount);
        memberCountStatus.add(checkedCountLabel);
        return memberCountStatus;
    }

    private JScrollPane paintNameListPanel() {
        List<Member> members = DataCache.getInstance().getMembers();

        JPanel nameListPanel = new JPanel();
        nameListPanel.setLayout(new BoxLayout(nameListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(nameListPanel);
        scrollPane.setVerticalScrollBarPolicy(scrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        initNameListPanel(nameListPanel, members);
        return scrollPane;
    }

    private void initNameListPanel(JPanel nameListPanel, List<Member> members) {
        members.forEach(member -> {
            JPanel namePanel = new JPanel();

            JCheckBox checkBox = new JCheckBox(member.name(), member.isChecked());
            checkBox.addItemListener(new CheckBoxListener());

            JButton deleteButton = new JButton("삭제");
            deleteButton.addActionListener(e -> {
                // TODO : 삭제시키는 로직이 들어갈 예정
            });

            namePanel.add(checkBox);
            namePanel.add(deleteButton);

            nameListPanel.add(namePanel);
        });
        nameListPanel.revalidate();
        nameListPanel.repaint();
    }

    private static JPanel paintAddMemberAndGroupingPanel() {
        JPanel addMemberAndGroupingPanel = new JPanel();
        addMemberAndGroupingPanel.setLayout(new BoxLayout(addMemberAndGroupingPanel, BoxLayout.Y_AXIS));
        addMemberAndGroupingPanel.add(paintAddPersonPanel());
        addMemberAndGroupingPanel.add(paintCreateGroupPanel());
        return addMemberAndGroupingPanel;
    }

    private static JPanel paintAddPersonPanel() {
        JPanel addPersonPanel = new JPanel();

        JTextField nameField = new JTextField(15);

        JButton addPersonButton = new JButton("인원 추가");
        addPersonButton.addActionListener(e -> {
//            addPersonName(nameListPanel, nameField.getText(), true, true);
        });

        addPersonPanel.add(nameField);
        addPersonPanel.add(addPersonButton);
        return addPersonPanel;
    }

    private static JPanel paintCreateGroupPanel() {
        JPanel createGroupPanel = new JPanel();
        JTextField groupCountField = new JTextField(15);

        JButton createGroupButton = new JButton("그룹 생성");
        createGroupButton.addActionListener(e -> {
//            readDataFromFile();
        });
        createGroupPanel.add(groupCountField);
        createGroupPanel.add(createGroupButton);
        return createGroupPanel;
    }

//    private void addPersonName(JPanel nameListPanel, String name, boolean isChecked, boolean isFirst) {
//        if (name.isEmpty() || name.equals(" ")) {
//            throw new IllegalArgumentException("이름이 비어있으면 안 됩니다.");
//        }
//
//        JPanel namePanel = new JPanel();
//        JButton deleteButton = new JButton("삭제");
//        JCheckBox checkBox = new JCheckBox(name, isChecked);
//
//        checkBox.addItemListener(new CheckBoxListener());
//
//        namePanel.add(checkBox);
//        namePanel.add(deleteButton);
//
//        deleteButton.addActionListener(e -> {
//            deleteName(nameListPanel, namePanel, checkBox.isSelected(), name);
//        });
//
//        nameListPanel.add(namePanel);
//        nameListPanel.revalidate();
//        nameListPanel.repaint();
//        nameField.setText("");
//
//        if (isFirst) {
//            saveData(name); // 데이터를 파일에 저장
//        }
//    }

//    private void deleteName(JPanel nameListPanel, JPanel panel, boolean isSelected, String name) {
//        totalCount--;
//        if (isSelected) {
//            checkedCount--;
//        }
//        nameListPanel.remove(panel);
//        nameListPanel.revalidate();
//        nameListPanel.repaint();
//
//        removeData(name);
//    }

    private class CheckBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox checkBox = (JCheckBox) e.getSource();
            String name = checkBox.getText();
            boolean isChecked = checkBox.isSelected();
//            if (isChecked) {
//                checkedCount++;
//            } else {
//                checkedCount--;
//            }

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveData(String name) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE, true))) {
            writer.println(name + "," + true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void loadData() {
//        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] data = line.split(",");
//                if (data.length == 2) {
//                    String name = data[0];
//                    boolean isChecked = Boolean.parseBoolean(data[1]);
//                    addPersonName(name, isChecked, false);
//                }
//            }
//
//        } catch (IOException | NumberFormatException e) {
//            e.printStackTrace();
//        }
//    }

//    private void readDataFromFile() {
//        ArrayList<String> names = new ArrayList<>();
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // 파일에서 각 줄을 파싱하여 이름과 체크 여부를 가져옵니다.
//                String[] parts = line.split(",");
//                if (parts.length == 2) {
//                    String name = parts[0];
//                    boolean isChecked = Boolean.parseBoolean(parts[1]);
//                    if (isChecked) {
//                        // 체크된 항목인 경우에만 리스트에 추가합니다.
//                        names.add(name);
//                    }
//                }
//            }
//
//            divideGroups(Integer.parseInt(groupCountField.getText()), names);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    private void divideGroups(int groupCount, ArrayList<String> names) {
//        rightTopPanel.removeAll();
//        rightTopPanel.revalidate();
//        rightTopPanel.repaint();
//
//        List<List<String>> groups = new ArrayList<>();
//        Collections.shuffle(names);
//        for (int i = 0; i < groupCount; i++) {
//            groups.add(new ArrayList<>());
//        }
//
//        int currentGroup = 0;
//        for (String person : names) {
//            groups.get(currentGroup).add(person);
//            currentGroup = (currentGroup + 1) % groupCount;
//        }
//
//        for (int i = 0; i < groups.size(); i++) {
//            JList<String> list = new JList<>(groups.get(i).toArray(new String[0]));
//            rightTopPanel.add(new JLabel("그룹" + (i + 1)));
//            rightTopPanel.add(list);
//        }
//    }
}
