public class Request {

	public byte ID;            // Item identification number
	public byte opCode;
	public byte numberOperands;
	public short operandOne;
	public short operandTwo;
	public byte totalLength;
	


	public Request(byte in_ID, byte op_Code, byte numberOfOperands, short operand1, short operand2)  {
		this.ID           = in_ID;
		this.opCode = op_Code;
		this.numberOperands = numberOfOperands;
		this.operandOne = operand1;
		this.operandTwo = operand2;
		this.totalLength = 8;
		
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
