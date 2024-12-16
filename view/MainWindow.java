package org.investment.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import org.investment.BriefSecurity;

public class MainWindow extends JFrame {
    private final JTable securitiesTable;
    private final DefaultTableModel tableModel;
    private final JButton addButton;
    private final JButton editButton;
    private final JButton deleteButton;
    private final JButton viewButton;
    private final JComboBox<String> sortComboBox;
    private final JSpinner pageSpinner;
    private final JLabel totalPagesLabel;
    private final int pageSize = 5;
    private final JButton prevButton;
    private final JButton nextButton;
    private final JLabel recordInfoLabel;
    public MainWindow() {
        setTitle("Securities Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Создаем модель таблицы
        String[] columnNames = {"Name", "Type", "Current Price"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Создаем таблицу
        securitiesTable = new JTable(tableModel);
        securitiesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(securitiesTable);

        // Панель с кнопками
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        viewButton = new JButton("View Details");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        // Панель сортировки
        JPanel sortPanel = new JPanel();
        sortPanel.add(new JLabel("Sort by:"));
        String[] sortOptions = {"name", "type", "currentPrice"};
        sortComboBox = new JComboBox<>(sortOptions);
        sortPanel.add(sortComboBox);

        // Панель пагинации
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));

        // Кнопки навигации
        prevButton = new JButton("◄");
        prevButton.setEnabled(false);
        paginationPanel.add(prevButton);

        // Спиннер страниц
        paginationPanel.add(new JLabel("Page:"));
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 1, 1);
        pageSpinner = new JSpinner(spinnerModel);
        pageSpinner.setPreferredSize(new Dimension(60, 25));
        paginationPanel.add(pageSpinner);

        // Общее количество страниц
        totalPagesLabel = new JLabel("of 1");
        paginationPanel.add(totalPagesLabel);

        nextButton = new JButton("►");
        nextButton.setEnabled(false);
        paginationPanel.add(nextButton);

        // Информация о записях
        recordInfoLabel = new JLabel("Showing 0-0 of 0 records");
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(paginationPanel, BorderLayout.CENTER);
        statusPanel.add(recordInfoLabel, BorderLayout.EAST);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Добавляем панель в окно
        add(statusPanel, BorderLayout.SOUTH);

        // Компоновка элементов
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(buttonPanel, BorderLayout.CENTER);
        topPanel.add(sortPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        add(paginationPanel, BorderLayout.SOUTH);

        // Изначально кнопки редактирования и удаления неактивны
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        viewButton.setEnabled(false);

        // Слушатель выделения строки в таблице
        securitiesTable.getSelectionModel().addListSelectionListener(e -> {
            boolean rowSelected = securitiesTable.getSelectedRow() != -1;
            editButton.setEnabled(rowSelected);
            deleteButton.setEnabled(rowSelected);
            viewButton.setEnabled(rowSelected);
        });
    }

    public void updateSecurities(List<BriefSecurity> securities, int totalItems) {
        tableModel.setRowCount(0);
        for (BriefSecurity security : securities) {
            tableModel.addRow(new Object[]{
                    security.getName(),
                    security.getType(),
                    security.getCurrentPrice()
            });
        }

        // Обновляем пагинацию
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        ((SpinnerNumberModel) pageSpinner.getModel()).setMaximum(totalPages);
        totalPagesLabel.setText("of " + totalPages);
    }

    public void updateTotalPages(int totalPages) {
        totalPagesLabel.setText("of " + totalPages);
    }

    public void updateRecordInfo(int startRecord, int endRecord, int totalRecords) {
        recordInfoLabel.setText(String.format("Showing %d-%d of %d records",
                startRecord, endRecord, totalRecords));
    }

    // Геттеры для новых компонентов
    public JButton getPrevButton() { return prevButton; }
    public JButton getNextButton() { return nextButton; }

    // Геттеры для компонентов UI
    public JButton getAddButton() { return addButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getViewButton() { return viewButton; }
    public JComboBox<String> getSortComboBox() { return sortComboBox; }
    public JSpinner getPageSpinner() { return pageSpinner; }
    public JTable getSecuritiesTable() { return securitiesTable; }
    public int getPageSize() { return pageSize; }
}