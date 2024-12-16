package org.investment.view;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import org.investment.Security;

public class AddEditSecurityWindow extends JDialog {
    private final JTextField nameField;
    private final JTextField typeField;
    private final JTextField currentPriceField;
    private final JTextField expectedReturnField;
    private final JButton saveButton;
    private final JButton cancelButton;
    private final boolean isEditMode;

    public AddEditSecurityWindow(JFrame parent, boolean isEditMode) {
        super(parent, isEditMode ? "Edit Security" : "Add Security", true);
        this.isEditMode = isEditMode;

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));

        // Панель с полями ввода
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Название
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        inputPanel.add(nameField, gbc);

        // Тип
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        typeField = new JTextField(20);
        inputPanel.add(typeField, gbc);

        // Текущая цена
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Current Price:"), gbc);
        gbc.gridx = 1;
        currentPriceField = new JTextField(20);
        inputPanel.add(currentPriceField, gbc);

        // Ожидаемая доходность
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Expected Return:"), gbc);
        gbc.gridx = 1;
        expectedReturnField = new JTextField(20);
        inputPanel.add(expectedReturnField, gbc);

        // Панель с кнопками
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Добавляем панели на форму
        add(new JLabel("  Enter security details:", SwingConstants.LEFT), BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Обработчик кнопки Cancel
        cancelButton.addActionListener(e -> dispose());
    }

    public void setSecurityData(Security security) {
        nameField.setText(security.getName());
        typeField.setText(security.getType());
        currentPriceField.setText(security.getCurrentPrice().toString());
        expectedReturnField.setText(security.getExpectedReturn().toString());
    }

    public Security getSecurityData() {
        try {
            return Security.createNewSecurity(
                    nameField.getText(),
                    typeField.getText(),
                    new BigDecimal(currentPriceField.getText()),
                    new BigDecimal(expectedReturnField.getText())
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format");
        }
    }

    // Геттеры для компонентов
    public JButton getSaveButton() { return saveButton; }
    public JButton getCancelButton() { return cancelButton; }
    public JTextField getNameField() { return nameField; }
    public JTextField getTypeField() { return typeField; }
    public JTextField getCurrentPriceField() { return currentPriceField; }
    public JTextField getExpectedReturnField() { return expectedReturnField; }
    public boolean isEditMode() { return isEditMode; }
}