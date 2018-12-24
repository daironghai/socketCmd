package clientCmd;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import entity.Cmd;
import utils.GenerateUtils;
import utils.SerializableUtils;

public class Client extends AbstractClient {
	
	public Client(){
		
	}
	
	public Client(String ip, int port){
		super(ip, port);
	}
	
	//only one
	private SocketChannel connChannel;
	private String uuid;
	
	@Override
	public void setConn(SocketChannel connChannel) {
		this.connChannel = connChannel;
	}
	@Override
	public void retUuid(String uuid) {
		this.uuid = uuid;
	}
	
	
	public void send(byte[] data){
    	try {
    		if(null!=connChannel && connChannel.isConnected()){
    			Cmd cmd = new Cmd();
    			cmd.setUuid(uuid);
    			cmd.setData(data);
    			cmd.setSessionid(GenerateUtils.UUID().substring(0, 10));
    			cmd.start = System.currentTimeMillis();
    			
    			ByteBuffer send = ByteBuffer.wrap(SerializableUtils.inputStream(cmd));
    			connChannel.write(send);
    		}else{
    			System.out.println("isConnected false");
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	
	@Override
	public void callback(byte[] data) {
		try {
			Cmd cmd = (Cmd) SerializableUtils.outputStream(data);
			
			String sms = uuid + " " + cmd.getSessionid() + " " + new String(cmd.getData()) + " " + (System.currentTimeMillis() - cmd.start) + "ms";
			System.out.println(sms);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	













	

}
