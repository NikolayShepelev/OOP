package org.investment.controller;

import org.investment.*;
import org.investment.view.AddEditSecurityWindow;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

public class AddEditSecurityController {
    private final AddEditSecurityWindow view;
    private final ISecurityRepository repository;
    private final Integer editingSecurityId;

    public AddEditSecurityController(JFrame parentWindow, ISecurityRepository repository) {
        this(parentWindow, repository, null);
    }

    public AddEditSecurityController(JFrame parentWindow, ISecurityRepository repository, Integer securityId) {
        this.repository = repository;
        this.editingSecurityId = securityId;
        this.view = new AddEditSecurityWindow(parentWindow, securityId != null);

        if (securityId != null) {
            Security security = repository.getById(securityId);
            if (security != null) {
                view.setSecurityData(security);
            }
        }

        initializeListeners();
        view.setVisible(true);
    }

    private void initializeListeners() {
        view.getSaveButton().addActionListener(this::handleSave);
    }

    private void handleSave(ActionEvent e) {
        try {
            validateInput();
            Security security = view.getSecurityData();

            if (view.isEditMode()) {
                if (editingSecurityId != null) {
                    Security updatedSecurity = Security.updateExistingSecurity(
                            editingSecurityId,
                            security.getName(),
                            security.getType(),
                            security.getCurrentPrice(),
                            security.getExpectedReturn()
                    );
                    repository.replaceSecurity(editingSecurityId, updatedSecurity);
                }
            } else {
                repository.addSecurity(security);
            }

            view.dispose();
        } catch (IllegalArgumentException | DuplicateSecurityNameException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    ex.getMessage(),
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void validateInput() {
        String name = view.getNameField().getText();
        String type = view.getTypeField().getText();
        String currentPrice = view.getCurrentPriceField().getText();
        String expectedReturn = view.getExpectedReturnField().getText();

        SecurityValidator.validateName(name);
        SecurityValidator.validateType(type);

        try {
            BigDecimal price = new BigDecimal(currentPrice);
            SecurityValidator.validatePrice(price);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid current price format");
        }

        try {
            BigDecimal returnValue = new BigDecimal(expectedReturn);
            SecurityValidator.validateExpectedReturn(returnValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid expected return format");
        }
    }
}