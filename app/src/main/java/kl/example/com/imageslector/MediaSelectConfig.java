package kl.example.com.imageslector;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.example.kl.dlna.MediaType;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MediaSelectConfig  implements Serializable {
    public Context mAppContex;
    public int mMaxSelectCount; //选择多少
    public int mDefaultShowMode;//Activity样式
    public MediaType mMediaType;//选择media类型
    public boolean mClickSound;//点击声音
    public boolean mShouldPreview; //是否预览
    public String mCashPath;//缓存路径
    public boolean mMulSelect;//单选或多项选择
    public boolean mUseCameraorRecord;//是否使用相机

    public int getmMaxSelectCount() {
        return mMaxSelectCount;
    }

    public int getmDefaultShowMode() {
        return mDefaultShowMode;
    }

    public MediaType getmMediaType() {
        return mMediaType;
    }

    public boolean ismClickSound() {
        return mClickSound;
    }

    public String  getmCashPath() {
        return mCashPath;
    }

    public boolean ismMulSelect() {
        return mMulSelect;
    }

    public void setmMaxSelectCount(int mMaxSelectCount) {
        this.mMaxSelectCount = mMaxSelectCount;
    }

    public void setmDefaultShowMode(int mDefaultShowMode) {
        this.mDefaultShowMode = mDefaultShowMode;
    }

    public void setmMediaType(MediaType mMediaType) {
        this.mMediaType = mMediaType;
    }

    public void setmClickSound(boolean mClickSound) {
        this.mClickSound = mClickSound;
    }

    public void setmCashPath(String mCashPath) {
        this.mCashPath = mCashPath;
    }

    public void setmMulSelect(boolean mMulSelect) {
        this.mMulSelect = mMulSelect;
    }

    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/appcash/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    public MediaSelectConfig(Context contex,MediaType mediaType)
    {
        mAppContex=contex;
        mMaxSelectCount=20;
        mDefaultShowMode=1;
        mClickSound = true;
        mCashPath=  getPath();
        mMulSelect=true;
        mMediaType = mediaType;
        mShouldPreview = true;//
        mUseCameraorRecord=true;
    }



    public void setmAppContex(Context mAppContex) {
        this.mAppContex = mAppContex;
    }

    public void setmShouldPreview(boolean mShouldPreview) {
        this.mShouldPreview = mShouldPreview;
    }

    public void setmUseCameraorRecord(boolean mUseCameraorRecord) {
        this.mUseCameraorRecord = mUseCameraorRecord;
    }


    public Context getmAppContex() {
        return mAppContex;
    }

    public boolean ismShouldPreview() {
        return mShouldPreview;
    }

    public boolean ismUseCameraorRecord() {
        return mUseCameraorRecord;
    }

    public void getPermissons(Activity mActivity)
    {
        RxPermissions permissions;
        permissions = new RxPermissions(mActivity);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(getmAppContex());
                } else {
                    Toast.makeText(getmAppContex(),
                            getmAppContex().getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
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
    public PictureSelectionModel getPictureDefaultConfig(Activity activity , List<LocalMedia> selectList)
    {
       return  PictureSelector.create( activity)
               .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
               .theme(R.style.picture_default_style )// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
               .maxSelectNum(getmMaxSelectCount())// 最大图片选择数量
               .minSelectNum(0)// 最小选择数量
               .imageSpanCount(4)// 每行显示个数
               .selectionMode( PictureConfig.MULTIPLE )// 多选 or 单选PictureConfig.SINGLE
               .previewImage(true)// 是否可预览图片
//               .previewVideo(true)// 是否可预览视频
//             .enablePreviewAudio(true) // 是否可播放音频
               .isCamera(true)// 是否显示拍照按钮
               .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
               .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
               .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
               .enableCrop(false)// 是否裁剪 /....剪裁出问题？？？
               .compress(true)// 是否压缩
               .synOrAsy(true)//同步true或异步false 压缩 默认同步
               .compressSavePath(getPath())//压缩图片保存地址
               //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
               .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//               .withAspectRatio(16, 9)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//               .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
               .isGif(true)// 是否显示gif图片
//               .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
//               .circleDimmedLayer(true)// 是否圆形裁剪
//               .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//               .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
               .openClickSound(true)// 是否开启点击声音
               .selectionMedia(selectList)// 是否传入已选图片
               //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
               .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
               //.cropCompressQuality(90)// 裁剪压缩质量 默认100
               .minimumCompressSize(100);// 小于100kb的图片不压缩
               //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
               //.rotateEnabled(true) // 裁剪是否可旋转图片
               //.scaleEnabled(true)// 裁剪是否可放大缩小图片
               //.videoQuality()// 视频录制质量 0 or 1
               //.videoSecond()//显示多少秒以内的视频or音频也可适用
               //.recordVideoSecond()//录制视频秒数 默认60s


        //.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code



    }
    public PictureSelectionModel getVideoDefaultConfig(Activity activity,List<LocalMedia>selectList)
    {
        return  PictureSelector.create( activity)
                .openGallery(PictureMimeType.ofVideo())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                 .theme( R.style.picture_default_style )// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(getmMaxSelectCount())// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode( PictureConfig.MULTIPLE )// 多选 or 单选PictureConfig.SINGLE
//                .previewImage(true)// 是否可预览图片
               .previewVideo(true)// 是否可预览视频
//             .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
//                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
//                .enableCrop(true)// 是否裁剪
//                .compress(true)// 是否压缩
//                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                .withAspectRatio(16, 9)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                .isGif(true)// 是否显示gif图片
//                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
//                .circleDimmedLayer(true)// 是否圆形裁剪
//                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(true);// 是否开启点击声音
//                .selectionMedia(selectList)// 是否传入已选图片
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
//                .previewEggs(true);// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
//                .minimumCompressSize(100);// 小于100kb的图片不压缩
        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
        //.rotateEnabled(true) // 裁剪是否可旋转图片
        //.scaleEnabled(true)// 裁剪是否可放大缩小图片
        //.videoQuality()// 视频录制质量 0 or 1
        //.videoSecond()//显示多少秒以内的视频or音频也可适用
//        .recordVideoSecond()//录制视频秒数 默认60s

    }
    public PictureSelectionModel getAudioDefaultConfig(Activity activity, List<LocalMedia>selectList)
    {
        return  PictureSelector.create( activity)
                .openGallery(PictureMimeType.ofAudio())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                 .theme( R.style.picture_default_style )// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(getmMaxSelectCount())// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode( PictureConfig.MULTIPLE )// 多选 or 单选PictureConfig.SINGLE
//                .previewImage(true)// 是否可预览图片
//               .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
//                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
//                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
//                .enableCrop(true)// 是否裁剪
//                .compress(true)// 是否压缩
//                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .compressSavePath(getPath())//压缩图片保存地址
//                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
//                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                .withAspectRatio(16, 9)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
//                .isGif(true)// 是否显示gif图片
//                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
//                .circleDimmedLayer(true)// 是否圆形裁剪
//                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(true)// 是否开启点击声音
                .selectionMedia(selectList);// 是否传入已选图片
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
//                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
//                .minimumCompressSize(100);// 小于100kb的图片不压缩
        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
        //.rotateEnabled(true) // 裁剪是否可旋转图片
        //.scaleEnabled(true)// 裁剪是否可放大缩小图片
        //.videoQuality()// 视频录制质量 0 or 1
        //.videoSecond()//显示多少秒以内的视频or音频也可适用
        //.recordVideoSecond()//录制视频秒数 默认60s

    }
    /**
     * start lib activity
     * inner:startactivityforresult();
     *
     * **/
    public void workgetPicture(Activity activity, List<LocalMedia>list )
    {
        PictureSelectionModel pictureSelectionModel =getPictureDefaultConfig(activity,list);
        pictureSelectionModel.forResult(PictureConfig.CHOOSE_REQUEST);

    }

    public void workgetAudio( Activity activity, List<LocalMedia>list )
    {
        PictureSelectionModel audioSelectionModel =getPictureDefaultConfig(activity,list);
        audioSelectionModel.forResult(PictureConfig.CHOOSE_REQUEST);

    }

    public void workgetVideo(Activity activity, List<LocalMedia>list )
    {
        PictureSelectionModel vedioSelectionModel =getPictureDefaultConfig(activity,list);
        vedioSelectionModel.forResult(PictureConfig.CHOOSE_REQUEST);
    }



}






