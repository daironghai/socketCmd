import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import clientCmd.Client;
import utils.GenerateUtils;

public class ClientMain {
	
	public static void main(String[] args) {
		
		String ip = "localhost"; //localhost
		int port = 10086;
		
//		String inputIp = new Scanner(System.in).nextLine();
//		if(null!=inputIp && !"".equals(inputIp.trim())){
//			ip = inputIp;
//		}
		
		
		final Map<String, Client> clientMap = new HashMap<String, Client>();
		
		for (int i = 0, count = 1; i < count; i++) {
			
			Client client = new Client(ip, port);
			client.setUuid(GenerateUtils.UUID().substring(0, 6));
			client.startRun();
			
			clientMap.put(client.getUuid(), client);
		}
		
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				while(true){
					String txt = new Scanner(System.in).nextLine();
					Iterator<Entry<String, Client>> it = clientMap.entrySet().iterator();
					while(it.hasNext()){
						Entry<String, Client> item = it.next();
						item.getValue().send(txt.getBytes());
					}
				}
			}
		});
		t2.start();
		
      
	}
	
}
