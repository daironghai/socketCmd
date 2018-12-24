package serverCmd;

import container.ConnectMap;
import entity.Cmd;
import utils.SerializableUtils;

public class Server extends AbstractServer {

	public Server(int port){
		super(port);
	}
	
	
	@Override
	public boolean validate(byte[] data) {
		String validateData = new String(data);
		if(null!=validateData && !"".equals(validateData.trim())){
			return true;
		}
		return false;
	}

	
	@Override
	public void callback(byte[] data) {
		try {
			Cmd cmd = (Cmd) SerializableUtils.outputStream(data);
			ServerJob job = new ServerJob(cmd, ConnectMap.get(cmd.getUuid()));
			job.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	





	
	
	
	
	
}
