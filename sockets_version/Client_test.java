import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;

public class Client_test {

	public static void main (String args[])
	{
		try {
			Socket socket = new Socket("localhost", 32000);
			DataOutputStream msg_to_send = new DataOutputStream(socket.getOutputStream());
			msg_to_send.writeInt(320000);
			msg_to_send.writeInt(24);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}	
}