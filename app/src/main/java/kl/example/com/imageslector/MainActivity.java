package kl.example.com.imageslector;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.jettyUnit.Helper;
import com.example.jettyUnit.JettyService;
import com.example.kl.dlna.MainService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private Button button;
    private int count=0;
    private ListView listView1;
    private ListAdapter listAdapter1;
    private List<String> list1 ;
    private VideoView testVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startService(new Intent(this,JettyService.class));
        startService(new Intent(this,MainService.class));
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        LinearLayout layout= (LinearLayout)findViewById(R.id.activity_main_linearlayout)  ;
        listView1=(ListView)findViewById(R.id.mainactivity_listview1);
        list1=new ArrayList<String>();
        list1.add("search devices");
        list1.add("image");
        list1.add("audio");
        list1.add("vedio");
        listAdapter1=new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,list1);
        listView1.setAdapter(listAdapter1);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent1=new Intent();

                switch (position) {
                    case 0: //"Search Device"
                        //Ǵ֯broserActivity
                        intent1.setClass(getApplicationContext(), workActivity.class);
                        startActivity(intent1);
                        break;
                    case 1: //"Photo",
                        intent1.setClass(getApplicationContext(), workActivity.class);
                        intent1.putExtra("mediatype",1);
                        startActivity(intent1);
                        break;
                    case 2:// "Music"
                        intent1.setClass(getApplicationContext(), workActivity.class);
                        intent1.putExtra("mediatype",2);
                        startActivity(intent1);
                        break;
                    case 3:// "Movie"
                        intent1.setClass(getApplicationContext(), workActivity.class);
                        intent1.putExtra("mediatype",3);
                        startActivity(intent1);
                        break;
                }

                }
        });

        button=(Button)findViewById(R.id.button1);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent();
                        intent.setClass(MainActivity.this, DevicesShowActivity.class);
                        intent.putExtra("",0);
                        startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 自定义组件的使用
     *
     * **/
    public void test(){

//1.
//        selfDef_baseview_TextView_CheckBox baseview=(selfDef_baseview_TextView_CheckBox)findViewById(R.id.baseview_text_radio_1);
//        baseview.setOnClickListener(baseview.mclicklistener);

////2.
//        selfDef_baseview_TextView_CheckBox baseview1=new selfDef_baseview_TextView_CheckBox(this,null);
//        baseview1.setCheck(false);
//        baseview1.setText("base1");
//        baseview1.setOnClickListener(baseview1.mclicklistener);
//
//        selfDef_baseview_TextView_CheckBox baseview2=new selfDef_baseview_TextView_CheckBox(this,null);
//        baseview2.setCheck(true);
//        baseview2.setText("base2");
//        baseview2.setOnClickListener(baseview2.mclicklistener);
//
//        layout.addView(baseview1);
//        layout.addView(baseview2);
//3.
//        String[] strs=new String[]{"textview","checkbox"};
//        Map<String,Object> map=new HashMap<String,Object>();
//        map.put("textview","11111111");
//        map.put("checkbox",true);
//
//        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
//        list.add(map);
//
//        SimpleAdapter adapter=new SimpleAdapter(
//                this,list,
//                R.layout.selfdef_baseview_textview_checkbox,
//                strs,new int[]{R.id.selfDef_base_textview,R.id.selfDef_base_checkbox}
//        );
//
//        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
//            @Override
//            public boolean setViewValue(View view, Object data, String textRepresentation) {
//                if(view instanceof Checkable)
//                {
//                    if (data instanceof Boolean) {
//                        ((Checkable) view).setChecked((Boolean) data);
//                        return true;
//                    } else if (view instanceof TextView) {
//                        ((TextView) view).setText((String)data);
//                        return true;
//                    }
//
//                }
//                else if(view instanceof TextView)
//                {
//                    if(data!=null) {
//                        TextView textView = (TextView)view;
//                        textView.setText((String) data);
//                        return true;
//                    }
//                }
//                else if(view instanceof ImageView)
//                {
//                    ImageView imageView=(ImageView) view;
//                    if(data instanceof  Integer){
//                        imageView.setImageResource((Integer) data);
//                    }else{
//                        imageView.setImageBitmap((Bitmap)data);
//                    }
//                    return true;
//                }
//
//                return false;
//            }
//        });
//        ListView listview=new ListView(this);
//        listview.setAdapter(adapter);
//
//        layout.addView(listview);
//
//

    }

}
