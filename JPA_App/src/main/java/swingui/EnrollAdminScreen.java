package swingui;

import operation.Crud;
import org.jpa.EnrollRecord;
import org.jpa.Subject;
import org.jpa.UserSubject;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EnrollAdminScreen {
    private JPanel panel1;
    private JTable table1;
    private JScrollPane ScrollPanel;
    private JButton clickToUpdateButton;
    private JButton goBackButton;

    private String currentSubject;
    List<UserSubject> registerList = null; // user_subject records


    public EnrollAdminScreen(String curSub) {
        currentSubject = curSub;
        loadTable();


        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String mark = "*";
                String unmark = "";

                String cellValue = (String) table1.getValueAt(table1.getSelectedRow(), table1.getSelectedColumn());
                if (cellValue == "*") {
                    table1.setValueAt((Object) unmark, table1.getSelectedRow(), table1.getSelectedColumn());
                } else if (cellValue == null) {
                    table1.setValueAt((Object) mark, table1.getSelectedRow(), table1.getSelectedColumn());
                }
            }
        });
        clickToUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Crud.deleteAllEnrollRecord(currentSubject);

                for (int row = 0; row < registerList.size(); row++) {
                    String user_id = registerList.get(row).getUser_id();
                    ArrayList<String> listDates = new ArrayList<>();
                    for (int col = 1; col <= 15; col++) {
                        // Loop through dates
                        String cellValue = (String) table1.getValueAt(row, col);
                        if (cellValue == "*") {
                            listDates.add(table1.getColumnName(col));
                        }
                    }
                    try {
                        Crud.createEnrollRecord(user_id, currentSubject, listDates);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }

                JOptionPane.showMessageDialog(table1,
                        "Update successfully successfully");
            }
        });
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor($$$getRootComponent$$$());
                topFrame.setContentPane(new PickSubjectToEnrollAdmin().$$$getRootComponent$$$());
                topFrame.setTitle("Kết quả điểm danh (click vào box để đánh dấu hoặc bỏ, click Update để lưu thay đổi)");
                topFrame.setSize(new Dimension(700, 350));
                topFrame.revalidate();
                topFrame.repaint();
            }
        });
    }

    public void loadTable() {
        String idQuery = currentSubject;
        SimpleDateFormat dayFormatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();

        table1.setDefaultEditor(Object.class, null);

        ArrayList<String> dateList = new ArrayList<String>();
        dateList.add("Student_Id");

        Subject currentSubject = Crud.getOneSubject(idQuery);
        Date startDate = currentSubject.getStartDay();
        cal.setTime(startDate);

        for (int i = 1; i <= 15; i++) {
            Date afterPlus = cal.getTime();
            dateList.add(dayFormatter.format(afterPlus));
            cal.add(Calendar.DATE, 7);
        }

        String[] columns = new String[dateList.size()];

        DefaultTableModel model = new DefaultTableModel(
                dateList.toArray(columns),
                0
        );

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table1.setDefaultRenderer(Object.class, centerRenderer);
//
//        // Get data from database
        registerList = Crud.getUserEnroll(idQuery);
        for (UserSubject user : registerList) {
            model.addRow(new Object[]{user.getUser_id()});
        }

        table1.setModel(model);

        // Update enroll
        List<EnrollRecord> enrollRecords = Crud.getEnrollRecord(idQuery);
        String mark = "*";

        for (int row = 0; row < registerList.size(); row++) {
            String current_userid = registerList.get(row).getUser_id();
            for (EnrollRecord er : enrollRecords) {
                if (er.getUser_id().equals(current_userid)) {
                    String dayRecord = dayFormatter.format(er.getDayEnroll());
                    if (dateList.contains(dayRecord)) {
                        table1.setValueAt((Object) mark, row, dateList.indexOf(dayRecord));
                    }
                }
            }
        }
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
        panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        ScrollPanel = new JScrollPane();
        panel1.add(ScrollPanel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table1 = new JTable();
        ScrollPanel.setViewportView(table1);
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Bảng kết quả điểm danh");
        panel2.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        clickToUpdateButton = new JButton();
        clickToUpdateButton.setText("Click to Update");
        panel1.add(clickToUpdateButton, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        goBackButton = new JButton();
        goBackButton.setText("Go back");
        panel1.add(goBackButton, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
