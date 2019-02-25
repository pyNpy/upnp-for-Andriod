package com.example.jettyUnit;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class JettyService extends Service{
	static  public int getPort() {
		return port;
	}

	final private static   int port=9000;
	public  Server jettyserver ;
	public void configureConnectors()
	{
		if (jettyserver == null)
			return ;
		SelectChannelConnector selectChannelConnector =new SelectChannelConnector();
		selectChannelConnector.setUseDirectBuffers(false);
//	    selectChannelConnector.setPort(9000);
	    selectChannelConnector.setHost("127.0.0.1");
	    jettyserver.addConnector(selectChannelConnector);
		
	}
	private void configureHandlers() {
	        if (jettyserver != null) {
	
	            HandlerCollection handlers = new HandlerCollection();
	            ContextHandlerCollection contexts = new ContextHandlerCollection();	 
	            contexts.addContext("/", "/sdcard/test/");
	            handlers.setHandlers(new org.eclipse.jetty.server.Handler[]{contexts, new ShareFileHandler()});
	            jettyserver.setHandler(handlers);
	        }
	    }
	
	@Override
	public void onCreate() {
		super.onCreate();
		try {
			jettyserver=new Server(port);
			configureConnectors();
			configureHandlers();
			jettyserver.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(jettyserver!=null)
			try {
				jettyserver.stop();
				jettyserver=null;

			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	
}
