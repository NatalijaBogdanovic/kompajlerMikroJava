// generated with ast extension for cup
// version 0.8
// 2/9/2025 9:20:17


package rs.ac.bg.etf.pp1.ast;

public class NoOrConditionList extends CondTermORList {

    public NoOrConditionList () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NoOrConditionList(\n");

        buffer.append(tab);
        buffer.append(") [NoOrConditionList]");
        return buffer.toString();
    }
}
