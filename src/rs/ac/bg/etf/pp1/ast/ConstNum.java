// generated with ast extension for cup
// version 0.8
// 10/9/2025 3:4:50


package rs.ac.bg.etf.pp1.ast;

public class ConstNum extends FirstConst {

    private Integer valueNum;

    public ConstNum (Integer valueNum) {
        this.valueNum=valueNum;
    }

    public Integer getValueNum() {
        return valueNum;
    }

    public void setValueNum(Integer valueNum) {
        this.valueNum=valueNum;
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
        buffer.append("ConstNum(\n");

        buffer.append(" "+tab+valueNum);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstNum]");
        return buffer.toString();
    }
}
