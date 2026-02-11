// generated with ast extension for cup
// version 0.8
// 10/9/2025 3:4:50


package rs.ac.bg.etf.pp1.ast;

public class StatmntList extends StatementListVar {

    private StatementListVar StatementListVar;
    private Statement Statement;

    public StatmntList (StatementListVar StatementListVar, Statement Statement) {
        this.StatementListVar=StatementListVar;
        if(StatementListVar!=null) StatementListVar.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public StatementListVar getStatementListVar() {
        return StatementListVar;
    }

    public void setStatementListVar(StatementListVar StatementListVar) {
        this.StatementListVar=StatementListVar;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StatementListVar!=null) StatementListVar.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatementListVar!=null) StatementListVar.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatementListVar!=null) StatementListVar.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatmntList(\n");

        if(StatementListVar!=null)
            buffer.append(StatementListVar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatmntList]");
        return buffer.toString();
    }
}
