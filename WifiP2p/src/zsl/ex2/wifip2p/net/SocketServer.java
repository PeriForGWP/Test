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
	//�������˿�
    private static final int SERVERPORT = 55555; 
    //�ͻ�������
    private static List<Socket> mClientList = new ArrayList<Socket>(); 
    //�̳߳�
    private ExecutorService mExecutorService;  

    private  ServerSocket mServerSocket ; 
    //private ServerSocket mControllerServerSocket;
    //����������
    

 
    private boolean _stopFlag=false;    
    
   
	public SocketServer( )
	{
		//�������粢����		
		try
		{
			//���÷������˿�
			 mServerSocket = new ServerSocket(SERVERPORT);
			//����һ���̳߳�
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
			//������ʱ����ͻ������ӵ�Socket����
			Socket client = null;
			//���տͻ����Ӳ���ӵ�list��
			try {
				if(mServerSocket!=null){
					client = mServerSocket.accept();
					if(client!=null){
						mClientList.add(client);
						//����һ���ͻ����߳�
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
				stopServer();//�ݹ�ֱ��ȫ��client���
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
	
    //ÿ���ͻ��˵�������һ���߳�
	static class ThreadServer implements Runnable
	{
		private Socket			_socket; //

		public ThreadServer(Socket socket) 
		{
			this._socket = socket;//�ͻ���socket
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
