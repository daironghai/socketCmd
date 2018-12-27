package entity;


import java.io.Serializable;

public class Cmd implements Serializable {


	private static final long serialVersionUID = 1L;

	
	private String cmd;
	private String uuid;
	private String sessionid;
	private byte[] data = new byte[1];

	
	public long start;
	public long end;
	
	
	public Cmd clone(){
		Cmd newCmd = new Cmd();
		newCmd.setCmd(this.cmd);
		newCmd.setUuid(this.uuid);
		return newCmd;
	}
	
	
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		if(null!=data && data.length>0){
			this.data = data;
		}
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	
	

}
