import javax.swing.*;

public class MainWindow {
    private final JFrame window;

    public MainWindow(ConnectionProvider connectionProvider) {
        // TODO: customize window with your project or team's name, add styling as you like
        window = new JFrame("Queryrunner2");
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        // TODO: replace with your own queries
        ReadOnlyQuery[] queries = new ReadOnlyQuery[] {
                new ReadOnlyQuery("Itinerary", "SELECT * FROM itinerary"),
                new ReadOnlyQuery("Manufacturer", "SELECT * FROM manufacturer"),
                new ReadOnlyQuery("Airport", "SELECT * FROM airport"),
                new ReadOnlyQuery("Flight details", "SELECT * FROM Flight_details"),
                new ReadOnlyQuery("Passenger info", "SELECT * FROM passenger"),
                new ReadOnlyQuery("User Accounts", "SELECT * FROM user_account"),
                new ReadOnlyQuery("Payment", "SELECT * FROM payment")
        };
        QueryTableView queryTableView = new QueryTableView(queries, connectionProvider);
        tabs.add("All Queries", queryTableView);

        // TODO: add your own components for showing parameterized queries
        //tabs.add("Parameterized Select", new JPanel());
        tabs.add("Create a Flight", new CreateFlightView(connectionProvider));
        tabs.add("Search a flight", new SearchFlightView(connectionProvider));
        //tabs.add("Insert, Update, or Delete", new JPanel());
        tabs.add("Book A Flight", new BookFlightView(connectionProvider));
        //tabs.add("Search for a flight", )

        window.setContentPane(tabs);
    }

    public void show() {
        window.setVisible(true);
    }
}
