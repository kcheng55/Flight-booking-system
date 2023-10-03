import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws Exception {
        String username = args[0];
        String password = args[1];
        SwingUtilities.invokeAndWait(() -> {
            new MainWindow(new BasicConnectionProvider(username, password)).show();
        });
    }

    // TODO: Replace me with something that actually creates connections!
    private static class BasicConnectionProvider implements ConnectionProvider {
        final private String username;
        final private String password;
        public BasicConnectionProvider (String username, String password) {
            this.username = username;
            this.password = password;
        }
        @Override
        public Connection getConnection() throws SQLException {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/airline_tickets", username, password);
            // return null;
        }
    }
}
