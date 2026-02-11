package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.ac.bg.etf.pp1.ExtendedTab;


public class SemanticAnalyzer extends VisitorAdaptor {
	
	boolean errorDetected = false;
	Logger log = Logger.getLogger(getClass());
	private Struct currType;
	private int value;
	private Struct constType;
	private Struct boolType = ExtendedTab.boolType;
	boolean methodDetected=false;
	private boolean returnFound;
	private Obj currentMethod, mainMeth;
	int nVars;
	
	private void checkFunctionArguments(Obj funcObj, ActParsOpt actParsOpt, SyntaxNode callNode) {
	    Collection<Obj> formalParams = funcObj.getLocalSymbols();
	    int formalParamCount = funcObj.getLevel();

	    if (actParsOpt instanceof NoActualParameter) {
	        if (formalParamCount != 0) {
	            report_error("Greska na liniji " + callNode.getLine() + ": Funkcija '" + funcObj.getName() + "' zahteva " + formalParamCount + " argumenata, a pozvana je bez njih.", null);
	        }
	        return;
	    }

	    // Slucaj 2: Poziv sa argumentima 
	    List<Struct> actualParamTypes = new ArrayList<>();
	    ActPars actPars = ((ActualParameterList) actParsOpt).getActPars();

	    // posto je moja gramatika levo-rekurzivna morala bih da skupljam arg i da ih na kraju okrenem ILI mogu da ih dodajem na pocetak liste

	    // Prvo obradimo ExprList, pa onda prvi Expr
	    ExprList currentList = actPars.getExprList();
	    while (currentList instanceof ExpressionList) {
	        ExpressionList exprList = (ExpressionList) currentList;
	        actualParamTypes.add(0, exprList.getExpr().struct); // Dodaj na pocetak
	        currentList = exprList.getExprList();
	    }
	    // Na kraju dodamo prvi argument, koji je sada na pocetku liste
	    actualParamTypes.add(0, actPars.getExpr().struct);
	    
	    int actualParamCount = actualParamTypes.size();

	    // provera broja i tipova 
	    if (actualParamCount != formalParamCount) {
	        report_error("Greska na liniji " + callNode.getLine() + ": Funkcija '" + funcObj.getName() + "' zahteva " + formalParamCount + " argumenata, a prosledjeno je " + actualParamCount, null);
	        return;
	    }

	    List<Obj> formalParamList = new ArrayList<>(formalParams);
	    for (int i = 0; i < formalParamCount; i++) {
	        Struct actualType = actualParamTypes.get(i);
	        Struct formalType = formalParamList.get(i).getType();

	        if (!actualType.assignableTo(formalType)) {
	            report_error("Greska na liniji " + callNode.getLine() + ": Ne poklapanje tipa za " + (i+1) + ". argument funkcije '" + funcObj.getName() + "'. Ocekivan je tip kompatibilan sa " + structToString(formalType) + ", a prosledjen je " + structToString(actualType), null);
	        }
	    }
	}
	
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
				case Struct.Class:
					//if (typeToConvert.equals(ExtendedTab.setType)) return "set";
					return "set";
					

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
	    	progName.obj = ExtendedTab.insert(Obj.Prog, progName.getProgName(), ExtendedTab.noType);
	    	ExtendedTab.openScope();
	    	
	    }
	    
	    @Override
	    public void visit(Program prog) {
	    	nVars = ExtendedTab.currentScope().getnVars();
	    	if(methodDetected==false || mainMeth.getLevel() >0) {
				report_error("Greska! Mora postojati funkcija main sa povratnim tipom void i bez formalnih parametara! ",null);
			}
	    	ExtendedTab.chainLocalSymbols(prog.getProgName().obj);
	    	ExtendedTab.closeScope();
	    }
	    

	    
	    //deklaracije konstanti
	    
	    @Override
	    public void visit(ListElems listElems){
	    	Obj cobj = ExtendedTab.find(listElems.getName());
	    	if(cobj == ExtendedTab.noObj) {
	    		if(constType.assignableTo(currType)) {
	    			cobj = ExtendedTab.insert(Obj.Con, listElems.getName(), currType);
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
	    	constType = ExtendedTab.intType;
	    }
	    
	    @Override
	    public void visit(ConstChar constchar) {
	    	value = constchar.getValueChar();
	    	constType = ExtendedTab.charType;
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
		    	vobj = ExtendedTab.find(varAssign_var.getVarName());
	    	}else {
	    		scope = "lokalna";
	    		vobj = ExtendedTab.currentScope().findSymbol(varAssign_var.getVarName());
	    	}
	    	if(vobj == ExtendedTab.noObj || vobj == null) {
	    		vobj = ExtendedTab.insert(Obj.Var, varAssign_var.getVarName(), currType);
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
		    	vobj = ExtendedTab.find(varAssign_array.getVarName());
	    	}else {
	    		scope = "lokalna";
	    		vobj = ExtendedTab.currentScope().findSymbol(varAssign_array.getVarName());
	    	}
	    	if(vobj == ExtendedTab.noObj || vobj == null) {
	    		vobj = ExtendedTab.insert(Obj.Var, varAssign_array.getVarName(), new Struct (Struct.Array, currType));	
	    		String typeName = structToString(currType);
	    		report_info("Definisana je "+ scope + " nizovska promenljiva- "+ varAssign_array.getVarName() + " -tipa "+ typeName + " : " + objToString(vobj),varAssign_array);
	    	}else {
	    		report_error("Greska: promenljiva - " + varAssign_array.getVarName() + " - je vec definisana i ponovo redefinisana", varAssign_array);
	    	}
	    }
	    
	    //deklaracije metoda
	    
	    @Override
	     public void visit(MethodStartType methStartType) {
	    	currentMethod=ExtendedTab.insert(Obj.Meth, methStartType.getName(), currType);
	    	methStartType.obj = currentMethod;
	    	ExtendedTab.openScope();
	    	report_info("Obradjuje se funkcija "+ methStartType.getName() + ": "+ objToString(currentMethod),methStartType);
	    }
	    
	    @Override
	     public void visit(MethodStartVoid methStartVoid) {
	    	//String methName=null;
	    	currentMethod=ExtendedTab.insert(Obj.Meth, methStartVoid.getName(), ExtendedTab.noType);
	    	methStartVoid.obj = currentMethod;
	    	ExtendedTab.openScope();
	    	if(methStartVoid.getName().equals("main")) {
	    		methodDetected=true;
	    		mainMeth = currentMethod;
	    	}
	    	
			report_info("Obradjuje se funkcija "+ methStartVoid.getName() + ": " + objToString(currentMethod),methStartVoid);
	    	
	    }
	    
	    @Override
		public void visit(MethodDecl methodDecl) {
	    	 if (!returnFound && !currentMethod.getType().equals(ExtendedTab.noType)) {
	    	        report_error("Greska na liniji " + methodDecl.getLine() + ": Metoda '" + currentMethod.getName() + "' nema 'return' naredbu, a nije void.", null);
	    	    }
			ExtendedTab.chainLocalSymbols(currentMethod);
			ExtendedTab.closeScope();
			currentMethod=null;
			returnFound = false;
		}
	    
	    
	    //deklaracija formalnih parametara
	    @Override
	    public void visit(Param_var param_var) {
	    	Obj vobj = null;
	        if (currentMethod == null) {
	            report_error("Semanticka greska pri obilasku cvora Param_var", param_var);
	            return; 
	        } else {
	            vobj = ExtendedTab.currentScope().findSymbol(param_var.getName());
	        }
	        
	        if (vobj == null || vobj == ExtendedTab.noObj) {
	            // 1. Prvo povecaj brojac parametara za metodu
	            currentMethod.setLevel(currentMethod.getLevel() + 1);
	            
	            vobj = ExtendedTab.insert(Obj.Var, param_var.getName(), currType);
	            
	            vobj.setFpPos(currentMethod.getLevel());
	            
	            String typeName = structToString(currType);
	            report_info("Definisan je formalni parametar- " + param_var.getName() + " -tipa " + typeName + " : " + objToString(vobj), param_var);
	        } else {
	            report_error("Greska: formalni parametar - " + param_var.getName() + " - je vec definisan", param_var);
	        }
	    }
	    
	    @Override
	    public void visit(Param_array param_array) {
	    	Obj vobj = null;
	        if (currentMethod == null) {
	            report_error("Semanticka greska pri obilasku cvora Param_array", param_array);
	            return; 
	        } else {
	            vobj = ExtendedTab.currentScope().findSymbol(param_array.getName());
	        }
	        
	        if (vobj == null || vobj == ExtendedTab.noObj) {
	            currentMethod.setLevel(currentMethod.getLevel() + 1);
	            
	            vobj = ExtendedTab.insert(Obj.Var, param_array.getName(), new Struct(Struct.Array, currType));
	            
	            vobj.setFpPos(currentMethod.getLevel());
	            
	            String typeName = structToString(currType);
	            report_info("Definisan je formalni parametar- " + param_array.getName() + " -tipa niz " + typeName + " : " + objToString(vobj), param_array);
	        } else {
	            report_error("Greska: formalni parametar - " + param_array.getName() + " - je vec definisan", param_array);
	        }
	    }
	    
	    
	    @Override
	    public void visit(Type type) {
	    	Obj typeObj = ExtendedTab.find(type.getTypeName());
	    	if(typeObj == ExtendedTab.noObj) {
	    		report_error("Greska: U tabeli simbola ne postoji ovaj tip: " + type.getTypeName() + " definisan", type);
	    		type.struct = ExtendedTab.noType;
	    	}
	    	else if(typeObj.getKind() != Obj.Type) {
	    		report_error("Greska: Tip podatka je neadekvatan: " + type.getTypeName(), type);
	    		type.struct = ExtendedTab.noType;
	    	}
	    	else {
	    		type.struct = typeObj.getType();
	    	}
	    	currType = type.struct;
	    }
	    
	    //---kontekstni uslovi-----------------------------
	    
	    //FACTOR SMENE
	    
	    @Override
	    public void visit(BoolFactor boolFactor) {
	    	boolFactor.struct = ExtendedTab.boolType;
	    }
	    
	    @Override
	    public void visit(CharFactor charFactor) {
	    	charFactor.struct = ExtendedTab.charType;
	    }
	    @Override
	    public void visit(NumFactor numFactor) {
	    	numFactor.struct = ExtendedTab.intType;
	    }
	    
	    @Override
	    public void visit(DesignatorVar designatorVar) {
	    	designatorVar.struct = designatorVar.getDesignator().obj.getType();
	    }
	    
	    @Override
	    public void visit(ArrayFactor arrayFactor) {
	    	/*
	    	if(arrayFactor.getExpr().struct.equals(ExtendedTab.intType)) {
	    		arrayFactor.struct = new Struct(Struct.Array, currType);
	    	}
	    	else {
	    		report_error("Greska na liniji " + arrayFactor.getLine() + ". Velicina niza mora biti tipa int!", null);
	    		arrayFactor.struct = ExtendedTab.noType;
	    	} */
	    	 // Proveravamo da li je izraz za velicinu/kapacitet tipa int (uslov 1 iz kont usslova)
	        Struct sizeExprType = arrayFactor.getExpr().struct;
	        if (sizeExprType == null) return;
	        if (!sizeExprType.equals(ExtendedTab.intType)) {
	            report_error("Greska na liniji " + arrayFactor.getLine() + ". Velicina niza ili kapacitet skupa mora biti tipa int!", null);
	            arrayFactor.struct = ExtendedTab.noType;
	            return; // Prekidamo dalje jer je uslov pao
	        }
	        // Dohvatamo tip naveden posle 'new' kako bismo proverili da li se kreira skup ili niz
	        Struct specifiedType = arrayFactor.getType().struct;

	        if (specifiedType.equals(ExtendedTab.setType)) {
	            // Ako je navedeni tip 'set', onda je rezultat celog izraza takode tipa 'set'.
	            // Primer: new set[50] -> tip celog izraza je 'set'.
	            arrayFactor.struct = ExtendedTab.setType;
	            report_info("Uspesno kreiran skup na liniji " + arrayFactor.getLine(), null);

	        } else {
	            // U svim ostalim situacijama, kreira se niz.
	            // Primer: new int[10] -> tip celog izraza je 'niz tipa int'.
	            arrayFactor.struct = new Struct(Struct.Array, specifiedType);
	            report_info("Uspesno kreiran niz na liniji " + arrayFactor.getLine(), null);
	        }
	    	
	    }
	    
	    @Override
	    public void visit(ExprFactor exprFactor) {
	    	exprFactor.struct = exprFactor.getExpr().struct;
	    }
	    
	    @Override
	    public void visit(DesignatorFuncCall designatorFuncCall) {
	    	
	    	// Dohvatamo Obj cvor za designator koji je vec obraden (recimo visit(DesignIdent))
	        Obj funcObj = designatorFuncCall.getDesignator().obj;

	        if (funcObj.getKind() == Obj.Meth) {
	            
	            // proveravamo da li se funkcija koja je 'void' koristi u izrazu, sto bi bila greska
	            if (funcObj.getType().equals(ExtendedTab.noType)) {
	                report_error("Greska na liniji " + designatorFuncCall.getLine() + ": Funkcija '" + funcObj.getName() + "' je void i ne moze se koristiti u izrazima.", null);
	                designatorFuncCall.struct = ExtendedTab.noType; // Rezultat je "error" tip.
	            } else {
	                // Sve je u redu. Tip ovog izraza je povratni tip funkcije.
	                report_info("Poziv funkcije '" + funcObj.getName() + "' na liniji " + designatorFuncCall.getLine(), null);
	                designatorFuncCall.struct = funcObj.getType();
	            }
	            
	            checkFunctionArguments(funcObj, designatorFuncCall.getActParsOpt(), designatorFuncCall);

	        } else {
	            // Nije funkcija, prijavljujemo gresku.
	            report_error("Greska na liniji " + designatorFuncCall.getLine() + ": Simbol '" + funcObj.getName() + "' nije funkcija i ne moze se pozvati.", null);
	            designatorFuncCall.struct = ExtendedTab.noType; // Rezultat je "error" tip.
	        }
	    }
	    
	    
	    //DESIGNATOR SMENE
	    
	    @Override
	    public void visit(DesignIdent designIdent) {
	    	Obj idnt_var = ExtendedTab.find(designIdent.getVarName());
	    	if(idnt_var != ExtendedTab.noObj && (idnt_var.getKind() == Obj.Var || idnt_var.getKind() == Obj.Con || idnt_var.getKind() == Obj.Meth)) {
	    		designIdent.obj = idnt_var ;
	    	}  
	    	else {
	    		if (idnt_var == ExtendedTab.noObj)
	    			report_error("Greska: nije definisan simbol " + designIdent.getVarName(), designIdent);
	    		else
	    			report_error("Greska: neadekvatna promenljiva ", designIdent);
	    		
	    		designIdent.obj = ExtendedTab.noObj;
	    	}
	    }
	    
	    @Override
	    public void visit(DesignArr designArr) {
	    	 // Samo preuzimamo Obj koji je dete (Designator) vec pronaslo.
	        Obj designatorObj = designArr.getDesignator().obj;

	        // Proveravamo da li je pronadeni simbol niz.
	        if (designatorObj.getType().getKind() == Struct.Array) {
	            // Jeste, prosledujemo informaciju "nagore" ovom cvoru.
	            designArr.obj = designatorObj;
	        } else {
	            // Nije, prijavljujemo gresku.
	            report_error("Greska na liniji " + designArr.getLine() + ": Simbol '" + designatorObj.getName() + "' nije niz.", null);
	            designArr.obj = ExtendedTab.noObj;
	        }
	    }
	    
	    @Override
	    public void visit(DesignArrElem designArrElem) {
	    	Obj arr_var = designArrElem.getDesignArr().obj;
	    	
	    	if(arr_var == ExtendedTab.noObj) {
	    		designArrElem.obj = ExtendedTab.noObj;
	    	}
	    	else if(!designArrElem.getExpr().struct.equals(ExtendedTab.intType)) {
	    		report_error("Greska na liniji " + designArrElem.getLine() + " za cvor [DesignArrElem]. Jedina moguca vrednost indexa je int!", null);
	    		designArrElem.obj = ExtendedTab.noObj;
	    	}
	    	else {
	    		designArrElem.obj = new Obj(Obj.Elem, arr_var.getName() + "[~]", arr_var.getType().getElemType());
	    		report_info("Pristup elementu niza '"+arr_var.getName()+"'", designArrElem);
	    	}
	    }
	    
	
	    
	    //TERM SMENE
	    
	    @Override
	    public void visit(TermFactor termFactor) {
	    	termFactor.struct = termFactor.getFactor().struct;
	    }
	    
	    @Override
	    public void visit(TermMulop termMulop) {
	    	Struct t = termMulop.getTerm().struct;
	    	Struct f = termMulop.getFactor().struct;
	    	if(t.equals(f) && f==ExtendedTab.intType){
	    		termMulop.struct = t;
	    	}
	    	else {
	    		report_error("Greska na liniji " + termMulop.getLine() + " : operacije mnozenja i deljenja moguce su samo nad int vrednostima!", null);
	    		termMulop.struct = ExtendedTab.noType;
	    	}
	    }
	    
	    //EXPR SMENE
	    
	    @Override
	    public void visit(ExprAddopListGlobal exprAddopListGlobal) {
	    	/*
	    	if(exprAddopListGlobal.getAddOp() instanceof Minus) {
	    		if(exprAddopListGlobal.getTerm().struct.equals(ExtendedTab.intType)) {
	    			exprAddopListGlobal.struct = ExtendedTab.intType;
	    		}
	    		else {
	    			report_error("Greska: pokusana negacija podatka koji nije broj.", exprAddopListGlobal);
	    			exprAddopListGlobal.struct = ExtendedTab.noType;
	    		}
	    	}
	    	else {
	    		exprAddopListGlobal.struct = exprAddopListGlobal.getTerm().struct;
	    	}  */
	    	/*
	    	Struct exp = exprAddopListGlobal.getExprAddopList().struct;
	    	Struct t = exprAddopListGlobal.getTerm().struct;

	    	if(t.equals(exp) && exp==ExtendedTab.intType){
	    		exprAddopListGlobal.struct = t;
	    	}
	    	else {
	    		report_error("Greska na liniji " + exprAddopListGlobal.getLine() + " : operacije sabiranja i oduzimanja moguce su samo nad int vrednostima!", null);
	    		exprAddopListGlobal.struct = ExtendedTab.noType;
	    	}*/
	    	
	    	Struct restOfListType = exprAddopListGlobal.getExprAddopList().struct; // Ovo moze biti null
	        Struct termType = exprAddopListGlobal.getTerm().struct;

	        // Proveravamo da li je trenutni term int I da li je ostatak liste (ako postoji) takodje int
	        if (termType.equals(ExtendedTab.intType) && (restOfListType == null || restOfListType.equals(ExtendedTab.intType))) {
	             exprAddopListGlobal.struct = ExtendedTab.intType;
	        } else {
	            report_error("Greska na liniji " + exprAddopListGlobal.getTerm().getLine() + ": operacije sabiranja i oduzimanja moguce su samo nad int vrednostima!", null);
	            exprAddopListGlobal.struct = ExtendedTab.noType;
	        }
	    	
	    }
	    
	    @Override
	    public void visit(ExprStartWithMinus exprStartWithMinus) {
	    	if(exprStartWithMinus.getTerm().struct.getKind() != Struct.Int) {
	    		report_error("Greska na liniji " + exprStartWithMinus.getLine() + " : moguca je negacija samo int vrednosti!", null);
	    		exprStartWithMinus.struct = ExtendedTab.noType; 
	    	}
	    	else {
	    		report_info("dodelio sam minusu tip int", exprStartWithMinus);
	    		exprStartWithMinus.struct = ExtendedTab.intType;
	    	}
	    }
	    
	    @Override
	    public void visit(ExprStartWithoutMinus exprStartWithoutMinus) {
	    	
	    	exprStartWithoutMinus.struct = exprStartWithoutMinus.getTerm().struct;
	    	
	    }
	    
	    @Override
	    public void visit(ExprFirstOpt exprFirstOpt) {
	    	
	    	//exprFirstOpt.struct = exprFirstOpt.getExprAddopList().struct;
	    	
	    	Struct firstTermType = exprFirstOpt.getExprStartOpt().struct;
	        Struct addopListType = exprFirstOpt.getExprAddopList().struct; // Bice null ako nema operacija

	        if (addopListType == null) { // Slucaj kada imamo samo jedan term (npr. "5" ili "a")
	            exprFirstOpt.struct = firstTermType;
	        } else { // Slucaj kada imamo izraz (npr. "a + b")
	            // Proveravamo da li su i prvi clan i ostatak izraza tipa int
	            if (firstTermType.equals(ExtendedTab.intType) && addopListType.equals(ExtendedTab.intType)) {
	                exprFirstOpt.struct = ExtendedTab.intType;
	            } 
	            else {
	            	if (firstTermType != ExtendedTab.noType && addopListType != ExtendedTab.noType) { //noType je redFlag koj nam kaze dosao sam sa greskom (vec prijavljena negde dublje u stablu)

		                report_error("Greska na liniji " + exprFirstOpt.getLine() + ": nekompatibilni tipovi za sabiranje/oduzimanje. Ocekivan je int.", null);
	            	}
	                exprFirstOpt.struct = ExtendedTab.noType;
	            }
	        }
	    	
	    }
	    
	    //DESIGNATORSTATEMENTS
	    
	    @Override
	    public void visit (DesignAssignExpr designAssignExpr) {
	    	
	    	int objKind = designAssignExpr.getDesignator().obj.getKind();
	    	if(objKind != Obj.Elem && objKind != Obj.Var) {
	    		report_error("Greska: Pokusana dodela u promenljivu " + designAssignExpr.getDesignator().obj.getName() + " neadekvatnog tipa ", designAssignExpr);
	    	}
	    	else if(!designAssignExpr.getExpr().struct.assignableTo(designAssignExpr.getDesignator().obj.getType())) {
	    		report_error("Greska: Pokusana dodela neadekvatne vrednosti u promenljivu "+ designAssignExpr.getDesignator().obj.getName(), designAssignExpr);
	    	}
	    }
	    
	    
	    @Override
	    public void visit(DesignFunctionCalling designFunctionCalling) {
	    	Obj funcObj = designFunctionCalling.getDesignator().obj;
	    	int kind = funcObj.getKind();
	    	if(kind != Obj.Meth) {
	    		report_error("Greska: poziv neadekvatne metode: " + designFunctionCalling.getDesignator().obj.getName(), designFunctionCalling);
	    	}
	    	else {
	    		report_info("Poziv procedure " + funcObj.getName(), designFunctionCalling);
	    		checkFunctionArguments(funcObj, designFunctionCalling.getActParsOpt(), designFunctionCalling);
	    	}
	    }
	    
	    
	    @Override
	    public void visit(DesignIncrement designIncrement) {
	    	int objKind = designIncrement.getDesignator().obj.getKind();
	    	if(objKind != Obj.Elem && objKind != Obj.Var) {
	    		report_error("Greska: Pokusan increment promenljive " + designIncrement.getDesignator().obj.getName() + " neadekvatnog tipa ", designIncrement);
	    	}
	    	else if(!designIncrement.getDesignator().obj.getType().equals(ExtendedTab.intType)) {
	    		report_error("Greska: Pokusan increment nad ne int promenljivom "+ designIncrement.getDesignator().obj.getName(), designIncrement);
	    	}
	    }
	    
	    @Override
	    public void visit(DesignDecrement designDecrement) {
	    	int objKind = designDecrement.getDesignator().obj.getKind();
	    	if(objKind != Obj.Elem && objKind != Obj.Var) {
	    		report_error("Greska: Pokusan decrement promenljive " + designDecrement.getDesignator().obj.getName() + " neadekvatnog tipa ", designDecrement);
	    	}
	    	else if(!designDecrement.getDesignator().obj.getType().equals(ExtendedTab.intType)) {
	    		report_error("Greska: Pokusan decrement nad ne int promenljivom "+ designDecrement.getDesignator().obj.getName(), designDecrement);
	    	}
	    }
	    
	    @Override
	    public void visit (ThreeDesignList threeDesignList) {
	    	/*
	    	int objKind = designAssignExpr.getDesignator().obj.getKind();
	    	if(objKind != Obj.Elem && objKind != Obj.Var) {
	    		report_error("Greska: Pokusana dodela u promenljivu " + designAssignExpr.getDesignator().obj.getName() + " neadekvatnog tipa ", designAssignExpr);
	    	}
	    	else if(!designAssignExpr.getExpr().struct.assignableTo(designAssignExpr.getDesignator().obj.getType())) {
	    		report_error("Greska: Pokusana dodela neadekvatne vrednosti u promenljivu "+ designAssignExpr.getDesignator().obj.getName(), designAssignExpr);
	    	} */
	    	
	    	Obj destSet = threeDesignList.getDesignator().obj;
	    	Obj op1Set = threeDesignList.getDesignator1().obj;
	    	Obj op2Set = threeDesignList.getDesignator2().obj;
	    	
	    	boolean isDestSet = destSet.getType().equals(ExtendedTab.setType);
	        boolean isOp1Set = op1Set.getType().equals(ExtendedTab.setType);
	        boolean isOp2Set = op2Set.getType().equals(ExtendedTab.setType);
	    	if(isDestSet && isOp1Set && isOp2Set) {
	    		report_info("Uspesna operacija unije skupova na liniji " + threeDesignList.getLine(), null);
	    	}
	    	else {
	    		if (!isDestSet) {
	                report_error("Greska na liniji " + threeDesignList.getLine() + ": Simbol '" + destSet.getName() + "' nije tipa 'set'.", null);
	            }
	            if (!isOp1Set) {
	                report_error("Greska na liniji " + threeDesignList.getLine() + ": Simbol '" + op1Set.getName() + "' nije tipa 'set' i ne moze se koristiti u operaciji unije.", null);
	            }
	            if (!isOp2Set) {
	                report_error("Greska na liniji " + threeDesignList.getLine() + ": Simbol '" + op2Set.getName() + "' nije tipa 'set' i ne moze se koristiti u operaciji unije.", null);
	            }
	    	}
	    }
	    
	    //Statement
	    
	    @Override
	    public void visit(ReadStmt readStmt) {
	    	Obj designatorObj = readStmt.getDesignator().obj;
	    	
	        int kind = designatorObj.getKind();
	        if (kind != Obj.Var && kind != Obj.Elem) {
	            report_error("Greska na liniji " + readStmt.getLine() + ": Simbol '" + designatorObj.getName() + "' nije promenljiva ili element niza, te se u njega ne moze ucitavati vrednost.", null);
	            return; //prekidam dalju proveru jer je osnovni uslov pao
	        }
	        // Proveravamo drugi uslov: da li je tip designatora jedan od dozvoljenih (int, char, bool).
	        Struct desType = designatorObj.getType();
	        if (!desType.equals(ExtendedTab.intType) && !desType.equals(ExtendedTab.charType) && !desType.equals(ExtendedTab.boolType)) {
	            report_error("Greska na liniji " + readStmt.getLine() + ": U 'read' naredbi se moze koristiti samo promenljiva (ili element niza) tipa int, char ili bool.", null);
	        } else {
	            // Ako su oba uslova zadovoljena, sve je u redu.
	            report_info("Uspesno obradjena 'read' naredba na liniji " + readStmt.getLine(), null);
	        }
	    }
	    
	    @Override
	    public void visit(PrintStmt printStmt) {
	        // cvor Expr je vec obradjen pre nego sto smo stigli ovde pa dohvatamo njegov tip (struct)
	        Struct exprType = printStmt.getExpr().struct;

	        // Proveravamo da li je tip jedan od dozvoljenih tipova.
	        if (exprType.equals(ExtendedTab.intType) || exprType.equals(ExtendedTab.charType) || exprType.equals(ExtendedTab.boolType) || exprType.equals(ExtendedTab.setType)) {
	            
	            report_info("Uspesno obradjena 'print' naredba na liniji " + printStmt.getLine(), null);

	        } else {
	            report_error("Greska na liniji " + printStmt.getLine() + ": 'print' naredba se moze koristiti samo za tipove int, char, bool ili set.", null);
	        }
	    }
	    
	    @Override
	    public void visit(PrintStmt1 printStmt1) {
	    	
	        Struct exprType = printStmt1.getExpr().struct;
	        
	        if (exprType.equals(ExtendedTab.intType) || exprType.equals(ExtendedTab.charType) || exprType.equals(ExtendedTab.boolType) || exprType.equals(ExtendedTab.setType)) {

	            report_info("Uspesno obradjena 'print' naredba sa sirinom ispisa na liniji " + printStmt1.getLine(), null);

	        } else {
	            report_error("Greska na liniji " + printStmt1.getLine() + ": 'print' naredba se moze koristiti samo za tipove int, char, bool ili set.", null);
	        }
	        
	    }
	    
	    @Override
	    public void visit(ReturnStmt returnStmt) {
	    	returnFound = true;
	        //Provera da li se 'return' nalazi unutar metode (uslov 3 ovog kont uslova)
	        if (currentMethod == null) {
	            report_error("Greska na liniji " + returnStmt.getLine() + ": 'return' naredba se ne sme nalaziti izvan tela metode.", null);
	            return; // Prekidamo dalje provere
	        }

	        ExprOption exprOption = returnStmt.getExprOption();
	        Struct methodReturnType = currentMethod.getType();

	        // Proveravamo da li je 'return;' (bez izraza)
	        if (exprOption instanceof NoExprStmt) {
	            // Ako izraz nedostaje, metoda mora biti void (uslov 2)
	            if (!methodReturnType.equals(ExtendedTab.noType)) {
	                report_error("Greska na liniji " + returnStmt.getLine() + ": Metoda '" + currentMethod.getName() + "' nije void i mora vratiti vrednost.", null);
	            }
	        } 
	        // Proveravamo da li je 'return Expr;' (sa izrazom)
	        else if (exprOption instanceof ExprOptionStmt) {
	            Struct returnExprType = ((ExprOptionStmt) exprOption).getExpr().struct;

	            // Provera da li void metoda pokušava da vrati vrednost
	            if (methodReturnType.equals(ExtendedTab.noType)) {
	                report_error("Greska na liniji " + returnStmt.getLine() + ": Metoda '" + currentMethod.getName() + "' je void i ne sme vracati vrednost.", null);
	            } 
	            // Tip izraza mora odgovarati povratnom tipu metode (uslov 1)
	            else if (!returnExprType.equals(methodReturnType)) {
	                report_error("Greska na liniji " + returnStmt.getLine() + ": Tip povratne vrednosti ne odgovara povratnom tipu metode '" + currentMethod.getName() + "'.", null);
	            }
	        }
	    }
	    

}
