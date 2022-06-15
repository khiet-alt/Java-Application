package swingui;

import operation.Crud;
import org.jpa.Subject;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

public class SubjectShow {

    private JTable table1;
    private JButton addSubjectButton;
    private JButton goBackButton;
    private JPanel JPanel1;
    private JPanel Top;
    private JButton removeSubjectButton;
    private JButton addSinhvien;
    private JButton ViewResult;
    private JButton xemSốSinhViênButton;

    private List<Subject> subjectList = null;
    DefaultTableModel model;

    public SubjectShow() {
        loadTable();

        // Listener
        removeSubjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table1.getSelectedRow();

                if (row == -1) {
                    JOptionPane.showMessageDialog(table1,
                            "Please choose record you want to delete",
                            "Missing",
                            JOptionPane.WARNING_MESSAGE);
                } else {

                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog(table1, "Do you want to delete this record?", "Warning", dialogButton);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        // Saving code here
                        String remove_id = subjectList.get(row).getId();
                        Crud.removeSubject(remove_id);
                        loadTable();
                    }
                }
            }
        });
        addSubjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor($$$getRootComponent$$$());

                AddSubject addSubject = new AddSubject(topFrame);
                addSubject.setTitle("Thêm môn học");
                addSubject.setContentPane(new AddSubject(topFrame).$$$getRootComponent$$$());
                addSubject.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                addSubject.setSize(new Dimension(700, 400));
                addSubject.pack();
                addSubject.setLocationRelativeTo(null);
                addSubject.setVisible(true);

            }
        });
        addSinhvien.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table1.getSelectedRow();

                if (row == -1) {
                    JOptionPane.showMessageDialog(table1,
                            "Please pick subject you want to add student",
                            "Missing information",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    String selectedSubject = subjectList.get(row).getId();

                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor($$$getRootComponent$$$());
                    topFrame.setTitle("Thêm sinh viên vào môn học");
                    topFrame.setContentPane(new AddSvIntoSubject(selectedSubject).$$$getRootComponent$$$());
                    topFrame.revalidate();
                    topFrame.repaint();
                }
            }
        });
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor($$$getRootComponent$$$());
                topFrame.setContentPane(new AdminHome().$$$getRootComponent$$$());
                topFrame.revalidate();
                topFrame.repaint();
            }
        });
        ViewResult.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor($$$getRootComponent$$$());
                topFrame.setContentPane(new PickSubjectToEnrollAdmin().$$$getRootComponent$$$());
                topFrame.revalidate();
                topFrame.repaint();
            }
        });
        xemSốSinhViênButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table1.getSelectedRow();

                if (row == -1) {
                    JOptionPane.showMessageDialog(table1,
                            "Chọn môn học trước đã !!!",
                            "Missing",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor($$$getRootComponent$$$());
                    topFrame.setContentPane(new StudentOfSubject(subjectList.get(row)).$$$getRootComponent$$$());
                    topFrame.revalidate();
                    topFrame.repaint();
                }
            }
        });
    }

    private void loadTable() {
        SimpleDateFormat dayFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:mm");

        table1.setDefaultEditor(Object.class, null);

        model = new DefaultTableModel(
                new String[]{"Id", "Name", "roomName", "Day start", "Day end", "Hour start", "Hour end", "Day of week"},
                0
        );

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table1.setDefaultRenderer(Object.class, centerRenderer);

        // Get data from database
        subjectList = Crud.listSubject();
        for (Subject sub : subjectList) {
            model.addRow(new Object[]{sub.getId(), sub.getName(), sub.getRoomName(), dayFormatter.format(sub.getStartDay()), dayFormatter.format(sub.getEndDay()), hourFormatter.format(sub.getStartHour()), hourFormatter.format(sub.getEndHour()), sub.getDayOfWeek()});
        }

        table1.setModel(model);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        JPanel1 = new JPanel();
        JPanel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        Top = new JPanel();
        Top.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(2, 3, 3, 3), -1, -1));
        JPanel1.add(Top, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Welcome, Admin");
        Top.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        JPanel1.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        addSubjectButton = new JButton();
        addSubjectButton.setText("Add Subject");
        panel1.add(addSubjectButton, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addSinhvien = new JButton();
        addSinhvien.setText("Add sinh viên vào môn học");
        panel1.add(addSinhvien, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ViewResult = new JButton();
        ViewResult.setText("Xem kết quả điểm danh");
        panel1.add(ViewResult, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        JPanel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table1 = new JTable();
        table1.setPreferredScrollableViewportSize(new Dimension(600, 200));
        scrollPane1.setViewportView(table1);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        JPanel1.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        goBackButton = new JButton();
        goBackButton.setText("Go Back");
        panel3.add(goBackButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        JPanel1.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xemSốSinhViênButton = new JButton();
        xemSốSinhViênButton.setText("Xem số sinh viên của môn học");
        panel4.add(xemSốSinhViênButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeSubjectButton = new JButton();
        removeSubjectButton.setText("Remove subject");
        panel4.add(removeSubjectButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return JPanel1;
    }

}