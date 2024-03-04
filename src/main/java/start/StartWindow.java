package start;

import logic.event.ProgramEventSource;
import presentation.MemberController;
import state.Member;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StartWindow extends JFrame {
    private final MemberController memberController;

    public StartWindow(MemberController memberController) {
        this.memberController = memberController;

        setTitle("그룹 생성기");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(500, 1300);

        JPanel topInfoPanel = getTopInfoPanel();
        add(topInfoPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = refreshNameListPanel();
        add(scrollPane, BorderLayout.WEST);

        JPanel rightScreenPanel = new JPanel(new BorderLayout());
        add(rightScreenPanel, BorderLayout.EAST);

        JPanel groupListPanel = new JPanel();
        groupListPanel.setLayout(new BoxLayout(groupListPanel, BoxLayout.Y_AXIS));
        rightScreenPanel.add(groupListPanel, BorderLayout.NORTH);

        JPanel addMemberAndGroupingPanel = paintAddMemberAndGroupingPanel(groupListPanel);
        rightScreenPanel.add(addMemberAndGroupingPanel, BorderLayout.SOUTH);
    }

    private JPanel getTopInfoPanel() {
        JPanel topInfoPanel = new JPanel(new BorderLayout());

        JPanel memberCountStatusPanel = paintMemberCountStatusPanel();
        topInfoPanel.add(memberCountStatusPanel, BorderLayout.WEST);

        JButton developerInfoButton = new JButton("개발자 정보");
        topInfoPanel.add(developerInfoButton, BorderLayout.EAST);

        developerInfoButton.addActionListener(e -> {
            ProgramEventSource.developerInfo();
        });
        return topInfoPanel;
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

    private JScrollPane refreshNameListPanel() {
        List<Member> members = memberController.getMembers();

        JPanel nameListPanel = new JPanel();
        nameListPanel.setLayout(new BoxLayout(nameListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(nameListPanel);
        scrollPane.setVerticalScrollBarPolicy(scrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        members.forEach(member -> {
            JPanel namePanel = new JPanel();

            JCheckBox checkBox = new JCheckBox(member.name(), member.isChecked());
            checkBox.addItemListener(e -> {
                JCheckBox jCheckBox = (JCheckBox) e.getSource();
                String name = jCheckBox.getText();
                boolean isChecked = jCheckBox.isSelected();

                memberController.updateData(name, isChecked);
                ProgramEventSource.refresh(this);
            });

            JButton deleteButton = new JButton("삭제");
            deleteButton.addActionListener(e -> {
                memberController.removeMember(member.name());
                ProgramEventSource.refresh(this);
            });

            namePanel.add(checkBox);
            namePanel.add(deleteButton);

            nameListPanel.add(namePanel);
        });

        return scrollPane;
    }

    private JPanel paintAddMemberAndGroupingPanel(JPanel groupListPanel) {
        JPanel addPersonPanel = new JPanel();
        JTextField nameField = new JTextField(15);

        JButton addPersonButton = new JButton("인원 추가");
        addPersonButton.addActionListener(e -> {
            memberController.saveData(nameField.getText());
            nameField.setText("");
            ProgramEventSource.refresh(this);
        });

        addPersonPanel.add(nameField);
        addPersonPanel.add(addPersonButton);

        JPanel createGroupPanel = groupListPanel(groupListPanel);

        JPanel rightBottomPanel = new JPanel();
        rightBottomPanel.setLayout(new BoxLayout(rightBottomPanel, BoxLayout.Y_AXIS));

        rightBottomPanel.add(addPersonPanel);
        rightBottomPanel.add(createGroupPanel);

        return rightBottomPanel;
    }

    private JPanel groupListPanel(JPanel groupListPanel) {
        JPanel createGroupPanel = new JPanel();
        JTextField groupCountField = new JTextField(15);

        JButton createGroupButton = new JButton("그룹 생성");
        createGroupButton.addActionListener(e -> {
            groupListPanel.removeAll();
            groupListPanel.revalidate();
            groupListPanel.repaint();

            List<List<String>> groups = memberController.createGroups(groupCountField.getText());

            for (int i = 0; i < groups.size(); i++) {
                JList<String> list = new JList<>(groups.get(i).toArray(new String[0]));
                groupListPanel.add(new JLabel("그룹" + (i + 1)));
                groupListPanel.add(list);
            }
        });

        createGroupPanel.add(groupCountField);
        createGroupPanel.add(createGroupButton);

        return createGroupPanel;
    }
}
