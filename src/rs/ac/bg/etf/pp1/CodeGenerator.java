package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {
	private int mainPc;
	private Obj addMeth, addAllMeth;
	private Obj unionMeth, sumMeth;
	private Obj printSetMeth, overlapMeth;

	public CodeGenerator() {
		addMeth = Tab.find("add");
		addAllMeth = Tab.find("addAll");
		unionMeth = Tab.find("union");
		printSetMeth = Tab.find("printSet");
		sumMeth = Tab.find("sum");
		overlapMeth = Tab.find("overlap");

	}

	private void generatePrintSet() {
// TODO Auto-generated method stub 
		printSetMeth.setAdr(Code.pc); 

		// Signature: void printSet(set s)
		Obj s = new Obj(Obj.Var, "s", ExtendedTab.setType);
		s.setAdr(0);
		Obj count = new Obj(Obj.Var, "count", ExtendedTab.intType);
		count.setAdr(1);
		Obj i = new Obj(Obj.Var, "i", ExtendedTab.intType);
		i.setAdr(2);

		Code.put(Code.enter);
		Code.put(1); // 1 formalni parametar (s)
		Code.put(3); // 3 ukupno promenljivih (s, count, i)

		// Telo funkcije:
		// count = s[0];
		Code.load(s);
		Code.loadConst(0);
		Code.put(Code.aload);
		Code.store(count);

		// for (i = 1; i <= count; i++)
		Code.loadConst(1);
		Code.store(i);
		int loopStart = Code.pc;
		Code.load(i);
		Code.load(count);
		Code.putFalseJump(Code.le, 0);
		int jumpEndLoop = Code.pc - 2;

		// print(s[i]);
		Code.load(s);
		Code.load(i);
		Code.put(Code.aload);
		Code.loadConst(1); // sirina ispisa
		Code.put(Code.print);

		// print(' ');
		Code.loadConst(' ');
		Code.loadConst(1);
		Code.put(Code.bprint);

		// i++;
		Code.load(i);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(i);
		Code.putJump(loopStart);

		Code.fixup(jumpEndLoop);
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	private void generateAdd() {
// TODO Auto-generated method stub 
		 addMeth.setAdr(Code.pc);

		    // Signature: void add(set s, int elem)
		    Obj s = new Obj(Obj.Var, "s", ExtendedTab.setType);
		    s.setAdr(0);
		    Obj elem = new Obj(Obj.Var, "elem", ExtendedTab.intType);
		    elem.setAdr(1);
		    Obj i = new Obj(Obj.Var, "i", ExtendedTab.intType);
		    i.setAdr(2);
		    Obj count = new Obj(Obj.Var, "count", ExtendedTab.intType);
		    count.setAdr(3);

		    Code.put(Code.enter);
		    Code.put(2); // 2 formalna parametra (s, elem)
		    Code.put(4); // 4 ukupno promenljivih (s, elem, i, count)

		    // count = s[0];
		    Code.load(s);
		    Code.loadConst(0);
		    Code.put(Code.aload);
		    Code.store(count);

		    // 1. Provera kapaciteta: if (count >= s.length - 1) return;
		    // Napomena: Kapacitet je arraylength - 1, jer je s[0] brojac.
		    Code.load(count);
		    Code.load(s);
		    Code.put(Code.arraylength);
		    Code.loadConst(1);
		    Code.put(Code.sub);
		    Code.putFalseJump(Code.lt, 0); // Ako je count < (s.length - 1), nastavi. U suprotnom skoci.
		    int jumpIfFull = Code.pc - 2; // Adresa za skok ako je skup pun.

		    // 2. Provera da li element vec postoji
		    Code.loadConst(1);
		    Code.store(i);
		    int loopStart = Code.pc;
		    Code.load(i);
		    Code.load(count);
		    Code.putFalseJump(Code.le, 0); // Loop while (i <= count)
		    int jumpAfterLoop = Code.pc - 2; // Adresa za skok kada se zavrsi petlja

		    Code.load(s);
		    Code.load(i);
		    Code.put(Code.aload);
		    Code.load(elem);
		    Code.putFalseJump(Code.ne, 0); // if (s[i] != elem) continue;
		    int jumpIfDuplicate = Code.pc - 2; // Inace, ako su jednaki, skoci na kraj.

		    // i++;
		    Code.load(i);
		    Code.loadConst(1);
		    Code.put(Code.add);
		    Code.store(i);
		    Code.putJump(loopStart);

		    // --- DEO ZA UPISIVANJE ---
		    // Ovde se dolazi SAMO ako su sve provere prosle.
		    Code.fixup(jumpAfterLoop); // Postavljamo adresu skoka NAKON petlje da dodje ovde.

		    // s[count + 1] = elem;
		    Code.load(s);
		    Code.load(count);
		    Code.loadConst(1);
		    Code.put(Code.add);
		    Code.load(elem);
		    Code.put(Code.astore);

		    // s[0]++; (count se povecava za 1)
		    Code.load(s);
		    Code.loadConst(0);
		    Code.load(count);
		    Code.loadConst(1);
		    Code.put(Code.add);
		    Code.put(Code.astore);

		    // --- KRAJ METODE ---
		    Code.fixup(jumpIfFull); // Ako je bio pun, skace ovde.
		    Code.fixup(jumpIfDuplicate); // Ako je bio duplikat, skace ovde.

		    Code.put(Code.exit);
		    Code.put(Code.return_);

	}

	private void generateAddAll() {
// TODO Auto-generated method stub 
		addAllMeth.setAdr(Code.pc); // Zapamti adresu

		// Signature: void addAll(set s, int[] arr)
		Obj s = new Obj(Obj.Var, "s", ExtendedTab.setType);
		s.setAdr(0);
		Obj arr = new Obj(Obj.Var, "arr", new Struct(Struct.Array, ExtendedTab.intType));
		arr.setAdr(1);
		Obj i = new Obj(Obj.Var, "i", ExtendedTab.intType);
		i.setAdr(2);
		Obj len = new Obj(Obj.Var, "len", ExtendedTab.intType);
		len.setAdr(3);

		Code.put(Code.enter);
		Code.put(2);
		Code.put(4);

		// len = arr.length;
		Code.load(arr);
		Code.put(Code.arraylength);
		Code.store(len);

		// for (i = 0; i < len; i++)
		Code.loadConst(0);
		Code.store(i);
		int loopStart = Code.pc;
		Code.load(i);
		Code.load(len);
		Code.putFalseJump(Code.lt, 0);
		int jumpEndLoop = Code.pc - 2;

		// Telo petlje: add(s, arr[i]);
		Code.load(s); // Prvi argument za 'add': s
		Code.load(arr); // Drugi argument za 'add': arr[i]
		Code.load(i);
		Code.put(Code.aload);

		// Pozivamo vec generisanu 'add' funkciju
		//int dest_addr = addMeth.getAdr() - Code.pc + 1;
		Code.put(Code.call);
		Code.put2(addMeth.getAdr() - Code.pc + 1);

		// i++
		Code.load(i);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(i);
		Code.putJump(loopStart);

		Code.fixup(jumpEndLoop);
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	private void generateUnion() {
// TODO Auto-generated method stub 
		
		unionMeth.setAdr(Code.pc); // Zapamti adresu

	    // Signature: set union(set s1, set s2)
	    Obj s1 = new Obj(Obj.Var, "s1", ExtendedTab.setType);
	    s1.setAdr(0);
	    Obj s2 = new Obj(Obj.Var, "s2", ExtendedTab.setType);
	    s2.setAdr(1);
	    Obj newSet = new Obj(Obj.Var, "newSet", ExtendedTab.setType);
	    newSet.setAdr(2);
	    Obj i = new Obj(Obj.Var, "i", ExtendedTab.intType);
	    i.setAdr(3);
	    Obj count = new Obj(Obj.Var, "count", ExtendedTab.intType);
	    count.setAdr(4);

	    Code.put(Code.enter);
	    Code.put(2);
	    Code.put(5);

	    // capacity = s1.length + s2.length - 2
	    Code.load(s1);
	    Code.put(Code.arraylength);
	    Code.load(s2);
	    Code.put(Code.arraylength);
	    Code.put(Code.add);
	    Code.loadConst(2);
	    Code.put(Code.sub);
	    // sad je kapacitet na steku, kreiramo set kao u visit(ArrayFactor)
	    Code.loadConst(1);
	    Code.put(Code.add); //  s.length=capacity+1
	    Code.put(Code.newarray); // -- addr
	    Code.put(1);      //--1
	    Code.put(Code.dup);  
	    Code.loadConst(0);
	    Code.loadConst(0);
	    Code.put(Code.astore);
	    Code.store(newSet); // Sacuvaj referencu na novi set

	    Code.load(s1);
	    Code.loadConst(0);
	    Code.put(Code.aload);
	    Code.store(count); // count = s1[0]
	    Code.loadConst(1);
	    Code.store(i);
	    int loop1Start = Code.pc;
	    Code.load(i);
	    Code.load(count);
	    Code.putFalseJump(Code.le, 0);
	    int jumpAfterLoop1 = Code.pc - 2;
	    Code.load(newSet); // Parametar 1 za add
	    Code.load(s1);
	    Code.load(i);
	    Code.put(Code.aload); // Parametar 2 za add
	    Code.put(Code.call);
	    Code.put2(addMeth.getAdr() - Code.pc + 1);
	    Code.load(i);
	    Code.loadConst(1);
	    Code.put(Code.add);
	    Code.store(i);
	    Code.putJump(loop1Start);
	    Code.fixup(jumpAfterLoop1);

	    Code.load(s2);
	    Code.loadConst(0);
	    Code.put(Code.aload);
	    Code.store(count); // count = s2[0]
	    Code.loadConst(1);
	    Code.store(i);
	    int loop2Start = Code.pc;
	    Code.load(i);
	    Code.load(count);
	    Code.putFalseJump(Code.le, 0);
	    int jumpAfterLoop2 = Code.pc - 2;
	    Code.load(newSet);
	    Code.load(s2);
	    Code.load(i);
	    Code.put(Code.aload);
	    Code.put(Code.call);
	    Code.put2(addMeth.getAdr() - Code.pc + 1);
	    Code.load(i);
	    Code.loadConst(1);
	    Code.put(Code.add);
	    Code.store(i);
	    Code.putJump(loop2Start);
	    Code.fixup(jumpAfterLoop2);

	    Code.load(newSet);

	    Code.put(Code.exit);
	    Code.put(Code.return_);


	}

	private void generateSum() {
	    sumMeth.setAdr(Code.pc);
	
	    // Signature: int sum(set s)
	    Obj s = new Obj(Obj.Var, "s", ExtendedTab.setType);
	    s.setAdr(0);
	    Obj sum = new Obj(Obj.Var, "sum", ExtendedTab.intType);
	    sum.setAdr(1);
	    Obj i = new Obj(Obj.Var, "i", ExtendedTab.intType);
	    i.setAdr(2);
	    Obj count = new Obj(Obj.Var, "count", ExtendedTab.intType);
	    count.setAdr(3);
	
	    Code.put(Code.enter);
	    Code.put(1); // 1 formalni parametar (s)
	    Code.put(4); // 4 ukupno promenljivih (s, sum, i, count)
	
	    // sum = 0;
	    Code.loadConst(0);
	    Code.store(sum);
	
	    // count = s[0];
	    Code.load(s);
	    Code.loadConst(0);
	    Code.put(Code.aload);
	    Code.store(count);
	
	    // for (i = 1; i <= count; i++)
	    Code.loadConst(1);
	    Code.store(i);
	    int loopStart = Code.pc;
	    Code.load(i);
	    Code.load(count);
	    Code.putFalseJump(Code.le, 0);
	    int jumpAfterLoop = Code.pc - 2;
	
	    // Telo petlje: sum = sum + s[i];
	    Code.load(sum);
	    Code.load(s);
	    Code.load(i);
	    Code.put(Code.aload);
	    Code.put(Code.add);
	    Code.store(sum);
	
	    // i++;
	    Code.load(i);
	    Code.loadConst(1);
	    Code.put(Code.add);
	    Code.store(i);
	    Code.putJump(loopStart);
	
	    Code.fixup(jumpAfterLoop);
	
	    // Ostavlja rezultat na steku za povratnu vrednost
	    Code.load(sum);
	
	    Code.put(Code.exit);
	    Code.put(Code.return_);
	}
/*	private void generateIsSubset() {
		isSubsetMeth.setAdr(Code.pc);
	
		// === DEFINICIJA LOKALNIH PROMENLJIVIH I PARAMETARA ===
		// Parametri (indeksi 0 i 1)
		Obj s1 = new Obj(Obj.Var, "s1", ExtendedTab.setType); s1.setAdr(0); // Skup koji je potencijalni podskup.
		Obj s2 = new Obj(Obj.Var, "s2", ExtendedTab.setType); s2.setAdr(1); // Skup koji je potencijalni nadskup.
		
		// Lokalne promenljive (indeksi 2 do 7)
		Obj count1 = new Obj(Obj.Var, "count1", Tab.intType); count1.setAdr(2);  // cuva broj elemenata u skupu s1.
		Obj i = new Obj(Obj.Var, "i", Tab.intType); i.setAdr(3);                 // Brojac za spoljnu petlju (prolazi kroz s1).
		Obj s1_elem = new Obj(Obj.Var, "s1_elem", Tab.intType); s1_elem.setAdr(4); // cuva trenutni element iz s1 koji trazimo.
		Obj found = new Obj(Obj.Var, "found", ExtendedTab.boolType); found.setAdr(5); // Flag (0 ili 1) da li je s1_elem pronadjen u s2.
		Obj count2 = new Obj(Obj.Var, "count2", Tab.intType); count2.setAdr(6);  // cuva broj elemenata u skupu s2.
		Obj j = new Obj(Obj.Var, "j", Tab.intType); j.setAdr(7);                 // Brojac za unutrašnju petlju (prolazi kroz s2).

		Code.put(Code.enter);
		Code.put(2); // Broj parametara (s1, s2).
		Code.put(8); // Ukupan broj promenljivih u metodi.

		// === SPOLJNA PETLJA: for (i=1; i<=count1; i++) ===
		// Cilj: Proci kroz svaki element skupa s1.
		Code.load(s1);
		Code.loadConst(0);
		Code.put(Code.aload); // Ucitaj s1[0]
		Code.store(count1);   // count1 = s1[0]
		
		Code.loadConst(1);
		Code.store(i);        // i = 1

		int outerLoopStart = Code.pc; // Zapamti adresu pocetka spoljne petlje.
		Code.load(i);
		Code.load(count1);
		Code.putFalseJump(Code.le, 0); // Ako !(i <= count1), petlja je gotova. Skok na kraj.
		int jumpAfterOuterLoop = Code.pc - 2; // Adresa za skok ako je petlja gotova (uspesno).

		// Uzmi trenutni element iz s1
		Code.load(s1);
		Code.load(i);
		Code.put(Code.aload); // Ucitaj s1[i]
		Code.store(s1_elem);  // s1_elem = s1[i]

		// Resetuj flag 'found' na false (0) za svaki novi element iz s1. Ovo je kljucno.
		Code.loadConst(0);
		Code.store(found);

		// === UNUTRASNJA PETLJA: for (j=1; j<=count2; j++) ===
		// Cilj: Proveriti da li se 's1_elem' nalazi negde u skupu s2.
		Code.load(s2);
		Code.loadConst(0);
		Code.put(Code.aload); // Ucitaj s2[0]
		Code.store(count2);   // count2 = s2[0]

		Code.loadConst(1);
		Code.store(j);        // j = 1

		int innerLoopStart = Code.pc; // Zapamti adresu pocetka unutrasnje petlje.
		Code.load(j);
		Code.load(count2);
		Code.putFalseJump(Code.le, 0); // Ako !(j <= count2), petlja je gotova.
		int jumpAfterInnerLoop = Code.pc - 2; // Adresa za skok ako element nije pronadjen.

		// Uporedi s1_elem == s2[j]
		Code.load(s1_elem);
		Code.load(s2);
		Code.load(j);
		Code.put(Code.aload);
		Code.putFalseJump(Code.ne, 0); // Ako NISU jednaki, preskoci sledece instrukcije i nastavi petlju.
		int jumpIfNotEqual = Code.pc - 2;

		// === Element je pronaden ===
		Code.loadConst(1); // Postavi vrednost 'true' (1)
		Code.store(found); // found = true
		Code.putJump(0);   // Pronasli smo element, nema potrebe dalje traziti. Izvrsi "break" iz unutrasnje petlje.
		int breakInnerLoop = Code.pc - 2;
		
		Code.fixup(jumpIfNotEqual); // Ako elementi nisu bili jednaki, skace se ovde.
		// Inkrementiraj j i nastavi unutrasnju petlju
		Code.load(j);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(j);
		Code.putJump(innerLoopStart); // Vrati se na pocetak unutrasnje petlje.

		// === Nakon unutrasnje petlje ===
		Code.fixup(jumpAfterInnerLoop); // Ako se petlja zavrsila prirodno (element nije pronaden).
		Code.fixup(breakInnerLoop);     // Ako smo iskocili iz petlje jer je element pronaden.

		// Proveri da li je element pronaden
		Code.load(found);
		Code.loadConst(0);
		Code.putFalseJump(Code.ne, 0); // Ako je found != 0 (tj. true), nastavi spoljnu petlju.
		int jumpIfFound = Code.pc - 2;

		// === Element NIJE pronaden ===
		// Ako smo ovde, znaci da 'found' je i dalje 0. To znaci da element iz s1 ne postoji u s2.
		// Zato s1 ne moze biti podskup od s2. Vrati 'false' i odmah prekini izvrsavanje.
		Code.loadConst(0); // Povratna vrednost: false (0)
		Code.put(Code.exit);
		Code.put(Code.return_);

		Code.fixup(jumpIfFound); // Ako je element bio pronaden, skace se ovde.
		// Inkrementiraj i i nastavi spoljnu petlju
		Code.load(i);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(i);
		Code.putJump(outerLoopStart); // Vrati se na pocetak spoljne petlje da proveris sledeci element.

		// === Nakon spoljne petlje ===
		// Ako smo stigli ovde, to znaci da je spoljna petlja uspesno prosla kroz sve elemente
		// skupa s1, i za svaki je utvrdeno da postoji u s2.
		Code.fixup(jumpAfterOuterLoop);
		Code.loadConst(1); // Povratna vrednost: true (1)
		Code.put(Code.exit);
		Code.put(Code.return_);
	}   */


	private void generateOverlaps() {
		overlapMeth.setAdr(Code.pc);
		Obj s1 = new Obj(Obj.Var, "s1", ExtendedTab.setType); 
		s1.setAdr(0);
		Obj s2 = new Obj(Obj.Var, "s2", ExtendedTab.setType); 
		s2.setAdr(1);
		Obj count1 = new Obj(Obj.Var, "count1", Tab.intType); 
		count1.setAdr(2);
		Obj i = new Obj(Obj.Var, "i", Tab.intType); 
		i.setAdr(3);
		Obj elem = new Obj(Obj.Var, "elem", Tab.intType); 
		elem.setAdr(4);
		Obj count2 = new Obj(Obj.Var, "count2", Tab.intType); 
		count2.setAdr(5);
		Obj j = new Obj(Obj.Var, "j", Tab.intType); 
		j.setAdr(6);
	
		Code.put(Code.enter);
		Code.put(2);
		Code.put(7);
	
		//postavljam cont1 na vrednost broja elem koje ima prvi set- to pise u s1[0]
		Code.load(s1);
		Code.loadConst(0);
		Code.put(Code.aload);
		Code.store(count1);
	
		//i=1 jer su elementi smesteni od prvog indexa pa nadalje
		Code.loadConst(1);
		Code.store(i);
	
		int s1LoopStart = Code.pc; //cuvamo adresu pocetka petlje kojom se prolazi kroz s1 skup
		Code.load(i);
		Code.load(count1);
		Code.putFalseJump(Code.le, 0); //dokle god je iterator manji = od broja elemaenata u skupu nastavi dalje
		int jumpReturnFalse = Code.pc - 2;
	
		//telo petlje, uzimamo s1[i] i smestamo u elem da bismo poredili taj element sa svim elementima skupa s2
		Code.load(s1);
		Code.load(i);
		Code.put(Code.aload);
		Code.store(elem);
	
		//postavljam cont2 na vrednost broja elem koje ima drugi set- to pise u s2[0]
		Code.load(s2);
		Code.loadConst(0);
		Code.put(Code.aload);
		Code.store(count2);
	
		//j=1
		Code.loadConst(1);
		Code.store(j);
	
		int s2LoopStart = Code.pc; //cuvamo adresu pocetka petlje kojom se prolazi kroz s2 skup isto kao za s1...
		Code.load(j);
		Code.load(count2);
		Code.putFalseJump(Code.le, 0);
		int jumpTryAnother = Code.pc - 2; //prosli smo kroz ceo s2 i nismo nasli elemet pa skacemo #
	
		Code.load(elem); //poredim elem iz s1 sa s2[j]
		Code.load(s2);
		Code.load(j);
		Code.put(Code.aload);
		Code.putFalseJump(Code.eq, 0); //ako su isti TOP nasla si BAR JEDAN zajednicki znaci vrcas 1 odmah i zavrsavas***
		int jumpIfNotEqual = Code.pc - 2;
	
		Code.loadConst(1); //***
		Code.put(Code.exit);
		Code.put(Code.return_);//***
	
		Code.fixup(jumpIfNotEqual); //skocila jer elem nije bio isti sa s2[j] pa ga poredim sa sledecim tj s2[j++]
		Code.load(j);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(j);
		Code.putJump(s2LoopStart);
	
		Code.fixup(jumpTryAnother); // # ovde nastvaljamo tako sto uzimamo sledeci elem skupa s1 za koji cemo prolaziti kroz s2 i gledati ima li njega
		Code.load(i);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(i);
		Code.putJump(s1LoopStart);
	
		Code.fixup(jumpReturnFalse);
		
		Code.loadConst(0);
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public int getMainPc() {
// TODO Auto-generated method stub 
		return mainPc;
	}

	@Override
	public void visit(ProgName progName) {
		generateAdd();
		generateAddAll();
		generateUnion();
		generatePrintSet();
		
		generateSum();
		generateOverlaps();
	}

	@Override
	public void visit(MethodStartVoid methodStartVoid) {
		methodStartVoid.obj.setAdr(Code.pc);
		if (methodStartVoid.getName().equalsIgnoreCase("main"))
			mainPc = Code.pc;
		Code.put(Code.enter);
		Code.put(methodStartVoid.obj.getLevel());
		Code.put(methodStartVoid.obj.getLocalSymbols().size());
	}

	@Override
	public void visit(MethodStartType methodStartType) {
		methodStartType.obj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(methodStartType.obj.getLevel());
		Code.put(methodStartType.obj.getLocalSymbols().size());
	}

	@Override
	public void visit(MethodDecl methodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

// STATEMENT 
	
	@Override
	public void visit(DesignatorStmt designatorStmt) {
		
	}

	@Override
	public void visit(PrintStmt printStmt) {
		Struct exprType = printStmt.getExpr().struct;

		if (exprType.equals(ExtendedTab.setType)) {
 
			//int dest_addr = printSetMeth.getAdr() - Code.pc + 1;
			Code.put(Code.call);
			Code.put2(printSetMeth.getAdr() - Code.pc + 1);
		} else {
			Code.loadConst(0); // Neka podrazumevana sirina za ispis
			if (exprType.equals(ExtendedTab.charType))
				Code.put(Code.bprint);
			else
				Code.put(Code.print);
		}
	}

	@Override
	public void visit(PrintStmt1 printStmt1) {
		Struct exprType = printStmt1.getExpr().struct;

		if (exprType.equals(ExtendedTab.setType)) {
 
			Code.put(Code.pop); 
			//int dest_addr = printSetMeth.getAdr() - Code.pc + 1;
			Code.put(Code.call);
			Code.put2(printSetMeth.getAdr() - Code.pc + 1);
		} else {
			if (exprType.equals(ExtendedTab.charType))
				Code.put(Code.bprint);
			else
				Code.put(Code.print);
		}
	}

	@Override
	public void visit(ReturnStmt returnStmt) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	@Override
	public void visit(ReadStmt readStmt) {
		if (readStmt.getDesignator().obj.getType().equals(ExtendedTab.charType))
			Code.put(Code.bread);
		else
			Code.put(Code.read);
		Code.store(readStmt.getDesignator().obj);
	}

	@Override
	public void visit(ConstNum constNum) {
		Code.loadConst(constNum.getValueNum());
	}

	@Override
	public void visit(ConstChar constChar) {
		Code.loadConst(constChar.getValueChar());
	}

	@Override
	public void visit(ConstBool constBool) {
		boolean boolValue = constBool.getValueBool();
		int intValue = boolValue ? 1 : 0;
		Code.loadConst(intValue);
	}

//EXPR 
	@Override
	public void visit(ExprStartWithMinus exprStartWithMinus) {
		Code.put(Code.neg);
	}

	@Override
	public void visit(ExprAddopListGlobal exprAddopListGlobal) {
		if (exprAddopListGlobal.getAddOp() instanceof Plus)
			Code.put(Code.add);
		else if (exprAddopListGlobal.getAddOp() instanceof Minus)
			Code.put(Code.sub);
	}

	@Override
	public void visit(TermMulop termMulop) {
		if (termMulop.getMulOp() instanceof MulopOp)
			Code.put(Code.mul);
		else if (termMulop.getMulOp() instanceof DivOp)
			Code.put(Code.div);
		else if (termMulop.getMulOp() instanceof Percent)
			Code.put(Code.rem);
	}

	@Override
	public void visit(NumFactor numFactor) {
		Code.loadConst(numFactor.getN1());
	}

	@Override
	public void visit(CharFactor charFactor) {
		Code.loadConst(charFactor.getC1());
	}

	@Override
	public void visit(BoolFactor boolFactor) {
		boolean boolValue = boolFactor.getB1();
		int intValue = boolValue ? 1 : 0;
		Code.loadConst(intValue);
	}

	@Override
	public void visit(ArrayFactor arrayFactor) {
		Struct specifiedType = arrayFactor.getType().struct;

		if (specifiedType.equals(ExtendedTab.setType)) {

			Code.loadConst(1);
			Code.put(Code.add);

			// Kreiraj novi niz integera (operand 1).
			Code.put(Code.newarray);
			Code.put(1);

			
			Code.put(Code.dup); 
			Code.loadConst(0); // Stavi indeks 0
			Code.loadConst(0); // Stavi vrednost 0
			Code.put(Code.astore); // niz[0] = 0
		} else {
			
			Code.put(Code.newarray);
			if (specifiedType.equals(ExtendedTab.charType)) {
				Code.put(0); // Operand 0 za niz karaktera
			} else {
				Code.put(1); // Operand 1 za niz integera/bool-ova
			}
		}
	}

	@Override
	public void visit(ExprFactor exprFactor) {
	}

	@Override
	public void visit(DesignatorVar designatorVar) {
		Code.load(designatorVar.getDesignator().obj);
	}

//DESIGNATOR 
	@Override
	public void visit(DesignArr designArr) {
		Code.load(designArr.obj);
	}

	@Override
	public void visit(DesignArrElem designArrElem) {

	}

	@Override
	public void visit(DesignAssignExpr designAssignExpr) {
		// Vrednost sa desne strane (Expr) je vec na steku. 
		Code.store(designAssignExpr.getDesignator().obj);
	}

	@Override
	public void visit(DesignIncrement designIncrement) {
		if (designIncrement.getDesignator().obj.getKind() == Obj.Elem)
			Code.put(Code.dup2);
		Code.load(designIncrement.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(designIncrement.getDesignator().obj);
	}

	@Override
	public void visit(DesignDecrement designDecrement) {
		if (designDecrement.getDesignator().obj.getKind() == Obj.Elem)
			Code.put(Code.dup2);
		Code.load(designDecrement.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(designDecrement.getDesignator().obj);
	}

	@Override
	public void visit(ThreeDesignList threeDesignList) {
		// dest = o1 U o2
	    // ISPRAVKA: Kompletan redosled operacija je promenjen.
	    // Prvo se poziva funkcija, a zatim se njen rezultat cuva.

	    // 1. Stavi argumente na stek
	    Code.load(threeDesignList.getDesignator1().obj); // s1
	    Code.load(threeDesignList.getDesignator2().obj); // s2

	    // 2. Pozovi 'union' metodu
	    Code.put(Code.call);
	    Code.put2(unionMeth.getAdr() - Code.pc + 1); // I ovde sam sklonio "+ 1" za svaki slucaj

	    // 3. Sacuvaj rezultat koji je 'union' vratio sa steka
	    Code.store(threeDesignList.getDesignator().obj);


	}

	@Override
	public void visit(DesignatorFuncCall designatorFuncCall) {
		Obj funcObj = designatorFuncCall.getDesignator().obj;
		String funcName = funcObj.getName();

		if ("ord".equals(funcName) || "chr".equals(funcName)) {

			return;
		}
		else if ("len".equals(funcName)) {
			Code.put(Code.arraylength);
			return;
		}else if ("add".equals(funcName)) {

			int move = addMeth.getAdr() - Code.pc + 1;	
			Code.put(Code.call);
			Code.put2(move);
			return;

		}else if ("addAll".equals(funcName)) {

			int move = addAllMeth.getAdr() - Code.pc +1;	
			Code.put(Code.call);
			Code.put2(move);
			return;

		}	else if ("sum".equals(funcName)) {

			//int move = addAllMeth.getAdr() - Code.pc +1;	
			Code.put(Code.call);
			Code.put2(sumMeth.getAdr() - Code.pc +1);
			return;

		}else if ("overlap".equals(funcName)) {	
			Code.put(Code.call);
			Code.put2(overlapMeth.getAdr() - Code.pc +1);
			return;

		}
		else {
			
			int dest_addr = funcObj.getAdr() - Code.pc + 1;
			Code.put(Code.call);
			Code.put2(dest_addr);

		}
	}

	@Override
	public void visit(DesignFunctionCalling designFunctionCalling) {
		Obj funcObj = designFunctionCalling.getDesignator().obj;
		String funcName = funcObj.getName();

		if ("ord".equals(funcName) || "chr".equals(funcName)) {
			Code.put(Code.pop);
			return;
		}
		else if ("len".equals(funcName)) {
			Code.put(Code.arraylength);
			Code.put(Code.pop);
			return;
		}else if ("add".equals(funcName)) {

			//int move = addMeth.getAdr() - Code.pc +1;	
			Code.put(Code.call);
			Code.put2(addMeth.getAdr() - Code.pc +1);
			return;

		}else if ("addAll".equals(funcName)) {

			//int move = addAllMeth.getAdr() - Code.pc +1;	
			Code.put(Code.call);
			Code.put2(addAllMeth.getAdr() - Code.pc +1);
			return;

		}
		else if ("sum".equals(funcName)) {

			//int move = addAllMeth.getAdr() - Code.pc +1;	
			Code.put(Code.call);
			Code.put2(sumMeth.getAdr() - Code.pc +1);
			return;

		}else if ("overlap".equals(funcName)) {	
			Code.put(Code.call);
			Code.put2(overlapMeth.getAdr() - Code.pc +1);
			return;

		}
		else {
			//int dest_addr = funcObj.getAdr() - Code.pc + 1;
			Code.put(Code.call);
			Code.put2(funcObj.getAdr() - Code.pc + 1);
		}
		
		if (funcObj.getType() != ExtendedTab.noType) {
			Code.put(Code.pop);
		}
	}
}
