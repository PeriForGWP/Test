package zsl.ex2.wifip2p;



import zsl.ex2.wifip2p.net.SocketServer;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ServerActivity extends Activity implements Runnable {
	private SocketServer server;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        server=new SocketServer();
        Thread th=new Thread(this);
        th.start();        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	public void run() {
		// TODO Auto-generated method stub
		server.startServer();
	}
}
