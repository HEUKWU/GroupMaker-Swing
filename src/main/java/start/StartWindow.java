package start;

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

public class StartWindow extends JFrame {
    private JPanel addPersonPanel;
    private JPanel nameListPanel;
    private JTextField nameField;
    private JButton addPersonButton;
    private JTextField groupCountField;
    private JButton createGroupButton;
    private JPanel createGroupPanel;
    private JPanel rightTopPanel;
    private JPanel rightBottomPanel;
    private JPanel rightPanel;
    private int totalCount = 0;
    private int checkedCount = 0;
    private static final String DATA_FILE = "src/main/resources/data.txt";
    private final MemberController memberController;

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
        nameListPanel = new JPanel();
        nameListPanel.setLayout(new BoxLayout(nameListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(nameListPanel);
        scrollPane.setVerticalScrollBarPolicy(scrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.WEST);

        // 인원 추가, 그룹 생성 버튼
        rightPanel = new JPanel(new BorderLayout());
        add(rightPanel, BorderLayout.EAST);

        addPersonPanel = new JPanel();
        nameField = new JTextField(15);

        addPersonButton = new JButton("인원 추가");
        addPersonButton.addActionListener(e -> {
            addPersonName(nameField.getText(), true, true);
        });

        rightTopPanel = new JPanel();
        rightTopPanel.setLayout(new BoxLayout(rightTopPanel, BoxLayout.Y_AXIS));
        rightPanel.add(rightTopPanel, BorderLayout.NORTH);

        addPersonPanel.add(nameField);
        addPersonPanel.add(addPersonButton);

        createGroupPanel = new JPanel();
        groupCountField = new JTextField(15);

        createGroupButton = new JButton("그룹 생성");
        createGroupButton.addActionListener(e -> {
            readDataFromFile();
        });
        createGroupPanel.add(groupCountField);
        createGroupPanel.add(createGroupButton);

        rightBottomPanel = new JPanel();
        rightBottomPanel.setLayout(new BoxLayout(rightBottomPanel, BoxLayout.Y_AXIS));

        rightBottomPanel.add(addPersonPanel);
        rightBottomPanel.add(createGroupPanel);

        rightPanel.add(rightBottomPanel, BorderLayout.SOUTH);

        loadData(); // 파일에서 데이터 불러오기
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

    private void addPersonName(String name, boolean isChecked, boolean isFirst) {
        if (name.isEmpty() || name.equals(" ")) {
            // 예외 처리
            return;
        }

        totalCount++;

        if (isChecked) {
            checkedCount++;
        }
//        totalCountLabel.setText("총인원 : " + totalCount);
//        checkedCountLabel.setText("참여인원 : " + checkedCount);

        JPanel namePanel = new JPanel();
        JButton deleteButton = new JButton("삭제");
        JCheckBox checkBox = new JCheckBox(name, isChecked);

        checkBox.addItemListener(new CheckBoxListener());

        namePanel.add(checkBox);
        namePanel.add(deleteButton);

        deleteButton.addActionListener(e -> {
            deleteName(namePanel, checkBox.isSelected(), name);
        });

        nameListPanel.add(namePanel);
        nameListPanel.revalidate();
        nameListPanel.repaint();
        nameField.setText("");

        if (isFirst) {
            saveData(name); // 데이터를 파일에 저장
        }
    }

    private void deleteName(JPanel panel, boolean isSelected, String name) {
        totalCount--;
        if (isSelected) {
            checkedCount--;
        }
        nameListPanel.remove(panel);
        nameListPanel.revalidate();
        nameListPanel.repaint();

        removeData(name);
    }

    private class CheckBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox checkBox = (JCheckBox) e.getSource();
            String name = checkBox.getText();
            boolean isChecked = checkBox.isSelected();
            if (isChecked) {
                checkedCount++;
            } else {
                checkedCount--;
            }


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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 2) {
                    String name = data[0];
                    boolean isChecked = Boolean.parseBoolean(data[1]);
                    addPersonName(name, isChecked, false);
                }
            }


        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void readDataFromFile() {
        ArrayList<String> names = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 파일에서 각 줄을 파싱하여 이름과 체크 여부를 가져옵니다.
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0];
                    boolean isChecked = Boolean.parseBoolean(parts[1]);
                    if (isChecked) {
                        // 체크된 항목인 경우에만 리스트에 추가합니다.
                        names.add(name);
                    }
                }
            }

            divideGroups(Integer.parseInt(groupCountField.getText()), names);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void divideGroups(int groupCount, ArrayList<String> names) {
        rightTopPanel.removeAll();
        rightTopPanel.revalidate();
        rightTopPanel.repaint();

        List<List<String>> groups = new ArrayList<>();
        Collections.shuffle(names);
        for (int i = 0; i < groupCount; i++) {
            groups.add(new ArrayList<>());
        }

        int currentGroup = 0;
        for (String person : names) {
            groups.get(currentGroup).add(person);
            currentGroup = (currentGroup + 1) % groupCount;
        }

        for (int i = 0; i < groups.size(); i++) {
            JList<String> list = new JList<>(groups.get(i).toArray(new String[0]));
            rightTopPanel.add(new JLabel("그룹" + (i + 1)));
            rightTopPanel.add(list);
        }
    }
}
