// generated with ast extension for cup
// version 0.8
// 2/9/2025 9:20:16


package rs.ac.bg.etf.pp1.ast;

public class SingleConstAssign extends ConstList {

    private ListElems ListElems;

    public SingleConstAssign (ListElems ListElems) {
        this.ListElems=ListElems;
        if(ListElems!=null) ListElems.setParent(this);
    }

    public ListElems getListElems() {
        return ListElems;
    }

    public void setListElems(ListElems ListElems) {
        this.ListElems=ListElems;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ListElems!=null) ListElems.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ListElems!=null) ListElems.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ListElems!=null) ListElems.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleConstAssign(\n");

        if(ListElems!=null)
            buffer.append(ListElems.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleConstAssign]");
        return buffer.toString();
    }
}
