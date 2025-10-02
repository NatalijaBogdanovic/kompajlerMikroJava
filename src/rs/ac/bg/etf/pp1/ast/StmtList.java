// generated with ast extension for cup
// version 0.8
// 2/9/2025 9:20:16


package rs.ac.bg.etf.pp1.ast;

public class StmtList extends Statement {

    private StatementListVar StatementListVar;

    public StmtList (StatementListVar StatementListVar) {
        this.StatementListVar=StatementListVar;
        if(StatementListVar!=null) StatementListVar.setParent(this);
    }

    public StatementListVar getStatementListVar() {
        return StatementListVar;
    }

    public void setStatementListVar(StatementListVar StatementListVar) {
        this.StatementListVar=StatementListVar;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StatementListVar!=null) StatementListVar.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatementListVar!=null) StatementListVar.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatementListVar!=null) StatementListVar.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StmtList(\n");

        if(StatementListVar!=null)
            buffer.append(StatementListVar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StmtList]");
        return buffer.toString();
    }
}
