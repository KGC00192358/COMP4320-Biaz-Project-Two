import java.net.*;  // for DatagramSocket and DatagramPacket
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
      
      // Receive binary-encoded friend                              
      // FriendDecoder decoder = new FriendDecoderBin();
      //FriendDecoder decoder = (args.length == 2 ?   // Which encoding              
	//			  new FriendDecoderBin(args[1]) :
	//			  new FriendDecoderBin() 
	byte[] recv = packet.getData();
	String echoMessage = new String(recv);


      //Friend receivedFriend = decoder.decode(packet);

      System.out.println("Received Message");
      System.out.println(echoMessage)
      //System.out.println(receivedFriend);
      
      sock.close();
	}
  }
}
