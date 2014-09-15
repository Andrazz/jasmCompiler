package jasmCompiler;

import java.util.Hashtable;

public class ConstantPool {

	static Hashtable<Integer, Constant> cpoolIndex;
	static Hashtable<String, Constant> cpoolValue;

	public ConstantPool() {
		cpoolIndex = new Hashtable<Integer, Constant>();
		cpoolValue = new Hashtable<String, Constant>();
	}

	public void addConstant (int index, int type, int index1, int index2, String typeName, String bytes) {
		Constant c = new Constant(index, type, index1, index2, typeName, bytes);
		cpoolIndex.put(index, c);
		cpoolValue.put(bytes, c);
	}

	public Constant get(int i) {
		return cpoolIndex.get(i);
	}
	
	public Constant get(String s) {
		return cpoolValue.get(s);
	}	
	
/* ********************************** DEBUG * ***************************** */
	public void printConst(int cc) {
		Constant c = cpoolIndex.get(cc);
		if(c != null) {
			int i = c.type();
			System.out.print("["+cc+"] "+c.typeName()+" - ");
			if(i == 1) {
				System.out.println(c.index1()+" "+c.bytes());
			}
			else if(i > 2 && i < 7) {
				System.out.println(c.bytes());
			}
			else if(i == 7 || i == 8) {
				System.out.println(c.index1());
			}
			else {
				System.out.println(c.index1()+" "+c.index2());
			}
		}
	}
}