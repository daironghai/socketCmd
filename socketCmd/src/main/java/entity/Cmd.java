package entity;


import java.io.Serializable;

public class Cmd implements Serializable {


	private static final long serialVersionUID = 1L;
	
	private String uuid;
	private byte[] data = new byte[1];
	private String sessionid;
	
	public long start;
	public long end;
	
	
	
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
	
	
	
	
	

}
