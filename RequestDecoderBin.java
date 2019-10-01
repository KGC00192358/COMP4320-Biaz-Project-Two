import java.io.*;
import java.net.DatagramPacket;

public class RequestDecoderBin implements RequestDecoder, RequestBinConst {
	
	
	public Request decode(InputStream wire) throws IOException {
		DataInputStream src = new DataInputStream(wire);
		byte length = src.readByte();
		byte ID	= src.readByte();
		byte op_code = src.readByte();
		byte numberOfOperands = src.readByte();
		short operandOne = src.readShort();
		short operandTwo = src.readShort();
		
		return new Request(ID, op_code, numberOfOperands, operandOne, operandTwo);
	}
	
	public Request decode(DatagramPacket p) throws IOException {
		ByteArrayInputStream payload = new ByteArrayInputStream(p.getData(), p.getOffset(), p.getLength());
		return decode(payload);
	}
}
