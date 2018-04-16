package pl0;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.StringReader;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Pl0 {
    public static BufferedReader finBufferedReader;
    public static BufferedWriter fa1BufferedWriter;
    public static StringReader lineReader;
    public static int whereLine=0;
    //NewMain中专用的。
    public static Queue<Character> everyLine;
    public static Map<String,Symbol> symbolMap;
    public static Set<Character> doubleFirst;
    //public static Set<Character> doubleSecond;
    public static StringBuilder symbolBuffer;

    public static Bool bool;
    public static final int NORW=13;
    public static final int TXMAX=100;
    public static final int NMAN=14;
    public static final int AL=10;
    public static final int AMAX=2047;
    public static final int LEVMAX=3;
    public static final int CXMAX=200;
    public static final int SYMNUM=32;
    public static final int FCTNUM=8;
    public static File fas;
    public static File fa;
    public static File fa1;
    public static File fa2;
    public static boolean listswitch;
    public static char ch;
    public static Symbol sym;
    public static String id;
    public static int num;
    public static int cc,ll;
    public static int cx;
    public static char[] line=new char[81];
    public static String a;
    public static Instruction[] code=new Instruction[CXMAX];
    public static String[] word=new String[NORW];
    public static Symbol[] wsym=new Symbol[NORW];
    public static Symbol[] ssym=new Symbol[256];
    public static String[] mnemonic=new String[FCTNUM];
    public static boolean[] declbegsys=new boolean[SYMNUM];
    public static boolean[] statbegsys=new boolean[SYMNUM];
    public static boolean[] facbegsys=new boolean[SYMNUM];
    public static TableStruct[] table=new TableStruct[TXMAX];
    public static File fin;
    public static File fout;
    public static char[] fname=new char[AL];
    public static int err;
}
