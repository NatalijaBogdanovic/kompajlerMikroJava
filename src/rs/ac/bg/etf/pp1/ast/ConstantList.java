// generated with ast extension for cup
// version 0.8
// 10/9/2025 3:4:50


package rs.ac.bg.etf.pp1.ast;

public class ConstantList extends ConstList {

    private ConstList ConstList;
    private ListElems ListElems;

    public ConstantList (ConstList ConstList, ListElems ListElems) {
        this.ConstList=ConstList;
        if(ConstList!=null) ConstList.setParent(this);
        this.ListElems=ListElems;
        if(ListElems!=null) ListElems.setParent(this);
    }

    public ConstList getConstList() {
        return ConstList;
    }

    public void setConstList(ConstList ConstList) {
        this.ConstList=ConstList;
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
        if(ConstList!=null) ConstList.accept(visitor);
        if(ListElems!=null) ListElems.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstList!=null) ConstList.traverseTopDown(visitor);
        if(ListElems!=null) ListElems.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstList!=null) ConstList.traverseBottomUp(visitor);
        if(ListElems!=null) ListElems.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstantList(\n");

        if(ConstList!=null)
            buffer.append(ConstList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ListElems!=null)
            buffer.append(ListElems.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstantList]");
        return buffer.toString();
    }
}
