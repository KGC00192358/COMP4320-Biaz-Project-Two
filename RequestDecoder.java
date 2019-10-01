import java.io.*;
import java.net.*;

public interface RequestDecoder {
	Request decode(InputStream source) throws IOException;
	Request decode(DatagramPacket packet) throws IOException;
}
