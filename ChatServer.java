import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

public class ChatServer {
    private static Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());
    private static Connection dbConn;

    public static void main(String[] args) throws Exception {
        dbConn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/chat_app", "root", "A@SHI1121");
        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("ChatServer started on port 1234");

        while (true) {
            Socket s = serverSocket.accept();
            ClientHandler handler = new ClientHandler(s, dbConn);
            clients.add(handler);
            new Thread(handler).start();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private Connection dbConn;
        private String username;

        public ClientHandler(Socket s, Connection conn) throws IOException {
            this.socket = s;
            this.dbConn = conn;
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        public void run() {
            try {
                out.println("Welcome! Enter: LOGIN <username> <password> or REGISTER <username> <password>");
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("LOGIN")) {
                        String[] parts = line.split(" ");
                        if (authenticate(parts[1], parts[2])) {
                            username = parts[1];
                            out.println("LOGIN_SUCCESS");
                            sendHistory();
                        } else {
                            out.println("LOGIN_FAIL");
                        }
                    } else if (line.startsWith("REGISTER")) {
                        String[] parts = line.split(" ");
                        if (register(parts[1], parts[2])) {
                            out.println("REGISTER_SUCCESS");
                        } else {
                            out.println("REGISTER_FAIL");
                        }
                    } else if (line.startsWith("MSG")) {
                        // MSG <receiver> <message>
                        String[] parts = line.split(" ", 3);
                        saveMessage(username, parts[1], parts[2]);
                        broadcast(parts[1], username + ": " + parts[2]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try { socket.close(); } catch (IOException ignored) {}
                clients.remove(this);
            }
        }

        private boolean authenticate(String user, String pass) throws SQLException {
            Statement st = dbConn.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT * FROM users WHERE username='" + user + "' AND password='" + pass + "'");
            return rs.next();
        }

        private boolean register(String user, String pass) throws SQLException {
            try {
                PreparedStatement ps = dbConn.prepareStatement(
                        "INSERT INTO users(username, password) VALUES(?, ?)");
                ps.setString(1, user);
                ps.setString(2, pass);
                ps.executeUpdate();
                return true;
            } catch (SQLException e) {
                return false; // Duplicate user, etc.
            }
        }

        private void saveMessage(String sender, String receiver, String content) throws SQLException {
            PreparedStatement ps = dbConn.prepareStatement(
                    "INSERT INTO messages(sender, receiver, content) VALUES(?,?,?)");
            ps.setString(1, sender);
            ps.setString(2, receiver);
            ps.setString(3, content);
            ps.executeUpdate();
        }

        private void sendHistory() throws SQLException {
            Statement st = dbConn.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT * FROM messages WHERE sender='" + username + "' OR receiver='" + username + "' ORDER BY timestamp");
            while (rs.next()) {
                out.println("HIST " + rs.getString("sender") + "->" +
                        rs.getString("receiver") + ": " + rs.getString("content"));
            }
        }

        private void broadcast(String receiver, String msg) {
            synchronized (clients) {
                for (ClientHandler ch : clients) {
                    if (ch.username != null && (ch.username.equals(receiver) || ch.username.equals(username))) {
                        ch.out.println("MSG " + msg);
                    }
                }
            }
        }
    }
}

