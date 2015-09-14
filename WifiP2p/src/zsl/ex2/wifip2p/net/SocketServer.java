package zsl.ex2.wifip2p.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;






import android.util.Log;


public class SocketServer {
	//服务器端口
    private static final int SERVERPORT = 55555; 
    //客户端连接
    private static List<Socket> mClientList = new ArrayList<Socket>(); 
    //线程池
    private ExecutorService mExecutorService;  

    private  ServerSocket mServerSocket ; 
    //private ServerSocket mControllerServerSocket;
    //开启服务器
    

 
    private boolean _stopFlag=false;    
    
   
	public SocketServer( )
	{
		//启动网络并监听		
		try
		{
			//设置服务器端口
			 mServerSocket = new ServerSocket(SERVERPORT);
			//创建一个线程池
			mExecutorService = Executors.newCachedThreadPool();
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}   
	
	
	
	public synchronized void setStopFlag(boolean flag){
		_stopFlag=flag;
	}
	
	public synchronized boolean getStopFlag(){
		return _stopFlag;
	}
	
	public void startServer(){
		setStopFlag(false);	
		
		while (!getStopFlag())
		{
			Log.i("server","canserver start...");
			//用来临时保存客户端连接的Socket对象
			Socket client = null;
			//接收客户连接并添加到list中
			try {
				if(mServerSocket!=null){
					client = mServerSocket.accept();
					if(client!=null){
						mClientList.add(client);
						//开启一个客户端线程
						ThreadServer ts=new ThreadServer(client);
						mExecutorService.execute(ts);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
	}
	
	public void stopServer(){
		setStopFlag(true);
		for(Socket client:mClientList){
			try {				
				Log.i("socketserver","client close");
				client.close();
						
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				stopServer();//递归直到全部client清除
			}
		}
		mClientList.removeAll(mClientList);	
		try {
			mServerSocket.close();
			mServerSocket=null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
    //每个客户端单独开启一个线程
	static class ThreadServer implements Runnable
	{
		private Socket			_socket; //

		public ThreadServer(Socket socket) 
		{
			this._socket = socket;//客户端socket
			Log.i("socketserver","Server Thread be born for client"+socket.getInetAddress());
		}
		
		public void run()
		{			
			Log.i("server:client ",_socket.toString());	
			StreamManager sm=new StreamManager(_socket);	
			try {
				sm.initOutput(StreamManager.TYPEOUT_PRINTWRITER);
				sm.initInput(StreamManager.TYPEIN_BUFFEREDREADER);
				sm.sendData("server is working", StreamManager.TYPEOUT_PRINTWRITER);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			boolean flag=true;
			while(flag){
			
				String msg;
				try {
					msg=(String) sm.receiveData(StreamManager.TYPEIN_BUFFEREDREADER);
					if(msg.length()>0){
					Log.i("server:client ",msg);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			
			}
			
			
			 
				
		}		
		
	}
}
