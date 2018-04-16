package test0402;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EmptyShell {
    public void start(Stage primaryStage){
        Label label=new Label("请输入pl0语句！");
        Button button=new Button("OK");
        button.setOnAction(event -> {
            primaryStage.close();
        });
        VBox vBox=new VBox(label,button);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10,10,10,10));
        Scene scene=new Scene(vBox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
