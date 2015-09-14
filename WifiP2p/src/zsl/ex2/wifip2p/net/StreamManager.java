package zsl.ex2.wifip2p.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
/**
 * 管理消息发送的输入输出流管道类，客户端和服务端均使用该类来管理流io管道。对于一个socket来说每一次通信均由这两对管道进行。
 * 
 */
public class StreamManager {
	public static final char TYPEIN_BUFFEREDREADER='0';//用于字符流读入
	public static final char TYPEIN_DATAINPUT='1';//用于字节流读入	
	
	public static final char TYPEOUT_PRINTWRITER='Z';//用于字符流输出
	public static final char TYPEOUT_DATAOUTPUT='Y';//用于字节流输出
	
	private static final int BUFFER_SIZE=8192*2;//缓冲区大小,系统默认值8192characters
	
	private Socket _socket; 
	
	private BufferedReader _bufferedReader;
	private DataInputStream _dataInputStream;
	
	private PrintWriter _printWriter;
	private DataOutputStream _dataOutputStream;	
	

	
	StreamManager(Socket socket) { //
		_socket=socket;
		
		
			/* 发生阻塞错误，必须一端先In另一端out，
				if(inout)//每个socket对应一对io流，客户端服务器端各有一对，其建立方式因为使用了锁因此要求建立的顺序要交叉。inout为真表明建立顺序是先in后out
				{
					mObjectInputStream=new ObjectInputStream(new BufferedInputStream(mSocket.getInputStream()));
					System.out.println("input");
				//	mObjectOutputStream=new ObjectOutputStream(new BufferedOutputStream(mSocket.getOutputStream()));
				//	System.out.println("output");
				}else{
					mObjectOutputStream=new ObjectOutputStream(new BufferedOutputStream(mSocket.getOutputStream()));
					System.out.println("output");
				//	mObjectInputStream=new ObjectInputStream(new BufferedInputStream(mSocket.getInputStream()));
				//	System.out.println("input");
				}
			*/	
					
	}
	
	public void initInput(char type) throws IOException {
		switch(type){
		case TYPEIN_BUFFEREDREADER:
			_bufferedReader=new BufferedReader(new InputStreamReader(_socket.getInputStream()));
			//_bufferedReader=new BufferedReader(new InputStreamReader(_socket.getInputStream()),BUFFER_SIZE);
			break;
		case TYPEIN_DATAINPUT:
			_dataInputStream=new DataInputStream(_socket.getInputStream());
			break;
		}
		
	}
	
	public void initOutput(char type) throws IOException{
		switch(type){
		case TYPEOUT_PRINTWRITER:
			_printWriter=new PrintWriter((_socket.getOutputStream()));
			break;
		case TYPEOUT_DATAOUTPUT:
			_dataOutputStream=new DataOutputStream(_socket.getOutputStream());
			break;
		}
	}
	
	public boolean checkPrintWriterError(){
		return _printWriter.checkError();
	}
	
	//发送一个消息对象
	public void sendData(Object object,char type) {
		switch(type){
		case TYPEOUT_PRINTWRITER:
			_printWriter.print((String)object);
			_printWriter.flush();
			break;
		case TYPEOUT_DATAOUTPUT:
			try {
				_dataOutputStream.write((byte[])object);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		
	}
	
	
	//接受一个消息(字符串或字节数组)
		public Object receiveData(char type) throws IOException {		
			 
					switch(type){
			        case TYPEIN_BUFFEREDREADER:	
							return (String)_bufferedReader.readLine();					
				     case TYPEIN_DATAINPUT:
					     byte[] b=new byte[this.BUFFER_SIZE*2];
					     int len=1234;				
					     _dataInputStream.readFully(b, 0, len);
					     return (byte[])b;
				   }
			 			
			return null;		
		}
	
	
	//关闭io流，
	public void close(char type){
		try {
			switch(type){
			case TYPEIN_BUFFEREDREADER:
				_bufferedReader.close();
				break;
			case TYPEIN_DATAINPUT:
				_dataInputStream.close();
				break;
			case TYPEOUT_PRINTWRITER:
				_printWriter.close();
				break;
			case TYPEOUT_DATAOUTPUT:			
					_dataOutputStream.close();			
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
