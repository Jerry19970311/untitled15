package pl0gaprepared;

import pl0.Symbol;

import java.util.*;

public class Reflect {
    private Sign sign;
    //存放本映射的右端所有符号（按顺序）。
    //因为在语法分析处理的时候要将其中的元素放到栈中，故选用双向链式表的数据结构。
    private LinkedList<Vital> vs;
    private Set<Symbol> selectResult;
    public Reflect(Sign sign){
        this.sign=sign;
        this.vs=new LinkedList<Vital>();
        this.selectResult=new HashSet<Symbol>();
    }
    public void addv(Vital v){
        vs.add(v);
    }
    //返回右端符号表vs的反向迭代器，用于将来主程序的入栈操作。
    public Iterator<Vital> getAntiIterator(){
        return this.vs.descendingIterator();
    }
    public List<Vital> getVs(){
        return this.vs;
    }

    public Sign getSign() {
        return sign;
    }
    public void addSelectSymbol(Symbol symbol){
        selectResult.add(symbol);
    }
    public void addSelectSymbolByFirst(Sign sign){
        Set<Symbol> set=new HashSet<Symbol>(sign.getFirstSymbol());
        set.remove(Symbol.EMPTY);
        selectResult.addAll(set);
    }
    public void addSelectSymbolByFollow(Sign sign){
        selectResult.addAll(sign.getFollowSymbol());
    }
    public boolean cleanEmpty(){
        return selectResult.remove(Symbol.EMPTY);
    }

    public Set<Symbol> getSelectResult() {
        return selectResult;
    }

    @Override
    public String toString() {
        /*String str="映射左端的非终结符为:"+sign.getName()+"\n映射右端的符号为:\n";
        for(Iterator<Vital> iterator=vs.iterator();iterator.hasNext();){
            Vital vital=iterator.next();
            str+=vital.getName()+"-"+vital.isVN()+" ";
        }
        str+="\n";*/
        String str=sign+"→"+vs.toString();
        return str;
    }
}
