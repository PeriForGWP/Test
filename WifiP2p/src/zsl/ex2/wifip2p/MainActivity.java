package zsl.ex2.wifip2p;



import zsl.ex2.wifip2p.net.SocketClient;
import zsl.ex2.wifip2p.net.SocketServer;
import zsl.ex2.wifip2p.receiver.BackHomeReceiver;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private SocketServer server;
	private BackHomeReceiver bhr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bts=(Button) findViewById(R.id.button1);
        Button btc=(Button) findViewById(R.id.button2);
        bts.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				server=new SocketServer();
				new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						server.startServer();
					}
					
				}).start();
			}        	
        });
        
        btc.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
			
				new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						new SocketClient();
						
					}
					
				}).start();
			
			   
			}        	
        });
    }

    
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
		IntentFilter intf = new IntentFilter();
        intf.addAction(BackHomeReceiver.BACKHOMERECEIVER_ACTION);
        bhr= new BackHomeReceiver();
        registerReceiver(bhr, intf);
               super.onResume();
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		this.unregisterReceiver(bhr);
	}
	
	

}
