// generated with ast extension for cup
// version 0.8
// 2/9/2025 9:20:17


package rs.ac.bg.etf.pp1.ast;

public class ExprAddopListGlobal extends ExprAddopList {

    private ExprAddopList ExprAddopList;
    private AddOp AddOp;
    private Term Term;

    public ExprAddopListGlobal (ExprAddopList ExprAddopList, AddOp AddOp, Term Term) {
        this.ExprAddopList=ExprAddopList;
        if(ExprAddopList!=null) ExprAddopList.setParent(this);
        this.AddOp=AddOp;
        if(AddOp!=null) AddOp.setParent(this);
        this.Term=Term;
        if(Term!=null) Term.setParent(this);
    }

    public ExprAddopList getExprAddopList() {
        return ExprAddopList;
    }

    public void setExprAddopList(ExprAddopList ExprAddopList) {
        this.ExprAddopList=ExprAddopList;
    }

    public AddOp getAddOp() {
        return AddOp;
    }

    public void setAddOp(AddOp AddOp) {
        this.AddOp=AddOp;
    }

    public Term getTerm() {
        return Term;
    }

    public void setTerm(Term Term) {
        this.Term=Term;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprAddopList!=null) ExprAddopList.accept(visitor);
        if(AddOp!=null) AddOp.accept(visitor);
        if(Term!=null) Term.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprAddopList!=null) ExprAddopList.traverseTopDown(visitor);
        if(AddOp!=null) AddOp.traverseTopDown(visitor);
        if(Term!=null) Term.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprAddopList!=null) ExprAddopList.traverseBottomUp(visitor);
        if(AddOp!=null) AddOp.traverseBottomUp(visitor);
        if(Term!=null) Term.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprAddopListGlobal(\n");

        if(ExprAddopList!=null)
            buffer.append(ExprAddopList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AddOp!=null)
            buffer.append(AddOp.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Term!=null)
            buffer.append(Term.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExprAddopListGlobal]");
        return buffer.toString();
    }
}
