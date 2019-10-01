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
			byte[] resultHeader = encodeAnswer(performRequest(operationPacket));
			System.out.println("Request Successful");
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

	private static Response performRequest(DatagramPacket p) throws IOException {
		byte[] buffer = p.getData();
		System.out.println(Arrays.toString(buffer));
		int recLngth = p.getLength();
		int result = 0;
		int operation = -1;
		RequestDecoderBin decoder = new RequestDecoderBin();     	      
		Request op = decoder.decode(p);
		
		operation = op.opCode;

		
		System.out.println("Attempting Request below: ");   
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
		Response tmp = null;
		if(recLngth == op.totalLength) {
			tmp = new Response(op.ID, (byte) 0, result);
		} else {
			tmp = new Response(op.ID, (byte) 127, result);
		}
		return tmp;

	}
	public static byte[] encodeAnswer(Response ans) throws IOException {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(buf);
		out.writeByte(ans.total_length);
		out.writeByte(ans.ID);
		out.writeByte(ans.errorCode);
		out.writeInt(ans.result);

		out.flush();
		return buf.toByteArray();
	}
}
