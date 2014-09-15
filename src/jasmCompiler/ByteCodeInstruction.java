package jasmCompiler;

public class ByteCodeInstruction {

	private int bopcode;
	private String bhexOpcode;
	private String bmnemonic;
	private int bargs;
	private int[] bargsize;
	private String[] bargtype;

	public ByteCodeInstruction(int opcode, String hexOpcode, String mnemonic, int[]argsize, String[]argtype) {
		this.bopcode = opcode;
		this.bhexOpcode = hexOpcode;
		this.bmnemonic = mnemonic;
		this.bargsize = argsize;
		this.bargtype = argtype;
		this.bargs = argsize == null ? 0 : argsize.length;
	}

	public int opcode() {
		return bopcode;
	}

	public String hexOpcode() {
		return bhexOpcode;
	}

	public String mnemonic() {
		return bmnemonic;
	}

	public int args() {
		return bargs;
	}

	public int argSize(int i) {
		if(bargsize == null)
			return 0;
		return bargsize[i-1];
	}

	public String argType(int i) {
		if(bargtype == null)
			return null;
		return bargtype[i-1];
	}

}
