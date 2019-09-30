import java.net.*;  // for DatagramSocket and DatagramPacket
import java.io.*;   // for IOException

public class ServerSide {

	public static void main (String[] args) throws Exception {
		InetAddress dest = InetAddress.getByName("localhost");

		if (args.length != 1 && args.length != 2)  // Test for correct # of args        
			throw new IllegalArgumentException("Parameter(s): <Port> [<encoding>]");

		int recPort = Integer.parseInt(args[0]);   // Receiving Port
		int destPort = 8080; //outgoing port
		while(true) { 


			DatagramPacket operationPacket = receivePacket(recPort);
			System.out.println("Recieved some shit");
			System.out.println("Attempting Operation");   
			byte[] resultHeader = performOperation(operationPacket);
			System.out.println("Operation Successfull");
			System.out.println("Attempting to send");            
			Thread.sleep(2000);
			if (!sendPacket(dest, destPort, resultHeader)) {
			System.out.println("Sending Failed");
			} else {
			System.out.println("Sent Successfully");
			}

		}


	}

	private static DatagramPacket receivePacket(int port) throws Exception {

		DatagramSocket sock = new DatagramSocket(port);  // UDP socket for receiving      
		DatagramPacket packet = new DatagramPacket(new byte[1024],1024);
		System.out.println("Awaiting operation");
		sock.receive(packet); 
		sock.close();
		return packet;

	}
	private static boolean sendPacket(InetAddress ip, int destPort,  byte[] header) {
                     try {
		     DatagramSocket sock = new DatagramSocket();
		     DatagramPacket outPacket = new DatagramPacket(header, header.length, ip, destPort);
		     sock.send(outPacket);
		     sock.close();
		     } catch (Exception e) {
			return false;
		     }
		     return true;
	}

	private static byte[] performOperation(DatagramPacket p) throws IOException {
		int recLngth = p.getLength();
		int result = 0;
		int operation = -1;
		OperationDecoderBin decoder = new OperationDecoderBin();     	      
		Operation op = decoder.decode(p);
		operation = op.opCode;

		switch (operation) {
			case(0):
				result = op.operandOne + op.operandTwo;
				break;
			case(1): 
				result = op.operandOne - op.operandTwo;
				break;
			case(2):
				result = op.operandOne*op.operandTwo;
				break;
			case(3):
				result = op.operandOne/op.operandTwo;
				break;
			case(4):
				break;
			case(5):
				break;
			case(6):
				break;
			default:
				break;
		}
		byte[] tmp;
		if(recLngth == op.totalLength) {
			tmp = encodeAnswer((byte) 01, result, (byte) 0);
		} else {
			tmp = encodeAnswer((byte) 01, result, (byte) 127);
		}
		return tmp;

	}
	public static byte[] encodeAnswer(byte ID, int result, byte errorCode) throws IOException {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(buf);
		out.writeByte(7);
		out.writeByte(ID);
		out.writeByte(errorCode);
		out.writeInt(result);

		out.flush();
		return buf.toByteArray();
	}
}
