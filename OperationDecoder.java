import java.io.*;
import java.net.*;

public interface OperationDecoder {
	Operation decode(InputStream source) throws IOException;
	Operation decode(DatagramPacket packet) throws IOException;
}
