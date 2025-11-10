import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ChatClient extends Application {
    private PrintWriter out;
    private BufferedReader in;

    private TextArea messages = new TextArea();
    private TextField input = new TextField();
    private TextField userField = new TextField();
    private PasswordField passField = new PasswordField();
    private TextField receiverField = new TextField();

    // Layouts as class fields
    private VBox loginBox;
    private VBox chatBox;
    private BorderPane root;

    @Override
    public void start(Stage stage) throws Exception {
        // Create login box
        loginBox = new VBox(5,
                new Label("Username:"), userField,
                new Label("Password:"), passField,
                new Button("Login") {{
                    setOnAction(e -> loginOrRegister("LOGIN"));
                }},
                new Button("Register") {{
                    setOnAction(e -> loginOrRegister("REGISTER"));
                }});

        // Create chat box
        chatBox = new VBox(5,
                new Label("Messages:"), messages,
                new Label("To User:"), receiverField, input,
                new Button("Send") {{
                    setOnAction(e -> sendMessage());
                }}
        );

        // Root layout
        root = new BorderPane();
        root.setCenter(loginBox);

        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Chat Client");
        stage.show();

        messages.setEditable(false);

        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 1234);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("LOGIN_SUCCESS")) {
                        Platform.runLater(() -> root.setCenter(chatBox));
                    } else if (line.startsWith("MSG ")) {
                        String finalLine1 = line;
                        Platform.runLater(() -> messages.appendText(finalLine1.substring(4) + "\n"));
                    } else if (line.startsWith("HIST ")) {
                        String finalLine = line;
                        Platform.runLater(() -> messages.appendText("[History] " + finalLine.substring(5) + "\n"));
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void loginOrRegister(String mode) {
        out.println(mode + " " + userField.getText().trim() + " " + passField.getText().trim());
    }

    private void sendMessage() {
        out.println("MSG " + receiverField.getText().trim() + " " + input.getText().trim());
        input.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
