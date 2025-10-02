// generated with ast extension for cup
// version 0.8
// 2/9/2025 9:20:17


package rs.ac.bg.etf.pp1.ast;

public class DesignAssign extends DesignatorStatement {

    private ThreeDesignList ThreeDesignList;

    public DesignAssign (ThreeDesignList ThreeDesignList) {
        this.ThreeDesignList=ThreeDesignList;
        if(ThreeDesignList!=null) ThreeDesignList.setParent(this);
    }

    public ThreeDesignList getThreeDesignList() {
        return ThreeDesignList;
    }

    public void setThreeDesignList(ThreeDesignList ThreeDesignList) {
        this.ThreeDesignList=ThreeDesignList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ThreeDesignList!=null) ThreeDesignList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ThreeDesignList!=null) ThreeDesignList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ThreeDesignList!=null) ThreeDesignList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignAssign(\n");

        if(ThreeDesignList!=null)
            buffer.append(ThreeDesignList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignAssign]");
        return buffer.toString();
    }
}
