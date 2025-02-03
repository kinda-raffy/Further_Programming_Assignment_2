package fp.assignments.assignment_2;

import fp.assignments.assignment_2.controller.EventDetailsController;
import fp.assignments.assignment_2.model.Event;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LMVMApplication extends Application {
    private static StackPane mainContainer;

    @Override
    public void start(Stage stage) throws IOException {
        mainContainer = new StackPane();

        Scene scene = new Scene(mainContainer, 1024, 768);
        stage.setTitle("Live Music Venue Matchmaker");
        stage.setScene(scene);
        stage.setMaximized(true);
        navigateToHome();
        stage.show();
    }

    public static void navigateToHome() throws IOException {
        FXMLLoader loader = new FXMLLoader(LMVMApplication.class.getResource("view/home-view.fxml"));
        mainContainer.getChildren().clear();
        mainContainer.getChildren().add(loader.load());
    }

    public static void navigateToEventDetails(Event eventData) throws IOException {
        FXMLLoader loader = new FXMLLoader(LMVMApplication.class.getResource("view/event-detail-view.fxml"));
        mainContainer.getChildren().add(loader.load());

        EventDetailsController controller = loader.getController();
        controller.setEvent(eventData);
    }

    public static void goBack() {
        if (mainContainer.getChildren().size() > 1) {
            mainContainer.getChildren().remove(mainContainer.getChildren().size() - 1);
        } else {
            try {
                navigateToHome();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}