package serverCmd;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import entity.Cmd;
import utils.SerializableUtils;

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
			String data = new String(cmd.getData(), "UTF-8");
			
			if(conn.isConnected()){
    			ByteBuffer send = ByteBuffer.wrap(SerializableUtils.inputStream(cmd));
    			
    			//run info print
    			String sms = uuid + " " 
    					+ cmd.getSessionid() + " " 
    					+ data + " " 
    					+ (System.currentTimeMillis() - start) + "ms";
    			System.out.println(sms);
    			
    			conn.write(send);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
