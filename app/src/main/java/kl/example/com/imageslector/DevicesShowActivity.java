package kl.example.com.imageslector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jettyUnit.Helper;
import com.example.kl.dlna.MainService;

import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DevicesShowActivity extends Activity {
    private LinearLayout linearLayout;
    //1
    private ListView listview_ipchoice;
    private BaseAdapter ipAdapter;
    private List<String> ipList=new ArrayList<>();
    public static  String   mSelectedLocalIp;
    public static String getLocalIP()
    {
        return mSelectedLocalIp;
    }

    //2
    private ListView listView_devices;
    private BaseAdapter devicesAdapter;
    private static List<DeviceDisplay> deviceslist=new ArrayList<>();
    private static Device mSelectedDevice;
    public static  Device getSelectDevice()
    {
        return mSelectedDevice;
    }
    public android.os.Handler mhandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            boolean ipchanged=false;
            List<String>tempiplist=Helper.getLocalIPList();
            for (String ip : tempiplist)
            {
                if(!ipList.contains(ip)){
                    ipchanged=true;
                    ipList.clear();
                    ipList.addAll(tempiplist);
                    ipAdapter.notifyDataSetChanged();
                }
            }
            //
            boolean deviceChanged=false;
            List<DeviceDisplay> temp_devicelist=new ArrayList<>();
            getDevicesINFO(MainService.mUpnpService_main.getControlPoint(),temp_devicelist);
            for ( DeviceDisplay  deviceDisplay:temp_devicelist )
            {
                if(!deviceslist.contains(deviceDisplay))
                {
                    ipchanged=true;
                    deviceslist.clear();
                    deviceslist.addAll(temp_devicelist);
                    devicesAdapter.notifyDataSetChanged();
                }
            }
            mhandler.postDelayed(this,1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.browserdlnasetting_layout);
        Toast.makeText(this,"test",Toast.LENGTH_SHORT).show();
        linearLayout= new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //ip
        TextView textView1=new TextView(this);
        textView1.setText("选择本机ip地址");
        textView1.setClickable(false);
        linearLayout.addView(textView1);
        initView_Ip();

        //devce
        TextView textView2=new TextView(this);
        textView2.setText("选择连接的设备");
        textView2.setClickable(false);
        linearLayout.addView(textView2);
        initView_Devices();


        setContentView(linearLayout);

        //
        mhandler.postDelayed(runnable ,2000L);
    }

    /**
     * show ip list
     * */
    private void initView_Ip()
    {
        ipList.clear();
        listview_ipchoice=new ListView(this);
        listview_ipchoice.setCacheColorHint(Color.GRAY);
        listview_ipchoice.setVisibility(View.VISIBLE);
        listview_ipchoice.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        listview_ipchoice.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listview_ipchoice.setBackgroundColor(Color.TRANSPARENT);
        Helper.getLocalIPList(ipList);
        ipAdapter = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_single_choice,
                ipList);

        this.listview_ipchoice.setAdapter(ipAdapter);
        this.listview_ipchoice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object ip= ipList.get(position);
                mSelectedLocalIp = String.valueOf(ip);
                Toast.makeText(getApplicationContext(),mSelectedLocalIp, Toast.LENGTH_SHORT).show();
            }
        });

        linearLayout.addView(listview_ipchoice);
    }
    private void initView_Devices()
    {
        deviceslist.clear();
        getDevicesINFO(MainService.mUpnpService_main.getControlPoint(),deviceslist);
        listView_devices =new ListView(this);
        listView_devices.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        listView_devices.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        devicesAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_single_choice,deviceslist);
        listView_devices.setAdapter(devicesAdapter);
        listView_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private int clickitem;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickitem=position;
                mSelectedDevice = deviceslist.get(position).getDevice();
            }
        });
        listView_devices.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showAlertDilog(deviceslist.get(position));
                return true;
            }
        });

        if(deviceslist.size()<1)
            Toast.makeText(this,"未发现设备",Toast.LENGTH_SHORT).show();
        linearLayout.addView(listView_devices);
    }
    private void showAlertDilog(DeviceDisplay deviceDisplay )
    {
        if(deviceDisplay==null)
            return;
        AlertDialog.Builder builder =new  AlertDialog.Builder( DevicesShowActivity.this  );
        builder.setTitle("info").setMessage(deviceDisplay.getDetailsMessage());
        builder.setCancelable(true);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void getDevicesINFO(ControlPoint cp , List<DeviceDisplay> list )
    {

        for (Device device :cp.getRegistry().getDevices())
        {
            DeviceDisplay deviceDisplay =  new DeviceDisplay(device);
            list.add(deviceDisplay);
        }

    }


    class DeviceDisplay implements Serializable {

        private Device device;
        public DeviceDisplay(Device device) {
            this.device = device;
        }

        public Device getDevice() {
            return device;
        }

        // DOC:DETAILS
        public String getDetailsMessage() {
            StringBuilder sb = new StringBuilder();
            sb.append(getDevice().getDisplayString());
            sb.append(":\r\n");
            for (Service service : getDevice().getServices()) {
                sb.append(service.getServiceType()).append("\n");
            }
            return sb.toString();
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DeviceDisplay that = (DeviceDisplay) o;
            return device.equals(that.device);
        }

        @Override
        public int hashCode() {
            return device.hashCode();
        }

        @Override
        public String toString() {
            String name =
                    getDevice().getDetails() != null && getDevice().getDetails().getFriendlyName() != null
                            ? getDevice().getDetails().getFriendlyName()
                            : getDevice().getDisplayString();
            // Display a little star while the device is being loaded (see performance optimization earlier)
//            return device.isFullyHydrated() ? name : name + " *";
            return device.isFullyHydrated() ? name : name + " *"+"\r\n" ;


        }
    }






}
