package com.car.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/*图片数据可以通过两种方式上传，第一种在请求头设置image_url参数，第二种将图片二进制数据写入请求体中。若同时设置，以第一种为准。
 *使用二进制数据写入请求体时，不需要在header中传递image_url参数
 *使用传递url参数时，请求体为空即可
 *本例采用将图片二进制数据写入请求体中的方式
 *具体请参考接口文档：https://doc.xfyun.cn/rest_api/
 */
public class Currency {
	    // webapi 接口地址
		private static final String URL = "http://tupapi.xfyun.cn/v1/currency";
		// 应用ID
		private static final String APPID = "914d6187";
		// 接口密钥
		private static final String API_KEY = "5c408579cb66d9e5bd374bb2046c2d93";
		// 图片名称
		private static final String IMAGE_NAME = "image.jpg";
		// 图片url
		//private static final String IMAGE_URL = " ";
		
		// 图片地址
		private static final String PATH = "src/image.jpg";

		public String doPicRec() throws IOException {
			Map<String, String> header = buildHttpHeader();
			byte[] imageByteArray = FileUtil.read(PATH);
			String result = HttpUtil.doPost1(URL, header, imageByteArray);
			System.out.println("接口调用结果：" + result);
			String result_Num = "";
			int i = 39;
			while (result.charAt(i) >= 48 && result.charAt(i) <= 57){
				result_Num += result.charAt(i);
				i++;
			}
			return result_Num;
		}
		/**
		 * 组装http请求头
		 */
		private static Map<String, String> buildHttpHeader() throws UnsupportedEncodingException {
			String curTime = System.currentTimeMillis() / 1000L + "";
			String param = "{\"image_name\":\"" + IMAGE_NAME + "\"}";
			String paramBase64 = new String(Base64.encodeBase64(param.getBytes("UTF-8")));
			String checkSum = DigestUtils.md5Hex(API_KEY + curTime + paramBase64);
			Map<String, String> header = new HashMap<String, String>();
			header.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			header.put("X-Param", paramBase64);
			header.put("X-CurTime", curTime);
			header.put("X-CheckSum", checkSum);
			header.put("X-Appid", APPID);
			return header;
		}
}