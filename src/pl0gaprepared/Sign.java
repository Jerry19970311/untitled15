package pl0gaprepared;

import pl0.Symbol;

import java.util.*;

//非终结符：pl0文法里面默认将上一次实验的Symbol枚举作为终结符。
//遇到的问题：
//①在求FIRST集合的时候如何考虑ε的情况？（已解决）
public class Sign implements Vital{
    private String name;
    private Queue<Sign> firstPrepared;
    private Set<Symbol> firstSymbol;
    private Queue<Sign> followPrepared;
    private Set<Symbol> followSymbol;
    private Map<Symbol,Reflect> selectReflect;
    public Sign(String name){
        this.name=name;
        this.firstPrepared=new LinkedList<Sign>();
        this.firstSymbol=new HashSet<Symbol>();
        this.followPrepared=new LinkedList<Sign>();
        this.followSymbol=new HashSet<Symbol>();
        this.selectReflect=new Hashtable<Symbol, Reflect>();
    }
    public void addFirstVN(Sign sign) throws NotLL1Exception {
        if(!firstPrepared.contains(sign)) {
            firstPrepared.add(sign);
        }
    }
    public void addFirstVT(Symbol symbol){
        firstSymbol.add(symbol);
    }
    public Set<Symbol> getFirstSymbol() {
        return firstSymbol;
    }

    public Queue<Sign> getFirstPrepared() {
        return firstPrepared;
    }

    // （本方法不考虑在与其它非终结符的FIRST取并集的时候可能带进来ε的情况。）
    public void addOtherFirst() throws NotLL1Exception {
        Set<Sign> signs=new HashSet<Sign>();
        while(!firstPrepared.isEmpty()){
            System.out.println(getName()+":"+this.firstPrepared);
            Sign temp=firstPrepared.poll();
            if(signs.add(temp)){
                Set<Symbol> symbols=new HashSet<Symbol>(temp.firstSymbol);
                //去除ε，无论什么情况ε都不应该在本阶段加入FIRST集。
                if(symbols.contains(Symbol.EMPTY)){
                    symbols.remove(Symbol.EMPTY);
                }
                this.firstSymbol.addAll(symbols);
                this.firstPrepared.addAll(temp.firstPrepared);
            }else{
                throw new NotLL1Exception("文法存在间接左递归");
            }
        }
    }
    public boolean haveEmpty(){
        return this.firstSymbol.contains(Symbol.EMPTY);
    }
    public boolean haveVN(){
        return !this.firstPrepared.isEmpty();
    }

    public String getName() {
        return name;
    }

    public void addFollowVT(Symbol symbol){
        followSymbol.add(symbol);
    }
    public void addFollowVTByFirst(Sign sign){
        Set<Symbol> temp=new HashSet<Symbol>(sign.firstSymbol);
        temp.remove(Symbol.EMPTY);
        followSymbol.addAll(temp);
    }
    public void addFollowVN(Sign sign){
        //增加sign!=this的目的是为了防止第二次分析的时候出现死循环。
        if((sign!=this)&&(!followPrepared.contains(sign))){
            followPrepared.add(sign);
        }
    }
    public void addOtherFollow(){
        while (!followPrepared.isEmpty()){
            //System.out.println("????????????????????????????????????????????????????????????????????????");
            Sign sign=followPrepared.poll();
            followPrepared.addAll(sign.followPrepared);
            followSymbol.addAll(sign.followSymbol);
        }
    }

    public Set<Symbol> getFollowSymbol() {
        return followSymbol;
    }

    public Queue<Sign> getFollowPrepared() {
        return followPrepared;
    }
    public void putSelect(Symbol symbol,Reflect reflect){
        this.selectReflect.put(symbol,reflect);
        //System.out.println("→预测数量为:"+selectReflect.size());
    }
    public Reflect getPreReflect(Symbol symbol){
        return this.selectReflect.get(symbol);
    }
    public String getPreTablePrint(){
        String result="";
        Iterator<Symbol> iterator=selectReflect.keySet().iterator();
        while (iterator.hasNext()){
            Symbol symbol=iterator.next();
            result+="f("+name+","+symbol+")="+selectReflect.get(symbol)+"\n";
        }
        return result;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean isVN() {
        return true;
    }
}
