package jasmCompiler;

public class Constant {

	private int cindex;
	private int ctype;
	private int cindex1;
	private int cindex2;
	private String ctypeName;
	private String cbytes;


	public Constant (int index, int type, int index1, int index2, String typeName, String bytes) {
		this.cindex = index;
		this.ctype = type;
		this.cindex1 = index1;
		this.cindex2 = index2;
		this.ctypeName = typeName;
		this.cbytes = bytes;
	}

	public int index() {
		return cindex;
	}

	public int type() {
		return ctype;
	}

	public int index1() {
		return cindex1;
	}

	public int index2() {
		return cindex2;
	}

	public String typeName() {
		return ctypeName;
	}

	public String bytes() {
		return cbytes;
	}
}
