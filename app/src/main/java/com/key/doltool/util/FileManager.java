package com.key.doltool.util;
/**
 * 返回APP目录下的file
 * @author Administrator
 * @version 2013-4-18
 * @日志 2013-4-18<br>
 * 
 */
public class FileManager {
	public static String BOAT="dol_boat/";
	public static String TROVE="dol_trove/";
	public static String TRADE="dol_trade/";
	public static String SKILL="dol_skill/";
	public static String ADC="dol_adc/";
	public static String WIKI="file:///android_asset/wiki_html/";
	public static String DOWNLOAD="apk/";
	public static final String IMAGE_FILE_LOCATION = "temp.png";
	public static String getSaveFilePath() {
		if (CommonUtil.hasSDCard()) {
			return CommonUtil.getRootFilePath() + "dol_db/";
		} else {
			return CommonUtil.getRootFilePath() + "com.key.doltool/files/";
		}
	}
}
