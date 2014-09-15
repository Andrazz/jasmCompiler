package jasmCompiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

//import java.util.Scanner;		// ce debugiramo z branjem indexov za konstante ...

public class Compiler {

	private static String filename;

	private static File source;
	private static File classSource;
	private static File sourceBackup;
	private static File sourceRemoved;
	private static File sourceLocated1;
	private static File sourceLocated2;
	private static File sourceReplaced1;
	private static File sourceReplaced2;
	private static File classRemoved;
	private static File classLocated1;
	private static File classLocated2;
	private static File classReplaced1;
	private static File classReplaced2;
	private static File tempFile;

	private static HashMap<Integer, HashMap<Integer, String>> jasm = new HashMap<Integer, HashMap<Integer, String>>();
	private static HashMap<Integer, HashMap<Integer, String[]>> optimizedJasm = new HashMap<Integer, HashMap<Integer, String[]>>();
	private static HashMap<Integer, LinkedList<Integer>> linearizedJasm = new HashMap<Integer, LinkedList<Integer>>();
	private static HashMap<Integer, Integer> blockSize = new HashMap <Integer, Integer>();
	private static HashMap<Integer, int[]> codeBlockLocation = new HashMap<Integer, int[]>();

	private static ConstantPool cpoolRemoved = new ConstantPool();
	private static ConstantPool cpoolLocated = new ConstantPool();
	private static ConstantPool cpoolReplaced = new ConstantPool();

	private static LinkedList<Integer> preMethodsCode = new LinkedList<Integer>();
	private static LinkedList<Integer> methodRemoved = new LinkedList<Integer>();
	private static LinkedList<Integer> methodLocated1 = new LinkedList<Integer>();
	private static LinkedList<Integer> methodLocated2 = new LinkedList<Integer>();
	private static LinkedList<Integer> methodReplaced1 = new LinkedList<Integer>();
	private static LinkedList<Integer> methodReplaced2 = new LinkedList<Integer>();
	private static LinkedList<Integer> postMethodsCode = new LinkedList<Integer>();

	public static void main(String[] args) throws IOException {
		checkParam(args);
		createFiles();
		createTempFile();

/*
for(Integer i : jasm.keySet()) {
	for(Integer j : jasm.get(i).keySet()) {
		System.out.println("Blok "+i+" lineNumber "+j+": "+jasm.get(i).get(j));
	}
}
*/

		source.renameTo(sourceBackup);			// ustvarimo kopijo prvotne kode

		adjustTempFile(sourceRemoved, 0);
		adjustTempFile(sourceLocated1, 1);
		adjustTempFile(sourceLocated2, 2);

		sourceRemoved.renameTo(source);			// preimenujemo, da lahko kompajlamo
		compile(source);
		classSource.renameTo(classRemoved);
		source.renameTo(sourceRemoved);			// preimenujemo, nazaj

		sourceLocated1.renameTo(source);			// preimenujemo, da lahko kompajlamo
		compile(source);
		classSource.renameTo(classLocated1);
		source.renameTo(sourceLocated1);			// preimenujemo, nazaj

		sourceLocated2.renameTo(source);			// preimenujemo, da lahko kompajlamo
		compile(source);
		classSource.renameTo(classLocated2);
		source.renameTo(sourceLocated2);			// preimenujemo, nazaj

		parseByteCode(classRemoved, false, methodRemoved, cpoolRemoved);

		parseByteCode(classLocated1, false, methodLocated1, cpoolLocated);
		parseByteCode(classLocated2, false, methodLocated2, null);

		checkMethodCode();




// ***************************** * BEBUG * ******************************
/*System.out.println("+++++++++++++++++++++++++++++++++");
for(int i : jasm.keySet()) {
	for(int j : jasm.get(i).keySet()) {
		System.out.print(jasm.get(i).get(j));
	}
	System.out.println();
}		
System.out.println();
for(int i : optimizedJasm.keySet()) {
	for(int j : optimizedJasm.get(i).keySet()) {
		for(String s : optimizedJasm.get(i).get(j)) {
			System.out.print(i+":"+j+":"+s+" ");
		}
	}
	System.out.println();
}
System.out.println("+++++++++++++++++++++++++++++++++");
for(int i : blockSize.keySet())
	System.out.println("Block "+i+" size = "+blockSize.get(i));
*/
// ***************************** * BEBUG * ****************************** 
//System.out.println();System.out.println("***********************************************");for(int x=0; x<methodRemoved.size(); x++) {System.out.print(Integer.toHexString(methodRemoved.get(x))+" ");if((x+1)%8 == 0) {System.out.println();}}System.out.println();System.out.println("***********************************************");System.out.println();Scanner in = new Scanner(System.in);while(true){int num = in.nextInt();System.out.println("!!! "+num);if(num==0) break;cpoolLocated.printConst(num);}in.close();		





		adjustTempFile(sourceReplaced1, 7);
		adjustTempFile(sourceReplaced2, -7);

		sourceReplaced1.renameTo(source);			// preimenujemo, da lahko kompajlamo
		compile(source);
		classSource.renameTo(classReplaced1);
		source.renameTo(sourceReplaced1);			// preimenujemo, nazaj

		sourceReplaced2.renameTo(source);			// preimenujemo, da lahko kompajlamo
		compile(source);
		classSource.renameTo(classReplaced2);
		source.renameTo(sourceReplaced2);			// preimenujemo, nazaj

		parseByteCode(classReplaced1, true, methodReplaced1, cpoolReplaced);
		parseByteCode(classReplaced2, false, methodReplaced2, null);

		linearizeByteCode();

/*for(int i = 0; i<methodReplaced1.size(); i++) {
	System.out.print(Integer.toHexString(methodReplaced1.get(i))+" ");
	if(i % 8 == 0) System.out.println();
}
System.out.println("+++++++++++");
for(int i = 0; i<methodReplaced1.size(); i++) {
	System.out.print(Integer.toHexString(methodReplaced2.get(i))+" ");
	if(i % 8 == 0) System.out.println();
}*/
/*


System.out.println("********************* methodRemoved *************************");
System.out.println("**********************************************");
for(int i=0;i<methodRemoved.size();i++) {
	System.out.print(methodRemoved.get(i)+" ");
	if((i+1)%8 == 0) System.out.println();
}
System.out.println("********************** methodReplaced ************************");
System.out.println("**********************************************");
for(int i=0;i<methodReplaced1.size();i++) {
	System.out.print(methodReplaced1.get(i)+" ");
	if((i+1)%8 == 0) System.out.println();
}
System.out.println();
System.out.println(methodRemoved.size()+" <- rem, rep -> "+methodReplaced1.size());
for(int i : codeBlockLocation.keySet()) {
	System.out.println("blok "+i+" je v metodi "+codeBlockLocation.get(i)[0]+" na poziciji "+codeBlockLocation.get(i)[1]);
}

*/
/*for(int i=0;i<methodReplaced1.size();i++) {
	System.out.print(methodReplaced1.get(i)+" ");
	if((i+1)%8 == 0) System.out.println();
}
System.out.println("++++++++++++++++++++++++++++++++++++++++++");*/


		insertJasmIntoBytecode();

/*for(int i = 0; i<methodReplaced1.size(); i++) {
	System.out.print(Integer.toHexString(methodReplaced1.get(i))+" ");
	if(i % 8 == 0) System.out.println();
}*/
/*System.out.println("++++++++++++++++++++++++++++++++++++++++++");
for(int i=0;i<methodReplaced1.size();i++) {
	System.out.print(methodReplaced1.get(i)+" ");
	if((i+1)%8 == 0) System.out.println();
}*/		

		buildClassFile();

	//	exit(0);

//		adjustTempFile(sourceReplaced, -1);		// ko izracunamo koliko dolg bo vsak bajtcode blok ...

		// analiziramo prvi class fajl
//		parsePreMethodsCode(classSource); //classRemoved);

		// sourceLocated v source in iz njega naredimo pravi sourceLocated in sourceReplaced s podatki iz analize :)

/*		sourceLocated.renameTo(source);
		compile(source);
		classSource.renameTo(classLocated);
		source.renameTo(sourceLocated);

		sourceReplaced.renameTo(source);
		compile(source);
		classSource.renameTo(classReplaced);
		source.renameTo(sourceReplaced);				//*/

		// zdaj imamo vse 3 classe iz katerih sestavimo sourceClass :) :) :) :)

		exit(0);
System.out.println("Konec :)");
	}

	private static void buildClassFile() {
		PrintStream ps = null;
		try {
			ps = new PrintStream(classSource);
			for(int c : preMethodsCode) {
				ps.write(c);
			}
			for(int c : methodReplaced1) {
				ps.write(c);
			}
			for(int c : postMethodsCode) {
				ps.write(c);
			}
		} catch (Exception e) {

		} finally {
			try {
				if (ps != null) ps.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}			
	}

	private static void insertJasmIntoBytecode() {
		int x = 0;
		int b1, b2, b3, b4;
		int block = 1;
		if(methodReplaced1.size() != methodReplaced2.size()) {
			System.out.println("NAPAKA Size method1/2Replaced!!!!");
			System.exit(1);
		}
		b1 = methodReplaced1.get(x++); b2 = methodReplaced1.get(x++);
		int msize = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
		for(int i=0; i<msize; i++) {
//			HashMap<String, int[]> localVariables = new HashMap<String, int[]>();
			x+=6;
			b1 = methodReplaced1.get(x++); b2 = methodReplaced1.get(x++);
			int attrCount = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
			for(int j=0; j<attrCount; j++) {
				b1 = methodReplaced1.get(x++); b2 = methodReplaced1.get(x++);
				int nameIndex = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
				b1 = methodReplaced1.get(x++); b2 = methodReplaced1.get(x++);b3 = methodReplaced1.get(x++); b4 = methodReplaced1.get(x++);
				int attrLength = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2)+""+Integer.toHexString(b3)+""+Integer.toHexString(b4), 16);
				if(cpoolReplaced.get(nameIndex).bytes().equals("Code")) {
					x+=4;
					b1 = methodReplaced1.get(x++); b2 = methodReplaced1.get(x++);b3 = methodReplaced1.get(x++); b4 = methodReplaced1.get(x++);
					int codeLength = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2)+""+Integer.toHexString(b3)+""+Integer.toHexString(b4), 16);
					for (int k=0; k<codeLength; k++) {
						if(!methodReplaced1.get(x).equals(methodReplaced2.get(x))) {
							LinkedList<Integer>jasmCode = linearizedJasm.get(block);
							while(jasmCode.size() < blockSize.get(block)) {
								jasmCode.addLast(0);		// dodajamo x00 = NOP - no operation ...
							}
							// vstavimo blockti block
							for(int z=0;z<jasmCode.size(); z++) {
								methodReplaced1.remove(x);
								methodReplaced1.add(x, jasmCode.get(z));
								x++;k++;
								// se preveriti ce x ali k padeta preko methodSize - INTERNAL ERROR?!?!?!!?
							}
							block++;
						}
						x++;
					}
					b1 = methodReplaced1.get(x++); b2 = methodReplaced1.get(x++);
					int exceptionTableLength = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
					x += exceptionTableLength*4;
					b1 = methodReplaced1.get(x++); b2 = methodReplaced1.get(x++);
					int codeAttrCount = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
					for(int k=0; k<codeAttrCount; k++) {
						b1 = methodReplaced1.get(x++); b2 = methodReplaced1.get(x++);
//						int attrNameIndex = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
						b1 = methodReplaced1.get(x++); b2 = methodReplaced1.get(x++);b3 = methodReplaced1.get(x++); b4 = methodReplaced1.get(x++);
						int codeAttrLength = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2)+""+Integer.toHexString(b3)+""+Integer.toHexString(b4), 16);
						x += codeAttrLength;
					}
				}
				else {
					x+= attrLength;
				}
			}
		}
	}

	private static void linearizeByteCode() {
		for(int i : optimizedJasm.keySet()) {
			LinkedList<Integer> jasm = new LinkedList<Integer>();
			for(int j : optimizedJasm.get(i).keySet()) {
				int[] intCodes = ByteCode.getInts(optimizedJasm.get(i).get(j));
				for(int x : intCodes)
					jasm.addLast(x);
			}
			linearizedJasm.put(i, jasm);
		}
	}

	private static void checkMethodCode() {
		int x = 0;
		int b1, b2, b3, b4;
		int block = 0;
		int blockDone = 0;
//DEBUG//
//for(int o=0; o<methodLocated1.size(); o++)System.out.print(Integer.toHexString(methodLocated1.get(o))+" ");

		if(methodLocated1.size() != methodLocated2.size()) {
			System.out.println("NAPAKA Size!!!!");
			System.exit(1);
		}
		b1 = methodLocated1.get(x++); b2 = methodLocated1.get(x++);
		int msize = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
//System.out.println(msize);
		for(int i=0; i<msize; i++) {
			HashMap<String, int[]> localVariables = new HashMap<String, int[]>();
			x+=6;
			b1 = methodLocated1.get(x++); b2 = methodLocated1.get(x++);
			int attrCount = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
//System.out.println(attrCount);
			for(int j=0; j<attrCount; j++) {
				b1 = methodLocated1.get(x++); b2 = methodLocated1.get(x++);
				int nameIndex = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
//System.out.println("nameIndex: "+nameIndex);
//System.out.println(cpoolLocated.get(nameIndex).bytes());	////////////////// CODE attribute
//cpoolLocated.printConst(nameIndex);
				b1 = methodLocated1.get(x++); b2 = methodLocated1.get(x++);b3 = methodLocated1.get(x++); b4 = methodLocated1.get(x++);
				int attrLength = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2)+""+Integer.toHexString(b3)+""+Integer.toHexString(b4), 16);
//System.out.println(attrLength);
				if(cpoolLocated.get(nameIndex).bytes().equals("Code")) {
//System.out.println("Code je ja!?!?!?!?");
					x+=4;
					b1 = methodLocated1.get(x++); b2 = methodLocated1.get(x++);b3 = methodLocated1.get(x++); b4 = methodLocated1.get(x++);
					int codeLength = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2)+""+Integer.toHexString(b3)+""+Integer.toHexString(b4), 16);
//System.out.println("codeLength = "+codeLength);
					for (int k=0; k<codeLength; k++) {
//System.out.println(methodLocated1.get(x)+" ?= "+methodLocated2.get(x));
//System.out.println("X = "+x+", k = "+k+" koda = "+methodLocated1.get(x)+" :::: "+methodLocated2.get(x));
						if(!methodLocated1.get(x).equals(methodLocated2.get(x))) {
							if(block == jasm.size()) {
								System.out.println("INTERNAL ERROR");
								System.exit(1);
							}
							codeBlockLocation.put(block, new int[] {i, k} );
							block++;
						}
						x++;
					}
					b1 = methodLocated1.get(x++); b2 = methodLocated1.get(x++);
					int exceptionTableLength = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
//System.out.println("Exception table length = "+exceptionTableLength+" x = "+x);
					x += exceptionTableLength*4;
//System.out.println("x = "+x);
					b1 = methodLocated1.get(x++); b2 = methodLocated1.get(x++);
					int codeAttrCount = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
//System.out.println("codeAttrCount = " + codeAttrCount);
					for(int k=0; k<codeAttrCount; k++) {
						b1 = methodLocated1.get(x++); b2 = methodLocated1.get(x++);
						int attrNameIndex = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
						b1 = methodLocated1.get(x++); b2 = methodLocated1.get(x++);b3 = methodLocated1.get(x++); b4 = methodLocated1.get(x++);
						int codeAttrLength = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2)+""+Integer.toHexString(b3)+""+Integer.toHexString(b4), 16);
//System.out.println("******* "+attrNameIndex);
//System.out.println(cpoolLocated.get(attrNameIndex).bytes());
						if(cpoolLocated.get(attrNameIndex).bytes().equals("LocalVariableTable")) {
							b1 = methodLocated1.get(x++); b2 = methodLocated1.get(x++);
							int localVariableCount = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
							for(int lvc = 0; lvc < localVariableCount; lvc++) {
								int[] localVariable = new int[5];
								for(int y=0; y<localVariable.length; y++) {
									b1 = methodLocated1.get(x++); b2 = methodLocated1.get(x++);
									int localVariableAttribute = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
									localVariable[y] = localVariableAttribute;
								}
								localVariables.put(cpoolLocated.get(localVariable[2]).bytes(), localVariable);
							}
						}
						else if(cpoolLocated.get(attrNameIndex).bytes().equals("LineNumberTable")) {
							x += codeAttrLength;
						}
						else {
							x += codeAttrLength;
						}
					}
				}
				else {
					x+= attrLength;
				}
			}
			// obdelava jasm blokov znotraj ene metode zaradi LocalVariableTable
			boolean jasmError = false;
			for(int b = blockDone; b < block; b++) {
//System.out.println("Smo v bloku: "+b);
				int bSize = 0;		// 1 bajt za ukaz kode, ostalo se priteje spodaj ...
//				LinkedList<String> optBlockCode = new LinkedList<String>();
				HashMap<Integer, String> blockCode = jasm.get(b+1);
				HashMap<Integer, String[]> optimizedCode = new HashMap<Integer, String[]>();
				int cPos = 1;
				for(Integer l : blockCode.keySet()) {
					String codeLine = blockCode.get(l);
					for(String s : codeLine.trim().split(";")) {
						bSize++;		// 1 bajt za ukaz kode, ostalo se pristeje spodaj ...
						String[] code = s.trim().split("\\s+");		// vsi whitespaci odpadejo!!!
						ByteCodeInstruction bi = ByteCode.code(code[0]);
						if(code.length-1 != bi.args()) {
							System.out.println("\n"+filename+":"+l+": wrong number of operands to instruction "+code[0]);
							jasmError = true;
						}
						else {
							for(int a=1; a<code.length; a++) {
								if(bi.argType(a).equals("local")) {
									int[] lvc = localVariables.get(code[a]);
									if(lvc == null) {
										System.out.println("\n"+filename+":"+l+": undefined local variable "+code[a]+" at instruction "+code[0]);
										jasmError = true;
									}
									else {
										if(lvc[0] <= codeBlockLocation.get(b)[1] && codeBlockLocation.get(b)[1] <= lvc[0]+lvc[1]-1) {
											// prepišemo index lokalne spremenljivke kar v argument ukaza, tako so argumenti tipa local že obdelani
											code[a] = ""+lvc[4];
										} else {
											System.out.println("\n"+filename+":"+l+": local variable "+code[a]+" out of scope for instruction "+code[0]);
											jasmError = true;
										}
									}
								}
							} if(!jasmError) {
								ByteCodeInstruction bci = ByteCode.code(code[0]);
								for(int bs=0; bs < bci.args(); bs++) {
									bSize += bci.argSize(bs+1);
								}
								code = ByteCode.optimize(code);
								optimizedCode.put(cPos, code);
								cPos++;
							}
						}
					}
				}
				optimizedJasm.put(b+1, optimizedCode);
				blockSize.put(b+1, bSize);
				blockDone++;
			}
			if(jasmError) {
				exit(1);
			}
//System.out.println("**********************************************************************");
		}
	}

	public static void parseByteCode(File file, boolean w, LinkedList<Integer> method, ConstantPool cpool) throws IOException {
		InputStream in = new FileInputStream(file);
		int b1,b2,b3,b4;
		// PRE METHOD CODE
		// initial magic numbers and version ...
		for(int i=0; i<8; i++) {
			b1 = in.read();
			if(w){preMethodsCode.addLast(b1);}
		}
		// constant pool table ...
		b1 = in.read(); b2 = in.read();
		if(w){preMethodsCode.addLast(b1);preMethodsCode.addLast(b2);}
		int cpsize = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
//System.out.println("cpsize = "+cpsize);
		for(int i=1; i<cpsize; i++) {		// cpsize je ena vecji od stevila konstant ...
			int constant = in.read();
			if(w){preMethodsCode.addLast(constant);}
			if(constant == 1) {
				b1 = in.read();
				b2 = in.read();
				if(w){preMethodsCode.addLast(b1);preMethodsCode.addLast(b2);}
				String arg1 = Integer.toHexString(b1)+""+Integer.toHexString(b2);
				int stringSize = Integer.parseInt(arg1, 16);
				String value1 = "";
				for(int j=0; j<stringSize; j++) {
					int b = in.read();
					if(w){preMethodsCode.addLast(b);}
					value1 += (char)b;
				}
				if(cpool != null)cpool.addConstant(i, constant, stringSize, -1, "UTF-8", value1);
//System.out.println("["+i+"] UTF-8 ("+stringSize+")\t"+value1);
			}
			else if(cpsize == 3) {
				b1 = in.read();
				b2 = in.read();
				b3 = in.read();
				b4 = in.read();
				if(w){preMethodsCode.addLast(b1);preMethodsCode.addLast(b2);preMethodsCode.addLast(b3);preMethodsCode.addLast(b4);}
				String arg3 = Integer.toHexString(b1)+""+Integer.toHexString(b2)+""+Integer.toHexString(b3)+""+Integer.toHexString(b4);
				int value3 = Integer.parseInt(arg3, 16);
				if(cpool != null)cpool.addConstant(i, constant, -1, -1, "Integer", Integer.toString(value3));
//System.out.println("["+i+"] Integer\t"+value3);
			}
			else if(constant == 4) {
				b1 = in.read();
				b2 = in.read();
				b3 = in.read();
				b4 = in.read();
				if(w){preMethodsCode.addLast(b1);preMethodsCode.addLast(b2);preMethodsCode.addLast(b3);preMethodsCode.addLast(b4);}				
				String arg4 = Integer.toHexString(b1)+""+Integer.toHexString(b2)+""+Integer.toHexString(b3)+""+Integer.toHexString(b4);
				Long l1 = Long.parseLong(arg4, 16);
				float value4 = Float.intBitsToFloat(l1.intValue());
				if(cpool != null)cpool.addConstant(i, constant, -1, -1, "Float", Float.toString(value4));
//System.out.println("["+i+"] Float\t"+value4);
			}
			else if(constant == 5) {
				b1 = in.read();
				b2 = in.read();
				b3= in.read();
				b4 = in.read();
				if(w){preMethodsCode.addLast(b1);preMethodsCode.addLast(b2);preMethodsCode.addLast(b3);preMethodsCode.addLast(b4);}			
				String arg5 = Integer.toHexString(b1)+""+Integer.toHexString(b2)+""+Integer.toHexString(b3)+""+Integer.toHexString(b4);
				b1 = in.read();
				b2 = in.read();
				b3= in.read();
				b4 = in.read();
				if(w){preMethodsCode.addLast(b1);preMethodsCode.addLast(b2);preMethodsCode.addLast(b3);preMethodsCode.addLast(b4);}
				arg5 += Integer.toHexString(b1)+""+Integer.toHexString(b2)+""+Integer.toHexString(b3)+""+Integer.toHexString(b4);
				long value5 = Long.parseLong(arg5, 16);
				if(cpool != null)cpool.addConstant(i, constant, -1, -1, "Long", Long.toString(value5));
//System.out.println("["+i+"] Long\t"+value5);
			}
			else if(constant == 6) {
				b1 = in.read();
				b2 = in.read();
				b3 = in.read();
				b4 = in.read();
				if(w){preMethodsCode.addLast(b1);preMethodsCode.addLast(b2);preMethodsCode.addLast(b3);preMethodsCode.addLast(b4);}
				String arg6 = Integer.toHexString(b1)+""+Integer.toHexString(b2)+""+Integer.toHexString(b3)+""+Integer.toHexString(b4);
				b1 = in.read();
				b2 = in.read();
				b3 = in.read();
				b4 = in.read();
				if(w){preMethodsCode.addLast(b1);preMethodsCode.addLast(b2);preMethodsCode.addLast(b3);preMethodsCode.addLast(b4);}
				arg6 += Integer.toHexString(b1)+""+Integer.toHexString(b2)+""+Integer.toHexString(b3)+""+Integer.toHexString(b4);
				Long l2 = Long.parseLong(arg6, 16);
				double value6 = Double.longBitsToDouble(l2);
				if(cpool != null)cpool.addConstant(i, constant, -1, -1, "Double", Double.toString(value6));
//System.out.println("["+i+"] Double\t"+value6);
			}
			else if(constant == 7) {
				b1 = in.read();
				b2 = in.read();
				if(w){preMethodsCode.addLast(b1);preMethodsCode.addLast(b2);}
				int index7 = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
				if(cpool != null)cpool.addConstant(i, constant, index7, -1, "Class reference", "");
//System.out.println("["+i+"] Class reference index="+index7);
			}
			else if(constant == 8) {
				b1 = in.read();
				b2 = in.read();
				if(w){preMethodsCode.addLast(b1);preMethodsCode.addLast(b2);}
				int index8 = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
				if(cpool != null)cpool.addConstant(i, constant, index8, -1, "String reference", "");
//System.out.println("["+i+"] String reference index="+index8);
			}
			else if(constant == 9) {
				b1 = in.read();
				b2 = in.read();
				b3 = in.read();
				b4 = in.read();
				if(w){preMethodsCode.addLast(b1);preMethodsCode.addLast(b2);preMethodsCode.addLast(b3);preMethodsCode.addLast(b4);}
				int index91 = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
				int index92 = Integer.parseInt(Integer.toHexString(b3)+""+Integer.toHexString(b4), 16);
				if(cpool != null)cpool.addConstant(i, constant, index91, index92, "Field reference", "");
//System.out.println("["+i+"] Field reference index1="+index91+" index2="+index92);
			}
			else if(constant == 10) {
				b1 = in.read();
				b2 = in.read();
				b3 = in.read();
				b4 = in.read();
				if(w){preMethodsCode.addLast(b1);preMethodsCode.addLast(b2);preMethodsCode.addLast(b3);preMethodsCode.addLast(b4);}
				int index101 = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
				int index102 = Integer.parseInt(Integer.toHexString(b3)+""+Integer.toHexString(b4), 16);
				if(cpool != null)cpool.addConstant(i, constant, index101, index102, "Method reference", "");
//System.out.println("["+i+"] Method reference index1="+index101+" index2="+index102);
			}
			else if(constant == 11) {
				b1 = in.read();
				b2 = in.read();
				b3 = in.read();
				b4 = in.read();
				if(w){preMethodsCode.addLast(b1);preMethodsCode.addLast(b2);preMethodsCode.addLast(b3);preMethodsCode.addLast(b4);}
				int index111 = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
				int index112 = Integer.parseInt(Integer.toHexString(b3)+""+Integer.toHexString(b4), 16);
				if(cpool != null)cpool.addConstant(i, constant, index111, index112, "Interface reference", "");
//System.out.println("["+i+"] Interface reference index1="+index111+" index2="+index112);
			}
			else if(constant == 12) {
				b1 = in.read();
				b2 = in.read();
				b3 = in.read();
				b4 = in.read();
				if(w){preMethodsCode.addLast(b1);preMethodsCode.addLast(b2);preMethodsCode.addLast(b3);preMethodsCode.addLast(b4);}
				int index121 = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
				int index122 = Integer.parseInt(Integer.toHexString(b3)+""+Integer.toHexString(b4), 16);
				if(cpool != null)cpool.addConstant(i, constant, index121, index122, " Name and Type reference", "");
//System.out.println("["+i+"] Name and Type reference index1="+index121+" index2="+index122);
			}
			else {
System.out.println("["+i+"] WTF je koda "+constant);
			}
		}
		for(int i=0; i<6; i++) {
			b1 = in.read();
			if(w){preMethodsCode.addLast(b1);}
		}
		// interface table ...				-- vsak interface dolg 2 bajta?		// !!!!!!!!!!!!!!!!!!!!! ZA TALE INTERFACE je treba se preverit!?!?!?!?
		b1 = in.read(); b2 = in.read();
		if(w){preMethodsCode.addLast(b1); preMethodsCode.addLast(b2);}
		int isize = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
		for(int i=0; i<isize; i++) {
			b1 = in.read();
			b2 = in.read();
			if(w){preMethodsCode.addLast(b1);preMethodsCode.addLast(b2);}
		}
		// field table ... definicija: http://www.csie.ntu.edu.tw/~comp2/2001/byteCode/byteCode.html#Fields
		b1 = in.read(); b2 = in.read();
		if(w){preMethodsCode.addLast(b1); preMethodsCode.addLast(b2);}
		int fsize = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
		for(int i=0; i<fsize; i++) {
			b1 = in.read(); b2 = in.read(); b3 = in.read();
			if(w){preMethodsCode.addLast(b1);preMethodsCode.addLast(b2);preMethodsCode.addLast(b3);}
			b1 = in.read(); b2 = in.read(); b3 = in.read();
			if(w){preMethodsCode.addLast(b1);preMethodsCode.addLast(b2);preMethodsCode.addLast(b3);}
			b1 = in.read(); b2 = in.read();
			if(w){preMethodsCode.addLast(b1); preMethodsCode.addLast(b2);}
			int attrSize = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
			for(int j=0; j<attrSize; j++) {
				b1 = in.read(); b2 = in.read();
				if(w){preMethodsCode.addLast(b1); preMethodsCode.addLast(b2);}
				b1 = in.read(); b2 = in.read(); b3 = in.read(); b4 = in.read();
				if(w){preMethodsCode.addLast(b1); preMethodsCode.addLast(b2);preMethodsCode.addLast(b3); preMethodsCode.addLast(b4);}
				b1 = in.read(); b2 = in.read();
				if(w){preMethodsCode.addLast(b1); preMethodsCode.addLast(b2);}
			}
		}
		// /PRE METHOD CODE
		// METHOD CODE
		// method table ...
		b1 = in.read(); b2 = in.read();
		method.addLast(b1);method.addLast(b2);
		int msize = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
		for(int i=0; i<msize; i++) {
			method.addLast(in.read());method.addLast(in.read());
			method.addLast(in.read());method.addLast(in.read());
			method.addLast(in.read());method.addLast(in.read());
			b1 = in.read(); b2 = in.read();
			method.addLast(b1);method.addLast(b2);
			int attrCount = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
			for(int j=0; j<attrCount; j++) {
				method.addLast(in.read());method.addLast(in.read());
				b1 = in.read();
				b2 = in.read();
				b3 = in.read();
				b4 = in.read();
				method.addLast(b1);method.addLast(b2);method.addLast(b3);method.addLast(b4);
				int attrLength = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2)+""+Integer.toHexString(b3)+""+Integer.toHexString(b4), 16);
				for(int k=0; k<attrLength; k++) {
					method.addLast(in.read());
				}
			}
		}
		// /METHOD CODE
		// POST METHOD CODE
		// attribute table ...
		b1 = in.read(); b2 = in.read();
		if(w){postMethodsCode.addLast(b1);postMethodsCode.addLast(b2);}
		// stevilo atributov:
		int asize = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2), 16);
		for(int i=0; i<asize; i++) {
			// 2 byte attribute name index
			b1 = in.read();
			b2 = in.read();
			if(w){postMethodsCode.addLast(b1);postMethodsCode.addLast(b2);}
			// 4 byte attribute length 
			b1 = in.read();
			b2 = in.read();
			b3 = in.read();
			b4 = in.read();
			if(w){postMethodsCode.addLast(b1);postMethodsCode.addLast(b2);postMethodsCode.addLast(b3);postMethodsCode.addLast(b4);}
			int attrLength = Integer.parseInt(Integer.toHexString(b1)+""+Integer.toHexString(b2)+""+Integer.toHexString(b3)+""+Integer.toHexString(b4), 16);
			// attribute bytes
			for(int j=0; j<attrLength; j++) {
				int b = in.read();
				if(w){postMethodsCode.addLast(b);}
			}
		}
		// ce in.read() != -1, potem smo nekaj sfalili!!!!
		if(in.read() == -1) {
			in.close();
		} else {
			System.out.println("INTERNAL ERROR");
			exit(1);
		}
	}

	public static void adjustTempFile(File f, int op) {
		BufferedReader br = null;
		PrintStream ps = null;
		try {
			ps = new PrintStream(f);
			br = new BufferedReader(new FileReader(tempFile));
			String cLine;
			while ((cLine = br.readLine()) != null) {
				if(cLine.contains("%%%%%")) {
					if(op == 0) {
						cLine = cLine.replaceAll("%%%%%", "");
					}
					else if(op == 1) {
						cLine = cLine.replaceAll("%%%%%", "System.exit(0);");
					}
					else if(op == 2) {
						cLine = cLine.replaceAll("%%%%%", "System.exit(-1);");
					}
					else if(op == 7) {
						int blockNumber = 1;
						while(cLine.contains("%%%%%")) {
							String placeHolder = "System.exit(0);";
							int bytesReserved = 4;
							while(bytesReserved < blockSize.get(blockNumber)) {
								placeHolder += "System.gc();";
								bytesReserved += 3;
							}
							blockNumber++;
							cLine = cLine.replaceFirst("%%%%%", placeHolder);
						}
					}
					else if(op == -7) {
						int blockNumber = 1;
						while(cLine.contains("%%%%%")) {
							String placeHolder = "System.exit(-1);";
							int bytesReserved = 4;
							while(bytesReserved < blockSize.get(blockNumber)) {
								placeHolder += "System.gc();";
								bytesReserved += 3;
							}
							blockNumber++;
							cLine = cLine.replaceFirst("%%%%%", placeHolder);
						}
					}
				}
				ps.println(cLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
				if (ps != null) ps.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void createTempFile() {
		boolean string = false;
		boolean mlcomm = false;
		boolean escseq = false;
		boolean jasmbc = false;
		boolean jasmcc = false;
		boolean jasmcn = false;

		int lineNumber = 0;
		int blockNumber = 1;

		BufferedReader br = null;
		PrintStream ps = null;
		try {
			ps = new PrintStream(tempFile);
			br = new BufferedReader(new FileReader(source));
			String cLine;
			HashMap<Integer, String> jasmLines = new HashMap<Integer, String>();
			String jasmCode = "";
			while ((cLine = br.readLine()) != null) {
				lineNumber++;
				for(int i=0; i<cLine.length(); i++) {
					char c = cLine.charAt(i);
					if(jasmcn) {
						if(c == '}') {
							jasmcn = false;
							if(jasmCode.trim().length() > 0) {
								jasmLines.put(lineNumber, jasmCode);
							}
							jasm.put(blockNumber, jasmLines);
							jasmLines = new HashMap<Integer, String>();
							jasmCode = "";
							blockNumber++;
						} else {
							jasmCode = jasmCode + c;
							if(i+1 == cLine.length()) {
								if(jasmCode.trim().length() > 0) {
									jasmLines.put(lineNumber, jasmCode);
								}
								jasmCode = "";
							}
						}
					} // end if jasmcn
					else if(jasmbc) {
						if(Character.isWhitespace(c)) {
							jasmcc = true;
						}
						else if (c == '{') {
							jasmbc = false;
							jasmcn = true;
							ps.print("%%%%%");
						}
						else {
							if(!jasmcc) {
								/* to pomeni, da smo prebrali jasm, nato pa se ime nadaljuje (jasmina ...)
								 */
								jasmbc = false;
								ps.write('j');ps.write('a');ps.write('s');ps.write('m');ps.write(c);
							} else {
								/* prišlo je do znaka za ukazom jasm, ki ni whitespace ali {
								 * se pravi je jasm uporabljen kot ime spremenljivke / metode, kar je napaka v kodi!!!!
								 */
								System.out.println(source.getName()+":"+lineNumber+" error: '{' expected - jasm is a reserved word");
								ps.close();
								exit(1);
							}
						}
					} // end else if jasmbc
					else if(string) {
						if(c == '\\') {
							if(escseq) {
								escseq = false;
							} else {
								escseq = true;
							}
						} else if(c == '"') {
							if(!escseq) {
								string = false;
							}
						} else {
							if(escseq) {
								escseq = false;
							}
						}
						ps.write(c);
					} // end else if string
					else if(mlcomm) {
						if(c == '*') {
							if(i+1 < cLine.length()) {
								i++;	// ker itak preskakujemo ...
								if(cLine.charAt(i) == '/') {
									mlcomm = false;
								}
							}
						}
					} // end else if mlcomm
					else {
						if(c == '"') {
							string = true;
							ps.write(c);
						} // end if "
						if(c == '/') {
							if(i+1 < cLine.length()) {
								if(cLine.charAt(i+1) == '/') {
									break;
								} else if(cLine.charAt(i+1) == '*') {
									mlcomm = true;
								} else {
									ps.write(c);
								}
							} else {
								ps.write(c);
							}
						} // end if /
						if(c == 'j') {
							boolean cond = false;
							if(i>0) {
								char x = cLine.charAt(i-1);
								if(!Character.isLetterOrDigit(x) && x != '$' && x != '_') {
									cond = true;
								}
							} else {
								cond = true;
							}
							if (cond) {
								if(i+3<cLine.length()) {
									if(cLine.charAt(i+1) == 'a' && cLine.charAt(i+2) == 's' && cLine.charAt(i+3) == 'm') {
										jasmbc = true; i+=3;
										if(i+4 == cLine.length()) {
											jasmcc = true;
										}
									}
								}
							}
							if(!jasmbc) {
								ps.write(c);
							}
						}
						else {
							ps.write(c);
						}
					} // end else
				}
				ps.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
				if (ps != null) ps.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void checkParam(String[] args) {
		if(args.length != 1) {
			System.out.println("Usage: javacasm <source file>");
			System.exit(1);
		}
		if(args[0].equals("-?") || args[0].equals("-help")) {
			System.out.println("Usage: javacasm <source file>");
			exit(1);
		} else if (args[0].startsWith("-")) {
			System.out.println("Unrecognized option: "+args[0]);
			exit(1);
		}
		if(!args[0].endsWith(".java")) {
			System.out.println("error: Only .java files are supported for javacasm parameter");
			exit(1);
		}
		String param = args[0];
		source = new File(param);
		filename = source.getName();
		if(!source.exists()) {
			source = new File(System.getProperty("user.dir")+param);
			if(!source.exists()) {
				System.out.println("javacasm: file not found: "+args[0]);
				System.out.println("Usage: javacasm <source file>");
				exit(1);
			}
		}		
	}

	public static void createFiles() {
		String path = source.getAbsoluteFile().getParentFile().getAbsolutePath();
		String fileName = source.getName();
		String fileName0  = "$originalBackup"+fileName.replace(".java", ".ja$va");
		String fileName1  = "$RemovedJASM"+fileName.replace(".java", ".ja$va");
		String fileName2  = "$Located1JASM"+fileName.replace(".java", ".ja$va");
		String fileName3  = "$Located2JASM"+fileName.replace(".java", ".ja$va");
		String fileName4  = "$Replaced1JASM"+fileName.replace(".java", ".ja$va");
		String fileName5  = "$Replaced2JASM"+fileName.replace(".java", ".ja$va");
		String fileName6  = "$RemovedJASM"+fileName.replace(".java", ".cla$$");
		String fileName7  = "$Located1JASM"+fileName.replace(".java", ".cla$$");
		String fileName8  = "$Located2JASM"+fileName.replace(".java", ".cla$$");
		String fileName9  = "$Replaced1JASM"+fileName.replace(".java", ".cla$$");
		String fileName10 = "$Replaced2JASM"+fileName.replace(".java", ".cla$$");
		String fileName11 = "$tempFile"+fileName.replace(".java", ".te$mp");
		classSource    = new File(path+File.separator+fileName.substring(0, fileName.length()-5)+".class");
		sourceBackup   = new File(path+File.separator+fileName0);
		sourceRemoved  = new File(path+File.separator+fileName1);
		sourceLocated1  = new File(path+File.separator+fileName2);
		sourceLocated2  = new File(path+File.separator+fileName3);
		sourceReplaced1 = new File(path+File.separator+fileName4);
		sourceReplaced2 = new File(path+File.separator+fileName5);
		classRemoved  = new File(path+File.separator+fileName6);
		classLocated1  = new File(path+File.separator+fileName7);
		classLocated2  = new File(path+File.separator+fileName8);
		classReplaced1 = new File(path+File.separator+fileName9);
		classReplaced2 = new File(path+File.separator+fileName10);
		tempFile = new File(path+File.separator+fileName11);
	}

	public static void compile(File file) throws IOException {
		File[] files = {file};
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if(compiler == null) {
			System.out.println("Can not get System Java Compiler!");
			System.out.println("Check that you have JDK installed and it's path in the PATH environment variable.");
			System.exit(1);
		}
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));
		List<String> optionList = new ArrayList<String>();
		optionList.addAll(Arrays.asList("-g"));
		boolean compiled = compiler.getTask(null, fileManager, null, optionList, null, compilationUnits).call();
		fileManager.close();
		if(!compiled) {
			exit(1);
		}
	}

	public static void compileEclipse(File file) {
		String compileParam = "-g -classpath rt.jar "+file.getName();
		org.eclipse.jdt.core.compiler.CompilationProgress progress = null; // instantiate your subclass
		if(!org.eclipse.jdt.core.compiler.batch.BatchCompiler.compile(compileParam, new PrintWriter(System.out), new PrintWriter(System.err), progress)) {
			exit(1);
		}
	}

	public static void compileCmd(File file) {
		Process pro;
		try {
			pro = Runtime.getRuntime().exec("javac -g "+source.getAbsolutePath());
			pro.waitFor();
			if(pro.exitValue() != 0) {
				InputStreamReader inputStreamReader = new InputStreamReader(pro.getErrorStream());
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String line;
				while((line = bufferedReader.readLine()) != null) {
					System.out.println(line);
				}
				source.delete();		// da nam ne ostane, ker v exit preimenujemo backup v source!!!
				exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			source.delete();			// da nam ne ostane, ker v exit preimenujemo backup v source!!!
			exit(1);
		}
	}

	public static void exit(int x) {
		/* vzpostavimo prvotno stanje */
		if(source != null) source.delete();
		sourceBackup.renameTo(source);
//System.out.println(source+"   "+sourceBackup);
		if(sourceRemoved != null) sourceRemoved.deleteOnExit();
		if(sourceLocated1 != null) sourceLocated1.deleteOnExit();
		if(sourceLocated2 != null) sourceLocated2.deleteOnExit();
		if(sourceReplaced1 != null) sourceReplaced1.deleteOnExit();
		if(sourceReplaced2 != null) sourceReplaced2.deleteOnExit();
		if(classRemoved != null) classRemoved.deleteOnExit();
		if(classLocated1 != null) classLocated1.deleteOnExit();
		if(classLocated2 != null) classLocated2.deleteOnExit();
		if(classReplaced1 != null) classReplaced1.deleteOnExit();
		if(classReplaced2 != null) classReplaced2.deleteOnExit();
		if(tempFile != null) tempFile.deleteOnExit();
		if(sourceBackup != null) sourceBackup.deleteOnExit();
		System.exit(x);
	}
}