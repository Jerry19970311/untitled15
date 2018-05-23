package pl0gaprepared;

import pl0.Symbol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Ref;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

//遇到的问题：
//非终结符（Sign类）和终结符（Symbol枚举）是两个类，那么在栈中如何使之共存？（已解决）
//创建统一接口Vital。
public class PreparedMain {
    public static Map<String,Sign> vnReflect;
    public static Map<String,Symbol> vtReflect;
    public static List<Reflect> reflects;
    //以下4个数据结构均为求FIRST所需的分析集合，作用如下：
    //①maybeHaveEmptySets:不确定是否能推导出空符号串的非终结符集合，
    //该集合初始化为vnReflect中的所有value
    //(见buildReflects方法)
    //在haveEmptySets和notHaveEmptySets的初步分析结果出来以后，会删除所有与这两个集合分别的交集
    //(见buildFirstOnce方法)
    //②haveEmptySets:确定了能够推导出空符号串的集合，因为将频繁用于查找，而不会删除，所以定为Set。
    //该集合初始化的元素为:在初次扫描的时候存在能够推导出空符号串的非终结符。
    //③notHaveEmptySets:确定了不能推导出空符号串的集合，因为将频繁用于查找，而不会删除，所以定为Set。
    //该集合初始化的元素为:在初次扫描之后没有依赖的非终结符，FIRST集中又不存在空符号串。
    //④needTwiceDealReflect:需要被二次分析的推导式集合。
    //该集合初始化的元素为:所有右部以非终结符开始的符号。
    //（注意：是否需要被二次分析取决于右部是否以终结符开始，而不是取决于左部的非终结符能否被推出空符号串）
    //（即needTwiceDealReflect推导式的左部非终结符，也可能在haveEmptySets中）
    public static Set<Sign> maybeHaveEmptySets;
    public static Set<Sign> haveEmptySets;
    public static Set<Sign> notHaveEmptySets;
    public static Queue<Reflect> needTwiceDealReflect;
    public static void init(){
        vnReflect=new Hashtable<String, Sign>();
        vtReflect=new Hashtable<String, Symbol>();
        //maybeHaveEmptySets=new LinkedList<Sign>();
        haveEmptySets=new HashSet<Sign>();
        notHaveEmptySets=new HashSet<Sign>();
        reflects=new ArrayList<Reflect>();
        //needTwiceDealReflect=new LinkedList<Reflect>();
        needTwiceDealReflect=new LinkedBlockingQueue<Reflect>();
        vtReflect.put("<id>",Symbol.IDENT);
        vtReflect.put("<integer>",Symbol.NUMBER);
        vtReflect.put(".",Symbol.PERIOD);
        vtReflect.put("const",Symbol.CONSTSYM);
        vtReflect.put(";",Symbol.SEMICOLON);
        vtReflect.put("=",Symbol.EQL);
        vtReflect.put("ε",Symbol.EMPTY);
        vtReflect.put(",",Symbol.COMMA);
        vtReflect.put("procedure",Symbol.PROCSYM);
        vtReflect.put(":=",Symbol.BECOMWS);
        vtReflect.put("if",Symbol.IFSYM);
        vtReflect.put("then",Symbol.THENSYM);
        vtReflect.put("while",Symbol.WHILESYM);
        vtReflect.put("do",Symbol.DOSYM);
        vtReflect.put("call",Symbol.CALLSYM);
        vtReflect.put("read",Symbol.READSYM);
        vtReflect.put("(",Symbol.LPAREN);
        vtReflect.put(")",Symbol.RPAREN);
        vtReflect.put("write",Symbol.WRITESYM);
        vtReflect.put("begin",Symbol.BEGINSYM);
        vtReflect.put("end",Symbol.ENDSYM);
        vtReflect.put("odd",Symbol.ODDSYM);
        vtReflect.put("#",Symbol.POUND);
        vtReflect.put("<",Symbol.LES);
        vtReflect.put(">",Symbol.MOR);
        vtReflect.put("<=",Symbol.LEQ);
        vtReflect.put(">=",Symbol.MOQ);
        vtReflect.put("+",Symbol.PLUS);
        vtReflect.put("-",Symbol.MINUS);
        vtReflect.put("*",Symbol.TIMES);
        vtReflect.put("/",Symbol.DIVIDE);
        vtReflect.put("var",Symbol.VARSYM);
        vtReflect.put("$",Symbol.DOLLAR);
    }
    public static void buildReflects() throws IOException, NotLL1Exception {
        FileReader fileReader=new FileReader(new File("src/pl0grammar"));
        BufferedReader bufferedReader=new BufferedReader(fileReader);
        String s;
        String[] strings;
        while ((s=bufferedReader.readLine())!=null){
            strings=s.split("::=");
            if(strings[1].startsWith(strings[0])){
                throw new NotLL1Exception("没有提取左公共因子");
            }
            if(!PreparedMain.vnReflect.containsKey(strings[0])) {
                PreparedMain.vnReflect.put(strings[0], new Sign(strings[0]));
            }
            Sign sign=PreparedMain.vnReflect.get(strings[0]);
            Reflect reflect=new Reflect(sign);
            String s1=strings[1];
            if("<".equals(s1)){
                //System.out.println(sign.getName()+":"+PreparedMain.vtReflect.get(s1));
                reflect.addv(PreparedMain.vtReflect.get(s1));
                PreparedMain.reflects.add(reflect);
                continue;
            }
            if(">".equals(s1)){
                //System.out.println(sign.getName()+":"+PreparedMain.vtReflect.get(s1));
                reflect.addv(PreparedMain.vtReflect.get(s1));
                PreparedMain.reflects.add(reflect);
                continue;
            }
            if("<=".equals(s1)){
                //System.out.println(sign.getName()+":"+PreparedMain.vtReflect.get(s1));
                reflect.addv(PreparedMain.vtReflect.get(s1));
                PreparedMain.reflects.add(reflect);
                continue;
            }
            if(">=".equals(s1)){
                //System.out.println(sign.getName()+":"+PreparedMain.vtReflect.get(s1));
                reflect.addv(PreparedMain.vtReflect.get(s1));
                PreparedMain.reflects.add(reflect);
                continue;
            }
            while(!s1.isEmpty()){
                int index=s1.indexOf("<");
                String temp;
                if(index>0){
                    temp=s1.substring(0,index);
                    s1=s1.substring(index);
                }else if(index==0){
                    int endIndex=s1.indexOf(">")+1;
                    temp=s1.substring(0,endIndex);
                    s1=s1.substring(endIndex);
                }else{
                    temp=s1;
                    s1=s1.substring(s1.length());
                }
                String[] ts=temp.split("'");
                for(int i=0;i<ts.length;i++) {
                    if((null==ts[i])||(ts[i].isEmpty())){
                        continue;
                    }
                    if (PreparedMain.vtReflect.containsKey(ts[i])) {
                        //System.out.println(PreparedMain.vtReflect.get(ts[i]));
                        reflect.addv(PreparedMain.vtReflect.get(ts[i]));
                    } else {
                        if(!PreparedMain.vnReflect.containsKey(ts[i])){
                            PreparedMain.vnReflect.put(ts[i],new Sign(ts[i]));
                        }
                        reflect.addv(PreparedMain.vnReflect.get(ts[i]));
                        //System.out.println(ts[i]);
                    }
                }
            }
            PreparedMain.reflects.add(reflect);
        }
        maybeHaveEmptySets=new HashSet<Sign>(vnReflect.values());
        //注：千万不要图省事使用如下语句！！！
        //maybeHaveEmptySets=vnReflect.values();
        //真**坑，Hashtable类的values方法只是提供了其Values集的地址，而不是一个新的Values集，如果使用如下方法获得Collection并频繁使用增删操作，将会影响Hashtable对象本身！！！
    }
    public static void buildFirst() throws NotLL1Exception {
        buildFirstOnce();
        buildFirstTwice();
        buildFirst3Times();
    }
    //第一次分析：遍历所有的推导式（遍历1次），对每个推导式的右端只关心第一个符号，
    //无论第一个符号串是否为非终结符，都将记入sign，非终结符记入prepare集合，终结符记入vt集合。
    //区别处理：
    //①如果第一个符号是非终结符，那么其左部的非终结符存在被推出空符号串的可能，但无法确定。
    //措施：将该推导式加入needTwiceDealReflect队列中，等到二次分析的时候重点处理。
    //②如果第一个符号是终结符，那么该推导式必定无法被推出空符号串，如果这一非终结符的所有推导式都是如此，那么就可以判定它推不出空符号串。
    //措施：在所有的遍历结束后检查每个sign，如果这个sign没有依赖的非终结符，其FIRST终结符集里面也没有EMPTY，则可以断定它推不出空符号串，将其加入notHaveEmptySets里。
    //如果有EMPTY，就加入haveEmptySets里。
    private static void buildFirstOnce() throws NotLL1Exception{
        for(int i=0;i<reflects.size();i++){
            Reflect reflect=reflects.get(i);
            Sign sign=reflect.getSign();
            List<Vital> vitals=reflect.getVs();
            Vital vital=vitals.get(0);
            if(vital.getName().equals(sign.getName())){
                throw new NotLL1Exception("出现了直接左递归:"+sign.getName());
            }
            if(vital.isVN()){
                sign.addFirstVN((Sign)vital);
                needTwiceDealReflect.add(reflect);
            }else{
                sign.addFirstVT((Symbol)vital);
            }

        }
        for(Iterator<Sign> iterator=vnReflect.values().iterator();iterator.hasNext();) {
            Sign sign=iterator.next();
            if (sign.haveEmpty()) {
                //System.out.println("yes:" + sign);
                haveEmptySets.add(sign);
            }
            if ((sign.haveEmpty() == false) && (sign.haveVN() == false)) {
                //System.out.println("no:" + sign);
                notHaveEmptySets.add(sign);
            }
        }
        maybeHaveEmptySets.removeAll(haveEmptySets);
        maybeHaveEmptySets.removeAll(notHaveEmptySets);
    }
    //第二次分析：进一步处理那些存在结果以非终结符开始的单步推导。
    //结果：所有的非终结符将确定是否有空，并且收集到完全确定的终结符以及有关的非终结符。
    //问题：当确定了是否能被推导为空符号串以后，是否需要继续进行分析？
    //答案：需要，因为本方法的目的除了分析是否能推出空符号串，还有搜寻所有的非终结符sign所依赖的非终结符。
    //确定能推导出空符号串，只能说明分析了某一个推导式，还有其它的推导式没有分析。
    private static void buildFirstTwice() throws NotLL1Exception {
        //本方法是基于所有右端以非终结符开始的推导式分析的（needTwiceDealReflect队列）
        //算法思路：取出队头的推导式，对其右部进行迭代分析，期间碰到两种情况则会中止迭代：
        //①遇到了notHaveEmptySets中的非终结符，该Set里面的所有非终结符都是确定无法推出空符号串的，因此没有必要再分析之后的符号。由于这一情况对该推导式是一个确定情况，因此无需再次入队。
        //②遇到了maybeHaveEmptySets中的非终结符，该Collection里面的非终结符尚未确认是否能推出空符号串，因此无法判定是否要继续分析后面的符号。由于这一情况尚不能对该推导式有确定结论，因此需要再次入队。
        while (!needTwiceDealReflect.isEmpty()){
            Reflect reflect=needTwiceDealReflect.poll();
            Sign sign=reflect.getSign();
            //System.out.println("sign:"+sign);
            for(Iterator<Vital> vitalIterator=reflect.getVs().iterator();vitalIterator.hasNext();){
                Vital vital=vitalIterator.next();
                //能被读到的符号都是属于FIRST集里面的，所以无论是否为非终结符都要加入。
                if(vital.isVN()){
                    sign.addFirstVN((Sign)vital);
                    if(notHaveEmptySets.contains((Sign)vital)){
                        break;
                    }
                    if(maybeHaveEmptySets.contains((Sign)vital)){
                        needTwiceDealReflect.add(reflect);
                        break;
                    }
                    if((vitalIterator.hasNext()==false)||haveEmptySets.contains((Sign)vital)){
                        maybeHaveEmptySets.remove(sign);
                        haveEmptySets.add(sign);
                        sign.addFirstVT(Symbol.EMPTY);
                    }
                }else{
                    sign.addFirstVT((Symbol)vital);
                    break;
                }
            }
            if(searchReflectsFromTwice(sign).isEmpty()&&maybeHaveEmptySets.contains(sign)){
                maybeHaveEmptySets.remove(sign);
                notHaveEmptySets.add(sign);
            }
        }
    }
    private static List<Reflect> searchReflectsFromTwice(Sign sign){
        List<Reflect> result=new ArrayList<Reflect>();
        Iterator<Reflect> iterator=needTwiceDealReflect.iterator();
        while (iterator.hasNext()){
            Reflect reflect=iterator.next();
            if(reflect.getSign()==sign){
                result.add(reflect);
            }
        }
        return result;
    }
    //第三次分析：直接调用Sign中的addOtherFirst方法，该方法可将其依赖的非终结符的所有FIRST终结符也加入到该sign中。
    private static void buildFirst3Times() throws NotLL1Exception {
        Collection<Sign> vns=vnReflect.values();
        Iterator<Sign> signIterator=vns.iterator();
        while (signIterator.hasNext()){
            signIterator.next().addOtherFirst();
        }
    }
    public static void buildFollow(){
        buildFollowOnce();
        buildFollowTwice();
    }
    private static void buildFollowOnce(){
        vnReflect.get("<程序>").addFollowVT(Symbol.DOLLAR);
        for(int i=0;i<reflects.size();i++){
            //System.out.println("1111111111111111111111111111111111111111111");
            LinkedList<Sign> signQueue=new LinkedList<Sign>();
            Reflect reflect=reflects.get(i);
            //System.out.println("-------------------------------------------------------------------");
            //System.out.println("当前被分析的Reflect是:"+reflect.getSign()+"→"+reflect.getVs());
            List<Vital> vitals=reflect.getVs();
            Iterator<Vital> iterator=vitals.iterator();
            while (iterator.hasNext()){
                //System.out.println("22222222222222222222222222222222222222222222222222222");
                Vital vital=iterator.next();
                //System.out.println("当前读到的Vital是:"+vital);
                //System.out.println("此时,队列有:"+signQueue);
                if(vital.isVN()){
                    for(Iterator<Sign> signIterator=signQueue.iterator();signIterator.hasNext();){
                        signIterator.next().addFollowVTByFirst((Sign)vital);
                    }
                    if((!signQueue.isEmpty())&&(!haveEmptySets.contains((Sign)vital))){
                        //System.out.println("(清空)");
                        signQueue.clear();
                    }
                    signQueue.add((Sign)vital);
                    if(!iterator.hasNext()){
                        while (!signQueue.isEmpty()){
                            //System.out.println("333333333333333333333333333333333333333333333333333333333333");
                            signQueue.poll().addFollowVN(reflect.getSign());
                        }
                    }
                }else{
                    while (!signQueue.isEmpty()){
                        //System.out.println("4444444444444444444444444444444444444444444444444444444444");
                        signQueue.poll().addFollowVT((Symbol)vital);
                    }
                }
            }
        }
    }
    private static void buildFollowTwice(){
        Collection<Sign> signs=vnReflect.values();
        Iterator<Sign> signIterator=signs.iterator();
        while (signIterator.hasNext()){
            signIterator.next().addOtherFollow();
        }
    }
    public static void buildSelect(){
        for(int i=0;i<reflects.size();i++){
            Reflect reflect=reflects.get(i);
            List<Vital> list=reflect.getVs();
            Iterator<Vital> vitalIterator=list.iterator();
            while (vitalIterator.hasNext()){
                Vital vital=vitalIterator.next();
                if(vital.isVN()){
                    reflect.addSelectSymbolByFirst((Sign)vital);
                    if(notHaveEmptySets.contains((Sign)vital)){
                        break;
                    }
                    if(!vitalIterator.hasNext()){
                        reflect.addSelectSymbol(Symbol.EMPTY);
                    }
                }else{
                    reflect.addSelectSymbol((Symbol)vital);
                    break;
                }
            }
            if(reflect.cleanEmpty()){
                reflect.addSelectSymbolByFollow(reflect.getSign());
            }
        }
    }
    public static boolean checkLL1(){
        Set<Symbol> set=new HashSet<Symbol>();
        Collection<Sign> signs=vnReflect.values();
        Iterator<Sign> iterator=signs.iterator();
        while (iterator.hasNext()){
            Set<Symbol> symbols=new HashSet<Symbol>();
            Sign sign=iterator.next();
            List<Reflect> reflects=searchReflects(sign);
            for(int i=0;i<reflects.size();i++){
                if(symbols.removeAll(reflects.get(i).getSelectResult())){
                    System.out.println("当前查的是:"+sign);
                    return false;
                }
                symbols.addAll(reflects.get(i).getSelectResult());
            }
        }
        return true;
    }
    public static void buildPreTable(){
        for(int i=0;i<reflects.size();i++){
            //System.out.println("当前的Reflect:"+reflects.get(i).toString());
            //System.out.println("SELECT结果为:\n"+reflects.get(i).getSelectResult());
            Iterator<Symbol> iterator=reflects.get(i).getSelectResult().iterator();
            while (iterator.hasNext()){
                Symbol symbol=iterator.next();
                //System.out.println("当前的Symbol:"+symbol);
                reflects.get(i).getSign().putSelect(symbol,reflects.get(i));
            }
        }
        System.out.println("--------------------------------------------------\n预测表如下:");
        for(Iterator<Sign> siterator=PreparedMain.vnReflect.values().iterator();siterator.hasNext();){
            System.out.println(siterator.next().getPreTablePrint());
        }
        System.out.println("--------------------------------------------------\n");
    }
    /*public static void buildFirstTwice() throws NotLL1Exception {
        while (!maybeHaveEmptySets.isEmpty()) {
            for (Iterator<Sign> iterator = maybeHaveEmptySets.iterator(); iterator.hasNext(); ) {
                System.out.println("--------------------------------------------------------------------");
                Sign sign = iterator.next();
                System.out.println(sign);
                //System.out.println("----------------------------------------\n"+sign);
                List<Reflect> signReflect = searchReflects(sign);
                System.out.println(signReflect);
                //除去右端以终结符开始的映射。
                signReflect.retainAll(needTwiceDealReflect);
                System.out.println(signReflect);
                for (int i = 0; i < signReflect.size(); i++) {
                    //boolean isOK = true;
                    Reflect reflect = signReflect.get(i);
                    System.out.println("reflect:"+reflect);
                    List<Vital> vitals = reflect.getVs();
                    System.out.println("vitals:"+vitals);
                    //对一句映射里面的右部所有符号进行分析。
                    for (Iterator<Vital> vitalIterator = vitals.iterator(); vitalIterator.hasNext(); ){
                        //迭代读取符号。
                        Vital vital = vitalIterator.next();
                        //System.out.println("vital:"+vital.getName());
                        //判断是否为终结符，如果是，则可以直接退出对本句的分析并将该符加入sign的vt集。
                        //如果不是，则需要进一步加以分析。
                        if (vital.isVN()) {
                            //如果该非终结符确定了能够推出空符，则再进行判断，判断之后是否还有符号，
                            //如果有，则不加强制地使其自然结束本次循环，然后去分析下一次读到的符号，
                            //如果没有，那么就说明根据该式，本非终结符也能推出空符，将EMPTY加入到sign的vt集中。
                            //无论结论有还是没有，该式对是否能推出空符号串都将是定论，因此如果是两种情况之一，分析之后都要及时将该式删除。
                            if (haveEmptySets.contains((Sign) vital)) {
                                sign.addFirstVN((Sign) vital);
                                if (!vitalIterator.hasNext()) {
                                    sign.addFirstVT(Symbol.EMPTY);
                                    needTwiceDealReflect.remove(reflect);
                                }
                            }
                            if (notHaveEmptySets.contains((Sign) vital)) {
                                sign.addFirstVN((Sign) vital);
                                needTwiceDealReflect.remove(reflect);
                                break;
                            }
                            //如果该非终结符目前阶段不能确定是否能推出空符号串，那么自然也没有根据去分析要不要分析接下来的符号串
                            //那么就强制退出本次对该式的分析，该式将继续留在needTwiceDealReflect中，等待下次分析。
                            if (maybeHaveEmptySets.contains((Sign) vital)) {
                                break;
                            }
                        } else {
                            sign.addFirstVT((Symbol) vital);
                            //本句结论已出，已不需要再被分析，需要从需要二次分析的表中取出。
                            needTwiceDealReflect.remove(reflect);
                            break;
                        }
                    }
                    //如果在对该非终结符的某一个推导式的分析之后，该终结符能推导出EMPTY，那么便无需再进行分析。
                    //这时应该对3处数据结构进行修改：
                    //①maybeHaveEmptySets中，删除该非终结符；
                    //②haveEmptySets中，加入该非终结符；
                    //③needTwiceDealReflect中，删除所有该非终结符的推导式。

                    //更新：needTwiceDealReflect在这一步暂时不能清空相关推导式，
                    //因为其它尚未分析的推导式可能存在新的sign所依赖的非终结符。
                    if (sign.haveEmpty()) {
                        //maybeHaveEmptySets.remove(sign);
                        haveEmptySets.add(sign);
                        //needTwiceDealReflect.removeAll(signReflect);
                    }
                }
                //如果该非终结符的所有推导式都分析过，由于确定推不出空符号串的推导式会被删除，
                //这时候就对该非终结符在needTwiceDealReflect里面的映射进行一次检查，
                //如果该List里面已经没有了该非终结符的推导式，那么就说明它已经确定了推不出空符号串，
                //(如果还存在，就说明情况待定，需要下一次遍历的时候再分析)
                //这时应该对2处数据结构进行修改：
                //①maybeHaveEmptySets中，删除该非终结符；
                //②notHaveEmptySets中，加入该非终结符。
                if ((!sign.haveEmpty())&&searchReflects(sign).isEmpty()) {
                    //maybeHaveEmptySets.remove(sign);
                    notHaveEmptySets.add(sign);
                }
            }
            maybeHaveEmptySets.removeAll(notHaveEmptySets);
            maybeHaveEmptySets.removeAll(haveEmptySets);
            System.out.println("--------------------------------------------");
        }
        //对所有的符号是否能推出空符号串分析完毕不代表对所有符号分析完毕，还需要对剩下没有分析的推导式再分析。
        while(!needTwiceDealReflect.isEmpty()){
            Iterator<Reflect> iterator=needTwiceDealReflect.iterator();
            while (iterator.hasNext()){
                Reflect reflect=iterator.next();
                Sign sign=reflect.getSign();
                List<Vital> vitals=reflect.getVs();
                Iterator<Vital> vitalIterator=vitals.iterator();
                while (vitalIterator.hasNext()){
                    Vital vital=vitalIterator.next();
                    if(vital.isVN()){
                        sign.addFirstVN((Sign)vital);
                        if(notHaveEmptySets.contains((Sign)vital)){
                            break;
                        }
                    }else{
                        sign.addFirstVT((Symbol)vital);
                    }
                }
            }
        }
    }*/
    public static List<Reflect> searchReflects(Sign sign){
        List<Reflect> result=new ArrayList<Reflect>();
        for(int i=0;i<reflects.size();i++){
            if(reflects.get(i).getSign()==sign){
                result.add(reflects.get(i));
            }
        }
        return result;
    }
}
