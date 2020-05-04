package com.ljy.graduate.util;

/**
 * Author: liuzhiyuan
 * Date: 2020/3/29
 * Description:
 */
public interface Constants {
    // 手写文字识别webapi接口地址
    String WEBOCR_URL = "https://webapi.xfyun.cn/v1/service/v1/ocr/handwriting";
    // 应用APPID(必须为webapi类型应用,并开通手写文字识别服务,参考帖子如何创建一个webapi应用：http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=36481)
    String TEST_APPID = "5e7752a2";
    // 接口密钥(webapi类型应用开通手写文字识别后，控制台--我的应用---手写文字识别---相应服务的apikey)
    String TEST_API_KEY = "0cfbc1d01db56f17b5aaafb1daa60e08";
    String FILE_BASE_PATH = "/home/deploy/";
    /**
     * 校验阈值
     */
    Float CONFIDENCE_THRESHOLD = 0.9F;

    interface UserConstants {
        String REGISTER_MAIL_TITLE = "智能家居系统账户激活邮件";



    }

    Integer cycleSizeMax=1000;


    Integer BASE_C_NUMBER=300000;

}
