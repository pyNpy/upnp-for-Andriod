package com.example.kl.dlna;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import static com.example.kl.dlna.FileType.AudioType;


public class MyBroadcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
        AndroidLog.log("receive broadcastreceiver");

		String action = intent.getAction();
		if(TextUtils.equals(action, "Device2TV")) {

            TVOperator tvOperator = (TVOperator) intent.getSerializableExtra(TVOperator.getTag());
            Bundle bundle = new Bundle();
            bundle.putSerializable(tvOperator.getTag(), tvOperator);
            switch (tvOperator.getMediatype()) {
                case Image:
                case Audio:
                case Video:    //...
                    Message message = new Message();
                    message.what = 200;
                    message.setData(bundle);
                    MainService.mhandler.sendMessage(message);
                    break;
                default:
                    break;
            }
        }
        //...


	}
}

