import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;

public class Server_test{
	
	public static void main (String args[]) {
		try {
			ServerSocket sock_serv = new ServerSocket(32000);
			Socket sock_comm = sock_serv.accept();
			DataInputStream msg = new DataInputStream (sock_comm.getInputStream ());
			int first_int = msg.readInt();
			int second_int = msg.readInt();
		
			System.out.println(first_int);
			System.out.println(second_int);			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

