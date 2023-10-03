import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.sql.*;

public class CreateFlightView extends JPanel {
    private final ConnectionProvider connectionProvider;
    private JTextField destField;

    private JTextField originField;
    private JTextField tailNumField;
    private JTextField originTimeField;

    private String name;
    private String destCode;
    private String originCode;

    //private String flightNum;
    private String tailNum;
    private String timeOrigin;

    private JLabel statusLabel;

    private JTable flightTable;

    //private String flightStatus;

    /*
    public AddOrderView() {
        //add (new JLabel("Passenger info"));
        add (new JLabel("Enter passenger name: "));
        add (new JTextField());
        JTextField nameField = new JTextField();
        nameField.setColumns(32);
        add (nameField);
    }*/

    public CreateFlightView(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;

        // add (new JLabel("Passenger info"));

        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        JLabel destLabel = new JLabel("Destination airport code:");
        layout.putConstraint(SpringLayout.WEST, destLabel, 4, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, destLabel, 4, SpringLayout.NORTH, this);
        add (destLabel);

        // add (new JTextField());
        // JTextField nameField = new JTextField(32);

        destField = new JTextField(3);
        layout.putConstraint(SpringLayout.WEST, destField, 8, SpringLayout.EAST, destLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, destField, 0, SpringLayout.VERTICAL_CENTER, destLabel);
        //destField.addActionListener(this::handleNameChange);
        // nameField.setColumns(32);
        add (destField);

        JLabel originLabel = new JLabel("Origin airport code:");
        layout.putConstraint(SpringLayout.EAST, originLabel, 0, SpringLayout.EAST, destLabel);
        layout.putConstraint(SpringLayout.NORTH, originLabel, 12, SpringLayout.SOUTH, destLabel);
        add(originLabel);
        // JTextField originField = new JTextField(3);
        originField = new JTextField(3);
        layout.putConstraint(SpringLayout.WEST, originField, 8, SpringLayout.EAST, originLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, originField, 0, SpringLayout.VERTICAL_CENTER, originLabel);
        add(originField);
        /*

        JLabel flightNumLabel = new JLabel("Flight number:");
        layout.putConstraint(SpringLayout.EAST, flightNumLabel, 0, SpringLayout.EAST, originLabel);
        layout.putConstraint(SpringLayout.NORTH, flightNumLabel, 12, SpringLayout.SOUTH, originLabel);
        add(flightNumLabel);
        JTextField flightNumField = new JTextField(6);
        layout.putConstraint(SpringLayout.WEST, flightNumField, 8, SpringLayout.EAST, flightNumLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, flightNumField, 0, SpringLayout.VERTICAL_CENTER, flightNumLabel);
        add(flightNumField);

         */
        JLabel tailNumLabel = new JLabel("Tail number:");
        layout.putConstraint(SpringLayout.EAST, tailNumLabel, 0, SpringLayout.EAST, originLabel);
        layout.putConstraint(SpringLayout.NORTH, tailNumLabel, 12, SpringLayout.SOUTH, originLabel);
        add(tailNumLabel);
        // JTextField tailNumField = new JTextField(5);
        tailNumField = new JTextField(5);
        layout.putConstraint(SpringLayout.WEST, tailNumField, 8, SpringLayout.EAST, tailNumLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, tailNumField, 0, SpringLayout.VERTICAL_CENTER, tailNumLabel);
        add(tailNumField);

        JLabel originTimeLabel = new JLabel("(yyyy-mm-dd hh:mm:ss):");
        layout.putConstraint(SpringLayout.EAST, originTimeLabel, 0, SpringLayout.EAST, tailNumLabel);
        layout.putConstraint(SpringLayout.NORTH, originTimeLabel, 12, SpringLayout.SOUTH, tailNumLabel);
        add(originTimeLabel);
        // JTextField originTimeField = new JTextField(16);
        originTimeField = new JTextField(16);
        layout.putConstraint(SpringLayout.WEST, originTimeField, 8, SpringLayout.EAST, originTimeLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, originTimeField, 0, SpringLayout.VERTICAL_CENTER, originTimeLabel);
        add(originTimeField);

        /*
        JLabel statusLabel = new JLabel("Flight status:");
        layout.putConstraint(SpringLayout.EAST, statusLabel, 0, SpringLayout.EAST, originTimeLabel);
        layout.putConstraint(SpringLayout.NORTH, statusLabel, 12, SpringLayout.SOUTH, originTimeLabel);
        add(statusLabel);
        JTextField statusField = new JTextField(6);
        layout.putConstraint(SpringLayout.WEST, statusField, 4, SpringLayout.EAST, statusLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, statusField, 0, SpringLayout.VERTICAL_CENTER, statusLabel);
        add(statusField);
         */

        JButton submitButton = new JButton("Create...");
        layout.putConstraint(SpringLayout.WEST, submitButton, 4, SpringLayout.WEST, originTimeLabel);
        layout.putConstraint(SpringLayout.NORTH, submitButton, 16, SpringLayout.SOUTH, originTimeLabel);
        submitButton.addActionListener(this::submitFlight);
        add(submitButton);

        // Create the status label
        statusLabel = new JLabel();
        layout.putConstraint(SpringLayout.WEST, statusLabel, 4, SpringLayout.WEST, submitButton);
        layout.putConstraint(SpringLayout.NORTH, statusLabel, 16, SpringLayout.SOUTH, submitButton);
        add(statusLabel);

        // Create the flight table
        flightTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(flightTable);
        layout.putConstraint(SpringLayout.WEST, scrollPane, 4, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, scrollPane, 16, SpringLayout.SOUTH, statusLabel);
        add(scrollPane);
    }
/*
    private void handleNameChange(ActionEvent event) {
        System.out.println("Name is now " + destField.getText());
        name = destField.getText();
    }
*/
    private void submitFlight(ActionEvent event) {
        // run our query
        // INSERT INTO PASSENGER (NAME, ...)
        try {
            // Retrieve values from text fields
            destCode = destField.getText();
            originCode = originField.getText();
            timeOrigin = originTimeField.getText();
            tailNum = tailNumField.getText();

            Connection connection = connectionProvider.getConnection();
            // Statement statement = connection.createStatement();
            // statement.executeQuery("SELECT * FROM passenger");
            // statement.executeUpdate("INSERT INTO passenger VALUES (" + )
            // PrepareStatement statement = connection.prepareStatement("INSERT INTO non_coffee_item (name, price, caloties) VALUES (?,?,?)");
            PreparedStatement statement = connection.prepareStatement("CALL insert_flight_details (?, ?, ?, ?)");

            //statement.setString(1, flightNum);
            statement.setString(1, originCode);
            statement.setString(2, destCode);
            statement.setString(3, timeOrigin);
            statement.setString(4, tailNum);
            //statement.setString(6, flightStatus);
            //statement.executeQuery();

            int numRowsUpdated = statement.executeUpdate();
            if (numRowsUpdated == 1) {
                statusLabel.setText("Creation successful!");
            }

            // Retrieve data from the flight_details table
            ResultSet resultSet = statement.executeQuery("SELECT * FROM flight_details");

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
            flightTable.setModel(tableModel);

            /*
            // Process the retrieved data as needed
            while (resultSet.next()) {
                // Access data using resultSet.getXXX() methods
                String originAirportCode = resultSet.getString("origin_airport_code");
            }
            */


            //statement.setDouble(2, 3.50);
            //statement.setDouble(3, 400);
            //statement.executeUpdate();
            //statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
