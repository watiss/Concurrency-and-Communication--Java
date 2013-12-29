public class Server_test(){
	public void main (String args[]) {
		SocketServer sock_serv = new SocketServer(32000);
		Socket sock_comm = sock_serv.accept();
		DataInputStream msg = new DataInputStream(to_socket.getIntputStream());
		int first_int = msg_to_send.readInt();
		System.out.println(first_int);
	}
}

