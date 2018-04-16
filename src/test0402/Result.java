package test0402;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl0.ErrorLog;

public class Result {
    public void start(Stage primaryStage){
        TextArea result=new TextArea();
        TextArea error=new TextArea();
        result.setEditable(false);
        error.setEditable(false);
        result.setText(TestPrepared.getTokensString());
        error.setText(ErrorLog.getes());
        Button button=new Button("OK");
        button.setOnAction(event -> {
            primaryStage.close();
        });
        Label labelResult=new Label("Result:");
        Label labelError=new Label("Error:");
        VBox vBox=new VBox(labelResult,result,labelError,error,button);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10,10,10,10));
        Scene scene=new Scene(vBox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
