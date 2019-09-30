import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException
import java.util.Scanner;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class ClientUDP {

	final static char NOT = '~';
	final static char SHIFTR = '>';
	final static char SHIFTL = '<';
	public static void main(String args[]) throws Exception {

		if (args.length != 2 && args.length != 3)  // Test for correct # of args        
			throw new IllegalArgumentException("Parameter(s): <Destination>" +
					" <Port> [<encoding]");


		InetAddress destAddr = InetAddress.getByName(args[0]);  // Destination address
		int destPort = Integer.parseInt(args[1]);               // Destination port
		int recPort = 10024;
		while(true) { 
			
			DatagramSocket sock = new DatagramSocket();
			Operation op = ClientSide.getOperation();
			OperationEncoderBin encoder = new OperationEncoderBin();
			byte[] operationHeader = encoder.encode(op);
			long startTime = System.nanoTime();
			if(sendPacket(sock, destAddr, destPort, operationHeader)) {	
				System.out.println("Sent operation: " + op);
			}
			DatagramPacket answerPacket = receivePacket(sock);
			long endTime = System.nanoTime();
			System.out.println("Recieved a answer packet: ");
			handleAnswer(answerPacket);
			long elaspedTime = endTime - startTime; 
			long elaspedTimeNoServer = endTime - startTime - 2000000000; //two is the server wait time
			System.out.println("Total Ellapsed Time: " + elaspedTime / 1000000 + " ms");
			System.out.println("Elasped Time without server wait: " + elaspedTimeNoServer / 1000000 + " ms");
			sock.close();

		}
	}

	private static boolean sendPacket(DatagramSocket sock, InetAddress ip, int port, byte[]header) {
		try {
			DatagramPacket outPacket = new DatagramPacket(header, header.length, ip, port);
			sock.send(outPacket);
		} catch (Exception e) {
			return false;
		}
		return true;

	}
	private static DatagramPacket receivePacket(DatagramSocket sock) throws Exception {

		DatagramPacket packet = new DatagramPacket(new byte[7],7);
		System.out.println("Awaiting operation");
		sock.receive(packet); 
		return packet;

	}

	public static Operation getOperation(){
		Operation retOp;
		byte ID = (byte) 1;	
		char operation;
		short operand1 = 0;
		short operand2 = 0;
		byte op_code;
		System.out.println("Please enter your operation(+ - * / >> << ~) ");
		Scanner in = new Scanner(System.in);
		operation = in.next().charAt(0);
		if (operation == NOT || operation == SHIFTL || operation == SHIFTR) {
			System.out.println("Please input your operand: ");
			operand1 = in.nextShort();
		} else {
			System.out.println("Please input your first operand: ");
			operand1 = in.nextShort();
			System.out.println("Please input your second operand: ");
			operand2 = in.nextShort();
		}
		switch (operation) {
			case '+':
				op_code = 0;
				break;
			case '-':
				op_code = 1;
				break;
			case '*':
				op_code = 2;
				break;
			case '/':
				op_code = 3;
				break;
			case '>':
				op_code = 4;
				break;
			case '<':
				op_code = 5;
				break;
			case '~':
				op_code = 6;
				break;		
			default:
				op_code = 7;
				break;
		}
		if (op_code == 6) {
			retOp = new Operation(ID, op_code, (byte) 1, operand1, (short) 0);
		} else {
			retOp = new Operation(ID, op_code, (byte) 2, operand1, operand2);
		}
		return retOp;

	}
	private static void handleAnswer(DatagramPacket p) throws IOException {
		byte[] buffer = p.getData();
		System.out.println(Arrays.toString(buffer));
		ByteArrayInputStream payload =
			new ByteArrayInputStream(p.getData(), p.getOffset(), p.getLength());
		DataInputStream src = new DataInputStream(payload);
		byte length = src.readByte();
		byte id = src.readByte();
		byte errorCode = src.readByte();
		int answer = src.readInt();
		System.out.println("Answer for request ID number " + id + ": " + answer);

	}
	
}
