// generated with ast extension for cup
// version 0.8
// 2/9/2025 9:20:16


package rs.ac.bg.etf.pp1.ast;

public class SingleParam extends FormParsList {

    private Param Param;

    public SingleParam (Param Param) {
        this.Param=Param;
        if(Param!=null) Param.setParent(this);
    }

    public Param getParam() {
        return Param;
    }

    public void setParam(Param Param) {
        this.Param=Param;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Param!=null) Param.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Param!=null) Param.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Param!=null) Param.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleParam(\n");

        if(Param!=null)
            buffer.append(Param.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleParam]");
        return buffer.toString();
    }
}
