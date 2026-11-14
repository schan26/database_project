package ca.ubc.cs304.src.ui;

import ca.ubc.cs304.src.delegates.homescreenDelegate;
import ca.ubc.cs304.src.model.PolicyModel;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Vector;

public class insertPolicyDialog extends JDialog {
    private final int TEXTFIELD_WIDTH = 15;
    private GridBagLayout gbl = new GridBagLayout();
    private GridBagConstraints gbc = new GridBagConstraints();

    public insertPolicyDialog(Frame parent, homescreenDelegate delegate) {
        super(parent,"Enter policy info", true);
        setLayout(gbl);
        
        // place label for policy ID
        JLabel policyIDLabel = new JLabel("Policy ID: ");
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 10, 5, 0);
        gbl.setConstraints(policyIDLabel, gbc);
        add(policyIDLabel);

        // place the text field for the policy ID
        JTextField policyIDField = new JTextField(TEXTFIELD_WIDTH);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 0, 5, 10);
        gbl.setConstraints(policyIDField, gbc);
        add(policyIDField);

        // place label for policy name
        JLabel NameLabel = new JLabel("Policy name (less than 100 characters): ");
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 10, 5, 0);
        gbl.setConstraints(NameLabel, gbc);
        add(NameLabel);

        // place the text field for the policy name
        JTextField nameField = new JTextField(TEXTFIELD_WIDTH);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 0, 5, 10);
        gbl.setConstraints(nameField, gbc);
        add(nameField);

        // place label for provider license
        JLabel licensLabel = new JLabel("Provider license number (less than 20 characters): ");
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 10, 5, 0);
        gbl.setConstraints(licensLabel, gbc);
        add(licensLabel);

        // place the text field for the provider license
        JTextField licenseField = new JTextField(TEXTFIELD_WIDTH);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 0, 5, 10);
        gbl.setConstraints(licenseField, gbc);
        add(licenseField);

        // place label for coverage limit
        JLabel coverageLabel = new JLabel("Coverage limit: ");
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 10, 5, 0);
        gbl.setConstraints(coverageLabel, gbc);
        add(coverageLabel);

        // place the text field for the coverage limit
        JTextField cvgField = new JTextField(TEXTFIELD_WIDTH);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 0, 5, 10);
        gbl.setConstraints(cvgField, gbc);
        add(cvgField);

        // place label for start date
        JLabel startLabel = new JLabel("Start date (YYYY-MM-DD): ");
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 10, 5, 0);
        gbl.setConstraints(startLabel, gbc);
        add(startLabel);

        // place the text field for the start date
        JTextField startField = new JTextField(TEXTFIELD_WIDTH);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 0, 5, 10);
        gbl.setConstraints(startField, gbc);
        add(startField);

        // place label for end date
        JLabel endLabel = new JLabel("End date (YYYY-MM-DD): ");
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 10, 5, 0);
        gbl.setConstraints(endLabel, gbc);
        add(endLabel);

        // place the text field for the end date
        JTextField endFIeld = new JTextField(TEXTFIELD_WIDTH);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 0, 5, 10);
        gbl.setConstraints(endFIeld, gbc);
        add(endFIeld);

        // place label for policy status
        JLabel policyStatus = new JLabel("Policy status: ");
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 10, 5, 0);
        gbl.setConstraints(policyStatus, gbc);
        add(policyStatus);

        // place the text field for the policy status
        JComboBox policyStatusComboBox = new JComboBox(new Vector<>(Arrays.asList("Pending", "Active", "Expired")));
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 0, 5, 10);
        gbl.setConstraints(policyStatusComboBox, gbc);
        add(policyStatusComboBox);

        // place label for premium
        JLabel premiumLabel = new JLabel("Premium: ");
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 10, 5, 0);
        gbl.setConstraints(premiumLabel, gbc);
        add(premiumLabel);

        // place the text field for the premium
        JTextField premiumField = new JTextField(TEXTFIELD_WIDTH);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 0, 5, 10);
        gbl.setConstraints(premiumField, gbc);
        add(premiumField);

        // place label for deduction
        JLabel dedLabel = new JLabel("Deduction: ");
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 10, 5, 0);
        gbl.setConstraints(dedLabel, gbc);
        add(dedLabel);

        // place the text field for the deduction
        JTextField dedField = new JTextField(TEXTFIELD_WIDTH);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 0, 5, 10);
        gbl.setConstraints(dedField, gbc);
        add(dedField);

        // place submit and cancel button below
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());

        JButton submit = new JButton("Submit");

        submit.addActionListener(e -> {
            if (policyStatusComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Please select policy status.");
            }
            else if (policyIDField.getText().isEmpty() || nameField.getText().isEmpty() || licenseField.getText().isEmpty()
                    || cvgField.getText().isEmpty() || premiumField.getText().isEmpty() || dedField.getText().isEmpty()
                    || startField.getText().isEmpty() || endFIeld.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all the fields!");
            } else {
                try {
                    Integer id = Integer.parseInt(policyIDField.getText());
                    String name = nameField.getText();
                    String license = licenseField.getText();
                    Integer coverageLimit = Integer.parseInt(cvgField.getText());
                    String startDate = startField.getText();
                    String endDate = endFIeld.getText();
                    String status = policyStatusComboBox.getSelectedItem().toString();
                    Integer premium = Integer.parseInt(premiumField.getText());
                    Integer deductible = Integer.parseInt(dedField.getText());

                    if (id < 0 || premium < 0 || deductible < 0) {
                        JOptionPane.showMessageDialog(null, "Please enter a non-negative number.","Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        PolicyModel model = new PolicyModel(id, name, license, coverageLimit, LocalDate.parse(startDate), LocalDate.parse(endDate), status, premium, deductible);
                        delegate.insertPolicy(model);
                        JOptionPane.showMessageDialog(null, "Policy successfully inserted!");
                        dispose();
                    }
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
                        case 2290:
                            msg = "Please enter a non-negative number.";
                            break;
                        default:
                            msg = err.getMessage();
                    }
                    JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
                } catch (DateTimeParseException err) {
                    JOptionPane.showMessageDialog(this, "Start or end date in wrong format", "Error", JOptionPane.ERROR_MESSAGE);
                }catch (NumberFormatException i) {
                    JOptionPane.showMessageDialog(this, "Invalid number entered for policy ID/Coverage limit/Premium/Deduction", "Error", JOptionPane.ERROR_MESSAGE );
                }
            }
        });

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dispose());

        buttons.add(submit);
        buttons.add(cancel);
        gbc.anchor = GridBagConstraints.CENTER;
        gbl.setConstraints(buttons, gbc);
        add(buttons);


        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

}

