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
	
	
    public AbstractClient() {
    	init();
    }
    public AbstractClient(String ip, int port) {
    	this.ip = ip;
    	this.port = port;
    	init();
    }
	
	
	private String ip = "localhost";
	private int port = 10086;
	private boolean run = true;
	
	protected String uuid;
	protected SocketChannel connChannel;
	
	private SocketChannel socketChannel;
    private Selector selector;

    
    public String getUuid(){
    	return this.uuid;
    }
    public void setUuid(String uuid){
    	this.uuid = uuid;
    }

    
    public abstract void callback(byte[] data);
    
    
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
    
    
    @Override
	public void run() {
    	try {
            while (run && selector.select() > 0) {
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
                            channel.write(ByteBuffer.wrap(uuid.getBytes(), 0, uuid.length()));
                            connChannel = channel;
                        }
                        else if (selectionKey.isReadable()) {
                            ByteBuffer buffer = ByteBuffer.allocate(1024 * 10);
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
    		run = false;
    		Thread.sleep(100);
    		if(null!=connChannel){
    			connChannel.close();
    			connChannel = null;
    		}
    		if(null!=selector){
    			if(selector.select() > 0){
    				Set<SelectionKey> keySet = selector.selectedKeys();
    				for (SelectionKey selectionKey : keySet) {
    	        		System.out.println("selectionKey");
    					selectionKey.cancel();
    					selectionKey.channel().close();
					}
    			}
            	selector.close();
            	selector = null;
    		}
    		if(null!=socketChannel){
    			socketChannel.close();
    			socketChannel = null;
    		}
    		this.stop();
		} catch (Exception e) {
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