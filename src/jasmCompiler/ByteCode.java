package jasmCompiler;

import java.util.HashMap;

public final class ByteCode {

	private static HashMap<String, ByteCodeInstruction> instructions;
	static {
		instructions = new HashMap<String, ByteCodeInstruction>();
		//	new ByteCodeInstructio(int opcode, String hexOpcode, String mnemonic, int[]argsize, String[]argtype)
		instructions.put("aaload", new ByteCodeInstruction ( 50 ,"32","aaload", null, null));									// arrayref, index -> value		load onto the stack a reference from an array
		instructions.put("aastore", new ByteCodeInstruction (83 ,"53","aastore", null, null));								// arrayref, index, value ->	store into a reference in an array
		instructions.put("aconst_null", new ByteCodeInstruction (1 ,"1","aconst_null", null, null)); 							// -> null						push a null reference onto the stack
		instructions.put("aload", new ByteCodeInstruction (25 ,"19","aload", new int[]{1}, new String[] {"local"}));			// 1: index -> objectref		load a reference onto the stack from a local variable #index
		instructions.put("aload_0", new ByteCodeInstruction (42 ,"2a","aload_0", null, null));								// -> objectref					load a reference onto the stack from local variable 0
		instructions.put("aload_1", new ByteCodeInstruction (43 ,"2b","aload_1", null, null)); 								// -> objectref					load a reference onto the stack from local variable 1
		instructions.put("aload_2", new ByteCodeInstruction (44 ,"2c","aload_2", null, null)); 								// -> objectref					load a reference onto the stack from local variable 2
		instructions.put("aload_3", new ByteCodeInstruction (45 ,"2d","aload_3", null, null)); 								// -> objectref					load a reference onto the stack from local variable 3
		instructions.put("anewarray", new ByteCodeInstruction (189 ,"bd","anewarray", new int[]{2}, new String[]{"class"}));	// 2: i1, i2 count -> arrayref	create a new array of references of length count and component type identified by the class reference index (i1 << 8 + i2) in the constant pool
		instructions.put("areturn", new ByteCodeInstruction (176 ,"b0","areturn", null, null)); 								// objectref -> [empty]			return a reference from a method
		instructions.put("arraylength", new ByteCodeInstruction (190 ,"be","arraylength", null, null)); 						// arrayref -> length			get the length of an array
		instructions.put("astore", new ByteCodeInstruction (58 ,"3a","astore", new int[]{1}, new String[]{"local"})); 		// 1: index objectref ->		store a reference into a local variable #index
		instructions.put("astore_0", new ByteCodeInstruction (75 ,"4b","astore_0", null, null));	 							// objectref ->					store a reference into local variable 0
		instructions.put("astore_1", new ByteCodeInstruction (76 ,"4c","astore_1", null, null)); 								// objectref ->					store a reference into local variable 1
		instructions.put("astore_2", new ByteCodeInstruction (77 ,"4d","astore_2", null, null)); 								// objectref ->					store a reference into local variable 2
		instructions.put("astore_3", new ByteCodeInstruction (78 ,"4e","astore_3", null, null)); 								// objectref ->					store a reference into local variable 3
		instructions.put("bipush", new ByteCodeInstruction (16 ,"10","bipush", new int[]{1}, new String[]{"int"})); 			// 1: byte -> value				push a byte onto the stack as an integer value
		instructions.put("breakpoint", new ByteCodeInstruction (202 ,"ca","breakpoint", null, null)); 						// 								reserved for breakpoints in Java debuggers; should not appear in any class file
		instructions.put("caload", new ByteCodeInstruction (52 ,"34","caload", null, null)); 									// arrayref, index -> value		load a char from an array
		instructions.put("castore", new ByteCodeInstruction (85 ,"55","castore", null, null)); 								// arrayref, index, value ->	store a char into an array
		instructions.put("d2f", new ByteCodeInstruction (144 ,"90","d2f", null, null)); 										// value -> result				convert a double to a float
		instructions.put("d2i", new ByteCodeInstruction (142 ,"8e","d2i", null, null)); 										// value -> result				convert a double to an int
		instructions.put("d2l", new ByteCodeInstruction (143 ,"8f","d2l", null, null)); 										// value -> result				convert a double to a long
		instructions.put("dadd", new ByteCodeInstruction (99 ,"63","dadd", null, null)); 										// value1, value2 -> result		add two doubles
		instructions.put("daload", new ByteCodeInstruction (49 ,"31","daload", null, null)); 									// arrayref, index -> value		load a double from an array
		instructions.put("dastore", new ByteCodeInstruction (82 ,"52","dastore", null, null));						 		// arrayref, index, value ->	store a double into an array
		instructions.put("dcmpg", new ByteCodeInstruction (152 ,"98","dcmpg", null, null)); 									// value1, value2 -> result		compare two doubles
		instructions.put("dcmpl", new ByteCodeInstruction (151 ,"97","dcmpl", null, null)); 									// value1, value2 -> result		compare two doubles
		instructions.put("dconst_0", new ByteCodeInstruction (14 ,"0e","dconst_0", null, null)); 								// -> 0.0 push the constant		0.0 onto the stack
		instructions.put("dconst_1", new ByteCodeInstruction (15 ,"0f","dconst_1", null, null)); 								// -> 1.0 push the constant		1.0 onto the stack
		instructions.put("ddiv", new ByteCodeInstruction (111 ,"6f","ddiv", null, null)); 									// value1, value2 -> result		divide two doubles
		instructions.put("dload", new ByteCodeInstruction (24 ,"18","dload", new int[]{1}, new String[]{"local"})); 			// 1: index -> value			load a double value from a local variable #index
		instructions.put("dload_0", new ByteCodeInstruction (38 ,"26","dload_0", null, null)); 								// -> value						load a double from local variable 0
		instructions.put("dload_1", new ByteCodeInstruction (39 ,"27","dload_1", null, null)); 								// -> value						load a double from local variable 1
		instructions.put("dload_2", new ByteCodeInstruction (40 ,"28","dload_2", null, null)); 								// -> value						load a double from local variable 2
		instructions.put("dload_3", new ByteCodeInstruction (41 ,"29","dload_3", null, null)); 								// -> value						load a double from local variable 3
		instructions.put("dmul", new ByteCodeInstruction (107 ,"6b","dmul", null, null)); 									// value1, value2 -> result		multiply two doubles
		instructions.put("dneg", new ByteCodeInstruction (119 ,"77","dneg", null, null)); 									// value -> result				negate a double
		instructions.put("drem", new ByteCodeInstruction (115 ,"73","drem", null, null)); 									// value1, value2 -> result		get the remainder from a division between two doubles
		instructions.put("dreturn", new ByteCodeInstruction (175 ,"af","dreturn", null, null)); 								// value -> [empty]				return a double from a method
		instructions.put("dstore", new ByteCodeInstruction (57 ,"39","dstore", new int[]{1}, new String[]{"local"})); 		// 1: index value ->			store a double value into a local variable #index
		instructions.put("dstore_0", new ByteCodeInstruction (71 ,"47","dstore_0", null, null));					 			// value ->						store a double into local variable 0
		instructions.put("dstore_1", new ByteCodeInstruction (72 ,"48","dstore_1", null, null)); 								// value ->						store a double into local variable 1
		instructions.put("dstore_2", new ByteCodeInstruction (73 ,"49","dstore_2", null, null)); 								// value ->						store a double into local variable 2
		instructions.put("dstore_3", new ByteCodeInstruction (74 ,"4a","dstore_3", null, null)); 								// value ->						store a double into local variable 3
		instructions.put("dsub", new ByteCodeInstruction (103 ,"67","dsub", null, null)); 									// value1, value2 -> result		subtract a double from another
		instructions.put("dup", new ByteCodeInstruction (89 ,"59","dup", null, null)); 										// value -> value, value		duplicate the value on top of the stack
		instructions.put("f2d", new ByteCodeInstruction (141 ,"8d","f2d", null, null)); 										// value -> result				convert a float to a double
		instructions.put("f2i", new ByteCodeInstruction (139 ,"8b","f2i", null, null)); 										// value -> result				convert a float to an int
		instructions.put("f2l", new ByteCodeInstruction (140 ,"8c","f2l", null, null)); 										// value -> result				convert a float to a long
		instructions.put("fadd", new ByteCodeInstruction (98 ,"62","fadd", null, null)); 										// value1, value2 -> result		add two floats
		instructions.put("faload", new ByteCodeInstruction (48 ,"30","faload", null, null)); 									// arrayref, index -> value		load a float from an array
		instructions.put("fastore", new ByteCodeInstruction (81 ,"51","fastore", null, null)); 								// arrayref, index, value ->	store a float in an array
		instructions.put("fcmpg", new ByteCodeInstruction (150 ,"96","fcmpg", null, null)); 									// value1, value2 -> result		compare two floats
		instructions.put("fcmpl", new ByteCodeInstruction (149 ,"95","fcmpl", null, null)); 									// value1, value2 -> result		compare two floats
		instructions.put("fconst_0", new ByteCodeInstruction (11 ,"0b","fconst_0", null, null)); 								// -> 0.0f						push 0.0f on the stack
		instructions.put("fconst_1", new ByteCodeInstruction (12 ,"0c","fconst_1", null, null)); 								// -> 1.0f						push 1.0f on the stack
		instructions.put("fconst_2", new ByteCodeInstruction (13 ,"0d","fconst_2", null, null)); 								// -> 2.0f						push 2.0f on the stack
		instructions.put("fdiv", new ByteCodeInstruction (110 ,"6e","fdiv", null, null)); 									// value1, value2 -> result		divide two floats
		instructions.put("fload", new ByteCodeInstruction (23 ,"17","fload", new int[]{1}, new String[]{"local"})); 			// 1: index -> value			load a float value from a local variable #index
		instructions.put("fload_0", new ByteCodeInstruction (34 ,"22","fload_0", null, null)); 								// -> value						load a float value from local variable 0
		instructions.put("fload_1", new ByteCodeInstruction (35 ,"23","fload_1", null, null)); 								// -> value						load a float value from local variable 1
		instructions.put("fload_2", new ByteCodeInstruction (36 ,"24","fload_2", null, null)); 								// -> value						load a float value from local variable 2
		instructions.put("fload_3", new ByteCodeInstruction (37 ,"25","fload_3", null, null)); 								// -> value						load a float value from local variable 3
		instructions.put("fmul", new ByteCodeInstruction (106 ,"6a","fmul", null, null)); 									// value1, value2 -> result		multiply two floats
		instructions.put("fneg", new ByteCodeInstruction (118 ,"76","fneg", null, null)); 									// value -> result				negate a float
		instructions.put("frem", new ByteCodeInstruction (114 ,"72","frem", null, null)); 									// value1, value2 -> result		get the remainder from a division between two floats
		instructions.put("freturn", new ByteCodeInstruction (174 ,"ae","freturn", null, null)); 								// value -> [empty]				return a float
		instructions.put("fstore", new ByteCodeInstruction (56 ,"38","fstore", new int[]{1}, new String[]{"local"})); 		// 1: index value ->			store a float value into a local variable #index
		instructions.put("fstore_0", new ByteCodeInstruction (67 ,"43","fstore_0", null, null)); 								// value ->						store a float value into local variable 0
		instructions.put("fstore_1", new ByteCodeInstruction (68 ,"44","fstore_1", null, null)); 								// value ->						store a float value into local variable 1
		instructions.put("fstore_2", new ByteCodeInstruction (69 ,"45","fstore_2", null, null)); 								// value ->						store a float value into local variable 2
		instructions.put("fstore_3", new ByteCodeInstruction (70 ,"46","fstore_3", null, null)); 								// value ->						store a float value into local variable 3
		instructions.put("fsub", new ByteCodeInstruction (102 ,"66","fsub", null, null)); 									// value1, value2 ->			result subtract two floats
		instructions.put("i2b", new ByteCodeInstruction (145 ,"91","i2b", null, null)); 										// value -> result				convert an int into a byte
		instructions.put("i2c", new ByteCodeInstruction (146 ,"92","i2c", null, null)); 										// value -> result				convert an int into a character
		instructions.put("i2d", new ByteCodeInstruction (135 ,"87","i2d", null, null)); 										// value -> result				convert an int into a double
		instructions.put("i2f", new ByteCodeInstruction (134 ,"86","i2f", null, null)); 										// value -> result				convert an int into a float
		instructions.put("i2l", new ByteCodeInstruction (133 ,"85","i2l", null, null)); 										// value -> result				convert an int into a long
		instructions.put("i2s", new ByteCodeInstruction (147 ,"93","i2s", null, null)); 										// value -> result				convert an int into a short
		instructions.put("iadd", new ByteCodeInstruction (96 ,"60","iadd", null, null)); 										// value1, value2 -> result		add two ints
		instructions.put("iaload", new ByteCodeInstruction (46 ,"2e","iaload", null, null)); 									// arrayref, index -> value		load an int from an array
		instructions.put("iand", new ByteCodeInstruction (126 ,"7e","iand", null, null)); 									// value1, value2 -> result		perform a bitwise and on two integers
		instructions.put("iastore", new ByteCodeInstruction (79 ,"4f","iastore", null, null)); 								// arrayref, index, value ->	store an int into an array
		instructions.put("iconst_0", new ByteCodeInstruction (3 ,"3","iconst_0", null, null)); 								// -> 0							load the int value 0 onto the stack
		instructions.put("iconst_1", new ByteCodeInstruction (4 ,"4","iconst_1", null, null)); 								// -> 1							load the int value 1 onto the stack
		instructions.put("iconst_2", new ByteCodeInstruction (5 ,"5","iconst_2", null, null)); 								// -> 2							load the int value 2 onto the stack
		instructions.put("iconst_3", new ByteCodeInstruction (6 ,"6","iconst_3", null, null)); 								// -> 3							load the int value 3 onto the stack
		instructions.put("iconst_4", new ByteCodeInstruction (7 ,"7","iconst_4", null, null)); 								// -> 4							load the int value 4 onto the stack
		instructions.put("iconst_5", new ByteCodeInstruction (8 ,"8","iconst_5", null, null)); 								// -> 5							load the int value 5 onto the stack
		instructions.put("iconst_m1", new ByteCodeInstruction (2 ,"2","iconst_m1", null, null)); 								// -> -1						load the int value -1 onto the stack
		instructions.put("idiv", new ByteCodeInstruction (108 ,"6c","idiv", null, null)); 									// value1, value2 -> result		divide two integers
		instructions.put("if_acmpeq", new ByteCodeInstruction (165 ,"a5","if_acmpeq", new int[]{2}, new String[]{"int"}));	// 2: bb1, bb2 val1, val2 ->	if references are equal, branch to instruction at branchoffset (signed short constructed from unsigned bytes bb1 << 8 + bb2)
		instructions.put("if_acmpne", new ByteCodeInstruction (166 ,"a6","if_acmpne", new int[]{2}, new String[]{"int"}));	// 2: bb1, bb2 val1, val2 ->	if references are not equal, branch to instruction at branchoffset (signed short constructed from unsigned bytes bb1 << 8 + bb2)
		instructions.put("if_icmpeq", new ByteCodeInstruction (159 ,"9f","if_icmpeq", new int[]{2}, new String[]{"int"}));	// 2: bb1, bb2 val1, val2 ->	if ints are equal, branch to instruction at branchoffset (signed short constructed from unsigned bytes bb1 << 8 + bb2)
		instructions.put("if_icmpge", new ByteCodeInstruction (162 ,"a2","if_icmpge", new int[]{2}, new String[]{"int"}));	// 2: bb1, bb2 val1, val2 ->	if value1 is greater than or equal to value2, branch to instruction at branchoffset (signed short constructed from unsigned bytes bb1 << 8 + bb2)
		instructions.put("if_icmpgt", new ByteCodeInstruction (163 ,"a3","if_icmpgt", new int[]{2}, new String[]{"int"}));	// 2: bb1, bb2 val1, val2 ->	if value1 is greater than value2, branch to instruction at branchoffset (signed short constructed from unsigned bytes bb1 << 8 + bb2)
		instructions.put("if_icmple", new ByteCodeInstruction (164 ,"a4","if_icmple", new int[]{2}, new String[]{"int"}));	// 2: bb1, bb2 val1, val2 ->	if value1 is less than or equal to value2, branch to instruction at branchoffset (signed short constructed from unsigned bytes bb1 << 8 + bb2)
		instructions.put("if_icmplt", new ByteCodeInstruction (161 ,"a1","if_icmplt", new int[]{2}, new String[]{"int"}));	// 2: bb1, bb2 val1, val2 ->	if value1 is less than value2, branch to instruction at branchoffset (signed short constructed from unsigned bytes bb1 << 8 + bb2)
		instructions.put("if_icmpne", new ByteCodeInstruction (160 ,"a0","if_icmpne", new int[]{2}, new String[]{"int"}));	// 2: bb1, bb2 val1, val2 ->	if ints are not equal, branch to instruction at branchoffset (signed short constructed from unsigned bytes bb1 << 8 + bb2)
		instructions.put("ifeq", new ByteCodeInstruction (153 ,"99","ifeq", new int[]{2}, new String[]{"int"})); 				// 2: bb1, bb2 value ->			if value is 0, branch to instruction at branchoffset (signed short constructed from unsigned bytes bb1 << 8 + bb2)
		instructions.put("ifge", new ByteCodeInstruction (156 ,"9c","ifge", new int[]{2}, new String[]{"int"})); 				// 2: bb1, bb2 value ->			if value is greater than or equal to 0, branch to instruction at branchoffset (signed short constructed from unsigned bytes bb1 << 8 + bb2)
		instructions.put("ifgt", new ByteCodeInstruction (157 ,"9d","ifgt", new int[]{2}, new String[]{"int"})); 				// 2: bb1, bb2 value ->			if value is greater than 0, branch to instruction at branchoffset (signed short constructed from unsigned bytes bb1 << 8 + bb2)
		instructions.put("ifle", new ByteCodeInstruction (158 ,"9e","ifle", new int[]{2}, new String[]{"int"})); 				// 2: bb1, bb2 value ->			if value is less than or equal to 0, branch to instruction at branchoffset (signed short constructed from unsigned bytes bb1 << 8 + bb2)
		instructions.put("iflt", new ByteCodeInstruction (155 ,"9b","iflt", new int[]{2}, new String[]{"int"})); 				// 2: bb1, bb2 value ->			if value is less than 0, branch to instruction at branchoffset (signed short constructed from unsigned bytes bb1 << 8 + bb2)
		instructions.put("ifne", new ByteCodeInstruction (154 ,"9a","ifne", new int[]{2}, new String[]{"int"})); 				// 2: bb1, bb2 value ->			if value is not 0, branch to instruction at branchoffset (signed short constructed from unsigned bytes bb1 << 8 + bb2)
		instructions.put("ifnonnull", new ByteCodeInstruction (199 ,"c7","ifnonnull", new int[]{2}, new String[]{"int"})); 	// 2: bb1, bb2 value ->			if value is not null, branch to instruction at branchoffset (signed short constructed from unsigned bytes bb1 << 8 + bb2)
		instructions.put("ifnull", new ByteCodeInstruction (198 ,"c6","ifnull", new int[]{2}, new String[]{"int"})); 			// 2: bb1, bb2 value ->			if value is null, branch to instruction at branchoffset (signed short constructed from unsigned bytes bb1 << 8 + bb2)
		instructions.put("iinc", new ByteCodeInstruction (132 ,"84","iinc", new int[]{1,1}, new String[]{"local", "int"}));	// 2: index, const [No change]	increment local variable #index by signed byte const
		instructions.put("iload", new ByteCodeInstruction (21 ,"15","iload", new int[]{1}, new String[]{"local"})); 			// 1: index -> value			load an int value from a local variable #index
		instructions.put("iload_0", new ByteCodeInstruction (26 ,"1a","iload_0", null, null)); 								// -> value						load an int value from local variable 0
		instructions.put("iload_1", new ByteCodeInstruction (27 ,"1b","iload_1", null, null)); 								// -> value						load an int value from local variable 1
		instructions.put("iload_2", new ByteCodeInstruction (28 ,"1c","iload_2", null, null)); 								// -> value						load an int value from local variable 2
		instructions.put("iload_3", new ByteCodeInstruction (29 ,"1d","iload_3", null, null)); 								// -> value						load an int value from local variable 3
		instructions.put("impdep1", new ByteCodeInstruction (254 ,"fe","impdep1", null, null)); 								//								reserved for implementation-dependent operations within debuggers; should not appear in any class file
		instructions.put("impdep2", new ByteCodeInstruction (255 ,"ff","impdep2", null, null)); 								//								reserved for implementation-dependent operations within debuggers; should not appear in any class file
		instructions.put("imul", new ByteCodeInstruction (104 ,"68","imul", null, null)); 									// value1, value2 -> result		multiply two integers
		instructions.put("ineg", new ByteCodeInstruction (116 ,"74","ineg", null, null)); 									// value -> result				negate int
		instructions.put("ior", new ByteCodeInstruction (128 ,"80","ior", null, null)); 										// value1, value2 -> result		bitwise int or
		instructions.put("irem", new ByteCodeInstruction (112 ,"70","irem", null, null)); 									// value1, value2 -> result		logical int remainder
		instructions.put("ireturn", new ByteCodeInstruction (172 ,"ac","ireturn", null, null)); 								// value -> [empty]				return an integer from a method
		instructions.put("ishl", new ByteCodeInstruction (120 ,"78","ishl", null, null)); 									// value1, value2 -> result		int shift left
		instructions.put("ishr", new ByteCodeInstruction (122 ,"7a","ishr", null, null)); 									// value1, value2 -> result		int arithmetic shift right
		instructions.put("istore", new ByteCodeInstruction (54 ,"36","istore", new int[]{1}, new String[]{"local"})); 		// 1: index value ->			store int value into variable #index
		instructions.put("istore_0", new ByteCodeInstruction (59 ,"3b","istore_0", null, null)); 								// value ->						store int value into variable 0
		instructions.put("istore_1", new ByteCodeInstruction (60 ,"3c","istore_1", null, null)); 								// value ->						store int value into variable 1
		instructions.put("istore_2", new ByteCodeInstruction (61 ,"3d","istore_2", null, null)); 								// value ->						store int value into variable 2
		instructions.put("istore_3", new ByteCodeInstruction (62 ,"3e","istore_3", null, null)); 								// value ->						store int value into variable 3
		instructions.put("isub", new ByteCodeInstruction (100 ,"64","isub", null, null)); 									// value1, value2 -> result		int subtract
		instructions.put("iushr", new ByteCodeInstruction (124 ,"7c","iushr", null, null)); 									// value1, value2 -> result		int logical shift right
		instructions.put("ixor", new ByteCodeInstruction (130 ,"82","ixor", null, null)); 									// value1, value2 -> result		int xor
		instructions.put("l2d", new ByteCodeInstruction (138 ,"8a","l2d", null, null)); 										// value -> result				convert a long to a double
		instructions.put("l2f", new ByteCodeInstruction (137 ,"89","l2f", null, null)); 										// value -> result				convert a long to a float
		instructions.put("l2i", new ByteCodeInstruction (136 ,"88","l2i", null, null)); 										// value -> result				convert a long to a int
		instructions.put("ladd", new ByteCodeInstruction (97 ,"61","ladd", null, null)); 										// value1, value2 -> result		add two longs
		instructions.put("laload", new ByteCodeInstruction (47 ,"2f","laload", null, null)); 									// arrayref, index -> value		load a long from an array
		instructions.put("land", new ByteCodeInstruction (127 ,"7f","land", null, null)); 									// value1, value2 -> result		bitwise and of two longs
		instructions.put("lastore", new ByteCodeInstruction (80 ,"50","lastore", null, null)); 								// arrayref, index, value ->	store a long to an array
		instructions.put("lcmp", new ByteCodeInstruction (148 ,"94","lcmp", null, null)); 									// value1, value2 -> result		compare two longs values
		instructions.put("lconst_0", new ByteCodeInstruction (9 ,"9","lconst_0", null, null)); 								// -> 0L						push the long 0 onto the stack
		instructions.put("lconst_1", new ByteCodeInstruction (10 ,"0a","lconst_1", null, null)); 								// -> 1L						push the long 1 onto the stack
		instructions.put("ldc", new ByteCodeInstruction (18 ,"12","ldc", new int[]{1}, new String[]{"const"})); 				// 1: index -> value			push a constant #index from a constant pool (String, int or float) onto the stack
		instructions.put("ldc2_w", new ByteCodeInstruction (20 ,"14","ldc2_w", new int[]{2}, new String[]{"const"})); 		// 2: i1, i2 -> value			push a constant #index from a constant pool (double or long) onto the stack (wide index is constructed as indexbyte1 << 8 + indexbyte2)
		instructions.put("ldc_w", new ByteCodeInstruction (19 ,"13","ldc_w", new int[]{2}, new String[]{"const"})); 			// 2: i1, i2 -> value			push a constant #index from a constant pool (String, int or float) onto the stack (wide index is constructed as indexbyte1 << 8 + indexbyte2)
		instructions.put("ldiv", new ByteCodeInstruction (109 ,"6d","ldiv", null, null)); 									// value1, value2 -> result		divide two longs
		instructions.put("lload", new ByteCodeInstruction (22 ,"16","lload", new int[]{1}, new String[]{"local"}));			// 1: index -> value			load a long value from a local variable #index
		instructions.put("lload_0", new ByteCodeInstruction (30 ,"1e","lload_0", null, null)); 								// -> value						load a long value from a local variable 0
		instructions.put("lload_1", new ByteCodeInstruction (31 ,"1f","lload_1", null, null)); 								// -> value						load a long value from a local variable 1
		instructions.put("lload_2", new ByteCodeInstruction (32 ,"20","lload_2", null, null));						 		// -> value						load a long value from a local variable 2
		instructions.put("lload_3", new ByteCodeInstruction (33 ,"21","lload_3", null, null)); 								// -> value						load a long value from a local variable 3
		instructions.put("lmul", new ByteCodeInstruction (105 ,"69","lmul", null, null)); 									// value1, value2 -> result		multiply two longs
		instructions.put("lneg", new ByteCodeInstruction (117 ,"75","lneg", null, null)); 									// value -> result				negate a long
		instructions.put("lor", new ByteCodeInstruction (129 ,"81","lor", null, null));								 		// value1, value2 -> result		bitwise or of two longs
		instructions.put("lrem", new ByteCodeInstruction (113 ,"71","lrem", null, null)); 									// value1, value2 -> result		remainder of division of two longs
		instructions.put("lreturn", new ByteCodeInstruction (173 ,"ad","lreturn", null, null)); 								// value -> [empty]				return a long value
		instructions.put("lshl", new ByteCodeInstruction (121 ,"79","lshl", null, null)); 									// value1, value2 -> result		bitwise shift left of a long value1 by int value2 positions
		instructions.put("lshr", new ByteCodeInstruction (123 ,"7b","lshr", null, null)); 									// value1, value2 -> result		bitwise shift right of a long value1 by int value2 positions
		instructions.put("lstore", new ByteCodeInstruction (55 ,"37","lstore", new int[]{1}, new String[]{"local"}));			// 1: index value ->			store a long value in a local variable #index
		instructions.put("lstore_0", new ByteCodeInstruction (63 ,"3f","lstore_0", null, null));						 		// value ->						store a long value in a local variable 0
		instructions.put("lstore_1", new ByteCodeInstruction (64 ,"40","lstore_1", null, null)); 								// value ->						store a long value in a local variable 1
		instructions.put("lstore_2", new ByteCodeInstruction (65 ,"41","lstore_2", null, null)); 								// value ->						store a long value in a local variable 2
		instructions.put("lstore_3", new ByteCodeInstruction (66 ,"42","lstore_3", null, null)); 								// value ->						store a long value in a local variable 3
		instructions.put("lsub", new ByteCodeInstruction (101 ,"65","lsub", null, null)); 									// value1, value2 -> result		subtract two longs
		instructions.put("lushr", new ByteCodeInstruction (125 ,"7d","lushr", null, null)); 									// value1, value2 -> result		bitwise shift right of a long value1 by int value2 positions, unsigned
		instructions.put("lxor", new ByteCodeInstruction (131 ,"83","lxor", null, null)); 									// value1, value2 -> result		bitwise exclusive or of two longs
		instructions.put("monitorenter", new ByteCodeInstruction (194 ,"c2","monitorenter", null, null));						// objectref ->					enter monitor for object ("grab the lock"- start of synchronized() section)
		instructions.put("monitorexit", new ByteCodeInstruction (195 ,"c3","monitorexit", null, null)); 						// objectref ->					exit monitor for object ("release the lock"- end of synchronized() section)
		instructions.put("new", new ByteCodeInstruction (187 ,"bb","new", new int[]{2}, new String[]{"class"}));				// 2: i1, i2 -> objectref		create new object of type identified by class reference in constant pool index (indexbyte1 << 8 + indexbyte2)
		instructions.put("newarray", new ByteCodeInstruction (188 ,"bc","newarray", new int[]{1}, new String[]{"????"}));		// 1: atype count -> arrayref	create new array with count elements of primitive type identified by atype
		instructions.put("nop", new ByteCodeInstruction (0 ,"0","nop", null, null));									 		// [No change]					perform no operation
		instructions.put("pop", new ByteCodeInstruction (87 ,"57","pop", null, null)); 										// value ->						discard the top value on the stack
		instructions.put("pop2", new ByteCodeInstruction (88 ,"58","pop2", null, null)); 										// {value2, value1} ->			discard the top two values on the stack (or one value, if it is a double or long)
		instructions.put("putfield", new ByteCodeInstruction (181 ,"b5","putfield", new int[]{2}, new String[]{"field"}));	// 2: i1, i2 objectref, val ->	set field to value in an object objectref, where the field is identified by a field reference index in constant pool (indexbyte1 << 8 + indexbyte2)
		instructions.put("putstatic", new ByteCodeInstruction (179 ,"b3","putstatic", new int[]{2}, new String[]{"field"}));	// 2: i1, i2 value ->			set static field to value in a class, where the field is identified by a field reference index in constant pool (indexbyte1 << 8 + indexbyte2)
		instructions.put("ret", new ByteCodeInstruction (169 ,"a9","ret", new int[]{1}, new String[]{"local"}));				// 1: index [No change]			continue execution from address taken from a local variable #index (the asymmetry with jsr is intentional)
		instructions.put("return", new ByteCodeInstruction (177 ,"b1","return", null, null)); 								// -> [empty]					return void from method
		instructions.put("saload", new ByteCodeInstruction (53 ,"35","saload", null, null)); 									// arrayref, index -> value		load short from array
		instructions.put("sastore", new ByteCodeInstruction (86 ,"56","sastore", null, null)); 								// arrayref, index, value ->	store short to array
		instructions.put("sipush", new ByteCodeInstruction (17 ,"11","sipush", new int[]{2}, new String[]{"int"})); 			// 2: byte1, byte2 -> value		push a short onto the stack
		instructions.put("swap", new ByteCodeInstruction (95 ,"5f","swap", null, null)); 										// val2, val1 -> val1, val2		swaps two top words on the stack (note that value1 and value2 must not be double or long)
		instructions.put("getfield", new ByteCodeInstruction (180 ,"b4","getfield", new int[]{2}, new String[]{"field"})); 	// 2: i1, i2 objectref -> value			get a field value of an object objectref, where the field is identified by field reference in the constant pool index (index1 << 8 + index2)
		instructions.put("getstatic", new ByteCodeInstruction (178 ,"b2","getstatic", new int[]{2}, new String[]{"field"})); 	// 2: i1, i2 -> value					get a static field value of a class, where the field is identified by field reference in the constant pool index (index1 << 8 + index2)
		instructions.put("goto", new ByteCodeInstruction (167 ,"a7","goto", new int[]{2}, new String[]{"int"})); 				// 2: bb1, bb2 [no change]				goes to another instruction at branchoffset (signed short constructed from unsigned bytes bb1 << 8 + bb2)
		instructions.put("goto_w", new ByteCodeInstruction (200 ,"c8","goto_w", new int[]{4}, new String[]{"int"})); 			// 4: bb1, bb2, bb3, bb4 [no change]	goes to another instruction at branchoffset (signed int constructed from unsigned bytes bb1 << 24 + bb2 << 16 + bb3 << 8 + bb4)
		instructions.put("jsr", new ByteCodeInstruction (168 ,"a8","jsr", new int[]{2}, new String[]{"int"})); 				// 2: bb1, bb2 -> address				jump to subroutine at branchoffset (signed short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2) and place the return address on the stack
		instructions.put("jsr_w", new ByteCodeInstruction (201 ,"c9","jsr_w", new int[]{4}, new String[]{"int"})); 			// 4: bb1, bb2, bb3, bb4 -> address		jump to subroutine at branchoffset (signed int constructed from unsigned bytes branchbyte1 << 24 + branchbyte2 << 16 + branchbyte3 << 8 + branchbyte4) and place the return address on the stack
		instructions.put("checkcast", new ByteCodeInstruction (192 ,"c0","checkcast", new int[]{2}, new String[]{"class"}));	// 2: i1, i2 objectref -> objectref		checks whether an objectref is of a certain type, the class reference of which is in the constant pool at index (indexbyte1 << 8 + indexbyte2)
		instructions.put("athrow", new ByteCodeInstruction (191 ,"bf","athrow", null, null)); 								// objectref -> [empty], objectref		throws an error or exception (notice that the rest of the stack is cleared, leaving only a reference to the Throwable)
		instructions.put("baload", new ByteCodeInstruction (51 ,"33","baload", null, null)); 									// arrayref, index -> value				load a byte or Boolean value from an array
		instructions.put("bastore", new ByteCodeInstruction (84 ,"54","bastore", null, null)); 								// arrayref, index, value ->			store a byte or Boolean value into an array
		instructions.put("dup2", new ByteCodeInstruction (92 ,"5c","dup2", null, null));	 				// {val2, val1} -> {val2, val1}, {val2, val1}				duplicate top two stack words (two values, if value1 is not double nor long; a single value, if value1 is double or long)
		instructions.put("dup2_x1", new ByteCodeInstruction (93 ,"5d","dup2_x1", null, null)); 			// val3, {val2, val1} -> {val2, val1}, val3, {val2, val1}	duplicate two words and insert beneath third word (see explanation above)
		instructions.put("dup2_x2", new ByteCodeInstruction (94 ,"5e","dup2_x2", null, null)); 			// {v4, v3}, {v2, v1} -> {v2, v1}, {v4, v3}, {v2, v1}		duplicate two words and insert beneath fourth word
		instructions.put("dup_x1", new ByteCodeInstruction (90 ,"5a","dup_x1", null, null)); 				// value2, value1 -> value1, value2, value1					insert a copy of the top value into the stack two values from the top. value1 and value2 must not be of the type double or long.
		instructions.put("dup_x2", new ByteCodeInstruction (91 ,"5b","dup_x2", null, null)); 				// val3, val2, val1 -> val1, val3, val2, val1				insert a copy of the top value into the stack two (if value2 is double or long it takes up the entry of value3, too) or three values (if value2 is neither double nor long) from the top
		instructions.put("instanceof", new ByteCodeInstruction (193 ,"c1","instanceof", new int[]{2}, new String[]{"class"}));								// 2: i1, i2 objectref -> result						determines if an object objectref is of a given type, identified by class reference index in constant pool (indexbyte1 << 8 + indexbyte2)
		instructions.put("invokedynamic", new ByteCodeInstruction (186 ,"ba","invokedynamic", new int[]{2,1,1}, new String[]{"method","int","int"})); 		// 4: i1, i2, 0, 0 [arg1, [arg2 ...]] ->				invokes a dynamic method identified by method reference index in constant pool (indexbyte1 << 8 + indexbyte2)
		instructions.put("invokeinterface", new ByteCodeInstruction (185 ,"b9","invokeinterface", new int[]{2,1,1}, new String[]{"method","int","int"}));		// 4: i1, i2, count, 0 objectref, [arg1, arg2, ...] ->	invokes an interface method on object objectref, where the interface method is identified by method reference index in constant pool (indexbyte1 << 8 + indexbyte2)
		instructions.put("invokespecial", new ByteCodeInstruction (183 ,"b7","invokespecial", new int[]{2}, new String[]{"method"}));					 		// 2: i1, i2 objectref, [arg1, arg2, ...] ->			invoke instance method on object objectref, where the method is identified by method reference index in constant pool (indexbyte1 << 8 + indexbyte2)
		instructions.put("invokestatic", new ByteCodeInstruction (184 ,"b8","invokestatic", new int[]{2}, new String[]{"method"})); 							// 2: i1, i2 [arg1, arg2, ...] ->						invoke a static method, where the method is identified by method reference index in constant pool (indexbyte1 << 8 + indexbyte2)
		instructions.put("invokevirtual", new ByteCodeInstruction (182 ,"b6","invokevirtual", new int[]{2}, new String[]{"method"})); 						// 2: i1, i2 objectref, [arg1, arg2, ...] ->			invoke virtual method on object objectref, where the method is identified by method reference index in constant pool (indexbyte1 << 8 + indexbyte2)
		instructions.put("lookupswitch", new ByteCodeInstruction (171 ,"ab","lookupswitch", new int[]{}, new String[]{}));				// 4+: <0-3 bytes padding>, defaultbyte1, defaultbyte2, defaultbyte3, defaultbyte4, npairs1, npairs2, npairs3, npairs4, match-offset pairs... key -> a target address is looked up from a table using a key and execution continues from the instruction at that address
	  	instructions.put("multianewarray", new ByteCodeInstruction (197 ,"c5","multianewarray", new int[]{}, new String[]{}));	 		// 3: indexbyte1, indexbyte2, dimensions count1, [count2,...] -> arrayref create a new array of dimensions dimensions with elements of type identified by class reference in constant pool index (indexbyte1 << 8 + indexbyte2)); the sizes of each dimension is identified by count1, [count2, etc.]
	  	instructions.put("tableswitch", new ByteCodeInstruction (170 ,"aa","tableswitch", new int[]{}, new String[]{})); 					// 4+: [0-3 bytes padding], defaultbyte1, defaultbyte2, defaultbyte3, defaultbyte4, lowbyte1, lowbyte2, lowbyte3, lowbyte4, highbyte1, highbyte2, highbyte3, highbyte4, jump offsets... index -> continue execution from an address in the table at offset index
	  	instructions.put("wide", new ByteCodeInstruction (196 ,"c4","wide", new int[]{}, new String[]{}));								// 3/5: opcode, indexbyte1, indexbyte2 or iinc, indexbyte1, indexbyte2, countbyte1, countbyte2 [same as for corresponding   instructions] execute opcode, where opcode is either iload, fload, aload, lload, dload, istore, fstore, astore, lstore, dstore, or ret, but assume the index is 16 bit; or execute iinc, where the index is 16 bits and the constant to increment by is a signed 16 bit short
	};

	public static ByteCodeInstruction code(String code) {
		return instructions.get(code);
	}

	public static int[] getInts(String[] code) {
		if(code.length > 1) {
			String newOpcode = code[0]+"_"+Integer.parseInt(code[1]);
			if(instructions.containsKey(newOpcode)) {
				return new int[] { instructions.get(newOpcode).opcode() };
			}
		}
		int[] iCode = new int[code.length];
		for(int x = 0; x<code.length; x++) {
			if(instructions.containsKey(code[x]))
				iCode[x] = instructions.get(code[x]).opcode();
			else
				iCode[x] = Integer.parseInt(code[x]);
		}
		return iCode;
	}

	public static String[] optimize(String[] code) {
		if(code.length == 2) {
			String newOpcode = code[0]+"_"+Integer.parseInt(code[1]);
			if(instructions.containsKey(newOpcode)) {
				return new String[] { newOpcode };
			}
			else {
				return code;
			}
		}
		else {
			return code;
		}
	}
}

