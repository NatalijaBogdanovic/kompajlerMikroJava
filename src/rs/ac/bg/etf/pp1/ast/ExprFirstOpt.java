// generated with ast extension for cup
// version 0.8
// 2/9/2025 9:20:17


package rs.ac.bg.etf.pp1.ast;

public class ExprFirstOpt extends Expr {

    private ExprStartOpt ExprStartOpt;
    private ExprAddopList ExprAddopList;

    public ExprFirstOpt (ExprStartOpt ExprStartOpt, ExprAddopList ExprAddopList) {
        this.ExprStartOpt=ExprStartOpt;
        if(ExprStartOpt!=null) ExprStartOpt.setParent(this);
        this.ExprAddopList=ExprAddopList;
        if(ExprAddopList!=null) ExprAddopList.setParent(this);
    }

    public ExprStartOpt getExprStartOpt() {
        return ExprStartOpt;
    }

    public void setExprStartOpt(ExprStartOpt ExprStartOpt) {
        this.ExprStartOpt=ExprStartOpt;
    }

    public ExprAddopList getExprAddopList() {
        return ExprAddopList;
    }

    public void setExprAddopList(ExprAddopList ExprAddopList) {
        this.ExprAddopList=ExprAddopList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprStartOpt!=null) ExprStartOpt.accept(visitor);
        if(ExprAddopList!=null) ExprAddopList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprStartOpt!=null) ExprStartOpt.traverseTopDown(visitor);
        if(ExprAddopList!=null) ExprAddopList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprStartOpt!=null) ExprStartOpt.traverseBottomUp(visitor);
        if(ExprAddopList!=null) ExprAddopList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprFirstOpt(\n");

        if(ExprStartOpt!=null)
            buffer.append(ExprStartOpt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ExprAddopList!=null)
            buffer.append(ExprAddopList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExprFirstOpt]");
        return buffer.toString();
    }
}
