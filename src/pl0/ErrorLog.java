package pl0;

import java.security.Key;
import java.util.*;

public class ErrorLog {
    //将错误码和错误信息关联。
    public static Map<Integer,String> errorsReflect;
    public static List<Errors> errors;
    public static void init(){
        errorsReflect=new Hashtable<Integer, String>();
        errors=new LinkedList<>();
        getReflects();
    }
    public static void addError(int line,int no){
        errors.add(new Errors(line,no));
    }
    public static String getes(){
        String str="";
        Iterator<Errors> errorsIterator=errors.iterator();
        while (errorsIterator.hasNext()){
            Errors errors=errorsIterator.next();
            str=str+errors.getLine()+":"+errorsReflect.get(errors.getErrorNo())+"\n";
        }
        return str;
    }
    private static void getReflects(){
        errorsReflect.put(1,"Program incomplete");//文件并没有以.结尾
        errorsReflect.put(2,"NULL Symbol");//标识符为NUL
        errorsReflect.put(3,"Number Exception");//数值过大
        errorsReflect.put(4,"Unexpected Symbol");//
        errorsReflect.put(5,"False Follow Symbol");
    }
}

class Errors{
    private int line;
    private int errorNo;
    Errors(int line,int errorNo){
        this.line=line;
        this.errorNo=errorNo;
    }

    public int getLine() {
        return line;
    }

    public int getErrorNo() {
        return errorNo;
    }
}
