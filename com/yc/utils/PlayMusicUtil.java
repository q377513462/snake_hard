package com.yc.utils;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

/**
 * 用来播放音乐的工具类
 * @author wwwch
 *
 */
public class PlayMusicUtil {
	
	private static AudioClip bgmAudio;
	private static AudioClip overAudio;
	private static AudioClip lifeAudio;
	
	public static void playBGM(){
		//获取音乐文件的路径
		URL url = PlayMusicUtil.class.getResource("../music/bgm.wav");
		//创建声音剪辑对象
		bgmAudio = Applet.newAudioClip(url);
		//播放声音即可
		bgmAudio.play();
	}
	
	public static void stopBGM(){
		
		bgmAudio.stop();
	}
	
	
	/**
	 * 播放gameover音效
	 */
	public static void playGameOver(){
		//获取音乐文件的路径
		URL url = PlayMusicUtil.class.getResource("../music/gameover.wav");
		//创建声音剪辑对象
		overAudio = Applet.newAudioClip(url);
		//播放声音即可
		overAudio.play();
	}
	
	/**
	 * 播放吃到豆子时的音效
	 * 
	 */
	public static void playEatBean(){
		//获取音乐文件的路径
		URL url = PlayMusicUtil.class.getResource("../music/life.wav");
		//创建声音剪辑对象
		lifeAudio = Applet.newAudioClip(url);
		//播放声音即可
		lifeAudio.play();
	}
}
