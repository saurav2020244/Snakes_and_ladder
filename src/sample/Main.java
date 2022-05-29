package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(root);
        Image icon = new Image("Snakes.png");
        stage.getIcons().add(icon);
        stage.setTitle("Snakes And Ladders");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

class Space extends Rectangle {
    int id, xp, yp;
    boolean odd;
    public Space(int miny, int minx, int iden, boolean od){
        xp = minx;
        yp = miny;
        this.id = iden;
        this.odd = od;
    }
}

