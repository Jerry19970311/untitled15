package test0511;

import pl0.Main;
import pl0gaprepared.NotLL1Exception;

import java.io.IOException;
import java.util.Scanner;

public class GATest {
    public static String file;
    public static void main(String[] args) throws IOException, NotLL1Exception {
        System.out.println("请输入要编译的文件名:");
        Scanner input=new Scanner(System.in);
        file=input.next();
        Main.grammarAnalysis(file);
    }
}
