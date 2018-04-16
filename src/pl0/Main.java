package pl0;

import test0402.Test1;

import java.io.IOException;

public class Main {
    public static void init(){
        for(int i=0;i<=255;i++){
            Pl0.ssym[i]=Symbol.NUL;
        }
        Pl0.ssym['+']=Symbol.PLUS;
        Pl0.ssym['-']=Symbol.MINUS;
        Pl0.ssym['*']=Symbol.TIMES;
        Pl0.ssym['/']=Symbol.DIVIDE;
        Pl0.ssym['(']=Symbol.LPAREN;
        Pl0.ssym[')']=Symbol.RPAREN;
        Pl0.ssym['=']=Symbol.EQL;
        Pl0.ssym[',']=Symbol.COMMA;
        Pl0.ssym['.']=Symbol.PERIOD;
        Pl0.ssym['#']=Symbol.POUND;
        Pl0.ssym[';']=Symbol.SEMICOLON;
        //这里的部署必须按照字母顺序。
        Pl0.word[0]="begin";
        Pl0.word[1]="call";
        Pl0.word[2]="const";
        Pl0.word[3]="do";
        Pl0.word[4]="end";
        Pl0.word[5]="if";
        Pl0.word[6]="odd";
        Pl0.word[7]="procedure";
        Pl0.word[8]="read";
        Pl0.word[9]="then";
        Pl0.word[10]="var";
        Pl0.word[11]="while";
        Pl0.word[12]="write";
        Pl0.wsym[0]=Symbol.BEGINSYM;
        Pl0.wsym[1]=Symbol.CALLSYM;
        Pl0.wsym[2]=Symbol.CONSTSYM;
        Pl0.wsym[3]=Symbol.DOSYM;
        Pl0.wsym[4]=Symbol.ENDSYM;
        Pl0.wsym[5]=Symbol.IFSYM;
        Pl0.wsym[6]=Symbol.ODDSYM;
        Pl0.wsym[7]=Symbol.PROCSYM;
        Pl0.wsym[8]=Symbol.READSYM;
        Pl0.wsym[9]=Symbol.THENSYM;
        Pl0.wsym[10]=Symbol.VARSYM;
        Pl0.wsym[11]=Symbol.WHILESYM;
        Pl0.wsym[12]=Symbol.WRITESYM;
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
    public static void error(int n){
    }

    //读取line字符串中的下一个词，置于ch中。
    public static int getch() throws IOException {
        //cc记录要读取的字符位置，ll为字符串长度，如果cc=ll，就说明当前行已读完，需要从文件中读入新行取代原来的line数组。
        //（因此如果是第一次使用该方法，为了读取首行，须在初始化的时候将cc和ll均置为0）
        if (Pl0.cc == Pl0.ll) {
            //利用BufferReader类每次读入一行。
            String buffer;
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
                //System.out.println("Program incomplete");
                ErrorLog.addError(Pl0.whereLine, 1);
                return -1;
            }
            //cc和ll全部重置。
            Pl0.ll = Pl0.cc = 0;
            //System.out.print(Pl0.cx+" ");
            Pl0.fa1BufferedWriter.write(Pl0.cx + " ");
            Pl0.fa1BufferedWriter.flush();
            /*Pl0.lineReader=new StringReader(buffer);
            if(buffer.length()<81){
                Pl0.lineReader.read(Pl0.line,0,buffer.length());
            }else{
                Pl0.lineReader.read(Pl0.line,0,Pl0.line.length);
            }*/
            //System.out.println("【"+buffer+"】"+Pl0.sym);
            //将包装着每一行的字符串对象转化为字符数组。
            //注：BufferReader在每次按行读入的时候，会自动把每行末尾的\n去掉，
            //因此如果没有其它的字符作为分割，词法分析getsym方法在调用本方法的时候
            //会将上一行的最后一个单词与下一行的第一个单词连在一起，造成不必要的麻烦
            //因此我们应该在BufferReader每次所读到的字符串末尾加上\n
            //这样读到的字符串在getsym方法中会被Pl0.ch==' '||Pl0.ch==10||Pl0.ch==9这一判定条件自动把两个字符串区分开。
            //（注：C语言没有出问题的原因是文件所有的内容都是一个字符串，原文件中行的分割用\n表示）
            Pl0.line = (buffer + "\n").toCharArray();
            Pl0.ch = ' ';
            //ll置为字符数组的长度。
            Pl0.ll = Pl0.line.length;
            //记录行的位置往下移一位。
            Pl0.whereLine++;
        }
        //将当前所指的字符记入ch中
        Pl0.ch = Pl0.line[Pl0.cc];
        //将标记位置后移一位
        Pl0.cc++;
        return 0;
    }
    //每运行一次从line中获取一个符号。
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
            //将id在word中进行二分查找，如果没找着就说明是名字。
            //二分查找法：
            //如果找到了，那么在本次循环结束以后，i-j=2（k在i,j中间），从而退出循环；
            //如果最终仍未找到，那么最后一次循环结束以后，i-j=1，从而退出循环。
            do{
                k=(i+j)/2;
                if(Pl0.id.compareTo(Pl0.word[k])<=0){
                    j=k-1;
                }
                if(Pl0.id.compareTo(Pl0.word[k])>=0){
                    i=k+1;
                }
            }while (i<=j);
            //System.out.println("-----------------\n"+Pl0.id+"\n------------------");
            if(i-1>j){
                Pl0.sym=Pl0.wsym[k];
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
            }
        }else if(Pl0.ch==':'){
            if(getch()==-1){
                return -1;
            }
            if(Pl0.ch=='='){
                Pl0.sym=Symbol.BECOMWS;
                if(getch()==-1){
                    return -1;
                }
            }else{
                Pl0.sym=Symbol.NUL;
            }
        }else if(Pl0.ch=='<'){
            if(getch()==-1){
                return -1;
            }
            if(Pl0.ch=='='){
                Pl0.sym=Symbol.LEQ;
                if(getch()==-1){
                    return -1;
                }
            }else {
                Pl0.sym=Symbol.LES;
            }
        }else if(Pl0.ch=='>'){
            if(getch()==-1){
                return -1;
            }
            if(Pl0.ch=='='){
                Pl0.sym=Symbol.MOQ;
                if(getch()==-1){
                    return -1;
                }
            }else{
                Pl0.sym=Symbol.MOR;
            }
        }else {
            Pl0.sym=Pl0.ssym[Pl0.ch];
            if(Pl0.sym!=Symbol.PERIOD){
                if(getch()==-1){
                    return -1;
                }
            }
        }
        if(Pl0.sym==Symbol.NUL){
            ErrorLog.addError(Pl0.whereLine,2);
            return -1;
        }
        return 0;
    }
}
