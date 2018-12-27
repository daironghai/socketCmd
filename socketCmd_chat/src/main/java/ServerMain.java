import serverCmd.Server;
import serverCmd.cmd.S_InputCmd1;

public class ServerMain {

	public static void main(String[] args) throws Exception {
		
		int port = 10086;
		
		Server server = new Server(port);
		server.startRun();
		//server.stopRun();
		
		Thread user = new Thread(new S_InputCmd1(server));
		user.start();
		
	}
	
}
