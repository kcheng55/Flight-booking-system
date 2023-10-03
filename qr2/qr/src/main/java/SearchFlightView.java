import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.sql.*;

public class SearchFlightView extends JPanel {
    private final ConnectionProvider connectionProvider;
    private JTextField destField;
    private JTextField originField;
    private JTextField dateField;
    private String destCity;
    private String originCity;
    private String flyDate;
    private JLabel statusLabel;
    private JTable serachTable;
    public SearchFlightView(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;

        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        JLabel destLabel = new JLabel("Destination city:");
        layout.putConstraint(SpringLayout.WEST, destLabel, 4, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, destLabel, 4, SpringLayout.NORTH, this);
        add (destLabel);
        destField = new JTextField(10);
        layout.putConstraint(SpringLayout.WEST, destField, 8, SpringLayout.EAST, destLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, destField, 0, SpringLayout.VERTICAL_CENTER, destLabel);
        add (destField);

        JLabel originLabel = new JLabel("Origin city:");
        layout.putConstraint(SpringLayout.EAST, originLabel, 0, SpringLayout.EAST, destLabel);
        layout.putConstraint(SpringLayout.NORTH, originLabel, 12, SpringLayout.SOUTH, destLabel);
        add(originLabel);
        originField = new JTextField(10);
        layout.putConstraint(SpringLayout.WEST, originField, 8, SpringLayout.EAST, originLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, originField, 0, SpringLayout.VERTICAL_CENTER, originLabel);
        add(originField);

        JLabel dateLabel = new JLabel("Date (yyyy-mm-dd):");
        layout.putConstraint(SpringLayout.WEST, dateLabel, 4, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, dateLabel, 12, SpringLayout.SOUTH, originLabel);
        add(dateLabel);
        dateField = new JTextField(10);
        layout.putConstraint(SpringLayout.WEST, dateField, 8, SpringLayout.EAST, dateLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, dateField, 0, SpringLayout.VERTICAL_CENTER, dateLabel);
        add(dateField);

        JButton submitButton = new JButton("Search...");
        layout.putConstraint(SpringLayout.WEST, submitButton, 4, SpringLayout.WEST, dateLabel);
        layout.putConstraint(SpringLayout.NORTH, submitButton, 16, SpringLayout.SOUTH, dateLabel);
        submitButton.addActionListener(this::submitFlight);
        add(submitButton);

        // Create the status label
        statusLabel = new JLabel();
        layout.putConstraint(SpringLayout.WEST, statusLabel, 4, SpringLayout.WEST, submitButton);
        layout.putConstraint(SpringLayout.NORTH, statusLabel, 16, SpringLayout.SOUTH, submitButton);
        add(statusLabel);

        // Create the search table
        serachTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(serachTable);
        layout.putConstraint(SpringLayout.WEST, scrollPane, 4, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, scrollPane, 16, SpringLayout.SOUTH, statusLabel);
        add(scrollPane);
    }
    private void submitFlight(ActionEvent event) {
        // run our query
        // INSERT INTO PASSENGER (NAME, ...)
        try {
            // Retrieve values from text fields
            destCity = destField.getText();
            originCity = originField.getText();
            flyDate = dateField.getText();

            Connection connection = connectionProvider.getConnection();

            PreparedStatement statement = connection.prepareStatement("CALL search_flights (?, ?, ?)");
            statement.setString(1, originCity);
            statement.setString(2, destCity);
            statement.setString(3, flyDate);

            boolean hasResults = statement.execute();
            if (!hasResults) {
                statusLabel.setText("No Flights Found :(");
            } else {
                ResultSet resultSet = statement.getResultSet();

                // Create a table model to hold the data
                DefaultTableModel tableModel = new DefaultTableModel();
                ResultSetMetaData metaData = resultSet.getMetaData();

                // Get column names
                int columnCount = metaData.getColumnCount();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    tableModel.addColumn(metaData.getColumnName(columnIndex));
                }

                // Get row data
                while (resultSet.next()) {
                    Object[] rowData = new Object[columnCount];
                    for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                        rowData[columnIndex - 1] = resultSet.getObject(columnIndex);
                    }
                    tableModel.addRow(rowData);
                }

                // Set the table model to the JTable
                serachTable.setModel(tableModel);
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

