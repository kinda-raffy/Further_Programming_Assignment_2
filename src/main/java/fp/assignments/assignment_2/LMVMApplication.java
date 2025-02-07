package fp.assignments.assignment_2;

import fp.assignments.assignment_2.controller.home.detail.EventDetailsController;
import fp.assignments.assignment_2.controller.home.detail.UserDetailController;
import fp.assignments.assignment_2.model.entity.Event;
import fp.assignments.assignment_2.model.entity.User;
import fp.assignments.assignment_2.service.ServiceProvider;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The LMVMApplication class is responsible for initialising
 * the primary stage and managing navigation between various views.
 */
public class LMVMApplication extends Application {
    private static StackPane mainContainer;
    private static boolean devMode = false;

    /**
     * Starts the JavaFX application by initialising the primary stage and main
     * container.
     * If developer mode is enabled, a dummy developer user is set and the
     * application navigates directly to the home view.
     * Otherwise, the application navigates to the login view.
     *
     * @param stage the primary stage for this application.
     * @throws IOException if any FXML resource fails to load.
     */
    @Override
    public void start(Stage stage) throws IOException {
        mainContainer = new StackPane();
        Scene scene = new Scene(mainContainer, 1710, 900);
        stage.setTitle("Live Music Venue Matchmaker");
        stage.setScene(scene);
        stage.setMaximized(true);

        if (devMode) {
            User devUser = new User(1, "dev", "dev", "dev", "dev", "manager");
            ServiceProvider.run(sp -> sp.session().setCurrentUser(devUser));
            navigateToHome();
        } else {
            navigateToLogin();
        }

        stage.show();
    }

    /**
     * Navigates to the login view of the application.
     *
     * @throws IOException if the FXML file for the login view is not found or
     *                     cannot be loaded.
     */
    public static void navigateToLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(LMVMApplication.class.getResource("view/landing/login-view.fxml"));
        mainContainer.getChildren().clear();
        mainContainer.getChildren().add(loader.load());
    }

    /**
     * Navigates to the home view of the application.
     *
     * @throws IOException if the FXML file for the home view is not found or cannot
     *                     be loaded.
     */
    public static void navigateToHome() throws IOException {
        FXMLLoader loader = new FXMLLoader(LMVMApplication.class.getResource("view/home/home-view.fxml"));
        mainContainer.getChildren().clear();
        mainContainer.getChildren().add(loader.load());
    }

    /**
     * Navigates to the event details view.
     *
     * @param eventData the Event object containing details to be displayed.
     * @throws IOException if the FXML file for the event detail view is not found
     *                     or cannot be loaded.
     */
    public static void navigateToEventDetails(Event eventData) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                LMVMApplication.class.getResource("view/home/detail/event-detail-view.fxml"));
        mainContainer.getChildren().add(loader.load());

        EventDetailsController controller = loader.getController();
        controller.setEvent(eventData);
    }

    /**
     * Navigates to the user details view.
     *
     * @param userData      the User object containing details to be displayed.
     * @param onUserDeleted a Runnable callback executed when the user is deleted.
     * @param onUserUpdated a Runnable callback executed when the user is updated.
     * @throws IOException if the FXML file for the user detail view is not found or
     *                     cannot be loaded.
     */
    public static void navigateToUserDetails(User userData, Runnable onUserDeleted, Runnable onUserUpdated)
            throws IOException {
        FXMLLoader loader = new FXMLLoader(LMVMApplication.class.getResource("view/home/detail/user-detail-view.fxml"));
        mainContainer.getChildren().add(loader.load());

        UserDetailController controller = loader.getController();
        controller.setUser(userData);
        controller.setOnUserDeleted(onUserDeleted);
        controller.setOnUserUpdated(onUserUpdated);
    }

    /**
     * Navigates to the manager sign up view.
     *
     * @throws IOException if the FXML file for the manager sign up view is not
     *                     found or cannot be loaded.
     */
    public static void navigateToManagerSignUp() throws IOException {
        FXMLLoader loader = new FXMLLoader(LMVMApplication.class.getResource("view/landing/manager-sign-up-view.fxml"));
        mainContainer.getChildren().clear();
        mainContainer.getChildren().add(loader.load());
    }

    /**
     * Navigates back to the previous view.
     * This method removes the topmost view from the main container if there is more
     * than one view.
     * If only one view is present, it navigates to the home view.
     */
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

    /**
     * Sets the developer mode flag.
     * When developer mode is enabled, the application bypasses the login screen by
     * utilising a dummy developer user.
     *
     * @param enabled a boolean value indicating whether developer mode should be
     *                enabled.
     */
    public static void setDevMode(boolean enabled) {
        devMode = enabled;
    }

    /**
     * Entry point.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        setDevMode(false);
        launch();
    }
}