import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.sql.*;

public class BookFlightView extends JPanel {
    private final ConnectionProvider connectionProvider;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField userIdField;
    private JTextField passportIdField;
    private JTextField ageField;
    private JTextField emailField;
    private JTextField passwordField;
    private JTextField securityQuestionField;
    private JTextField phoneNumberField;
    private JComboBox<String> paymentMethodComboBox;
    private JTextField bankNameField;
    private JTextField amountPaidField;
    private JTextField flightNumField;
    private JLabel statusLabel;

    public BookFlightView(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;

        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        // Create and position the input labels and fields
        JLabel firstNameLabel = new JLabel("First Name:");
        layout.putConstraint(SpringLayout.WEST, firstNameLabel, 4, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, firstNameLabel, 4, SpringLayout.NORTH, this);
        add(firstNameLabel);
        firstNameField = new JTextField(10);
        layout.putConstraint(SpringLayout.WEST, firstNameField, 8, SpringLayout.EAST, firstNameLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, firstNameField, 0, SpringLayout.VERTICAL_CENTER, firstNameLabel);
        add(firstNameField);

        JLabel lastNameLabel = new JLabel("Last Name:");
        layout.putConstraint(SpringLayout.WEST, lastNameLabel, 4, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, lastNameLabel, 12, SpringLayout.SOUTH, firstNameLabel);
        add(lastNameLabel);
        lastNameField = new JTextField(10);
        layout.putConstraint(SpringLayout.WEST, lastNameField, 8, SpringLayout.EAST, lastNameLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, lastNameField, 0, SpringLayout.VERTICAL_CENTER, lastNameLabel);
        add(lastNameField);

        JLabel userIdLabel = new JLabel("User ID:");
        layout.putConstraint(SpringLayout.WEST, userIdLabel, 4, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, userIdLabel, 12, SpringLayout.SOUTH, lastNameLabel);
        add(userIdLabel);
        userIdField = new JTextField(10);
        layout.putConstraint(SpringLayout.WEST, userIdField, 8, SpringLayout.EAST, userIdLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, userIdField, 0, SpringLayout.VERTICAL_CENTER, userIdLabel);
        add(userIdField);

        JLabel passportIdLabel = new JLabel("Passport ID:");
        layout.putConstraint(SpringLayout.WEST, passportIdLabel, 4, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, passportIdLabel, 12, SpringLayout.SOUTH, userIdLabel);
        add(passportIdLabel);
        passportIdField = new JTextField(10);
        layout.putConstraint(SpringLayout.WEST, passportIdField, 8, SpringLayout.EAST, passportIdLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, passportIdField, 0, SpringLayout.VERTICAL_CENTER, passportIdLabel);
        add(passportIdField);

        JLabel ageLabel = new JLabel("Age:");
        layout.putConstraint(SpringLayout.WEST, ageLabel, 4, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, ageLabel, 12, SpringLayout.SOUTH, passportIdLabel);
        add(ageLabel);
        ageField = new JTextField(10);
        layout.putConstraint(SpringLayout.WEST, ageField, 8, SpringLayout.EAST, ageLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, ageField, 0, SpringLayout.VERTICAL_CENTER, ageLabel);
        add(ageField);

        JLabel emailLabel = new JLabel("Email:");
        layout.putConstraint(SpringLayout.WEST, emailLabel, 4, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, emailLabel, 12, SpringLayout.SOUTH, ageLabel);
        add(emailLabel);
        emailField = new JTextField(10);
        layout.putConstraint(SpringLayout.WEST, emailField, 8, SpringLayout.EAST, emailLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, emailField, 0, SpringLayout.VERTICAL_CENTER, emailLabel);
        add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        layout.putConstraint(SpringLayout.WEST, passwordLabel, 4, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, passwordLabel, 12, SpringLayout.SOUTH, emailLabel);
        add(passwordLabel);
        passwordField = new JTextField(10);
        layout.putConstraint(SpringLayout.WEST, passwordField, 8, SpringLayout.EAST, passwordLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, passwordField, 0, SpringLayout.VERTICAL_CENTER, passwordLabel);
        add(passwordField);

        JLabel securityQuestionLabel = new JLabel("Security Question:");
        layout.putConstraint(SpringLayout.WEST, securityQuestionLabel, 4, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, securityQuestionLabel, 12, SpringLayout.SOUTH, passwordLabel);
        add(securityQuestionLabel);
        securityQuestionField = new JTextField(10);
        layout.putConstraint(SpringLayout.WEST, securityQuestionField, 8, SpringLayout.EAST, securityQuestionLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, securityQuestionField, 0, SpringLayout.VERTICAL_CENTER, securityQuestionLabel);
        add(securityQuestionField);

        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        layout.putConstraint(SpringLayout.WEST, phoneNumberLabel, 4, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, phoneNumberLabel, 12, SpringLayout.SOUTH, securityQuestionLabel);
        add(phoneNumberLabel);
        phoneNumberField = new JTextField(10);
        layout.putConstraint(SpringLayout.WEST, phoneNumberField, 8, SpringLayout.EAST, phoneNumberLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, phoneNumberField, 0, SpringLayout.VERTICAL_CENTER, phoneNumberLabel);
        add(phoneNumberField);

        JLabel paymentMethodLabel = new JLabel("Payment Method:");
        layout.putConstraint(SpringLayout.WEST, paymentMethodLabel, 4, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, paymentMethodLabel, 12, SpringLayout.SOUTH, phoneNumberLabel);
        add(paymentMethodLabel);
        String[] paymentMethods = { "visa", "master card", "paypal", "bitcoin", "monero" };
        paymentMethodComboBox = new JComboBox<>(paymentMethods);
        layout.putConstraint(SpringLayout.WEST, paymentMethodComboBox, 8, SpringLayout.EAST, paymentMethodLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, paymentMethodComboBox, 0, SpringLayout.VERTICAL_CENTER, paymentMethodLabel);
        add(paymentMethodComboBox);

        JLabel bankNameLabel = new JLabel("Bank Name:");
        layout.putConstraint(SpringLayout.WEST, bankNameLabel, 4, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, bankNameLabel, 12, SpringLayout.SOUTH, paymentMethodLabel);
        add(bankNameLabel);
        bankNameField = new JTextField(10);
        layout.putConstraint(SpringLayout.WEST, bankNameField, 8, SpringLayout.EAST, bankNameLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, bankNameField, 0, SpringLayout.VERTICAL_CENTER, bankNameLabel);
        add(bankNameField);

        JLabel amountPaidLabel = new JLabel("Amount Paid:");
        layout.putConstraint(SpringLayout.WEST, amountPaidLabel, 4, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, amountPaidLabel, 12, SpringLayout.SOUTH, bankNameLabel);
        add(amountPaidLabel);
        amountPaidField = new JTextField(10);
        layout.putConstraint(SpringLayout.WEST, amountPaidField, 8, SpringLayout.EAST, amountPaidLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, amountPaidField, 0, SpringLayout.VERTICAL_CENTER, amountPaidLabel);
        add(amountPaidField);

        JLabel flightNumLabel = new JLabel("Flight Number:");
        layout.putConstraint(SpringLayout.WEST, flightNumLabel, 4, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, flightNumLabel, 12, SpringLayout.SOUTH, amountPaidLabel);
        add(flightNumLabel);
        flightNumField = new JTextField(10);
        layout.putConstraint(SpringLayout.WEST, flightNumField, 8, SpringLayout.EAST, flightNumLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, flightNumField, 0, SpringLayout.VERTICAL_CENTER, flightNumLabel);
        add(flightNumField);

        JButton submitButton = new JButton("Book Flight");
        layout.putConstraint(SpringLayout.WEST, submitButton, 4, SpringLayout.WEST, flightNumField);
        layout.putConstraint(SpringLayout.NORTH, submitButton, 16, SpringLayout.SOUTH, flightNumField);
        submitButton.addActionListener(this::submitBooking);
        add(submitButton);

        statusLabel = new JLabel();
        layout.putConstraint(SpringLayout.WEST, statusLabel, 4, SpringLayout.WEST, submitButton);
        layout.putConstraint(SpringLayout.NORTH, statusLabel, 16, SpringLayout.SOUTH, submitButton);
        add(statusLabel);
    }

    private void submitBooking(ActionEvent event) {
        try {
            // Retrieve values from text fields
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String userId = userIdField.getText();
            String passportId = passportIdField.getText();
            String age = ageField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String securityQuestion = securityQuestionField.getText();
            String phoneNumber = phoneNumberField.getText();
            String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();
            String bankName = bankNameField.getText();
            String amountPaid = amountPaidField.getText();
            String flightNum = flightNumField.getText();

            Connection connection = connectionProvider.getConnection();

            CallableStatement statement = connection.prepareCall("{CALL book_flight(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, userId);
            statement.setString(4, passportId);
            statement.setString(5, age);
            statement.setString(6, email);
            statement.setString(7, password);
            statement.setString(8, securityQuestion);
            statement.setString(9, phoneNumber);
            statement.setString(10, paymentMethod);
            statement.setString(11, bankName);
            statement.setString(12, amountPaid);
            statement.setString(13, flightNum);

            boolean hasResults = statement.execute();
            if (!hasResults) {
                statusLabel.setText("Flight booked successfully!");
            } else {
                statusLabel.setText("Error occurred while booking flight.");
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
