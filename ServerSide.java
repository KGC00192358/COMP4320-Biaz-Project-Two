import java.net.*;  // for DatagramSocket and DatagramPacket

import com.sun.org.apache.xpath.internal.operations.Operation;

import java.io.*;   // for IOException

public class ServerSide {

  public static void main(String[] args) throws Exception {

      if (args.length != 1 && args.length != 2)  // Test for correct # of args        
	  throw new IllegalArgumentException("Parameter(s): <Port> [<encoding>]");
      
      int port = Integer.parseInt(args[0]);   // Receiving Port
     	while(true) { 
      DatagramSocket sock = new DatagramSocket(port);  // UDP socket for receiving      
      DatagramPacket packet = new DatagramPacket(new byte[1024],1024);
      sock.receive(packet); 
      
      byte[] resultPacket = performOperation(packet);
            
      sock.send(resultPacket);
      
      sock.close();
	}
     	public static byte[] performOperation(DatagramPacket p) {
     		int recLngth = p.getLength()
     		int result = 0;
     		int operation = -1;
     		OperationDecoderBin decoder = new OperationDecoderBin();     	      
     	      Operation op = decoder.decoder(p);
     	      operation = op.opCode;
     	      
     	      switch (operation) {
     	      case(0):
     	    	  result = op.operandOne + op.operandTwo;
     	      	break;
     	      case(1): 
     	    	  result = op.operandOne - op.operandTwo;
     	      	break;
     	      case(3):
     	    	  result = op.operandOne * op.operandTwo;
     	      	  break;
     	      case(4):
     	    	  result = op.operandOne / op.operandTwo;
     	      case(5):
     	    	  break;
     	      case(6):
     	      	break;
     	      default:
     	    	  break;
     	      }
     	      byte[] tmp;
     	      if(recLngth == op.totalLength) {
     	    	   tmp = encodeAnswer(op.ID, result, 0);
     	      } else {
     	    	  tmp = encodeAnser(op.ID, result, 127);
     	      }
     	      return tmp;
     	      

     	}
     	public static byte[] encodeAnswer(int ID, int result, int errorCode) {
     		ByteArrayOutputStream buf = new ByteArrayOutputStream();
    		DataOutputStream out = new DataOutputStream(buf);
    		out.writeInt(7);
    		out.writeInt(ID);
    		out.writeInt(errorCode);
    		out.writeInt(result);
    		
    		out.flush();
    		return buf.toByteArray();
     	}
  }
}
