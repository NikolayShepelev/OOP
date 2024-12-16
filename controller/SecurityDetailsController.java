package org.investment.controller;

import org.investment.Security;
import org.investment.view.SecurityDetailsWindow;
import javax.swing.*;

public class SecurityDetailsController {
    private final SecurityDetailsWindow view;
    private final Security security;

    public SecurityDetailsController(JFrame parentWindow, Security security) {
        this.security = security;
        this.view = new SecurityDetailsWindow(parentWindow);
        displaySecurityDetails();
        view.setVisible(true);
    }

    private void displaySecurityDetails() {
        if (security != null) {
            view.displaySecurity(security);
        } else {
            JOptionPane.showMessageDialog(
                    view,
                    "Error: Security details not found",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            view.dispose();
        }
    }
}