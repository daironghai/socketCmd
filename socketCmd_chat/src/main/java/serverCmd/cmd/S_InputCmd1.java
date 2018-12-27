package serverCmd.cmd;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.Scanner;

import cmd.CmdServer;
import container.ConnectMap;
import entity.Cmd;
import serverCmd.Server;
import utils.SerializableUtils;

public class S_InputCmd1 implements Runnable {

	private Server server;
	
	
	public S_InputCmd1(Server server){
		this.server = server;
	}
	
	
	@Override
	public void run() {
		while(true){
			try {
				String txt = new Scanner(System.in).nextLine();
				String[] cmdArr = txt.split(" ");
				if(cmdArr.length==0){
					continue;
				}
				
				if(CmdServer.IP.equals(cmdArr[0])){
					Collection<SocketChannel> list = ConnectMap.get().values();
					for (SocketChannel conn : list) {
						System.out.println(conn.getLocalAddress().toString());
					}
				}
				else if(CmdServer.REMOVE.equals(cmdArr[0])){
					String uuid = cmdArr[1];
					SocketChannel conn = ConnectMap.get(uuid);
					if(null!=conn){
						Cmd cmd = new Cmd();
						cmd.setUuid(uuid);
						cmd.setCmd(CmdServer.SYSTEM_INFO);
						cmd.setData("server remove your conn".getBytes());
						conn.write(ByteBuffer.wrap(SerializableUtils.inputStream(cmd)));
						Thread.sleep(20);
						conn.close();
						ConnectMap.remove(uuid);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}
	
	
	
}
