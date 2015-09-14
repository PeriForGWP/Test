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
 * ������Ϣ���͵�����������ܵ��࣬�ͻ��˺ͷ���˾�ʹ�ø�����������io�ܵ�������һ��socket��˵ÿһ��ͨ�ž��������Թܵ����С�
 * 
 */
public class StreamManager {
	public static final char TYPEIN_BUFFEREDREADER='0';//�����ַ�������
	public static final char TYPEIN_DATAINPUT='1';//�����ֽ�������	
	
	public static final char TYPEOUT_PRINTWRITER='Z';//�����ַ������
	public static final char TYPEOUT_DATAOUTPUT='Y';//�����ֽ������
	
	private static final int BUFFER_SIZE=8192*2;//��������С,ϵͳĬ��ֵ8192characters
	
	private Socket _socket; 
	
	private BufferedReader _bufferedReader;
	private DataInputStream _dataInputStream;
	
	private PrintWriter _printWriter;
	private DataOutputStream _dataOutputStream;	
	

	
	StreamManager(Socket socket) { //
		_socket=socket;
		
		
			/* �����������󣬱���һ����In��һ��out��
				if(inout)//ÿ��socket��Ӧһ��io�����ͻ��˷������˸���һ�ԣ��佨����ʽ��Ϊʹ���������Ҫ������˳��Ҫ���档inoutΪ���������˳������in��out
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
	
	//����һ����Ϣ����
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
	
	
	//����һ����Ϣ(�ַ������ֽ�����)
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
	
	
	//�ر�io����
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
