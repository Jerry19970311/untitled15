package test0402;

class Token{
    private Integer no;
    private String ex;
    public Token(Integer no,String ex){
        this.no=no;
        this.ex=ex;
    }
    public Token(int no,String ex){
        this.no=new Integer(no);
        this.ex=ex;
    }
    public String getExString(){
        if(this.no!=1){
            return "'"+ex+"'";
        }
        return ex;
    }
    @Override
    public String toString() {
        return "("+no.toString()+","+getExString()+")";
    }
}
