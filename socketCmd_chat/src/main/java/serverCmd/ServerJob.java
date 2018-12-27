package serverCmd;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Set;

import cmd.CmdChat;
import container.ConnectMap;
import entity.Cmd;
import utils.SerializableUtils;
import utils.StringUtils;

public class ServerJob extends Thread {

	
	private Cmd cmd;
	private SocketChannel conn;
	
	
	public ServerJob(Cmd cmd, SocketChannel conn){
		this.cmd = cmd;
		this.conn = conn;
	}
	
	
	@Override
	public void run() {
		long start = System.currentTimeMillis();
		
		try {
			String uuid = cmd.getUuid();
			String sessionid = cmd.getSessionid();
			String cmdStr = cmd.getCmd();
			String data = new String(cmd.getData());
			
			cmdMake();
			
			//ret
			if(conn.isConnected()){
    			//log run info
    			String sms = uuid + "; " 
    					+ sessionid + "; " 
    	    			+ cmdStr + "; " 
    					+ data + "; " 
    					+ (System.currentTimeMillis() - start) + "ms";
    			System.out.println(sms);
    			//return cmd
    			ByteBuffer send = ByteBuffer.wrap(SerializableUtils.inputStream(cmd));
    			conn.write(send);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void cmdMake(){
		try {
			String cmdStr = cmd.getCmd();
			//
			if(CmdChat.SEND.equals(cmdStr)){
				String data = new String(cmd.getData());
				int index = data.indexOf(" ");
				
				Cmd targetCmd = cmd.clone();
				targetCmd.setCmd(CmdChat.SEND_IN);
				targetCmd.setData(data.substring(index + 1).getBytes());
				
				ConnectMap.send(data.substring(0, index), targetCmd);
			}
			else if(CmdChat.LS.equals(cmdStr)){
				Set<String> keySet = ConnectMap.get().keySet();
				cmd.setData(StringUtils.join(keySet, ";").getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
}
