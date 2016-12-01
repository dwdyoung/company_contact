package com.monday.companycontact.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PingYinUtil {
	/**
	 * 将字符串中的中文转化为拼音,其他字符不变
	 *
	 * @param inputString
	 * @return
	 */

	private static HanyuPinyinOutputFormat _format;
	public static String getPingYin(String inputString) {
		if(inputString.length() == 1){
			char c = inputString.charAt(0);
			if(c <= 'z' && c >= 'a')
				return inputString.toUpperCase();

			if(c <= 'Z' && c >= 'A')
				return inputString;

			// 为其他字符
			HanyuPinyinOutputFormat form = getFormat();
			String[] res = PinyinHelper.toHanyuPinyinStringArray(c);
			if(res == null){
				return "#";
			}
			if(res.length > 0){
				return res[0];
			} else {
			}
		}
		HanyuPinyinOutputFormat format = getFormat();

		char[] input = inputString.trim().toCharArray();
		String output = "";

		try {
			for (int i = 0; i < input.length; i++) {
				if (java.lang.Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i],format);
					output += temp[0];
				} else
					output += java.lang.Character.toString(
							input[i]);
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return output;
	}

	private static HanyuPinyinOutputFormat getFormat(){
		if(_format == null){
			_format = new HanyuPinyinOutputFormat();
			_format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
			_format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			_format.setVCharType(HanyuPinyinVCharType.WITH_V);
		}
		return _format;
	}
}
