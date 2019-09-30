import java.io.*;

public class OperationEncoderBin implements OperationEncoder, OperationBinConst {
	
	public byte[] encode(Operation op) throws Exception {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(buf);
		out.writeByte(op.totalLength);
		out.writeByte(op.ID);
		out.writeByte(op.opCode);
		out.writeByte(op.numberOperands);
		out.writeShort(op.operandOne);
		out.writeShort(op.operandTwo);
		
		out.flush();
		return buf.toByteArray();
	}
}
