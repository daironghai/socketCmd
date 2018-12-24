import serverCmd.Server;

public class ServerMain {

	public static void main(String[] args) throws Exception {
		
		int port = 10086;
		Server server = new Server(port);
		server.startRun();
		
	}
	
}
