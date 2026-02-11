package rs.ac.bg.etf.pp1;

import org.apache.log4j.*;
import rs.ac.bg.etf.pp1.ast.*;

public class RuleVisitor extends VisitorAdaptor{
	
	int printCallCount = 0;
	int varDeclCount = 0;
	int readCallCount = 0;
	
	Logger log = Logger.getLogger(getClass());

	public void visit(VarDecl vardecl){
		varDeclCount++;
	}
	
    public void visit(PrintStmt print) {
		printCallCount++;
		log.info("Prepoznata naredba print");
	}
    
    public void visit(PrintStmt1 print) {
		printCallCount++;
		log.info("Prepoznata naredba print");
	}
    
    public void visit(ReadStmt read) {
    	readCallCount++;
		log.info("Prepoznata naredba read");
	}
    
	

}