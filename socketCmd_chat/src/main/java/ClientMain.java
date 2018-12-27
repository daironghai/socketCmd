import clientCmd.Client;
import clientCmd.cmd.C_InputCmd1;
import utils.GenerateUtils;

public class ClientMain {
	
	public static void main(String[] args) {
		
		String ip = "localhost"; //localhost
		int port = 10086;
		
		Client client = new Client(ip, port);
		client.setShowLog(false);
		client.setUuid(GenerateUtils.UUID().substring(0, 2));
		client.startRun();
		
		
		Thread userInput = new Thread(new C_InputCmd1(client));
		userInput.start();
		
		
	}
	
}
