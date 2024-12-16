package org.investment.view;

import javax.swing.*;
import java.awt.*;
import org.investment.Security;

public class SecurityDetailsWindow extends JDialog {
    private final JLabel nameValue;
    private final JLabel typeValue;
    private final JLabel currentPriceValue;
    private final JLabel expectedReturnValue;
    private final JButton closeButton;

    public SecurityDetailsWindow(JFrame parent) {
        super(parent, "Security Details", true);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));

        // Панель с информацией
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Стиль для заголовков
        Font labelFont = new Font(Font.DIALOG, Font.BOLD, 12);

        // Название
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        infoPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        nameValue = new JLabel();
        infoPanel.add(nameValue, gbc);

        // Тип
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setFont(labelFont);
        infoPanel.add(typeLabel, gbc);

        gbc.gridx = 1;
        typeValue = new JLabel();
        infoPanel.add(typeValue, gbc);

        // Текущая цена
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel priceLabel = new JLabel("Current Price:");
        priceLabel.setFont(labelFont);
        infoPanel.add(priceLabel, gbc);

        gbc.gridx = 1;
        currentPriceValue = new JLabel();
        infoPanel.add(currentPriceValue, gbc);

        // Ожидаемая доходность
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel returnLabel = new JLabel("Expected Return:");
        returnLabel.setFont(labelFont);
        infoPanel.add(returnLabel, gbc);

        gbc.gridx = 1;
        expectedReturnValue = new JLabel();
        infoPanel.add(expectedReturnValue, gbc);

        // Панель с кнопкой
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        // Добавляем панели на форму
        add(new JLabel("  Security Details:", SwingConstants.LEFT), BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void displaySecurity(Security security) {
        nameValue.setText(security.getName());
        typeValue.setText(security.getType());
        currentPriceValue.setText("$" + security.getCurrentPrice().toString());
        expectedReturnValue.setText(security.getExpectedReturn() + "%");
    }
}