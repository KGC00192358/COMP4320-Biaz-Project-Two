import java.io.*;
import java.net.DatagramPacket;

public class OperationDecoderBin implements OperationDecoder, OperationBinConst {
	
	
	public Operation decode(InputStream wire) throws IOException {
		DataInputStream src = new DataInputStream(wire);
		int length = src.readInt();
		long ID	= src.readLong();
		int op_code = src.readInt();
		int numberOfOperands = src.readInt();
		short operandOne = src.readShort();
		short operandTwo = src.readShort();
		
		return new Operation(ID, op_code, numberOfOperands, operandOne, operandTwo);
	}
	
	public Operation decode(DatagramPacket p) throws IOException {
		ByteArrayInputStream payload = new ByteArrayInputStream(p.getData(), p.getOffset(), p.getLength());
		return decode(payload);
	}
}
