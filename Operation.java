public class Operation {

	public long ID;            // Item identification number
	int opCode;
	int numberOperands;
	short operandOne;
	short operandTwo;
	int totalLength;
	


	public Operation(long in_ID, int op_Code, int numberOfOperands, short operand1, short operand2)  {
		this.ID           = in_ID;
		this.opCode = op_Code;
		this.numberOperands = numberOfOperands;
		this.operandOne = operand1;
		this.operandTwo = operand2;
		this.totalLength = 4 + 12 + 8; //2 2 Byte numbers(short) + 3 4 Byte numbers(int) + 1 8 byte number (long)
		if (numberOfOperands < 2) {
			this.totalLength -= 2; //only one operand, losing 1 short.
		}
	}

	public String toString() {
		final String EOLN = java.lang.System.getProperty("line.separator");
		String value = "Operation # = " + ID + EOLN +
			"Number of Operands = " + numberOperands + EOLN +
			"First Operand  = " + operandOne + EOLN +
			"Two Operand = " + operandTwo + EOLN;
		switch (opCode){
			case 0:
				value += "Operation = Sum\n";
				break;
			case 1:
				value += "Operation = Difference\n";
				break;
			case 2:
				value += "Operation = Product\n";
				break;
			case 3:
				value += "Operation = Quotient\n";
				break;
			case 4:
				value += "Operation = Shift Right\n";
				break;
			case 5:
				value += "Operation = Shift Left\n";
				break;
			case 6:
				value += "Operation = NOT/Compliment";
				break;
			default:
				value += "Operation not supported"; 
				break;

		}

		return value;
	}
}
