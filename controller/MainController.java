package org.investment.controller;

import org.investment.*;
import org.investment.util.Observable;
import org.investment.util.Observer;
import org.investment.view.MainWindow;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MainController implements Observer {
    private final MainWindow view;
    private final ISecurityRepository repository;
    private String currentSortField = "name";
    private int currentPage = 1;

    public MainController(ISecurityRepository repository) {
        this.repository = repository;
        this.view = new MainWindow();

        if (repository instanceof Observable) {
            ((Observable) repository).addObserver(this);
        }

        initializeListeners();
        initializePagination();
        updateView();
        view.setVisible(true);
    }

    private void initializeListeners() {
        // Существующие обработчики кнопок...
        view.getAddButton().addActionListener(this::handleAdd);
        view.getEditButton().addActionListener(this::handleEdit);
        view.getDeleteButton().addActionListener(this::handleDelete);
        view.getViewButton().addActionListener(this::handleView);
        view.getSortComboBox().addActionListener(this::handleSort);

        // Обновленный обработчик пагинации
        view.getPageSpinner().addChangeListener(e -> handlePageChange());
        view.getPrevButton().addActionListener(e -> navigateToPage(currentPage - 1));
        view.getNextButton().addActionListener(e -> navigateToPage(currentPage + 1));
    }

    private void initializePagination() {
        int totalItems = repository.get_count();
        int totalPages = calculateTotalPages(totalItems);

        // Настраиваем спиннер страниц
        SpinnerNumberModel spinnerModel = (SpinnerNumberModel) view.getPageSpinner().getModel();
        spinnerModel.setMinimum(1);
        spinnerModel.setMaximum(Math.max(1, totalPages));
        spinnerModel.setValue(1);

        updatePaginationControls(totalItems);
    }

    private void handlePageChange() {
        try {
            int newPage = (Integer) view.getPageSpinner().getValue();
            navigateToPage(newPage);
        } catch (Exception e) {
            // Восстанавливаем предыдущее значение в случае ошибки
            view.getPageSpinner().setValue(currentPage);
        }
    }

    private void navigateToPage(int newPage) {
        int totalItems = repository.get_count();
        int totalPages = calculateTotalPages(totalItems);

        // Проверяем границы
        if (newPage < 1) {
            newPage = 1;
        } else if (newPage > totalPages) {
            newPage = totalPages;
        }

        if (newPage != currentPage) {
            currentPage = newPage;
            view.getPageSpinner().setValue(currentPage);
            updateView();
        }

        // Обновляем состояние кнопок навигации
        updatePaginationControls(totalItems);
    }

    private void updatePaginationControls(int totalItems) {
        int totalPages = calculateTotalPages(totalItems);

        // Обновляем спиннер
        SpinnerNumberModel spinnerModel = (SpinnerNumberModel) view.getPageSpinner().getModel();
        spinnerModel.setMaximum(Math.max(1, totalPages));

        // Обновляем надпись с общим количеством страниц
        view.updateTotalPages(totalPages);

        // Обновляем состояние кнопок
        view.getPrevButton().setEnabled(currentPage > 1);
        view.getNextButton().setEnabled(currentPage < totalPages);

        // Обновляем информацию о записях
        int startRecord = (currentPage - 1) * view.getPageSize() + 1;
        int endRecord = Math.min(currentPage * view.getPageSize(), totalItems);
        view.updateRecordInfo(startRecord, endRecord, totalItems);
    }

    private int calculateTotalPages(int totalItems) {
        return Math.max(1, (int) Math.ceil((double) totalItems / view.getPageSize()));
    }

    @Override
    public void update() {
        SwingUtilities.invokeLater(() -> {
            int totalItems = repository.get_count();
            int totalPages = calculateTotalPages(totalItems);

            if (currentPage > totalPages) {
                currentPage = Math.max(1, totalPages);
                view.getPageSpinner().setValue(currentPage);
            }

            updateView();
            updatePaginationControls(totalItems);
        });
    }

    private void updateView() {
        List<BriefSecurity> securities = repository.get_k_n_short_list(
                currentPage,
                view.getPageSize(),
                currentSortField
        );
        int totalItems = repository.get_count();
        view.updateSecurities(securities, totalItems);
        updatePaginationControls(totalItems);
    }

    private void handleAdd(ActionEvent e) {
        new AddEditSecurityController(view, repository);
    }

    private void handleEdit(ActionEvent e) {
        int selectedRow = view.getSecuritiesTable().getSelectedRow();
        if (selectedRow != -1) {
            // Получаем данные о выбранной ценной бумаге
            String name = (String) view.getSecuritiesTable().getValueAt(selectedRow, 0);
            List<BriefSecurity> currentPageItems = repository.get_k_n_short_list(
                    currentPage,
                    view.getPageSize(),
                    currentSortField
            );

            // Находим выбранную ценную бумагу по имени
            BriefSecurity selectedSecurity = currentPageItems.stream()
                    .filter(s -> s.getName().equals(name))
                    .findFirst()
                    .orElse(null);

            if (selectedSecurity != null) {
                new AddEditSecurityController(view, repository, selectedSecurity.getSecurityId());
            }
        }
    }

    private void handleDelete(ActionEvent e) {
        int selectedRow = view.getSecuritiesTable().getSelectedRow();
        if (selectedRow != -1) {
            // Получаем данные о выбранной ценной бумаге
            String name = (String) view.getSecuritiesTable().getValueAt(selectedRow, 0);
            List<BriefSecurity> currentPageItems = repository.get_k_n_short_list(
                    currentPage,
                    view.getPageSize(),
                    currentSortField
            );

            // Находим выбранную ценную бумагу по имени
            BriefSecurity selectedSecurity = currentPageItems.stream()
                    .filter(s -> s.getName().equals(name))
                    .findFirst()
                    .orElse(null);

            if (selectedSecurity != null) {
                int option = JOptionPane.showConfirmDialog(
                        view,
                        "Are you sure you want to delete security '" + name + "'?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (option == JOptionPane.YES_OPTION) {
                    try {
                        repository.deleteSecurity(selectedSecurity.getSecurityId());

                        // Проверяем, не была ли удалена последняя запись на странице
                        int totalItems = repository.get_count();
                        int totalPages = (int) Math.ceil((double) totalItems / view.getPageSize());

                        // Если текущая страница больше общего количества страниц, переходим на последнюю
                        if (currentPage > totalPages && totalPages > 0) {
                            currentPage = totalPages;
                            ((SpinnerNumberModel) view.getPageSpinner().getModel()).setValue(currentPage);
                        }

                        // Обновление не требуется, так как произойдет автоматически через Observer
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(
                                view,
                                "Error deleting security: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        }
    }

    private void handleView(ActionEvent e) {
        int selectedRow = view.getSecuritiesTable().getSelectedRow();
        if (selectedRow != -1) {
            // Получаем данные о выбранной ценной бумаге
            String name = (String) view.getSecuritiesTable().getValueAt(selectedRow, 0);
            List<BriefSecurity> currentPageItems = repository.get_k_n_short_list(
                    currentPage,
                    view.getPageSize(),
                    currentSortField
            );

            // Находим выбранную ценную бумагу по имени
            BriefSecurity selectedSecurity = currentPageItems.stream()
                    .filter(s -> s.getName().equals(name))
                    .findFirst()
                    .orElse(null);

            if (selectedSecurity != null) {
                Security fullSecurity = repository.getById(selectedSecurity.getSecurityId());
                new SecurityDetailsController(view, fullSecurity);
            }
        }
    }

    private void handleSort(ActionEvent e) {
        currentSortField = (String) view.getSortComboBox().getSelectedItem();
        updateView();
    }


}