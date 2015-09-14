package zsl.ex2.wifip2p.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BackHomeReceiver extends BroadcastReceiver {
    public static final String BACKHOMERECEIVER_ACTION="zsl.ex2.wifip2p.receiver.backhomereceiver.action";
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		 arg0.startActivity(intent) ;
		
	}

}
