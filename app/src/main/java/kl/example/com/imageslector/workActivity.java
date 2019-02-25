package kl.example.com.imageslector;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.Options;
import com.example.kl.dlna.MediaType;
import com.example.kl.dlna.MyBroadcastReceiver;
import com.example.kl.dlna.TVOperator;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import kl.example.com.imageslector.adapter.FullyGridLayoutManager;
import kl.example.com.imageslector.adapter.GridImageAdapter;

public class workActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = workActivity.class.getSimpleName();
    private List<LocalMedia> selectList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private int maxSelectNum = 9;
    private ImageView left_back ;
    private TextView right_warning ;


    private MediaType mMediaType ;
//.........<include layout:"musicplayer_layout">
    private ImageView mPreviewpicture_imageview1;
    private ImageView mPreviewvideoFirstFragment_imageview2;
    //music player view
    private LinearLayout ll_musicplayer;
    private TextView mcurrentTime;
    private TextView mtotalTime;
    private SeekBar  mseekBar;
    private ImageButton mPlay;
    private ImageButton mPause;
    private ImageButton mReset;

    private  MusicPlayer mMusicPlayer=new MusicPlayer();
    private LocalMedia selectedMedia;
    //
    private String castTotv_mediaPath=null;
    private Button castTotv_Button;


    private void init_previewLayout()
    {
        ll_musicplayer=(LinearLayout)findViewById(R.id.musicplayer_layout);
        mPreviewpicture_imageview1=(ImageView)findViewById(R.id.previewvideoFirstFragment_imageview2);
        mPreviewvideoFirstFragment_imageview2=(ImageView) findViewById(R.id.previewvideoFirstFragment_imageview2);
        //init def musicplayer
        mcurrentTime=(TextView)findViewById(R.id.musicplayer_currenttime);
        mseekBar=(SeekBar)findViewById(R.id.musicplayer_seekbar);
        mtotalTime=(TextView)findViewById(R.id.musicplayer_totaltime);
        mPlay =(ImageButton)findViewById(R.id.musicplayer_play);
        mPause=(ImageButton)findViewById(R.id.musicplayer_pause);
        mReset=(ImageButton)findViewById(R.id.musicplayer_reset);
        this.mMusicPlayer.Create(workActivity.this,mcurrentTime,mtotalTime,mseekBar,mPlay,mPause,mReset);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_selelctmedia);
        getPermisson();
        init_previewLayout();
        int value =  this.getIntent().getIntExtra("mediatype",0);
        switch (value){
            case 1:
                mMediaType= MediaType.Image;
                mPreviewpicture_imageview1.setVisibility(View.VISIBLE);
                break;
            case 2:
                mMediaType= MediaType.Audio;
                ll_musicplayer.setVisibility(View.VISIBLE);
                break;
            case 3:
                mMediaType= MediaType.Video;
                mPreviewvideoFirstFragment_imageview2.setVisibility(View.VISIBLE);
                break;
        };
        right_warning=(TextView) findViewById(R.id.tv_right);
        right_warning.setOnClickListener(this);

        FullyGridLayoutManager manager = new FullyGridLayoutManager
                (workActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView=(RecyclerView)findViewById(R.id.recycler);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(workActivity.this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    selectedMedia = selectList.get(position);
                    switch (mMediaType) {
                        case Image:
                            // 预览图片 可自定长按保存路径
                            Toast.makeText(workActivity.this, "点击图片", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap1 = BitmapFactory.decodeFile(selectedMedia.getPath());
                            mPreviewpicture_imageview1.setImageBitmap(bitmap1);
                            break;
                        case Audio:
                            // 预览音频
                            workActivity.this.mMusicPlayer.init(selectedMedia.getPath());
                            //PictureSelector.create(workActivity.this).externalPictureAudio(media.getPath());
                            break;

                        case Video:
                            // 预览视频
                            //PictureSelector.create(workActivity.this).externalPictureVideo(media.getPath());
                            workActivity.this.mPreviewvideoFirstFragment_imageview2.setBackgroundColor(0);
                            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                            retriever.setDataSource(selectedMedia.getPath());
                            Bitmap bitmap2 = retriever.getFrameAtTime(1,MediaMetadataRetriever.OPTION_CLOSEST_SYNC );
                            retriever.release();
                            workActivity.this.mPreviewvideoFirstFragment_imageview2.setImageBitmap(bitmap2);
                            workActivity.this.mPreviewvideoFirstFragment_imageview2.setMaxHeight(200);
                            mPreviewvideoFirstFragment_imageview2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PictureSelector.create(workActivity.this).
                                            externalPictureVideo(selectedMedia.getPath());
                                }
                            });
                            break;
                    }
                }
            }
        });

        //清空
        TextView textView= (TextView)findViewById(R.id.tv_right);
        textView.setOnClickListener(this);


        castTotv_Button =(Button)findViewById(R.id.cast_to_tv_button1);
        castTotv_Button.setOnClickListener(this);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mMusicPlayer.Destroy();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void getPermisson()
    {
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(getApplicationContext());
                } else {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onComplete() {
            }
        });

    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            MediaSelectConfig config=new MediaSelectConfig(getApplication(),mMediaType);
            PictureSelectionModel mode=null;
            switch (mMediaType){
                case Image:
                    mode=config.getPictureDefaultConfig(workActivity.this,selectList);
                    break;
                case Audio:
                    mode=config.getAudioDefaultConfig(workActivity.this,selectList);
                    break;
                case Video:
                    mode=config.getVideoDefaultConfig(workActivity.this,selectList);
                    break;
                default:
                    break;
            }
            if(mode!=null)
                mode.forResult(PictureConfig.CHOOSE_REQUEST);
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
                        Log.i("图片-----》", media.getPath());
                    }
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_back:
                finish();
                break;
            case R.id.tv_right:
                //清空grid selectlist;
                adapter.clear();
                if(ll_musicplayer.getVisibility()==View.VISIBLE) {
                    //其他控件
                    this.mMusicPlayer.clear();
                }
                if(workActivity.this.mPreviewvideoFirstFragment_imageview2.getVisibility()==View.VISIBLE)
                {
                    workActivity.this.mPreviewvideoFirstFragment_imageview2.setImageBitmap(null);
                }
                if(workActivity.this.mPreviewpicture_imageview1.getVisibility()==View.VISIBLE)
                {
                    workActivity.this.mPreviewpicture_imageview1.setImageBitmap(null);
                }
                break;
            case R.id.cast_to_tv_button1:
                if(!TextUtils.isEmpty(castTotv_mediaPath)&&TextUtils.equals(castTotv_mediaPath,this.selectedMedia.getPath()))
                {
                    Toast.makeText(workActivity.this,"已经投送到屏幕",Toast.LENGTH_SHORT).show();
                    break;
                }
                if(this.selectedMedia==null)
                    break;
                if(this.mMediaType==MediaType.Image)
                {

                    TVOperator tvOperator=new TVOperator(this.mMediaType,this.selectedMedia.getPath());
                    tvOperator.setCast2tv(true);
                    Intent intentimage= new Intent();
                    intentimage.setClass(workActivity.this, MyBroadcastReceiver.class);
                    intentimage.setAction("Device2TV");
                    intentimage.putExtra(TVOperator.getTag(), tvOperator);
                    sendBroadcast(intentimage);
                }
                if(this.mMediaType==MediaType.Audio)
                {
                    TVOperator tvOperator=new TVOperator(this.mMediaType,this.selectedMedia.getPath());
                    tvOperator.setCast2tv(true);
                    Intent intentaudio= new Intent();
                    intentaudio.setClass(workActivity.this, MyBroadcastReceiver.class);
                    intentaudio.setAction("Device2TV");
                    intentaudio.putExtra(TVOperator.getTag(), tvOperator);
                    sendBroadcast(intentaudio);
                }
                if(this.mMediaType==MediaType.Video)
                {
                    TVOperator tvOperator=new TVOperator(this.mMediaType,this.selectedMedia.getPath());
                    tvOperator.setCast2tv(true);
                    Intent intentvedio= new Intent();
                    intentvedio.setClass(workActivity.this, MyBroadcastReceiver.class);
                    intentvedio.setAction("Device2TV");
                    intentvedio.putExtra(TVOperator.getTag(), tvOperator);
                    sendBroadcast(intentvedio);
                }
                break;
        }
    }


    /**
     * 自定义压缩存储地址
     *
     * @return
     */
    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/test/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }








}
