import java.net.*;  // for DatagramSocket and DatagramPacket
import java.io.*;   // for IOException
import java.util.*;

public class ServerUDP {

	public static void main (String[] args) throws Exception {
		InetAddress dest = InetAddress.getByName("localhost");

		if (args.length != 1 && args.length != 2)  // Test for correct # of args        
			throw new IllegalArgumentException("Parameter(s): <Port> [<encoding>]");

		int recPort = Integer.parseInt(args[0]);   // Receiving Port
		int destPort = 8080; //outgoing port

		while(true) { 

			
			DatagramSocket sock = new DatagramSocket(recPort);  // UDP socket for receiving      
			DatagramPacket operationPacket = receivePacket(sock);
			System.out.println("Recieved operationPacket");
			byte[] resultHeader = performOperation(operationPacket);
			System.out.println("Operation Successfull");
			System.out.println("Attempting to send");            
			Thread.sleep(2000);
			if (!sendPacket(sock, operationPacket, resultHeader)) {
			System.out.println("Sending Failed");
			} else {
			System.out.println("Sent Successfully");
			}

		sock.close();
		}


	}

	private static DatagramPacket receivePacket(DatagramSocket sock) throws Exception {

		DatagramPacket packet = new DatagramPacket(new byte[8],8);
		System.out.println("Awaiting operation");
		sock.receive(packet); 
		return packet;

	}
	private static boolean sendPacket(DatagramSocket sock, DatagramPacket p,  byte[] header) {
                     try {
		     DatagramPacket outPacket = new DatagramPacket(header, header.length, p.getAddress(), p.getPort());
		     sock.send(outPacket);
		     } catch (Exception e) {
			return false;
		     }
		     return true;
	}

	private static byte[] performOperation(DatagramPacket p) throws IOException {
		byte[] buffer = p.getData();
		System.out.println(Arrays.toString(buffer));
		int recLngth = p.getLength();
		int result = 0;
		int operation = -1;
		OperationDecoderBin decoder = new OperationDecoderBin();     	      
		Operation op = decoder.decode(p);
		
		operation = op.opCode;

		
		System.out.println("Attempting Operation below: ");   
		System.out.println(op);

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
				result = op.operandOne >> 1;
				break;
			case(5):
				result = op.operandOne << 1;
				break;
			case(6):
				result = ~op.operandOne;
				break;
			default:
				break;
		}
		byte[] tmp;
		if(recLngth == op.totalLength) {
			tmp = encodeAnswer(op.ID, result, (byte) 0);
		} else {
			tmp = encodeAnswer(op.ID, result, (byte) 127);
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
