package pl0;

import pl0gaprepared.Vital;

public enum Symbol implements Vital{
    EMPTY,DOLLAR,
    //不能被识别的符号
    NUL,
    //标识符
    IDENT,
    //无符号整数
    NUMBER,
    //运算符:+,-,*,/,=,#,<,<=,>,>=,:=
    PLUS,MINUS,TIMES,DIVIDE,EQL,POUND,LES,LEQ,MOR,MOQ,BECOMWS,
    //保留字:const,var,procedure,begin,end,odd,if,then,call,while,do,read,write
    CONSTSYM,VARSYM,PROCSYM,BEGINSYM,ENDSYM,ODDSYM,IFSYM,THENSYM,CALLSYM,WHILESYM,DOSYM,READSYM,WRITESYM,
    //界符:(,),,,;,.
    LPAREN,RPAREN,COMMA,SEMICOLON,PERIOD;
    public String getName(){
        return toString();
    }

    @Override
    public boolean isVN() {
        return false;
    }
}
