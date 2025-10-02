package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;


public class SemanticAnalyzer extends VisitorAdaptor {
	
	boolean errorDetected = false;
	Logger log = Logger.getLogger(getClass());
	private Struct currType;
	private int value;
	private Struct constType;
	private Struct boolType = Tab.find("bool").getType();
	boolean methodDetected=false;
	private Obj currentMethod, mainMeth;
	
	  public void report_error(String message, SyntaxNode info) {
	    	errorDetected = true;
	    	StringBuilder msg = new StringBuilder(message); 
	    	int l = (info == null) ? 0: info.getLine();
	    	if (l != 0)
	            msg.append (" na liniji ").append(l);
	        log.error(msg.toString());
	    }
	    
	    public void report_info(String message, SyntaxNode info) {
	    	StringBuilder msg = new StringBuilder(message); 
	    	int l = (info == null) ? 0: info.getLine();
	    	if (l != 0)
	            msg.append (" na liniji ").append(l);
	        log.info(msg.toString());
	    }
	    
	    public boolean passed() {
	    	return !errorDetected;
	    }
	    
	    private String structToString(Struct typeToConvert) {
			if (typeToConvert == null) {
				return "null";
			}

			switch (typeToConvert.getKind()) {
				case Struct.None:
					return "void"; 
				case Struct.Int:
					return "int";
				case Struct.Char:
					return "char";
				case Struct.Bool:
					return "bool";
				case Struct.Array:
					// Rekurzivni poziv da bismo dobili ime tipa elemenata niza
					return "niz tipa " + structToString(typeToConvert.getElemType());

				default:
					return "nepoznat tip";
			}
		}
	    
	    private String objToString(Obj objToConvert) {
			if (objToConvert == null) {
				return "null_obj";
			}
			
			StringBuilder sb = new StringBuilder();
			
			switch (objToConvert.getKind()) {
				case Obj.Con:  sb.append("Con "); break;
				case Obj.Var:  sb.append("Var "); break;
				case Obj.Type: sb.append("Type "); break;
				case Obj.Meth: sb.append("Meth "); break;
				case Obj.Prog: sb.append("Prog "); break;
				default:       sb.append("Obj "); break;
			}
			
			sb.append(objToConvert.getName());
			sb.append(": ");
			
			sb.append(structToString(objToConvert.getType()));
			
			sb.append(", ");
			sb.append(objToConvert.getAdr());
			sb.append(", ");
			sb.append(objToConvert.getLevel());
			
			return sb.toString();
		}
	    
	    @Override
	    public void visit(ProgName progName) {
	    	progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
	    	Tab.openScope();
	    	
	    }
	    
	    @Override
	    public void visit(Program prog) {
	    	if(methodDetected==false || mainMeth.getLevel() >0) {
				report_error("Greska! Mora postojati funkcija main sa povratnim tipom void i bez formalnih parametara! ",null);
			}
	    	Tab.chainLocalSymbols(prog.getProgName().obj);
	    	Tab.closeScope();
	    }
	    

	    
	    //deklaracije konstanti
	    
	    @Override
	    public void visit(ListElems listElems){
	    	Obj cobj = Tab.find(listElems.getName());
	    	if(cobj == Tab.noObj) {
	    		if(constType.assignableTo(currType)) {
	    			cobj = Tab.insert(Obj.Con, listElems.getName(), currType);
			    	cobj.setAdr(value);
			    	String typeName = structToString(currType);
			    	report_info("Definisana je konstanta- "+ listElems.getName() + " -tipa " + typeName + " : " + objToString(cobj),listElems);
	    		}
	    		else {
	    			report_error("Greska: pokusana dodela vrednosti nekompatibilnog tipa datoj konstanti: " + listElems.getName(), listElems);
	    		}
	    		
	    	}else {
	    		report_error("Greska: konstanta - " + listElems.getName() + " - je vec definisana", listElems);
	    	}
	    }
	    
	    @Override
	    public void visit(ConstNum constnum) {
	    	value = constnum.getValueNum();
	    	constType = Tab.intType;
	    }
	    
	    @Override
	    public void visit(ConstChar constchar) {
	    	value = constchar.getValueChar();
	    	constType = Tab.charType;
	    }
	    
	    @Override
	    public void visit(ConstBool constbool) {
	    	Boolean a = constbool.getValueBool();
	    	int adrVal = 0; 
	    	if (a == true) {
	    		adrVal = 1;
	    	}else {
	    		adrVal = 0;
	    	}
	    	value = adrVal;
	    	constType = boolType;
	    }
	    
	    //deklaracije varijabli
	    
	    @Override
	    public void visit(VarAssign_var varAssign_var) {
	    	Obj vobj = null;
	    	String scope = "";
	    	if(currentMethod == null) {
	    		scope = "globalna";
		    	vobj = Tab.find(varAssign_var.getVarName());
	    	}else {
	    		scope = "lokalna";
	    		vobj = Tab.currentScope().findSymbol(varAssign_var.getVarName());
	    	}
	    	if(vobj == Tab.noObj || vobj == null) {
	    		vobj = Tab.insert(Obj.Var, varAssign_var.getVarName(), currType);
	    		String typeName = structToString(currType);
	    		report_info("Definisana je " + scope +" promenljiva- "+ varAssign_var.getVarName() + " -tipa " + typeName + " : " + objToString(vobj),varAssign_var);
	    	}else {
	    		report_error("Greska: promenljiva - " + varAssign_var.getVarName() + " - je vec definisana i ponovo redefinisana", varAssign_var);
	    	}
	    }
	    
	    @Override
	    public void visit(VarAssign_array varAssign_array) {
	    	Obj vobj = null;
	    	String scope = "";
	    	if(currentMethod == null) {
	    		scope = "globalna";
		    	vobj = Tab.find(varAssign_array.getVarName());
	    	}else {
	    		scope = "lokalna";
	    		vobj = Tab.currentScope().findSymbol(varAssign_array.getVarName());
	    	}
	    	if(vobj == Tab.noObj || vobj == null) {
	    		vobj = Tab.insert(Obj.Var, varAssign_array.getVarName(), new Struct (Struct.Array, currType));	
	    		String typeName = structToString(currType);
	    		report_info("Definisana je "+ scope + " nizovska promenljiva- "+ varAssign_array.getVarName() + " -tipa "+ typeName + " : " + objToString(vobj),varAssign_array);
	    	}else {
	    		report_error("Greska: promenljiva - " + varAssign_array.getVarName() + " - je vec definisana i ponovo redefinisana", varAssign_array);
	    	}
	    }
	    
	    //deklaracije metoda
	    
	    @Override
	     public void visit(MethodStartType methStartType) {
	    	currentMethod=Tab.insert(Obj.Meth, methStartType.getName(), currType);
	    	methStartType.obj = currentMethod;
	    	Tab.openScope();
	    	report_info("Obradjuje se funkcija "+ methStartType.getName() + ": "+ objToString(currentMethod),methStartType);
	    }
	    
	    @Override
	     public void visit(MethodStartVoid methStartVoid) {
	    	//String methName=null;
	    	currentMethod=Tab.insert(Obj.Meth, methStartVoid.getName(), Tab.noType);
	    	methStartVoid.obj = currentMethod;
	    	Tab.openScope();
	    	if(methStartVoid.getName().equals("main")) {
	    		methodDetected=true;
	    		mainMeth = currentMethod;
	    	}
	    	
			report_info("Obradjuje se funkcija "+ methStartVoid.getName() + ": " + objToString(currentMethod),methStartVoid);
	    	
	    }
	    
	    @Override
		public void visit(MethodDecl methodDecl) {
			Tab.chainLocalSymbols(currentMethod);
			Tab.closeScope();
			currentMethod=null;
		}
	    
	    
	    //deklaracija formalnih parametara
	    @Override
	    public void visit(Param_var param_var) {
	    	Obj vobj = null;
	    	if(currentMethod == null) {
	    		report_error("Semanticka greska pri obilasku cvora Param_var", param_var);
	    	}else {
	    		vobj = Tab.currentScope().findSymbol(param_var.getName());
	    	}
	    	if(vobj == Tab.noObj || vobj == null) {
	    		vobj = Tab.insert(Obj.Var, param_var.getName(), currType);
	    		vobj.setFpPos(1);
	    		currentMethod.setLevel(currentMethod.getLevel() + 1);
	    		String typeName = structToString(currType);
	    		report_info("Definisan je formalni parametar- "+ param_var.getName() + " -tipa " + typeName + " : " + objToString(vobj) ,param_var);
	    	}else {
	    		report_error("Greska: formalni parametar - " + param_var.getName() + " - je vec definisan", param_var);
	    	}
	    }
	    
	    @Override
	    public void visit(Param_array param_array) {
	    	Obj vobj = null;
	    	if(currentMethod == null) {
	    		report_error("Semanticka greska pri obilasku cvora Param_array", param_array);
	    	}else {
	    		vobj = Tab.currentScope().findSymbol(param_array.getName());
	    	}
	    	if(vobj == Tab.noObj || vobj == null) {
	    		vobj = Tab.insert(Obj.Var, param_array.getName(), new Struct (Struct.Array, currType));
	    		vobj.setFpPos(1);
	    		currentMethod.setLevel(currentMethod.getLevel() + 1);
	    		String typeName = structToString(currType);
	    		report_info("Definisan je formalni parametar- "+ param_array.getName() + " -tipa " + typeName + " : " + objToString(vobj) ,param_array);
	    	}else {
	    		report_error("Greska: formalni parametar - " + param_array.getName() + " - je vec definisan", param_array);
	    	}
	    }
	    
	    
	    @Override
	    public void visit(Type type) {
	    	Obj typeObj = Tab.find(type.getTypeName());
	    	if(typeObj == Tab.noObj) {
	    		report_error("U tabeli simbola ne postoji ovaj tip: " + type.getTypeName() + " definisan", type);
	    		currType = Tab.noType;
	    	}
	    	else if(typeObj.getKind() != Obj.Type) {
	    		report_error("Tip podatka je neadekvatan: " + type.getTypeName(), type);
	    		currType = Tab.noType;
	    	}
	    	else {
	    		currType = typeObj.getType();
	    	}
	    }

}
