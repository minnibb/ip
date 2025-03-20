package luke;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main Application class for Luke with JavaFX GUI.
 */
public class Main extends Application {
    private Luke luke;
    private TextField userInput;
    private VBox dialogContainer;
    private ScrollPane scrollPane;

    @Override
    public void start(Stage stage) {
        luke = new Luke("data/luke.txt");

        AnchorPane mainLayout = new AnchorPane();

        scrollPane = new ScrollPane();
        dialogContainer = new VBox(10);
        scrollPane.setContent(dialogContainer);

        userInput = new TextField();
        Button sendButton = new Button("Send");

        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        setLayoutConstraints(mainLayout, scrollPane, userInput, sendButton);

        sendButton.setOnMouseClicked((event) -> {
            handleUserInput();
        });

        userInput.setOnAction((event) -> {
            handleUserInput();
        });

        dialogContainer.heightProperty().addListener((observable) -> scrollPane.setVvalue(1.0));

        Scene scene = new Scene(mainLayout, 400, 600);
        stage.setTitle("Luke");
        stage.setMinHeight(400);
        stage.setMinWidth(300);
        stage.setScene(scene);
        stage.show();

        // Show welcome message using Luke's welcome method
        Label welcomeMsg = new Label(luke.getWelcomeMessage());
        dialogContainer.getChildren().add(welcomeMsg);
    }

    /**
     * Sets up the layout constraints for all components.
     */
    private void setLayoutConstraints(AnchorPane mainLayout, ScrollPane scrollPane,
                                      TextField userInput, Button sendButton) {
        // Set main layout size
        mainLayout.setPrefSize(400, 600);

        scrollPane.setPrefSize(385, 535);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setFitToWidth(true);

        AnchorPane.setTopAnchor(scrollPane, 1.0);
        AnchorPane.setLeftAnchor(scrollPane, 1.0);
        AnchorPane.setRightAnchor(scrollPane, 1.0);

        userInput.setPrefWidth(325.0);
        AnchorPane.setBottomAnchor(userInput, 1.0);
        AnchorPane.setLeftAnchor(userInput, 1.0);

        sendButton.setPrefWidth(55.0);
        AnchorPane.setBottomAnchor(sendButton, 1.0);
        AnchorPane.setRightAnchor(sendButton, 1.0);

        AnchorPane.setBottomAnchor(scrollPane, 41.0); // TextField height + padding
    }

    /**
     * Process user input, send it to Luke, and display the response.
     */
    private void handleUserInput() {
        String input = userInput.getText();

        if (input.trim().isEmpty()) {
            return;
        }

        // Add user message to the dialog
        HBox userMessageBox = new HBox();
        Label userMessageLabel = new Label("You: " + input);
        userMessageLabel.setWrapText(true);
        userMessageBox.getChildren().add(userMessageLabel);
        userMessageBox.setAlignment(Pos.CENTER_RIGHT);
        dialogContainer.getChildren().add(userMessageBox);

        // Process the command and get Luke's response
        String response = processCommand(input);

        // Add Luke's response
        HBox lukeMessageBox = new HBox();
        Label lukeMessageLabel = new Label("Luke: " + response);
        lukeMessageLabel.setWrapText(true);
        lukeMessageBox.getChildren().add(lukeMessageLabel);
        lukeMessageBox.setAlignment(Pos.CENTER_LEFT);
        dialogContainer.getChildren().add(lukeMessageBox);

        // Clear the input field
        userInput.clear();
    }

    /**
     * Process a command and get Luke's response.
     */
    private String processCommand(String input) {
        if (input.equals("bye")) {
            // Schedule application to exit after showing response
            Platform.runLater(() -> {
                try {
                    Thread.sleep(800);
                    Platform.exit();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            return "Bye. Hope to see you again!!";
        }

        // Use Luke's getResponse method for all commands
        return luke.getResponse(input);
    }
}