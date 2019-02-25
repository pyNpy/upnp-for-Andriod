package com.example.kl.dlna;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.fourthline.cling.Main;
import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.android.FixedAndroidLogHandler;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;

import com.example.jettyUnit.JettyService;
import com.example.kl.units.OperationSetting;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Path;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.types.UDN;

import kl.example.com.imageslector.DevicesShowActivity;

public class MainService extends Service{
    public static  MainServiceHandler mhandler;

	static	public AndroidUpnpService mUpnpService_main;
    private MyRegistryListener mRegistryListener = new MyRegistryListener();
    public static org.fourthline.cling.model.meta.Service mAvtransportservice;

    private	MyBroadcastReceiver myBroadcastReceiver;
    static 	public Device mConnectedDevice = null ;
    public  String mConnectionID;
	private ServiceConnection serviceConnection = new ServiceConnection() {

	        public void onServiceConnected(ComponentName className, IBinder service) {
	        	mUpnpService_main = (AndroidUpnpService) service;
	        	mUpnpService_main.getRegistry().addListener(mRegistryListener);
	        	mUpnpService_main.getControlPoint().search();
	        }
	        public void onServiceDisconnected(ComponentName className) {
	           
	        }
	    };
	@Override
	public void onCreate() {

        org.seamless.util.logging.LoggingUtil.resetRootHandler(new FixedAndroidLogHandler());
        // Now you can enable logging as needed for various categories of Cling:
        Logger.getLogger("org.fourthline.cling").setLevel(Level.FINEST);
        
        mhandler = new MainServiceHandler();
        UpnpOperations();
		super.onCreate();


	}
	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}
	@Override
	public void onDestroy() {
		mUpnpService_main.getRegistry().removeListener(mRegistryListener);
		mUpnpService_main=null;
		super.onDestroy();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void UpnpOperations(){
		
		Thread searchthread =new Thread(new Runnable() {
			@Override
			public void run() {
				getApplicationContext().bindService(new Intent(getApplicationContext() ,BrowserUpnpService.class), serviceConnection, Service.BIND_AUTO_CREATE);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	
		searchthread.run();
	}
	
	
}

class MainServiceRunable implements Runnable{
    private TVOperator tvOperator;
    private String localIp;
    private Device seldevice;
    private org.fourthline.cling.model.meta.Service AVTransportService;
    public  MainServiceRunable(TVOperator tvOperator1,String ip,Device device) {
        tvOperator = tvOperator1;
        localIp = ip;
        seldevice = device;
        if (seldevice != null) {
            for (org.fourthline.cling.model.meta.Service service : seldevice.getServices()) {
                if (service.getServiceType().toString().contains("AVTransport"))
                    AVTransportService = service;
                    break;
            }
        }
    }
    @Override
    public void run() {
        try {
            ControlPoint cp = MainService.mUpnpService_main.getControlPoint();
            OperationSetting.AVTransportServiceAction.init(cp, AVTransportService);
            if (tvOperator.isSeek())
                OperationSetting.AVTransportServiceAction.Seek(tvOperator.getSeektotime());
            if (tvOperator.isPause())
                OperationSetting.AVTransportServiceAction.Pause();
            if (tvOperator.isPlay())
                OperationSetting.AVTransportServiceAction.Play();
            if (tvOperator.isStop())
                OperationSetting.AVTransportServiceAction.Stop();
            if (tvOperator.isCast2tv()) {
//          when the server jetty is complite,request should be like this:
//           "http://192.168.88.106:9000/test.html?file=/sdcard/test/423062.mp3&&type=audio"
//           String type = tvOperator.getMediatype()==MediaType.Image?"image":(tvOperator.getMediatype()==MediaType.Audio?"audio":"video");
//           String request="http://"+localIp+ JettyService.getPort()+"/test.html?"
//                   +"file="+tvOperator.getMeidapath()+"&&type="+type;
             //now easy provide file download server
             //http://192.168.88.106:9000/test.html?file=/sdcard/test/423062.mp3
                String request = "http://" + localIp +":"+JettyService.getPort() + "/test.html?"
                        + "file=" + tvOperator.getMeidapath().replace(" ","%20");
                OperationSetting.AVTransportServiceAction.
                        SetAVTransportURI(request);
                //OperationSetting.AVTransportServiceAction.SetAVTransportURI(request);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}

class MainServiceHandler extends Handler{
	protected String mConnectionID;
	@Override
	public void handleMessage(Message msg) {
		if(msg.what ==100) {
            AndroidLog.log("test function in handleMessage....");
            if(MainService.mUpnpService_main!=null)
            {	AndroidLog.log("upnpService_main not null");
                for(Device d :  MainService.mUpnpService_main.getRegistry().getDevices()){
                    AndroidLog.log(d.getDisplayString());
                    AndroidLog.log(d.getIdentity().getUdn().toString());//get uuid
                }
            }
		}
        if(msg.what==200){
            //TVOperator
            TVOperator tvoperator =(TVOperator)msg.getData().getSerializable(TVOperator.getTag());
            if(MainService.mUpnpService_main!=null){
                DevicesShowActivity.getSelectDevice();//device
                DevicesShowActivity.getLocalIP();//ip
                MainServiceRunable runable = new MainServiceRunable(tvoperator,
                        DevicesShowActivity.getLocalIP(),DevicesShowActivity.getSelectDevice());
                post(runable);

            }
        }

		super.handleMessage(msg);
	}

    private void test(){
        if(false){
            //every time ,tv startup can generate a new value of udn
            if(MainService.mConnectedDevice==null)
            {
                //fordebug
                MainService.mConnectedDevice= MainService.mUpnpService_main.getControlPoint()
                        .getRegistry().getDevice(new UDN("F7CA5454-3F48-4390-8009-0c59e483c969"), true);
            }

            if(MainService.mConnectedDevice!=null){
                for(org.fourthline.cling.model.meta.Service service :MainService.mConnectedDevice.getServices())
                {
                    if(service.getServiceType().toString().contains("AVTransport"))
                    {
                        MainService.mAvtransportservice = service;
                        ControlPoint cPoint = MainService.mUpnpService_main.getControlPoint();

                        OperationSetting.AVTransportServiceAction.init(cPoint,service);
                        OperationSetting.AVTransportServiceAction.SetAVTransportURI("http://192.168.88.106:9000/test/423062.mp3");

                        AndroidLog.log("...");

                    }
                }
            }
        }
    }

}

class MyRegistryListener extends DefaultRegistryListener {

    /* Discovery performance optimization for very slow Android devices! */
    @Override
    public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
    	AndroidLog.log("remoteDeviceDiscoveryStarted");
    }

    @Override
    public void remoteDeviceDiscoveryFailed(Registry registry, final RemoteDevice device, final Exception ex) {
    	AndroidLog.log("remoteDeviceDiscoveryFailed");
    	  
    }
    /* End of optimization, you can remove the whole block if your Android handset is fast (>= 600 Mhz) */

    @Override
    public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
    	AndroidLog.log("remoteDeviceAdded");
    	 
    }

    @Override
    public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
        deviceRemoved(device);
        AndroidLog.log("remoteDeviceRemoved");
    }

    @Override
    public void localDeviceAdded(Registry registry, LocalDevice device) {
        AndroidLog.log("localDeviceAdded");
    	 
    }

    @Override
    public void localDeviceRemoved(Registry registry, LocalDevice device) {
        deviceRemoved(device);
    }

    public void deviceAdded(final Device device) {
    	AndroidLog.log("localDeviceAdded");
     	 
    }

    public void deviceRemoved(final Device device) {
    	AndroidLog.log("deviceRemoved");
    }
}







