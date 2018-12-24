package clientCmd;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstractClient extends Thread {
	
	
	private String ip = "localhost";
	private int port = 10086;
	
	private String uuid;
	private SocketChannel socketChannel;
	private SocketChannel connChannel;
    private Selector selector;

    
    public AbstractClient() {
    	init();
    }
    public AbstractClient(String ip, int port) {
    	this.ip = ip;
    	this.port = port;
    	init();
    }
    
    public void init(){
    	try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
        	socketChannel.configureBlocking(false);
        	socketChannel.connect(new InetSocketAddress(ip, port));
        	socketChannel.register(selector, SelectionKey.OP_CONNECT);
        	System.out.println("init client");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void setUuid(String uuid){
    	this.uuid = uuid;
    }
    public String getUuid(){
    	return this.uuid;
    }
    
    public abstract void callback(byte[] data);
    public abstract void setConn(SocketChannel connChannel);
    public abstract void retUuid(String uuid);
    
    
    @Override
	public void run() {
    	try {
            while (true) {
            	int count = selector.select();
            	if(count == 0){
            		continue;
            	}
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = null;
                    try {
                        selectionKey = (SelectionKey) iterator.next();
                        iterator.remove();
                    	SocketChannel channel = (SocketChannel) selectionKey.channel();
                        
                        if (selectionKey.isConnectable()) {
                        	//如果正在连接，则完成连接
                            if(channel.isConnectionPending()){
                                channel.finishConnect();
                            }
                            channel.configureBlocking(false);
                            channel.register(selector, SelectionKey.OP_READ);
                            channel.write(ByteBuffer.wrap(uuid.getBytes()));
                            setConn(channel);
                            connChannel = channel;
                            retUuid(uuid);
                        }
                        else if (selectionKey.isReadable()) {
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            int length = channel.read(buffer);
                            byte[] data = Arrays.copyOf(buffer.array(), length);
                            callback(data);
                        }
                        else{
                        	closeConn(selectionKey);
                        }
                    } catch (Exception e) {
                    	closeConn(selectionKey);
                    }
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
			close();
		}
	}

    
    public void startRun(){
    	this.start();
    	
    }
    

    public void close(){
    	try {
    		if(null!=selector){
            	selector.close();
    		}
    		if(null!=socketChannel){
    			socketChannel.close();
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    public void closeConn(SelectionKey selectionKey){
        try {
            selectionKey.cancel();
            selectionKey.channel().close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    
    
    
}