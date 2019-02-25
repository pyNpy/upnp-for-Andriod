package com.example.kl.units;

import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Action;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.avtransport.callback.GetMediaInfo;
import org.fourthline.cling.support.avtransport.callback.GetPositionInfo;
import org.fourthline.cling.support.avtransport.callback.Next;
import org.fourthline.cling.support.avtransport.callback.Pause;
import org.fourthline.cling.support.avtransport.callback.Play;
import org.fourthline.cling.support.avtransport.callback.Previous;
import org.fourthline.cling.support.avtransport.callback.Seek;
import org.fourthline.cling.support.avtransport.callback.SetAVTransportURI;
import org.fourthline.cling.support.avtransport.callback.SetPlayMode;
import org.fourthline.cling.support.avtransport.callback.Stop;
import org.fourthline.cling.support.avtransport.impl.state.PausedPlay;
import org.fourthline.cling.support.model.MediaInfo;
import org.fourthline.cling.support.model.PlayMode;
import org.fourthline.cling.support.model.PositionInfo;
import org.fourthline.cling.support.model.SeekMode;

import com.example.kl.dlna.AndroidLog;
import com.example.kl.dlna.MainService;

import junit.framework.Test;

public class OperationSetting {
/*
	public static void ExecBasiAction(ControlPoint mControlPoint ,Service mservice )
	{
		Action action = mservice.getAction( "SetAVTransportURI");
		ActionInvocation<org.fourthline.cling.model.meta.Service> ActionInvocation = new ActionInvocation<>(action);
		ActionInvocation.setInput( "InstanceID", 0x0L );
		ActionInvocation.setInput( "CurrentURI", "http://pic.qiantucdn.com/58pic/13/71/35/24k58PICSiB_1024.jpg");	
		ActionCallback actionCallback = new ActionCallback(ActionInvocation) {
			
			@Override
			public void success(org.fourthline.cling.model.action.ActionInvocation invocation) {
				AndroidLog.log("...");
				
			}
		@Override
		public void failure(org.fourthline.cling.model.action.ActionInvocation invocation,
				UpnpResponse operation, String defaultMsg) {
		}
		};
		mControlPoint.execute(actionCallback);
	}
	*/
	
	public static class AVTransportServiceAction
	{ 
		public static ControlPoint mControlPoint ;
		public static Service mAvtransportservice;
		public static void init(ControlPoint cpoint ,Service avtransportservice)
		{
			mControlPoint = cpoint;
			mAvtransportservice= avtransportservice;
		}
		
		/**
		 * service AVTransportService
		 * */
		 public static void SetAVTransportURI(String mUrl)
		{
			//http://65.ierge.cn/14/211/423062.mp3
			//http://pics.sc.chinaz.com/files/pic/pic9/201812/bpic9783.jpg
			SetAVTransportURI avTransport_callback =new SetAVTransportURI( mAvtransportservice,
					mUrl) {
				
				@Override
				public void success(ActionInvocation invocation) {
					super.success(invocation);
				}
				@Override
				public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
					
					AndroidLog.log(operation.getResponseDetails());
				}
			};
			ControlPoint cp = mControlPoint ;
			cp.execute(avTransport_callback);
		}
		 /**
		  * get intelgent TV  MediaInfo
		  * */
		 public static PositionInfo mPositionInfo;
		 public static void getPostionInfo()
		 {
			 GetPositionInfo getPositionInfo =new GetPositionInfo(new UnsignedIntegerFourBytes(0),mAvtransportservice) {
				
				@Override
				public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
				}
				
				@Override
				public void received(ActionInvocation invocation, PositionInfo positionInfo) {
					mPositionInfo = positionInfo;
				}
			};
			mControlPoint.execute(getPositionInfo);
		 }
		 public static MediaInfo mMediaInfo;
		 public static void GetMediaInfo()
		 {
			 org.fourthline.cling.support.avtransport.callback.GetMediaInfo getMediaInfo =
					 new GetMediaInfo(new UnsignedIntegerFourBytes(0),mAvtransportservice) {
						
						@Override
						public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
							AndroidLog.log(defaultMsg );
						}
						@Override
						public void received(ActionInvocation invocation, MediaInfo mediaInfo) {
							mMediaInfo = mediaInfo;
						}
						@Override
						public void success(ActionInvocation invocation) {
							super.success(invocation);
						}
					};
			 mControlPoint.execute(getMediaInfo);
			  
		 }
		/**
		 * pause the tv
		 * */
		 public static void Pause()
		 {
			 org.fourthline.cling.support.avtransport.callback.Pause pause = 
					 new Pause(new UnsignedIntegerFourBytes(0), mAvtransportservice) {
						
						@Override
						public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {

						}
						@Override
						public void success(ActionInvocation invocation) {
							super.success(invocation);
						}
						
					}; 
			mControlPoint.execute(pause);
		 }
		/**
		 * play the tv
		 * */
		 public static void Play()
		 {
			 org.fourthline.cling.support.avtransport.callback.Play play=
					 new Play(new UnsignedIntegerFourBytes(0),mAvtransportservice) {
						@Override
						public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
						}
						@Override
						public void success(ActionInvocation invocation) {
							super.success(invocation);
						}
					};
					mControlPoint.execute(play);
		 }
		 /**
		  * stop over to cast at tv
		  * */
		 public static void Stop()
		 {
			 org.fourthline.cling.support.avtransport.callback.Stop stop=
					 new Stop(new UnsignedIntegerFourBytes(0), mAvtransportservice) {
						
						@Override
						public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
						}
						@Override
						public void success(ActionInvocation invocation) {
							super.success(invocation);
						}
					};
			 
			 mControlPoint.execute(stop);
		 }
		 /**
		  * @param formatTime : time format as "00:00:10"
		  * 
		  * */
		 public static void Seek(String formatTime)
		 {
			org.fourthline.cling.support.avtransport.callback.Seek seek =new 
			Seek(new UnsignedIntegerFourBytes(0), mAvtransportservice,SeekMode.ABS_TIME,formatTime) {
						
						@Override
						public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
							AndroidLog.log("...");
						}
						@Override
						public void success(ActionInvocation invocation) {
							super.success(invocation);
						}
					};
			mControlPoint.execute(seek);
		 }
		 public static void SetPlayMode(PlayMode playMode )
		 {
			 org.fourthline.cling.support.avtransport.callback.SetPlayMode setplaymode=
					 new SetPlayMode(mAvtransportservice,playMode) {
						@Override
						public void success(ActionInvocation invocation) {
							// TODO Auto-generated method stub
							super.success(invocation);
						}
						@Override
						public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
							// TODO Auto-generated method stub
							
						}
					};
				mControlPoint.execute(setplaymode);
		 }
 		 public static void Previous()
 		 {
 			 org.fourthline.cling.support.avtransport.callback.Previous previous=
 					 new Previous(new UnsignedIntegerFourBytes(0),mAvtransportservice) {
						@Override
						public void success(ActionInvocation invocation) {
							super.success(invocation);
						}
						@Override
						public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
						}
					};
			mControlPoint.execute(previous);
 		 }
 		 public static void Next()
 		 {
 			 org.fourthline.cling.support.avtransport.callback.Next next =
 					 new Next(new UnsignedIntegerFourBytes(0),mAvtransportservice) {
						@Override
						public void success(ActionInvocation invocation) {
							super.success(invocation);
						}
						@Override
						public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
						}
					};
 			 mControlPoint.execute(next);
 			 
 		 }
	}



//eg:
//http://65.ierge.cn/14/211/423062.mp3
//https://video.pearvideo.com/mp4/adshort/20190106/cont-1501780-13444406_adpkg-ad_hd.mp4
//					OperationSetting.AVTransportServiceAction.SetAVTransportURI("http://65.ierge.cn/14/211/423062.mp3");
//					OperationSetting.AVTransportServiceAction.GetMediaInfo();
//					MediaInfo mediaInfo = OperationSetting.AVTransportServiceAction.mMediaInfo;
//					OperationSetting.AVTransportServiceAction.Pause();
//					OperationSetting.AVTransportServiceAction.Play();
//					OperationSetting.AVTransportServiceAction.Seek("00:00:20");
//					OperationSetting.AVTransportServiceAction.Stop();



}
