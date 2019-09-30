import java.io.*;

public class OperationEncoderBin implements OperationEncoder, OperationBinConst {
	
	public byte[] encode(Operation op) throws Exception {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(buf);
		out.writeInt(op.totalLength);
		out.writeLong(op.ID);
		out.writeInt(op.opCode);
		out.writeInt(op.numberOperands);
		out.writeShort(op.operandOne);
		out.writeShort(op.operandTwo);
		
		out.flush();
		return buf.toByteArray();
	}
}
