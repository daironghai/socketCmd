package clientCmd.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import clientCmd.Client;
import cmd.CmdChat;
import utils.GenerateUtils;
import utils.StringUtils;

public class C_InputCmd1 implements Runnable {
	
	
	private Client client;
	
	
	public C_InputCmd1(Client client){
		this.client = client;
	}
	
	
	@Override
	public void run() {
		while(true){
			String txt = new Scanner(System.in).nextLine();
			if(null==txt || "".equals(txt.trim())){
				continue;
			}
			String[] cmdArr = txt.split(";");
			//
			List<String[]> cmdList = new ArrayList<>();
			for (String cmd : cmdArr) {
				String[] cmdArgs = cmd.split(" ");
				if(cmdArgs.length > 0){
					cmdList.add(cmdArgs);
				}
			}
			//
			for (String[] excCmd : cmdList) {
				try {
					excCmdStr(excCmd);
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}
	}

	
	private void excCmdStr(String[] cmdArr) throws Exception {
		String frist = cmdArr[0];
		
		if(CmdChat.SEND.equals(frist)){
			if(null != cmdArr[1]){
				client.send(
					CmdChat.SEND,
					StringUtils.join(Arrays.copyOfRange(cmdArr, 1, cmdArr.length), " ").getBytes()
				);
			}
		}else if(CmdChat.LS.equals(frist)){
			client.send(CmdChat.LS);
		}
		else if(CmdChat.UUID.equals(frist)){
			System.out.println(client.getUuid());
		}
		else if(CmdChat.STOP.equals(frist)){
			client.close();
		}
//		else if(CmdChat.START.equals(frist)
//				|| CmdChat.RE_START.equals(frist)){
//			System.out.println("start");
//			client.startRun();
//		}
	}
	
	
	
	
	
	
	
	
}
