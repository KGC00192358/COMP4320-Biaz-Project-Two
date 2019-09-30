import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException
import java.util.Scanner;

public class ClientSide {

		final static char NOT = '~';
	public static void main(String args[]) throws Exception {

		if (args.length != 2 && args.length != 3)  // Test for correct # of args        
			throw new IllegalArgumentException("Parameter(s): <Destination>" +
					" <Port> [<encoding]");


		InetAddress destAddr = InetAddress.getByName(args[0]);  // Destination address
		int destPort = Integer.parseInt(args[1]);               // Destination port

		while(true) { 
			DatagramSocket sock = new DatagramSocket(); // UDP socket for sending

			Operation op = ClientSide.getOperation();
			OperationEncoderBin encoder = new OperationEncoderBin();
			byte[] operationHeader = encoder.encode(op);

			DatagramPacket message = new DatagramPacket(operationHeader, operationHeader.length, 
					destAddr, destPort);
			sock.send(message);

			sock.close();
		}
	}
	public static Operation getOperation(){
		Operation retOp;
		long ID = (int) Math.random() * 9999 + 1000;	
		char operation;
		short operand1 = 0;
		short operand2 = 0;
		int op_code;
		System.out.println("Please enter your operation(+ - * / >> << ~) ");
		Scanner in = new Scanner(System.in);
		operation = in.next().charAt(0);
		if (operation == NOT) {
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
			retOp = new Operation(ID, op_code, 1, operand1, (short) 0);
		} else {
			retOp = new Operation(ID, op_code, 2, operand1, operand2);
		}
		return retOp;

	}
}
