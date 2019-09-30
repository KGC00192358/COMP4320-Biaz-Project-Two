import java.io.*;

public class OperationEncoderBin implements OperationEncoder, OperationBinConst {
	
	public byte[] encode(Operation op) throws Exception {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(buf);
		out.writeInt(op.totalLength);
		out.writeLong(op.ID);
		out.writeInt(op.op_code);
		out.writeInt(op.numberOperands);
		out.writeShort(op.operand1);
		out.writeShort(op.operand2);
		
		out.flush();
		return buf.toByteArray();
	}
}