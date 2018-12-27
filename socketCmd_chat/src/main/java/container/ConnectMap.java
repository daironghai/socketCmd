package container;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import entity.Cmd;
import utils.GenerateUtils;
import utils.SerializableUtils;

public class ConnectMap {

	
	public static final Map<String, SocketChannel> connectMap = new HashMap<String,SocketChannel>();
	
	
	
	
	
	public static int size(){
		if(null!=connectMap){
			return connectMap.size();
		}
		return 0;
	}
	
	public static void add(String uuid, SocketChannel channel){
		if(null==channel && !channel.isConnected()){
			return;
		}
		if(null!=uuid && !"".equals(uuid)
				&& !connectMap.containsValue(channel)){
			System.out.println("conn : " + uuid);
			connectMap.put(uuid, channel);
		}
	}
	
	public static void add(SocketChannel channel){
		add(GenerateUtils.UUID(), channel);
	}
	
	public static Map<String, SocketChannel> get(){
		return connectMap;
	}
	
	public static SocketChannel get(String uuid){
		if(connectMap.containsKey(uuid)){
			return connectMap.get(uuid);
		}
		return null;
	}
	
	public static void removeAll(){
		connectMap.clear();
	}
	
	public static void remove(String uuid){
		connectMap.remove(uuid);
	}
	
	public static void remove(SocketChannel channel){
		if(null!=connectMap && connectMap.containsValue(channel)){
			String key = "";
			Iterator<Entry<String, SocketChannel>> it = connectMap.entrySet().iterator();
			while(it.hasNext()){
				Entry<String, SocketChannel> item = it.next();
				if(item.getValue()==channel){
					it.remove();
				}
			}
		}
	}
	
	
	public static void broadcast(byte[] data){
		if(null!=connectMap && connectMap.size()>0){
			Iterator<Entry<String, SocketChannel>> it = connectMap.entrySet().iterator();
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
		if(null!=connectMap && connectMap.size()>0){
			if(connectMap.containsKey(uuid)){
				SocketChannel channel = connectMap.get(uuid);
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
	
	public static void send(String target, Cmd cmd){
		try {
			send(target, SerializableUtils.inputStream(cmd));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
