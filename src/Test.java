import pl0.NewMain;
import pl0.Symbol;
import pl0gaprepared.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Test {
    public static void main(String[] args) throws IOException, NotLL1Exception {
        PreparedMain.init();
        /*Set<Integer> integer1=new HashSet<Integer>();
        Set<Integer> integer2=new HashSet<Integer>();
        integer1.add(1);
        integer1.add(2);
        integer1.add(3);
        integer2.add(4);
        integer2.add(5);
        integer2.add(6);
        System.out.println(integer1);
        System.out.println(integer2);
        integer2=new HashSet<>(integer1);
        integer2.remove(2);
        System.out.println("---------------------------------------");
        System.out.println(integer1);
        System.out.println(integer2);
        System.out.println("---------------------------------------");*/
        //System.out.println(Symbol.ε.getName());
        /*String[] strings="1234:=5678900→0000".split("3|67|90|:=|→");
        System.out.println(Arrays.toString(strings));*/
        PreparedMain.buildReflects();
        PreparedMain.buildFirst();
        PreparedMain.buildFollow();
        PreparedMain.buildSelect();
        PreparedMain.buildPreTable();
        //System.out.println(PreparedMain.vnReflect);
        /*for(Iterator<Reflect> signIterator=PreparedMain.needTwiceDealReflect.iterator();signIterator.hasNext();){
            System.out.println(signIterator.next());
        }*/
        //System.out.println("-------------------------------------------------------------------------");
        //System.out.println("肯定没有空符:\n"+PreparedMain.notHaveEmptySets);
        /*for(Iterator<Sign> iterator=PreparedMain.notHaveEmptySets.iterator();iterator.hasNext();){
            Sign sign=iterator.next();
            System.out.println(sign.getName()+":"+sign.getFirstSymbol()+":"+sign.getFirstPrepared());
        }*/
        //System.out.println("\n肯定有空符:\n"+PreparedMain.haveEmptySets);
        /*for(Iterator<Sign> iterator=PreparedMain.haveEmptySets.iterator();iterator.hasNext();){
            Sign sign=iterator.next();
            System.out.println(sign.getName()+":"+sign.getFirstSymbol()+":"+sign.getFirstPrepared());
        }*/
        /*System.out.println("\n待确定:");
        for(Iterator<Sign> iterator=PreparedMain.maybeHaveEmptySets.iterator();iterator.hasNext();){
            Sign sign=iterator.next();
            System.out.println(sign.getName()+":"+sign.getFirstSymbol()+":"+sign.getFirstPrepared());
        }*/
        System.out.println("-------------------------------------------------------------------------");
        //System.out.println(PreparedMain.vnReflect);
        for(Iterator<Sign> iterator=PreparedMain.vnReflect.values().iterator();iterator.hasNext();){
            System.out.println(iterator.next().getPreTablePrint());
        }
        /*for(Iterator<Sign> iterator=PreparedMain.vnReflect.values().iterator();iterator.hasNext();){
            Sign sign=iterator.next();
            System.out.println(sign.getName()+":"+sign.getFollowSymbol()+":"+sign.getFollowPrepared());
        }*/
        /*System.out.println("-------------------------------------------------------------------------");
        for(int i=0;i<PreparedMain.reflects.size();i++){
            System.out.println(PreparedMain.reflects.get(i));
        }
        System.out.println("-------------------------------------------------------------------------");*/
        //System.out.println(PreparedMain.checkLL1());
    }
}
