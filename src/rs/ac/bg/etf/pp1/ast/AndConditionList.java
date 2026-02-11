// generated with ast extension for cup
// version 0.8
// 10/9/2025 3:4:50


package rs.ac.bg.etf.pp1.ast;

public class AndConditionList extends CondFactANDList {

    private CondFactANDList CondFactANDList;
    private CondFact CondFact;

    public AndConditionList (CondFactANDList CondFactANDList, CondFact CondFact) {
        this.CondFactANDList=CondFactANDList;
        if(CondFactANDList!=null) CondFactANDList.setParent(this);
        this.CondFact=CondFact;
        if(CondFact!=null) CondFact.setParent(this);
    }

    public CondFactANDList getCondFactANDList() {
        return CondFactANDList;
    }

    public void setCondFactANDList(CondFactANDList CondFactANDList) {
        this.CondFactANDList=CondFactANDList;
    }

    public CondFact getCondFact() {
        return CondFact;
    }

    public void setCondFact(CondFact CondFact) {
        this.CondFact=CondFact;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondFactANDList!=null) CondFactANDList.accept(visitor);
        if(CondFact!=null) CondFact.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondFactANDList!=null) CondFactANDList.traverseTopDown(visitor);
        if(CondFact!=null) CondFact.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondFactANDList!=null) CondFactANDList.traverseBottomUp(visitor);
        if(CondFact!=null) CondFact.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AndConditionList(\n");

        if(CondFactANDList!=null)
            buffer.append(CondFactANDList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondFact!=null)
            buffer.append(CondFact.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AndConditionList]");
        return buffer.toString();
    }
}
