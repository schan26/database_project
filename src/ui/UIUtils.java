package ca.ubc.cs304.src.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.List;
import java.util.Vector;

public class UIUtils {
    public static void showQueryResultsInDialog(List<Object> tblData, JFrame parent) {
        Vector<String> columnNames = (Vector<String>) tblData.get(0);
        Vector<Vector<Object>> data = (Vector<Vector<Object>>) tblData.get(1);

        JTable table = new JTable(new DefaultTableModel(data, columnNames));
        JScrollPane scrollPane = new JScrollPane(table);

        JDialog resultDialog = new JDialog(parent, "Query Results", Dialog.ModalityType.MODELESS);
        resultDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        resultDialog.add(scrollPane);
        resultDialog.setSize(600, 400);
        resultDialog.setLocationRelativeTo(parent);
        resultDialog.setVisible(true);

    }
}
