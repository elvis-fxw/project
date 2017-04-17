package com.huangfw.crawler.utils;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	//歌单url
	public static final String SOURCE_URL = "http://music.163.com/discover/playlist/?";
	
	//163主域名
	public static final String DOMAIN = "http://music.163.com";
	public static final String BASE_URL = "http://music.163.com/";
	
	//获取评论的API路径(被加密)
	public static final String NET_EASE_COMMENT_API_URL = "http://music.163.com/weapi/v1/resource/comments/R_SO_4_";
	
	//解密用的文本
	public static final String TEXT = "{\"username\": \"\", \"rememberLogin\": \"true\", \"password\": \"\"}";
	
	//存储歌曲信息文本路径
	public static final String COMMENT_MESSAGE_PATH = "/home/user/workspace/NetEaseMusicCrawler/log/comment_message.xls";
	
	//存储评论内容文本路径
	public static final String COMMENTS_PATH = "/home/user/workspace/NetEaseMusicCrawler/log/comments_";
	
	public static final String COMMENTS_SUFFIX = ".xls";
	
	//TOP歌曲文本路径
	public static final String TOP_MUSIC_PATH = "/home/user/workspace/NetEaseMusicCrawler/log/top_music.xls";
	
	//歌曲评论大于某个值文本路径
	public static final String TOP_COMMENT_MORE_MUSIC_PATH = "/home/user/workspace/NetEaseMusicCrawler/log/music_comment_gt.xls";
	
	//要爬取的歌单数
	public static final int MUSIC_LIST_COUNT = 2/*100*/;
	
	//分页数
	public static final int PER_PAGE = 2/*35*/;
	
	//偏移量
	public static final int OFFSET = 0;
	
	//要爬取的TOP歌曲数
	public static final int TOP_MUSIC_COUNT = 20;
	
	//获取评论数大于该值的歌曲
	public static final int COMMENTS_LIMIT = 100000;

	//本地存储吉他谱图片路径
	public static final String SAVE_PATH = "/workspace/crawler/gtpImage/";

	//计算推荐值时的权重
	public static final double COMMENTS_WEIGHT = 0.25;
	public static final double COLLECT_WEIGHT = 0.10;

	//数据字典，对应标签
	public static final Map<String,String > TAG_MAP = new HashMap<String,String>(){{
		put("","所有");
		put("华语","华语");
		put("轻音乐","轻音乐");
		put("欧美","欧美");
		put("影视原声","影视原声");
		put("运动","运动");
		put("钢琴","钢琴");
		put("流行","流行");
		put("古典","古典");
		put("摇滚","摇滚");
		put("粤语","粤语");
		put("伤感","伤感");
		put("古风","古风");
		put("游戏","游戏");
		put("学习","学习");
		put("后摇","后摇");
		put("日语","日语");
		put("小语种","小语种");
		put("电子","电子");
		put("另类/独立","另类/独立");
		put("怀旧","怀旧");
		put("ACG","ACG");
		put("经典","经典");
		put("民谣","民谣");
		put("榜单","榜单");
		put("兴奋","兴奋");
		put("感动","感动");
		put("治愈","治愈");
		put("浪漫","浪漫");
		put("爵士","爵士");
		put("韩语","韩语");
		put("说唱","说唱");
		put("世界音乐","世界音乐");
		put("安静","安静");
		put("金属","金属");
		put("器乐","器乐");
		put("清晨","清晨");
		put("下午茶","下午茶");
		put("放松","放松");
		put("舞曲","舞曲");
		put("民族","民族");
		put("校园","校园");
		put("清新","清新");
		put("夜晚","夜晚");
		put("吉他","吉他");
		put("90后","90后");
		put("80后","80后");
		put("孤独","孤独");
		put("酒吧","酒吧");
		put("旅行","旅行");
		put("性感","性感");
		put("R&B/Soul","R&B/Soul");
	}};
}