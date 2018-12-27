package clientCmd;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Set;

import cmd.CmdChat;
import entity.Cmd;
import utils.GenerateUtils;
import utils.SerializableUtils;

public class Client extends AbstractClient {
	
	public Client(){
		
	}
	
	public Client(String ip, int port){
		super(ip, port);
	}
	
	
	private boolean showLog = true;
	public void setShowLog(boolean showLog) {
		this.showLog = showLog;
	}

	public void send(String cmdStr, byte[] data){
    	try {
    		if(null!=super.connChannel && super.connChannel.isConnected()){
    			Cmd cmd = new Cmd();
    			cmd.setUuid(super.uuid);
    			cmd.setData(data);
    			cmd.setCmd(cmdStr);
    			cmd.setSessionid(GenerateUtils.UUID().substring(0, 10));
    			cmd.start = System.currentTimeMillis();
    			
    			byte[] sendData = SerializableUtils.inputStream(cmd);
    			ByteBuffer send = ByteBuffer.wrap(sendData);
    			
    			if(showLog){
    				System.out.println("send length :" + sendData.length);
    			}
    			
    			super.connChannel.write(send);
    		}else{
    			System.out.println("isConnected false");
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	

	public void send(byte[] data){
    	send(null, data);
    }
	
	public void send(String cmdStr){
    	send(cmdStr, null);
    }
	
	
	@Override
	public void callback(byte[] data) {
		try {
			Cmd cmd = (Cmd) SerializableUtils.outputStream(data);
			cmdMake(cmd);
			
			if(showLog){
				String time = cmd.start==0 ? "" : (System.currentTimeMillis() - cmd.start) + "ms";
				String sms = uuid + " " + cmd.getSessionid() + " " + new String(cmd.getData()) + time;
				System.out.println("return info : " + sms);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private void cmdMake(Cmd cmd) throws Exception{
		String cmdStr = cmd.getCmd();
		//
		if(CmdChat.LS.equals(cmdStr)){
			String[] dataArr = new String(cmd.getData()).split(";");
			System.out.println("============");
			for (String key : dataArr) {
				System.out.println(key);
			}
			System.out.println("============");
		}
		else if(CmdChat.SEND_IN.equals(cmdStr)){
			System.out.println("info by [" + cmd.getUuid() + "] : " + new String(cmd.getData()));
		}
		else if(CmdChat.SYSTEM_INFO.equals(cmdStr)){
			System.out.println(new String(cmd.getData()));
		}
	}
	
	
	
	
	
	

}
