package  kl.example.com.imageslector;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class selfDef_baseview_TextView_CheckBox
        extends LinearLayout  {
    public TextView mtextview;
    public CheckBox mcheckbox;
    public String mText="";
    public boolean mCheck=false;
    public Object mDefTag=null;


    public OnClickListener mclicklistener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("def", v.toString());
                if(v instanceof selfDef_baseview_TextView_CheckBox)
                {
                    CheckBox checkBox=
                            ((selfDef_baseview_TextView_CheckBox)v).getCheckBox();
                    checkBox.setChecked(!checkBox.isChecked());
                }
            }
        };
    public selfDef_baseview_TextView_CheckBox(Context context) {
        super(context);
        initView(context,null);

    }
    public selfDef_baseview_TextView_CheckBox(Context context,AttributeSet attrs,int defStyleAttr)
    {
        super(context,attrs,defStyleAttr);
        initView(context,attrs);

    }
    public selfDef_baseview_TextView_CheckBox(Context context, AttributeSet attrs) {
        super(context,attrs);
        initView(context,attrs);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    public void initView(Context context,AttributeSet attrs)
    {
        LayoutInflater.from(context).
                inflate(R.layout.selfdef_baseview_textview_checkbox
                        ,this,true);
        mtextview=(TextView)findViewById(R.id.selfDef_base_textview);
        mcheckbox=(CheckBox) findViewById(R.id.selfDef_base_checkbox);
        mcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCheck= mcheckbox.isChecked() ;
                Toast.makeText(buttonView.getContext(),"value:"+mCheck,Toast.LENGTH_SHORT).show();
            }
        });

        //获取自定义属性
        TypedArray array= context.obtainStyledAttributes(attrs, R.styleable.selfDef_baseview_TextView_CheckBox);
        mText = array.getString(R.styleable.selfDef_baseview_TextView_CheckBox_mText);
        mCheck = array.getBoolean(R.styleable.selfDef_baseview_TextView_CheckBox_mCheck,false);
        array.recycle();
        Log.i("selfdef","获取自定义属性ok");

        if(mText==null)
            mText="";
        if(!TextUtils.isEmpty(mText))
            mtextview.setText(mText);
        mcheckbox.setChecked(mCheck);

    }






    public void setCheck(boolean checked) {
        mCheck=checked;
        mcheckbox.setChecked(mCheck);
    }

    public boolean getCheck() {

        mCheck=mcheckbox.isChecked();
        return mCheck;
    }

    public  void setDefTag(Object obj)
    {
        mDefTag=obj;
    }

    public Object getDefTag()
    {
        return mDefTag;
    }

    public void setText(String text)
    {
        mText=text;
        mtextview.setText(mText);

    }
    public String getText()
    {
        mText=new String(mtextview.getText().toString());
        return mText;
    }

    public TextView getTextView(){
        return mtextview;
    }
    public  CheckBox getCheckBox(){
        return mcheckbox;
    }














}