package ca.ubc.cs304.src.ui;

import ca.ubc.cs304.src.delegates.homescreenDelegate;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;


public class homescreen extends JPanel{
    private frame frame;
    private JButton insert;
    private JButton update;
    private JButton select;
    private JButton nestedGroup;
    private JButton groupBy;
    private JButton having;
    private JButton project;
    private JButton join;
    private JButton div;
    private JButton delete;
    private JButton view;
    private homescreenDelegate delegate = null;
    private JLabel errorMsg;


    public homescreen(frame f, homescreenDelegate delegate) {
        this.delegate = delegate;
        this.frame = f;
        setBackground(frame.BACKGROUND_COLOR);
        setSize(frame.WINDOW_WIDTH, frame.WINDOW_HEIGHT);
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(gbl);


        JLabel instructions = new JLabel("Please choose one of the options:");
        instructions.setFont(new Font("Arial", Font.BOLD, 18));
        instructions.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(instructions, gbc);
        add(instructions, gbc);

        JPanel optionsPanel = buildOptionsPanel(new JPanel());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.WEST;
        gbl.setConstraints(optionsPanel, gbc);
        add(optionsPanel, gbc);
    }

    private JPanel buildTablePanel(JPanel p) {
        p.setSize(1800, 400);
        return p;
    }

    private JPanel buildOptionsPanel(JPanel p) {
        p.setBackground(ca.ubc.cs304.src.ui.frame.BACKGROUND_COLOR);
        p.setSize(200,400);
        p.setLayout(new GridLayout(0,1,0,2));
        initComponents();

        p.add(view);
        p.add(insert);
        p.add(update);
        p.add(select);
        p.add(project);
        p.add(delete);
        p.add(groupBy);
        p.add(having);
        p.add(nestedGroup);
        p.add(join);
        p.add(div);

        return p;
    }

    private void initComponents() {
        insert = new JButton("Add new health policy <insert>");
        update = new JButton("Update a policy <update>");
        select = new JButton("Filter healthcare policies <select>");
        project = new JButton("View selected info <project>");
        delete = new JButton("Delete Policy <delete>");
        join = new JButton("Find the accident date and location, claim amount and status for a specified client <join>");
        groupBy = new JButton("Find the number of reported accidents per client <group by>");
        having = new JButton("Find the policies that have been offered to more than one client <group by + having>");
        nestedGroup = new JButton("Find the provider that has the most clients <nested aggregation>");
        div = new JButton("Find agents who manage policies with all providers <division>");
        view = new JButton("Display table");
        view.setBackground(Color.YELLOW);

        insert.addActionListener(e -> new insertPolicyDialog(frame, delegate));
        update.addActionListener(e -> showUpdateClientDialog());
        select.addActionListener(e -> showSelectPolicyDialog());
        delete.addActionListener(e -> showDeletePolicyDialog());
        project.addActionListener(e -> new projectDialog(frame, delegate));
        view.addActionListener(e -> new displayTblDialog(frame, delegate));
        having.addActionListener(e -> UIUtils.showQueryResultsInDialog(delegate.findCount(),frame));
        div.addActionListener(e -> UIUtils.showQueryResultsInDialog(delegate.findAgent(),frame));
        join.addActionListener(e -> new findClaimsDialog(frame, delegate));
        nestedGroup.addActionListener(e -> UIUtils.showQueryResultsInDialog(delegate.findProvider(),frame));
        groupBy.addActionListener(e -> UIUtils.showQueryResultsInDialog(delegate.findAccidentCount(),frame));
    }

    private void showDeletePolicyDialog() {
        JDialog d = new JDialog(frame, "Delete a policy ", true);
        GridBagLayout gbl = new GridBagLayout();
        d.setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();

        //show the policy tuples
        List<Object> policyTable = null;
        try {
            policyTable = delegate.selectTable(new ArrayList<>(Arrays.asList("POLICY")));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        Vector<String> columnNames = (Vector<String>) policyTable.get(0);
        Vector<Vector<Object>> data = (Vector<Vector<Object>>) policyTable.get(1);

        JTable table = new JTable(data, columnNames);
        JScrollPane sp = new JScrollPane(table);
        d.add(sp, gbc);


        JLabel tblLabel = new JLabel("Select Policy ID to delete: ");
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 10, 5, 0);
        d.add(tblLabel, gbc);

        JComboBox<String> policySelector = new JComboBox<>();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        d.add(policySelector, gbc);

        int index = columnNames.indexOf("POLICYID");

        for (Vector<Object> row : data) {
            policySelector.addItem(row.get(index).toString());
        }

        policySelector.addActionListener(e -> {
            Integer id = Integer.parseInt(policySelector.getSelectedItem().toString());
            d.dispose();

            try {
                delegate.deletePolicy(id);
                JOptionPane.showMessageDialog(null, "Policy successfully Deleted!");
            } catch (SQLException err) {
                int code = err.getErrorCode();
                String msg = "";
                switch (code) {
                    case 1:
                        msg = "Policy already exists. Please enter another policy ID!";
                        break;
                    case 2291:
                        msg = "Provider license does not exist.";
                        break;
                    case 12899:
                        msg = "Policy name/status/provider license input is too long.";
                        break;
                    default:
                        msg = err.getMessage();
                }
                JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException s) {
                JOptionPane.showMessageDialog(null, "Invalid number entered for policy ID ");
            }

        });
        gbc.anchor = GridBagConstraints.CENTER;

        d.pack();
        d.setLocationRelativeTo(frame);
        d.setVisible(true);


    }

    private void showUpdateClientDialog() {
        JDialog d = new JDialog(frame, "Update a policy ", true);
        GridBagLayout gbl = new GridBagLayout();
        d.setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();

        //show the policy tuples
        List<Object> policyTable = null;
        try {
            policyTable = delegate.selectTable(new ArrayList<>(Arrays.asList("POLICY")));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        Vector<String> columnNames = (Vector<String>) policyTable.get(0);
        Vector<Vector<Object>> data = (Vector<Vector<Object>>) policyTable.get(1);

        JTable table = new JTable(data, columnNames);
        JScrollPane sp = new JScrollPane(table);
        d.add(sp, gbc);


        JLabel tblLabel = new JLabel("Select Policy ID: ");
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 10, 5, 0);
        d.add(tblLabel, gbc);

        JComboBox<String> policySelector = new JComboBox<>();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        d.add(policySelector, gbc);

        int index = columnNames.indexOf("POLICYID");

        for (Vector<Object> row : data) {
            policySelector.addItem(row.get(index).toString());
        }

        policySelector.addActionListener(e -> {
            Integer clientId = Integer.parseInt(policySelector.getSelectedItem().toString());
            d.dispose();
            new policyAttribute(frame, delegate, clientId);

        });


        gbc.anchor = GridBagConstraints.CENTER;

        d.pack();
        d.setLocationRelativeTo(frame);
        d.setVisible(true);
    }


    private void showSelectPolicyDialog() {
        final int TEXTFIELD_WIDTH = 15;
        Vector<String> wordOperators = new Vector<>(Arrays.asList("equals", "less than", "larger than", "less than or equal to", "larger than or equal to", "not equal to", "contains the word"));
        Vector<String> fieldOptions = new Vector<>(Arrays.asList("client ID", "policy status", "provider license#", "coverage limit"));
        Vector<String> operators = new Vector<>(Arrays.asList("=", "<", ">", "<=", ">=", "<>", "LIKE"));
        Vector<String> fields = new Vector<>(Arrays.asList("clientID", "policyStatus", "pLicense#", "coverageLimit"));
        JDialog d = new JDialog(frame, "Advanced Policy Filter", true);
        d.setLayout(new BorderLayout());

        JPanel conditionsPanel = new JPanel();
        conditionsPanel.setLayout(new BoxLayout(conditionsPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(conditionsPanel);
        d.add(scrollPane, BorderLayout.CENTER);

        // Add button to append condition rows
        JButton addConditionBtn = new JButton("Add Condition");
        addConditionBtn.addActionListener(e -> {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));

            JComboBox<String> fieldBox = new JComboBox<>(fieldOptions);
            JComboBox<String> operatorBox = new JComboBox<>(wordOperators);
            JTextField valueField = new JTextField(TEXTFIELD_WIDTH);
            JComboBox<String> andOrBox = new JComboBox<>(new String[]{"AND", "OR"});
            row.add(new JLabel("Field:"));
            row.add(fieldBox);
            row.add(new JLabel("Operator:"));
            row.add(operatorBox);
            row.add(new JLabel("Value:"));
            row.add(valueField);

            // Only show AND/OR if it's not the first row
            if (conditionsPanel.getComponentCount() > 0) {
                row.add(new JLabel("Join:"));
                row.add(andOrBox);
            } else {
                andOrBox.setVisible(false);  // first condition doesn’t have AND/OR
            }

            conditionsPanel.add(row);
            conditionsPanel.revalidate();
            conditionsPanel.repaint();
        });

        // Add initial condition row
        addConditionBtn.doClick();

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton applyBtn = new JButton("Apply Filter");
        JButton cancelBtn = new JButton("Cancel");
        buttonPanel.add(addConditionBtn);
        buttonPanel.add(applyBtn);
        buttonPanel.add(cancelBtn);

        d.add(buttonPanel, BorderLayout.SOUTH);

        applyBtn.addActionListener(e -> {
            StringBuilder clauseBuilder = new StringBuilder();
            List<Object> paramList = new ArrayList<>();
            boolean inputError = false;

            Component[] rows = conditionsPanel.getComponents();
            for (int i = 0; i < rows.length; i++) {
                JPanel row = (JPanel) rows[i];
                JComboBox<String> fieldBox = (JComboBox<String>) row.getComponent(1);
                JComboBox<String> opBox = (JComboBox<String>) row.getComponent(3);
                JTextField valueField = (JTextField) row.getComponent(5);
                String fieldWord = (String) fieldBox.getSelectedItem();
                String opWord = (String) opBox.getSelectedItem();
                String val = valueField.getText().trim();

                // Change operator and field to names in db
                String op = operators.get(wordOperators.indexOf(opWord));
                String field = fields.get(fieldOptions.indexOf(fieldWord));

                if (val.isEmpty()) {
                    JOptionPane.showMessageDialog(d, "Please enter a value in row " + (i + 1));
                    return;
                }

                // Add AND/OR joiner if not the first row
                if (i > 0) {
                    JComboBox<String> andOrBox = (JComboBox<String>) row.getComponent(7);
                    clauseBuilder.append(" ").append(andOrBox.getSelectedItem()).append(" ");
                }

                clauseBuilder.append(field).append(" ").append(op).append(" ?");

                if (op.equalsIgnoreCase("LIKE") && (field.equals("coverageLimit") || field.equals("clientID"))) {
                    JOptionPane.showMessageDialog(d, "Please do not choose this operator for clientID or coverageLimit", "Error", JOptionPane.ERROR_MESSAGE);
                    inputError = true;
                }
                else if (op.equalsIgnoreCase("LIKE")) {
                    paramList.add("%"+val+"%");
                } else if (field.equals("coverageLimit") || field.equals("clientID")) {
                    int value = 0;
                    try {
                        value = Integer.parseInt(val);
                        paramList.add(value);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(d, "Invalid number entered for clientID/Coverage limit", "Error", JOptionPane.ERROR_MESSAGE);
                        inputError = true;
                    }
                } else {
                    if (!op.equals("LIKE") && !op.equals("=")) {
                        JOptionPane.showMessageDialog(d, "Please do not choose this operator for policy status or provider license", "Error", JOptionPane.ERROR_MESSAGE);
                        inputError = true;
                    } else {
                        paramList.add(val);
                    }
                }
            }
            if (!inputError) {
                try {
                    System.out.println("Built clause: " + clauseBuilder.toString());
                    UIUtils.showQueryResultsInDialog(delegate.selectPoliciesByCustomClause(clauseBuilder.toString(), paramList), frame);
                    d.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(d, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        cancelBtn.addActionListener(e -> d.dispose());

        d.setSize(700, 400);
        d.setLocationRelativeTo(frame);
        d.setVisible(true);
    }
}
