package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;

public class ExtendedTab extends Tab {

	public static final Struct boolType = new Struct(Struct.Bool);

	public static final Struct setType = new Struct(Struct.Class);
	public static final Struct ArrayOfIntsType = new Struct(Struct.Array, Tab.intType);
	// public static final Struct arrayIntType = new Struct(Struct.Array,
	// Tab.intType);

	public static void init() {
		Tab.init();

		Tab.currentScope.addToLocals(new Obj(Obj.Type, "bool", boolType));
		Tab.currentScope.addToLocals(new Obj(Obj.Type, "set", setType));
		Struct ArrayOfIntsType = new Struct(Struct.Array, Tab.intType);

		// void add(set s, int elem)
		Obj addMeth = insert(Obj.Meth, "add", Tab.noType);
		// addMeth.setAdr(1);
		openScope();
		insert(Obj.Var, "s", setType);
		insert(Obj.Var, "elem", Tab.intType);
		addMeth.setLocals(currentScope.getLocals());
		addMeth.setLevel(2);
		// Tab.chainLocalSymbols(addMeth);
		closeScope();

		// void addAll(set s, int[] arr)
		Obj addAllMeth = insert(Obj.Meth, "addAll", Tab.noType);
		// addAllMeth.setAdr(2);
		openScope();
		insert(Obj.Var, "s", setType);
		insert(Obj.Var, "arr", ArrayOfIntsType);
		addAllMeth.setLocals(currentScope.getLocals());
		// Tab.chainLocalSymbols(addAllMeth);
		addAllMeth.setLevel(2);
		closeScope();

		// set union(set s1, set s2)
		Obj unionMeth = insert(Obj.Meth, "union", setType);
		// unionMeth.setAdr(3); // Fiksna adresa za 'union'
		openScope();
		insert(Obj.Var, "s1", setType);
		insert(Obj.Var, "s2", setType);
		unionMeth.setLocals(currentScope.getLocals());
		unionMeth.setLevel(2);
		// Tab.chainLocalSymbols(unionMeth);
		closeScope();

		// void printSet(set s)
		Obj printSetMeth = insert(Obj.Meth, "printSet", Tab.noType);
		// printSetMeth.setAdr(4); // Fiksna adresa za 'printSet'
		openScope();
		insert(Obj.Var, "s", setType).setFpPos(1);
		printSetMeth.setLocals(currentScope.getLocals());
		printSetMeth.setLevel(1);
		// Tab.chainLocalSymbols(printSetMeth);
		closeScope();

		// int sum(set s)
		Obj sumMeth = insert(Obj.Meth, "sum", Tab.intType);
		openScope();
		insert(Obj.Var, "s", setType);
		sumMeth.setLocals(currentScope.getLocals());
		sumMeth.setLevel(1);
		closeScope();

		// bool overlap(s1, s2)
		Obj overlapMeth = insert(Obj.Meth, "overlap", boolType);
		openScope();
		insert(Obj.Var, "s1", setType);
		insert(Obj.Var, "s2", setType);
		overlapMeth.setLocals(currentScope.getLocals());
		overlapMeth.setLevel(2);
		closeScope();

	}

}
