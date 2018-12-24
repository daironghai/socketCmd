package serverCmd;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import container.ConnectMap;

public abstract class AbstractServer extends Thread {
	
	//
	private int defaultPort = 10086;
	//
    private ServerSocketChannel serverSocketChannel = null;
    private Selector selector = null;
	//
	private boolean isRun = true;
	
	
	public AbstractServer(){
		
	}
	
	public AbstractServer(int port){
		this.defaultPort = port;
	}
	
	public void startRun(){
		this.isRun = true;
		super.start();
	}
	
	public void stopRun(){
		this.isRun = false;
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
		closeSocket();
		super.stop();
	}
	
	
	private void init(int port){
		try {
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
	        serverSocketChannel.socket().bind(new InetSocketAddress(port));
	        serverSocketChannel.configureBlocking(false);
	        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
	        System.out.println("init : " + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public abstract boolean validate(byte[] data);
    public abstract void callback(byte[] data);
	
	
	@Override
	public void run() {
		try {
			init(defaultPort);
			if(null==selector){
				System.out.println("selector is null");
				isRun = false;
			}
			while (isRun) {
				int size = selector.select();
				if(size == 0){
					continue;
				}
				Iterator ite = this.selector.selectedKeys().iterator();
				while (ite.hasNext()) {
					SelectionKey key = (SelectionKey) ite.next();
					ite.remove();
					if(key.isAcceptable()){
						ServerSocketChannel server = (ServerSocketChannel) key.channel();
						SocketChannel channel = server.accept();
						try {
							try {
								ByteBuffer buffer = ByteBuffer.allocate(64);
								channel.read(buffer);
								byte[] data = buffer.array();
						        if(!validate(data)){
									channel.close();
									closeConn(key, "no check info 1");
									ConnectMap.remove(channel);
						        }else{
									channel.configureBlocking(false);
									channel.register(selector, SelectionKey.OP_READ);
						        	ConnectMap.add(new String(data).trim(), channel);
						        }
							} catch (Exception e) {
								channel.close();
								closeConn(key, "no check info 2");
								ConnectMap.remove(channel);
							}
						} catch (Exception e) {
							channel.close();
							closeConn(key, e.getMessage());
							ConnectMap.remove(channel);
						}
					}
					else if(key.isReadable()){
						SocketChannel channel = (SocketChannel) key.channel();
						try {
							ByteBuffer buffer = ByteBuffer.allocate(1024);
							channel.read(buffer);
							byte[] data = buffer.array();
							callback(data);
						} catch (Exception e) {
							closeConn(key, e.getMessage());
							ConnectMap.remove(channel);
						}
					}
				}
			}
			//
			closeSocket();
		} catch (Exception e) {
			e.printStackTrace();
			closeSocket();
		}
		System.out.println("run end");
	}

	private void closeConn(SelectionKey key, String info){
		try {
			key.cancel();
			key.channel().close();
			System.out.println(info);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void closeSocket(){
		try {
			if(null!=selector){
				selector.close();
				selector = null;
			}
			if(null!=serverSocketChannel){
				serverSocketChannel.close();
				serverSocketChannel = null;
			}
			ConnectMap.removeAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
