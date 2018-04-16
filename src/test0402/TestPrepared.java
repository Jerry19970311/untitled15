package test0402;

import pl0.Pl0;
import pl0.Symbol;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class TestPrepared {
    public static Map<Symbol,Integer> sign;
    public static Map<Symbol,String> count;
    public static List<Token> tokens;
    public static Map<String,Integer> ramTokens;
    public static void init(){
        tokens=new ArrayList<Token>();
        ramTokens=new Hashtable<String, Integer>();
        initSign();
        initCount();
    }
    public static void initSign(){
        sign=new Hashtable<Symbol, Integer>();
        sign.put(Symbol.IDENT,1);
        sign.put(Symbol.NUMBER,2);
        sign.put(Symbol.CONSTSYM,3);
        sign.put(Symbol.VARSYM,3);
        sign.put(Symbol.PROCSYM,3);
        sign.put(Symbol.BEGINSYM,3);
        sign.put(Symbol.ENDSYM,3);
        sign.put(Symbol.ODDSYM,3);
        sign.put(Symbol.IFSYM,3);
        sign.put(Symbol.THENSYM,3);
        sign.put(Symbol.CALLSYM,3);
        sign.put(Symbol.WHILESYM,3);
        sign.put(Symbol.DOSYM,3);
        sign.put(Symbol.READSYM,3);
        sign.put(Symbol.WRITESYM,3);
        sign.put(Symbol.PLUS,4);
        sign.put(Symbol.MINUS,4);
        sign.put(Symbol.TIMES,4);
        sign.put(Symbol.DIVIDE,4);
        sign.put(Symbol.EQL,4);
        sign.put(Symbol.POUND,4);
        sign.put(Symbol.LES,4);
        sign.put(Symbol.LEQ,4);
        sign.put(Symbol.MOR,4);
        sign.put(Symbol.MOQ,4);
        sign.put(Symbol.BECOMWS,4);
        sign.put(Symbol.LPAREN,5);
        sign.put(Symbol.RPAREN,5);
        sign.put(Symbol.COMMA,5);
        sign.put(Symbol.SEMICOLON,5);
        sign.put(Symbol.PERIOD,5);
    }
    public static void initCount(){
        count=new Hashtable<Symbol, String>();
        count.put(Symbol.PLUS,"+");
        count.put(Symbol.MINUS,"-");
        count.put(Symbol.TIMES,"*");
        count.put(Symbol.DIVIDE,"/");
        count.put(Symbol.EQL,"=");
        count.put(Symbol.POUND,"#");
        count.put(Symbol.LES,"<");
        count.put(Symbol.LEQ,"<=");
        count.put(Symbol.MOR,">");
        count.put(Symbol.MOQ,">=");
        count.put(Symbol.BECOMWS,":=");
        count.put(Symbol.LPAREN,"(");
        count.put(Symbol.RPAREN,")");
        count.put(Symbol.COMMA,",");
        count.put(Symbol.SEMICOLON,";");
        count.put(Symbol.PERIOD,".");
    }
    public static void addToken(Symbol symbol){
        if(symbol==Symbol.IDENT) {
            Token temp;
            if (ramTokens.containsKey(Pl0.a)){
                temp = new Token(sign.get(symbol), String.valueOf(ramTokens.get(Pl0.a)));
            }else{
                temp= new Token(sign.get(symbol), String.valueOf(Pl0.cx));
                ramTokens.put(Pl0.a,Pl0.cx);
                Pl0.cx++;
            }
            tokens.add(temp);
        }else{
            if(count.containsKey(symbol)) {
                tokens.add(new Token(sign.get(symbol), count.get(symbol)));
            }else{
                tokens.add(new Token(sign.get(symbol), symbol.toString()));
            }
        }
    }
    public static String getTokensString(){
        String str="";
        for(int i=0;i<tokens.size();i++){
            str=str+tokens.get(i).toString()+"\n";
        }
        return str;
    }
}

