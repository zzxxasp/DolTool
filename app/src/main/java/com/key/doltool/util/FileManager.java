package com.key.doltool.util;

import android.content.Context;

/**
 * 返回APP目录下的file
 * 管理所有文件的目录
 */
public class FileManager {
	public static String ROOT="file:///android_asset/";
	public static String BOAT="dol_boat/";
	public static String TROVE="dol_trove/";
	public static String TRADE="dol_trade/";
	public static String ITEM="dol_item/";
	public static String SKILL="dol_skill/";
	public static String VOYAGE="dol_voyage/";
	public static String ADC="dol_adc/";
	public static String WIKI="file:///android_asset/wiki_html/";
	public static String DOWNLOAD="apk/";
	public static final String IMAGE_FILE_LOCATION =getSaveFilePath()+"temp.png";
	public static String getSaveFilePath() {
		if (CommonUtil.hasSDCard()) {
			return CommonUtil.getRootFilePath() + "dol_db/";
		} else {
			return CommonUtil.getRootFilePath() + "com.key.doltool/files/";
		}
	}

	public static String getSaveFilePath(Context context) {
		return context.getFilesDir().getPath();
	}
}
