package container;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import utils.GenerateUtils;

public class ConnectMap {

	
	public static final ThreadLocal<Map<String, SocketChannel>> connectMap = new ThreadLocal<Map<String,SocketChannel>>();
	
	
	
	static{
		if(null==connectMap.get()){
			connectMap.set(new HashMap<String, SocketChannel>());
		}
	}
	
	
	public static int size(){
		if(null!=connectMap){
			return connectMap.get().size();
		}
		return 0;
	}
	
	public static void add(String uuid, SocketChannel channel){
		if(null==channel && !channel.isConnected()){
			return;
		}
		if(null!=uuid && !"".equals(uuid)
				&& !connectMap.get().containsValue(channel)){
			System.out.println("conn : " + uuid);
			connectMap.get().put(uuid, channel);
		}
	}
	
	public static void add(SocketChannel channel){
		add(GenerateUtils.UUID(), channel);
	}
	
	public static Map<String, SocketChannel> get(){
		return connectMap.get();
	}
	
	public static SocketChannel get(String uuid){
		if(connectMap.get().containsKey(uuid)){
			return connectMap.get().get(uuid);
		}
		return null;
	}
	
	public static void removeAll(){
		connectMap.get().clear();
	}
	
	public static void remove(String uuid){
		connectMap.get().remove(uuid);
	}
	
	public static void remove(SocketChannel channel){
		if(null!=connectMap.get() && connectMap.get().containsValue(channel)){
			String key = "";
			Iterator<Entry<String, SocketChannel>> it = connectMap.get().entrySet().iterator();
			while(it.hasNext()){
				Entry<String, SocketChannel> item = it.next();
				if(item.getValue()==channel){
					it.remove();
				}
			}
		}
	}
	
	
	public static void broadcast(byte[] data){
		if(null!=connectMap.get() && connectMap.get().size()>0){
			Iterator<Entry<String, SocketChannel>> it = connectMap.get().entrySet().iterator();
			while(it.hasNext()){
				Entry<String, SocketChannel> item = it.next();
				SocketChannel channel = item.getValue();
				if(channel.isOpen()){
					try {
						channel.write(ByteBuffer.wrap(data));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	public static void broadcast(String data){
		broadcast(data.getBytes());
	}
	
	
	public static void send(String uuid, byte[] data){
		System.out.println(connectMap.get());
		if(null!=connectMap.get() && connectMap.get().size()>0){
			if(connectMap.get().containsKey(uuid)){
				SocketChannel channel = connectMap.get().get(uuid);
				if(channel.isOpen()){
					try {
						channel.write(ByteBuffer.wrap(data));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	public static void send(String uuid, String data){
		send(uuid, data.getBytes());
	}
	
	
	
}
