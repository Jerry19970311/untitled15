package test0402;

import java.io.*;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pl0.*;

//专用于词法分析的测试，仅输出TOKEN。
public class Test1 extends Application{
    public static boolean isFile=true;
    public static String path;
    public static String shell;
    public static String[] stmts;
    public static int index;
    public static void prepare() throws IOException {
        //System.out.println("Input pl/0 file?");
        //Scanner input=new Scanner(System.in);
        //Pl0.fin=new File(input.next());
            Pl0.fa1 = new File("fa1.tmp");
            Pl0.fa1.createNewFile();
            Pl0.fa1BufferedWriter = new BufferedWriter(new FileWriter(Pl0.fa1));
            Main.init();
            ErrorLog.init();
            TestPrepared.init();
            index=0;
            Pl0.err=0;
            Pl0.cc=Pl0.cx=Pl0.ll=Pl0.whereLine=0;
            Pl0.ch=' ';
            while (-1!=Main.getsym()){
                //System.out.println("/////////////////////////////////////");
                //System.out.println(Pl0.sym);
                TestPrepared.addToken(Pl0.sym);
                //PERIOD是pl0的终结符，完整的编译程序会有专门对此的处理方法；
                //但这里只是简单的词法分析阶段，因此我们需要考虑该情况，当遇到终结符时停止循环，否则会无限循环下去。
                if(Pl0.sym==Symbol.PERIOD){
                    break;
                }
            }
            //System.out.println(TestPrepared.getTokensString());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane gridPane=new GridPane();
        TextField path=new TextField();
        TextArea sh=new TextArea();
        Button buttonPath=new Button("从文件中读取");
        buttonPath.setOnAction(event -> {
            try {
                Pl0.fin = new File(path.getText());
                Pl0.finBufferedReader = new BufferedReader(new FileReader(Pl0.fin));
                isFile=true;
                prepare();
                new Result().start(new Stage());
            }catch (Exception e){
                //e.printStackTrace();
                new ErrorPath().start(new Stage());
            }
        });
        Button buttonSh=new Button("即时读取");
        buttonSh.setOnAction(event -> {
            try {
                String temp = sh.getText();
                //TextArea中，如果没有输入字符串，getText()方法返回的是""，而不是NULL。
                if (null == temp||temp.length()==0) {
                    System.out.println("?????????????????????????????????????????????????");
                    new EmptyShell().start(new Stage());
                } else {
                    isFile = false;
                    Test1.shell = temp;
                    Test1.stmts = shell.split("\n");
                    prepare();
                    new Result().start(new Stage());
                }
            }catch (Exception e){
                e.printStackTrace();
                //new EmptyShell().start(new Stage());
            }
        });
        gridPane.add(path,0,0);
        gridPane.add(sh,0,1);
        gridPane.add(buttonPath,1,0);
        gridPane.add(buttonSh,1,1);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10,10,10,10));
        Scene scene=new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
