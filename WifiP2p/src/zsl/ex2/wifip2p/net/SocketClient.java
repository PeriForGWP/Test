package zsl.ex2.wifip2p.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




public class SocketClient {
	private static final int		PORT	= 55555; 
	private static final String     ADDRESS="127.0.0.1";
	private static ExecutorService	exec	= Executors.newCachedThreadPool();
	private Socket _socket;
	private StreamManager _sm;
	
	public SocketClient()
	{
		//StreamManager sm=null;
		//_candata=new CanData();
		try
		{
		
		_socket = new Socket(ADDRESS, PORT);		 
		
		_sm=new StreamManager(_socket);
		 _sm.initInput(StreamManager.TYPEIN_BUFFEREDREADER);
		 _sm.initOutput(StreamManager.TYPEOUT_PRINTWRITER);
		 String str;
			while ((str =(String)_sm.receiveData(StreamManager.TYPEIN_BUFFEREDREADER)) != null)
			{								
				String msg="hello i am client";				
				_sm.sendData(msg, StreamManager.TYPEOUT_PRINTWRITER);
			}	
		
			
		}
		catch (Exception e)
		{
          
          
		}finally{
			
		}
	} 	

	
	
   

}
