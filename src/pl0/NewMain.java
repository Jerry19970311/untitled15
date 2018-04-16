package pl0;

import test0402.Test1;

import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

//改造后的Main类，使用了更多的Java库
public class NewMain {
    public static void addStringToCharQueue(String temp){
        //Queue<Character> result=new LinkedBlockingQueue<Character>();
        char[] chars=temp.toCharArray();
        for(int i=0;i<chars.length;i++){
            Pl0.everyLine.add(Character.valueOf(chars[i]));
        }
        //return result;
    }
    public static void init(){
        Pl0.everyLine=new LinkedBlockingQueue<Character>();
        Pl0.symbolMap=new Hashtable<String, Symbol>();
        Pl0.doubleFirst=new HashSet<Character>();
        //Pl0.doubleSecond=new HashSet<Character>();
        Pl0.symbolBuffer=new StringBuilder();
        Pl0.doubleFirst.add(':');
        Pl0.doubleFirst.add('<');
        Pl0.doubleFirst.add('>');
        Pl0.symbolMap.put("+",Symbol.PLUS);
        Pl0.symbolMap.put("-",Symbol.MINUS);
        Pl0.symbolMap.put("*",Symbol.TIMES);
        Pl0.symbolMap.put("/",Symbol.DIVIDE);
        Pl0.symbolMap.put("(",Symbol.LPAREN);
        Pl0.symbolMap.put(")",Symbol.RPAREN);
        Pl0.symbolMap.put("=",Symbol.EQL);
        Pl0.symbolMap.put(",",Symbol.COMMA);
        Pl0.symbolMap.put(".",Symbol.PERIOD);
        Pl0.symbolMap.put("#",Symbol.POUND);
        Pl0.symbolMap.put(";",Symbol.SEMICOLON);
        Pl0.symbolMap.put(":=",Symbol.BECOMWS);
        Pl0.symbolMap.put("<",Symbol.LES);
        Pl0.symbolMap.put("<=",Symbol.LEQ);
        Pl0.symbolMap.put(">",Symbol.MOR);
        Pl0.symbolMap.put(">=",Symbol.MOQ);
        Pl0.symbolMap.put("begin",Symbol.BEGINSYM);
        Pl0.symbolMap.put("call",Symbol.CALLSYM);
        Pl0.symbolMap.put("const",Symbol.CONSTSYM);
        Pl0.symbolMap.put("do",Symbol.DOSYM);
        Pl0.symbolMap.put("end",Symbol.ENDSYM);
        Pl0.symbolMap.put("if",Symbol.IFSYM);
        Pl0.symbolMap.put("odd",Symbol.ODDSYM);
        Pl0.symbolMap.put("procedure",Symbol.PROCSYM);
        Pl0.symbolMap.put("read",Symbol.READSYM);
        Pl0.symbolMap.put("then",Symbol.THENSYM);
        Pl0.symbolMap.put("var",Symbol.VARSYM);
        Pl0.symbolMap.put("while",Symbol.WHILESYM);
        Pl0.symbolMap.put("write",Symbol.WRITESYM);
        Pl0.mnemonic[0]="lit";
        Pl0.mnemonic[1]="opr";
        Pl0.mnemonic[2]="lod";
        Pl0.mnemonic[3]="sto";
        Pl0.mnemonic[4]="cal";
        Pl0.mnemonic[5]="int";
        Pl0.mnemonic[6]="jmp";
        Pl0.mnemonic[7]="jpc";
        for(int i=0;i<Pl0.SYMNUM;i++){
            Pl0.declbegsys[i]=false;
            Pl0.statbegsys[i]=false;
            Pl0.facbegsys[i]=false;
        }
        //设置符号集……
    }
    public static int getch() throws IOException {
        //cc记录要读取的字符位置，ll为字符串长度，如果cc=ll，就说明当前行已读完，需要从文件中读入新行取代原来的line数组。
        //（因此如果是第一次使用该方法，为了读取首行，须在初始化的时候将cc和ll均置为0）
        if (Pl0.everyLine.isEmpty()) {
            //利用BufferReader类每次读入一行。
            String buffer;
            //词法分析程序中专设，如果isFile为真，则从文件中读取新行，否则从程序临时区中读取新行。
            if (Test1.isFile) {
                buffer = Pl0.finBufferedReader.readLine();
            }else {
                if(Test1.index>=Test1.stmts.length){
                    ErrorLog.addError(Pl0.whereLine, 1);
                    return -1;
                }
                buffer=Test1.stmts[Test1.index];
                System.out.println("--------------------------");
                System.out.println(buffer);
                Test1.index++;
            }
            //判定本次读入的是否为空行，是则输出异常信息返回异常值-1。
            if (buffer == null) {
                ErrorLog.addError(Pl0.whereLine, 1);
                return -1;
            }
            Pl0.fa1BufferedWriter.write(Pl0.cx + " ");
            Pl0.fa1BufferedWriter.flush();
            //将包装着每一行的字符串对象转化为字符数组。
            //注：BufferReader在每次按行读入的时候，会自动把每行末尾的\n去掉，
            //因此如果没有其它的字符作为分割，词法分析getsym方法在调用本方法的时候
            //会将上一行的最后一个单词与下一行的第一个单词连在一起，造成不必要的麻烦
            //因此我们应该在BufferReader每次所读到的字符串末尾加上\n
            //这样读到的字符串在getsym方法中会被Pl0.ch==' '||Pl0.ch==10||Pl0.ch==9这一判定条件自动把两个字符串区分开。
            //（注：C语言没有出问题的原因是文件所有的内容都是一个字符串，原文件中行的分割用\n表示）
            addStringToCharQueue(buffer + "\n");
            Pl0.ch = ' ';
            //记录行的位置往下移一位。
            Pl0.whereLine++;
        }
        //从everyLine队列里面取出一个字符放到ch变量中
        Pl0.ch = Pl0.everyLine.poll();
        return 0;
    }
    public static int getsym() throws IOException {
        //i,j作为保留字进行二分查找的辅助变量，i初始值为0，j初始值为1。
        int i=0,j=Pl0.NORW-1,k;
        //排除空格，换行，制表符这些纯书写格式用的情况。
        while (Pl0.ch==' '||Pl0.ch==10||Pl0.ch==9){
            if(getch()==-1){
                return -1;
            }
        }
        //判断将要读取的字符，如果是小写字母就继续往下读，直到遇到非小写字母或者非数字的字符为止，
        //将期间所有读到的字符依次记在临时区数组a中。
        if(Pl0.ch>='a'&&Pl0.ch<='z'){
            //创建一个字符串创建类，便于动态加入字符。
            StringBuilder temp=new StringBuilder();
            //k=0;
            //往里面加入一个字符以后即调用getch()方法将下一个字符装入ch。
            //（由于是先用后加，因此在执行时必须开始时先用一次getch方法，或者在初始化时将ch设定为空格，触发本方法中调用get方法的代码。否则ch为空值）
            do{
                //System.out.println("char:"+(int)Pl0.ch);
                temp.append(Pl0.ch);
                /*if(k<Pl0.line.length){
                    Pl0.a[k]=Pl0.ch;
                    k++;
                }*/
                if(getch()==-1){
                    return -1;
                }
            }while (Pl0.ch>='a'&&Pl0.ch<='z'||Pl0.ch>='0'&&Pl0.ch<'9');
            //Pl0.a[k]=0;
            //将装好的字符串生成存入临时变量a，然后将a中内容复制到标识符变量id中。
            Pl0.a=temp.toString();
            Pl0.id=new String(Pl0.a);
            if(Pl0.symbolMap.containsKey(Pl0.id)){
                Pl0.sym=Pl0.symbolMap.get(Pl0.id);
            }else{
                Pl0.sym=Symbol.IDENT;
            }
        }else if(Pl0.ch>='0'&&Pl0.ch<='9'){
            k=0;
            Pl0.num=0;
            Pl0.sym=Symbol.NUMBER;
            do{
                Pl0.num=10*Pl0.num+Pl0.ch-'0';
                k++;
                if(getch()==-1){
                    return -1;
                }
            }while (Pl0.ch>='0'&&Pl0.ch<='9');
            k--;
            if(k>Pl0.NMAN){
                //错误处理。
                ErrorLog.addError(Pl0.whereLine,3);
            }
            //以下语句到本级选择结束前均为判断运算符的代码。
            //分为两个情况：
            // ①（潜在的）双字符运算符情况的处理；
            // ②其它的普通单字符运算符和结束符的处理。
            //分为两个情况讨论的原因：
            //关于是否要进行下一步操作，情况有3：
            //1，普通的单字符的运算符（加减乘除等等），这类字符在识别以后，需要再获取下一位字符
            // ，而且这类字符在正常情况下后面必定还有其他字符，因而也需要这么做；
            //2，双字符运算符（大于等于、小于等于、赋值），这类字符在本次识别以后，还需要读取下一位字符
            // ，以确认这两次读取的字符拼起来是不是运算符，而且不管是与不是，它们正常情况下后面必定也还
            // 有其它字符，因此无论是进一位还是进两位，正常情况下识别完毕后都还要再进一位；
            //3，结束符，该字符识别以后就可以正常退出全部的词法分析过程，因此正常情况下它的后面是不应该
            // 有其它字符的，如果类似普通的单字符处理，极有可能会因为读不到下一个字符而报错。
        }else if(Pl0.doubleFirst.contains(Pl0.ch)){
            Pl0.symbolBuffer.append(Pl0.ch);
            if(getch()==-1){
                return -1;
            }
            if(Pl0.ch=='=') {
                Pl0.symbolBuffer.append(Pl0.ch);
                if(getch()==-1){
                    return -1;
                }
            }
            if(Pl0.symbolMap.containsKey(Pl0.symbolBuffer.toString())){
                Pl0.sym=Pl0.symbolMap.get(Pl0.symbolMap.get(Pl0.symbolBuffer.toString()));
            }else{
                Pl0.sym=Symbol.NUL;
            }
            Pl0.symbolBuffer=new StringBuilder();
        }else {
            Pl0.symbolBuffer.append(Pl0.ch);
            String temp=Pl0.symbolBuffer.toString();
            if(Pl0.symbolMap.containsKey(temp)){
                Pl0.sym=Pl0.symbolMap.get(temp);
            }else{
                Pl0.sym=Symbol.NUL;
            }
            if(Pl0.sym!=Symbol.PERIOD){
                if(getch()==-1){
                    return -1;
                }
            }
            Pl0.symbolBuffer=new StringBuilder();
        }
        if(Pl0.sym==Symbol.NUL){
            ErrorLog.addError(Pl0.whereLine,2);
            return -1;
        }
        return 0;
    }
}
