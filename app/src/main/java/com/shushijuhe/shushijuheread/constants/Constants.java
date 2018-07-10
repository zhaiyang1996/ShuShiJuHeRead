package com.shushijuhe.shushijuheread.constants;

import java.io.File;

/**
 * Created by zhaiyang on 2018/6/4.
 * 全局常量
 */

public class Constants {
    public static final String IMG_BASE_URL = "http://statics.zhuishushenqi.com";
    public static final String API_BASE_URL = "http://api.zhuishushenqi.com";
    public static final String[] CIRCUIT = {"http://beta.metmt.com/?url=",
                                             "https://www.pohaier.com/kuku/index.php?url=",
                                            "http://www.itono.cn/ty/mdparse/index.php?id="};
    public static final String[] CIRCUIT_NAME = {"高清线路",
            "超清线路",
            "流氓线路",
            "女王线路",
            "更多线路正在开发中"};
    public static final String[] TRANSITIONIMAGEURL = {"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531220256823&di=d759f5bc206f7206bd358407c4a44cd1&imgtype=0&src=http%3A%2F%2Fimgfs.oppo.cn%2Fuploads%2Fthread%2Fattachment%2F2017%2F05%2F19%2F14951566699727.jpg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531220256822&di=f95eb04025979dae4dcbe53101355f91&imgtype=0&src=http%3A%2F%2Fwww.vsread.com%2Ficonograph1%2F2015-11-18%2Fb953bd9f3942b5adfa433700aaefa1c7.jpg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531220387891&di=25567fc37663c79178a46e688acca81d&imgtype=jpg&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D1897359835%2C1190554384%26fm%3D214%26gp%3D0.jpg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531220443063&di=2c9be5b8e818c6a23efc3838a3760cb6&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201503%2F22%2F20150322182103_szfxc.thumb.224_0.jpeg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531220588239&di=8b79b5858c514da3ea8bc63bca040511&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fmobile%2Fa%2F5594a5072fe44.jpg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531220605443&di=b69ee0c5a6a8614b6a07d507c986a9ea&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2F201708%2F17%2F172315itkd8zrpowk10wm7.jpg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531220622238&di=58abd49bd476d4188efc95f2bf00c952&imgtype=0&src=http%3A%2F%2Fimg4q.duitang.com%2Fuploads%2Fitem%2F201406%2F21%2F20140621231911_jZXAZ.thumb.700_0.jpeg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531220644541&di=343f41c65955efe90bd6e641dd46f044&imgtype=0&src=http%3A%2F%2Fp3.wmpic.me%2Farticle%2F2017%2F01%2F05%2F1483602968_EfWLbwzV.jpg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531220729006&di=817c66ff72e917b8c9bf0e92113b9b67&imgtype=0&src=http%3A%2F%2Fimage.tianjimedia.com%2FuploadImages%2F2014%2F363%2F20%2F146282F5GT57_1000x500.jpg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531220857141&di=5a9600ed55c50caf48c3dc8960215c75&imgtype=0&src=http%3A%2F%2Fb.zol-img.com.cn%2Fsjbizhi%2Fimages%2F4%2F320x510%2F1370337670329.jpg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531220895895&di=91c15da49af7c5e25d2aeb560e23bd74&imgtype=0&src=http%3A%2F%2Fpic.ffpic.com%2Ffiles%2F2013%2F1008%2Fsj1010adw06.jpg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531220964765&di=f80a4d4a509be015c78152e43792b35a&imgtype=0&src=http%3A%2F%2Fpic.ffpic.com%2Ffiles%2F2018%2F0607%2F0531xqxcygqsjbz4_s.jpg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531221124478&di=7290e6874115ecbd053491dd6d75bd6e&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0178fc57a199730000012e7e0b9559.jpg%401280w_1l_2o_100sh.jpg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531222305542&di=ce6bcc167602438fccc76687d5019652&imgtype=0&src=http%3A%2F%2Fimage.tianjimedia.com%2FuploadImages%2F2014%2F363%2F19%2FIAAYO72353L9.jpg",
    "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1390139041,595759495&fm=27&gp=0.jpg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531222391269&di=442bfc2cce5706c56177f0718791c3d2&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201511%2F27%2F20151127200759_dkhJL.jpeg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531222428776&di=6a430f6259edf0ae30fecdd6418bec43&imgtype=jpg&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D2895033255%2C46988781%26fm%3D214%26gp%3D0.jpg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531222448701&di=114d22a89ecc0a9b39d5acceee66c2f2&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201408%2F04%2F20140804160447_UYjvd.thumb.700_0.png",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531222466739&di=771f28f9e61f1c871cbcab05285a0dc1&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201608%2F30%2F20160830180938_5M3rE.jpeg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531222483678&di=b95897ec56abe35655cadf9f2cd5f660&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201408%2F27%2F20140827105357_4zNTU.jpeg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531222514323&di=eff4e1fe2005201a65d49dfda996dd8f&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fq_mini%2Cc_zoom%2Cw_640%2Fupload%2F20170707%2Ffa4ab653fb9d4b3e800e1493eb6304b4_th.jpg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531222565902&di=8707b047477562a05b4d935815cab59d&imgtype=0&src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201501%2F03%2F20150103193811_L8Zs5.jpeg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531222624417&di=52e58c5fb51bc6164a18df6ccf5b80ce&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201605%2F05%2F20160505114639_dLhQ3.jpeg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531222643905&di=ab77432ea6c9b14b892d3ebfa4085bc2&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fwallpaper%2F1409%2F28%2Fc0%2F39084687_1411890777335_320x480.jpg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531222656919&di=6c3bb4fc463a2343b365122504aab99a&imgtype=0&src=http%3A%2F%2Fpic.ffpic.com%2Ffiles%2F2015%2F0208%2F0203wmsgdzsjbz2.jpg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531222669842&di=4e9e7e80a0c1685adf390c3ee8b831cf&imgtype=0&src=http%3A%2F%2Fimg18.3lian.com%2Fd%2Ffile%2F201704%2F10%2F015370b414e8b19fcb3db615f503f9fb.jpg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531222702458&di=c6a6c1f5782e2f472ecf69b115cc8b92&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201608%2F09%2F20160809223242_NsinK.jpeg"};
}
