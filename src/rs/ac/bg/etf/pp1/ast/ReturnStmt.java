// generated with ast extension for cup
// version 0.8
// 2/9/2025 9:20:16


package rs.ac.bg.etf.pp1.ast;

public class ReturnStmt extends Statement {

    private ExprOption ExprOption;

    public ReturnStmt (ExprOption ExprOption) {
        this.ExprOption=ExprOption;
        if(ExprOption!=null) ExprOption.setParent(this);
    }

    public ExprOption getExprOption() {
        return ExprOption;
    }

    public void setExprOption(ExprOption ExprOption) {
        this.ExprOption=ExprOption;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprOption!=null) ExprOption.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprOption!=null) ExprOption.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprOption!=null) ExprOption.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ReturnStmt(\n");

        if(ExprOption!=null)
            buffer.append(ExprOption.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ReturnStmt]");
        return buffer.toString();
    }
}
