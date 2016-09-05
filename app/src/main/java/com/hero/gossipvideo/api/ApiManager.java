package com.hero.gossipvideo.api;

import com.hero.gossipvideo.config.AndroidParam;
import com.hero.gossipvideo.config.Constants;
import com.hero.gossipvideo.content.PlayUrl;
import com.ltc.lib.net.api.HttpResult;
import com.ltc.lib.net.api.HttpWorker;
import com.ltc.lib.net.api.RequestPrepare;
import com.ltc.lib.net.param.Method;
import com.ltc.lib.utils.JsonUtil;

import java.util.Map;


/**
 * Created by Administrator on 2015/12/13.
 */
public class ApiManager {

    public static final String VIDEO_LIST_URL = "http://vcsp.ifeng.com/vcsp/appData/recommendGroupByTeamid.do";
    public static final String VIDEO_PLAY_LIST_URL = "http://vcsp.ifeng.com/vcsp/appData/videoGuid.do";

    public static final String NEWS_LIST_URL = "http://api.app.happyjuzi.com/v2.9/article/list/home";
    public static final String NEWS_DETAIL_URL = "http://api.app.happyjuzi.com/v2.9/article/detail";

//    http://vcsp.ifeng.com/vcsp/appData/recommendGroupByTeamid.do?
//    positionId=0
//    &showType=double
//    &channelId=100391-0
//    &adapterNo=6.13.1
//    &useType=androidPhone
//    &isNotModified=0
//    &pageSize=20

    //视频列表
    public static HttpResult getVideoContent(String positionId) {
        final RequestPrepare prepare = new RequestPrepare(Method.GET, VIDEO_LIST_URL);
        prepare.parseDomainToIp = false;
        prepare.setReqParam("positionId", positionId);
        prepare.setReqParam("showType", "double");
        prepare.setReqParam("channelId", "100391-0");
        prepare.setReqParam("adapterNo", "6.13.1");
        prepare.setReqParam("useType", "androidPhone");
        prepare.setReqParam("isNotModified", "0");
        prepare.setReqParam("pageSize", "20");
        prepare.setReqHeader("User-agent", AndroidParam.getUserAgent());
        return HttpWorker.getInstance().request(prepare);
    }


//    http://vcsp.ifeng.com/vcsp/appData/videoGuid.do?
//    guid=0106522b-92f4-4c89-9f83-0249c31d9aed

    public static HttpResult getVideoPlayList(String guid) {
        final RequestPrepare prepare = new RequestPrepare(Method.GET, VIDEO_PLAY_LIST_URL);
        prepare.parseDomainToIp = false;
        prepare.setReqParam("guid", guid);
        prepare.setReqHeader("User-agent", AndroidParam.getUserAgent());
        return HttpWorker.getInstance().request(prepare);
    }

    //播放url，在子线程中调用
    public static synchronized String getVideoPlayUrl(String guid) {

        final HttpResult rs = getVideoPlayList(guid);
        if (rs == null) {
            return "";
        }

        final PlayUrl playUrl = JsonUtil.fromJson(rs.data, PlayUrl.class);
        if (playUrl != null && playUrl.videos != null) {

            final Map<String, PlayUrl.Media> map = playUrl.videos;
            if (map.get("102") != null) {
                return map.get("102").mediaUrl;
            }

            for (Map.Entry<String, PlayUrl.Media> en : map.entrySet()) {
                final String url = en.getValue().mediaUrl;
                if (url.lastIndexOf(".mp4") > 0) {
                    return url;
                }
            }
        }

        return "";
    }

//    "http://api.app.happyjuzi.com/v2.9/article/list/home?" +
//            "uid=3943816990545003" +
//            "&res=1080x1920" +
//            "&ver=2.9.0.3" +
//            "&startup=1" +
//            "&pf=android" +
//            "&channel=xiaomi" +
//            "&accesstoken=120cd9ce210fa615f8c7974b0d436c0d" +
//            "&page=1" +
//            "&net=wifi" +
//            "&mac=02-00-00-00-00-00" +
//            "&ts=0";

    public static HttpResult getNewsContent(int page) {
        final String uid = Constants.randomUid();
        final RequestPrepare prepare = new RequestPrepare(Method.GET, NEWS_LIST_URL);
        prepare.parseDomainToIp = false;
        prepare.setReqParam("uid", uid);
        prepare.setReqParam("res", AndroidParam.getScreenSize());
        prepare.setReqParam("ver", "2.9.0.3");
        prepare.setReqParam("startup", "1");
        prepare.setReqParam("pf", "android");
        prepare.setReqParam("channel", "xiaomi");
        prepare.setReqParam("accesstoken", Constants.DEF_ACCESS_TOKEN.get(uid));
        prepare.setReqParam("page", String.valueOf(page));
        prepare.setReqParam("net", "wifi");
        prepare.setReqParam("mac", AndroidParam.getMac());
        prepare.setReqParam("ts", "0");
        return HttpWorker.getInstance().request(prepare);
    }

//    "http://api.app.happyjuzi.com/v2.9/article/detail?" +
//            "uid=3943816990545003" +
//            "&res=1080x1920" +
//            "&ver=2.9.0.3" +
//            "&pf=android" +
//            "&channel=xiaomi" +
//            "&accesstoken=120cd9ce210fa615f8c7974b0d436c0d" +
//            "&id=51635" +
//            "&net=wifi" +
//            "&mac=02-00-00-00-00-00";

    public static HttpResult getNewsDetailContent(String newsId) {
        final String uid = Constants.randomUid();
        final RequestPrepare prepare = new RequestPrepare(Method.GET, NEWS_DETAIL_URL);
        prepare.parseDomainToIp = false;
        prepare.setReqParam("uid", uid);
        prepare.setReqParam("res", AndroidParam.getScreenSize());
        prepare.setReqParam("ver", "2.9.0.3");
        prepare.setReqParam("pf", "android");
        prepare.setReqParam("channel", "xiaomi");
        prepare.setReqParam("accesstoken", Constants.DEF_ACCESS_TOKEN.get(uid));
        prepare.setReqParam("id", newsId);
        prepare.setReqParam("net", "wifi");
        prepare.setReqParam("mac", AndroidParam.getMac());
        return HttpWorker.getInstance().request(prepare);
    }

    public static final String DISCOVER_ITEM_URL = "http://solr.51tv.com/solr/spiderVideoIndex/select/";
    public static final String DISCOVER_DETAIL_URL = "http://tujie.ikuaishou.com/tuwen/weixinShare.jsp?id=";

//    "http://solr.51tv.com/solr/spiderVideoIndex/select/?" +
//            "q=*%3A*" +
//            "&sort=lastmodify+desc" +
//            "&start=0" +
//            "&rows=20" +
//            "&fq=type%3A26+OR+type%3A25+OR+type%3A27+OR+type%3A28+OR+type%3A29+OR+type%3A31+OR+type%3A32" +
//            "&wt=json";

    public static HttpResult getDiscoverItems(int page) {
        final RequestPrepare prepare = new RequestPrepare(Method.GET, DISCOVER_ITEM_URL);
        prepare.parseDomainToIp = false;
        prepare.setReqParam("q", "*%3A*");
        prepare.setReqParam("sort", "lastmodify+desc");
        prepare.setReqParam("start", String.valueOf(page));
        prepare.setReqParam("rows", "20");
        prepare.setReqParam("fq", "type%3A26+OR+type%3A25+OR+type%3A27+OR+type%3A28+OR+type%3A29+OR+type%3A31+OR+type%3A32");
        prepare.setReqParam("wt", "json");
        return HttpWorker.getInstance().request(prepare);
    }

    //http://solr.51tv.com/solr/spiderVideoIndex/select/?
    // q=*%3A*
    // &sort=createtime+desc
    // &start=0
    // &rows=20
    // &fq=newtag:2
    // &packageName=com.kandian.hdtogoapp
    // &partner=yingyongbao
    // &appVersion=-162
    // &wt=json

    public static HttpResult getSpareVideoContent(int page) {
        final RequestPrepare prepare = new RequestPrepare(Method.GET, DISCOVER_ITEM_URL);
        prepare.parseDomainToIp = false;
        prepare.setReqParam("q", "*%3A*");
        prepare.setReqParam("sort", "lastmodify+desc");
        prepare.setReqParam("start", String.valueOf(page));
        prepare.setReqParam("rows", "20");
        prepare.setReqParam("fq", "newtag:2");
        prepare.setReqParam("packageName", "com.kandian.hdtogoapp");
        prepare.setReqParam("partner", "yingyongbao");
        prepare.setReqParam("appVersion", "-162");
        prepare.setReqParam("wt", "json");
        return HttpWorker.getInstance().request(prepare);
    }

    public static String getDiscoverDetailUrl(String discoverId) {
        return DISCOVER_DETAIL_URL + discoverId;
    }
}
