package com.example.xinran.mingxing.http;


import com.example.xinran.ftp.FTPTools;
import okhttp3.*;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.springframework.util.Assert.notNull;

/**
 *
 */
public class HttpUtils {

	private static enum MediaTypeEnum {

		JSON("application/json; charset=utf-8"), TEXT("application/x-www-form-urlencoded; charset=utf-8");

		private String typeString;

		private MediaType type;

		private MediaTypeEnum(String typeString) {
			this.typeString = typeString;
			this.type = MediaType.parse(this.typeString);
		}

		public MediaType toMediaType() {
			return type;
		}
	}

	private static final MediaType MEDIA_TYPE_JSON = MediaTypeEnum.JSON.toMediaType();
	// MEDIA_TYPE_TEXT
	// post请求不是application/x-www-form-urlencoded的，全部直接返回，不作处理，即不会解析表单数据来放到request
	// parameter map中。所以通过request.getParameter(name)是获取不到的。只能使用最原始的方式，读取输入流来获取。
	private static final MediaType MEDIA_TYPE_TEXT = MediaTypeEnum.TEXT.toMediaType();

	private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	public static OkHttpClient getOKHttpClient() {
		return getOKHttpClient(10,20);
	}
	
	public static OkHttpClient getOKHttpClient(int connectTimeout,int readTimeout) {
		return new OkHttpClient.Builder().connectTimeout(connectTimeout, TimeUnit.SECONDS).readTimeout(readTimeout, TimeUnit.SECONDS)
				.build();
	}
	

	/**
	 * @param url getUrl
	 * @return java.lang.String
	 * @author xiaobu
	 * @date 2019/3/4 11:20
	 * @descprition
	 * @version 1.0
	 */

	public static String sendByGetUrl(String url) {
		try {
			Response response = sendByGetUrl(url, null);
			String result = response.body().string();
			return result;
		} catch (IOException e) {
			logger.error("Error on calling " + url + ",Message:" + e.getMessage(), e);
		}
		return null;
	}

	/**
	 *
	 * @param url
	 * @param headers
	 * @return
	 */
	public static Response sendByGetUrl(String url, Headers headers) throws IOException {
		OkHttpClient client = getOKHttpClient();
		logger.debug("Calling {}", url);
		Request.Builder builder = new Request.Builder().url(url);
		if (headers != null && !headers.names().isEmpty()) {
			logger.debug("with {}", headers);
			builder.headers(headers);
		}
		Request request = builder.get().build();
		Response response = client.newCall(request).execute();
		if (response == null) {
			logger.warn("not get response from {}", url);
		}
		logger.debug("response HTTP code {}", response.code());
		logger.debug("response HTTP headers {}", response.headers().toString());
		return response;

	}

	/**
	 * delete请求
	 * 
	 * @param url
	 * @param headers
	 * @return
	 */
	public static Response sendByDeleteUrl(String url, Headers headers) throws IOException {
		OkHttpClient client = getOKHttpClient();
		logger.debug("Calling {}", url);
		Request.Builder builder = new Request.Builder().url(url);
		if (headers != null && !headers.names().isEmpty()) {
			logger.debug("with {}", headers);
			builder.headers(headers);
		}
		Request request = builder.delete().build();
		Response response = client.newCall(request).execute();
		if (response == null) {
			logger.warn("not get response from {}", url);
		}
		logger.debug("response HTTP code {}", response.code());
		logger.debug("response HTTP headers {}", response.headers().toString());
		return response;
	}

	/**
	 * @param url , json
	 * @return java.lang.String
	 * @author xiaobu
	 * @date 2019/3/4 11:13
	 * @descprition
	 * @version 1.0 post+json方式
	 */
	public static String sendByJson(Headers headers, String url, String json, HttpMethod methodType) {
		notNull(url, "Request url must not be null");
		OkHttpClient client = getOKHttpClient();
		RequestBody body = StringUtils.isEmpty(json) ? null : RequestBody.create(MEDIA_TYPE_JSON, json);
		Request request = createRequest(headers, url, body, methodType);
		Response response = null;
		try {
			response = client.newCall(request).execute();
			return response.body().string();
		} catch (IOException e) {
			logger.error("Error on calling " + url + ",Message:" + e.getMessage(), e);
		}
		return null;
	}


	/**
	 *
	 * @param url
	 * @param json
	 * @param headers
	 * @author huanglei
	 * @version 1.0 post+json方式
	 */
	public static Response sendByPostJson(String url, String json, Headers headers) throws IOException {
		OkHttpClient client = getOKHttpClient();
		RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
		Request request = new Request.Builder().url(url).headers(headers).post(body).build();
		return client.newCall(request).execute();
	}

	private static String buildContent(Map<String, String> params) {
		StringBuilder content = new StringBuilder();
		Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> entry = iterator.next();
			content.append(entry.getKey()).append("=").append(entry.getValue());
			if (iterator.hasNext()) {
				content.append("&");
			}
		}
		String msg = content.toString();
		logger.debug(msg);
		return msg;
	}

	public static String sendByPostMap(String url, Map<String, String> params) {
		OkHttpClient client = getOKHttpClient();
		RequestBody requestBody = RequestBody.create(MEDIA_TYPE_TEXT, buildContent(params));
		Request request = new Request.Builder().url(url).post(requestBody).build();
		Response response = null;
		try {
			response = client.newCall(request).execute();
			return response.body().string();
		} catch (IOException e) {
			logger.error("Error on calling " + url + ",Message:" + e.getMessage(), e);
		}
		return null;
	}

	public static String sendByPostString(String url, String content) throws IOException {
		OkHttpClient client = getOKHttpClient();
		RequestBody requestBody = RequestBody.create(MEDIA_TYPE_TEXT, content);
		Request request = new Request.Builder().url(url).post(requestBody).build();
		Response response = null;
		try {
			response = client.newCall(request).execute();
			return response.body().string();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return response.body().string();
	}
	
	
	
	public static RequestBody buildMultiPartRequest(String keyName,Map<String,InputStream> fileGroup) {
		MultipartBody.Builder builder= new MultipartBody.Builder();
		if(!fileGroup.isEmpty()) {
			int i=-1;
			for(Map.Entry<String,InputStream> e:fileGroup.entrySet()) {		
				RequestBody body=buildFileUploadRequest(MultipartBody.FORM,e.getValue());
				builder.addFormDataPart(keyName+String.format("[%d]", ++i), e.getKey(), body);
			}
			builder.setType(MultipartBody.FORM);
		}
		return builder.build();
	}
	
	public static RequestBody buildMultiPartRequest(Map<String,InputStream> fileGroup) {
		MultipartBody.Builder builder= new MultipartBody.Builder();
		
		if(!fileGroup.isEmpty()) {
			int i=-1;
			for(Map.Entry<String,InputStream> e:fileGroup.entrySet()) {		
				RequestBody body=buildFileUploadRequest(MultipartBody.FORM,e.getValue());
				builder.addFormDataPart(String.format("file[%d]", ++i), e.getKey(), body);
			}
		}
		builder.setType(MultipartBody.FORM);
		return builder.build();
	}
	
	
	public static RequestBody buildFileUploadRequest(MediaType mediaType, InputStream inputStream) {
		return new RequestBody() {
			@Override
			public MediaType contentType() {
				return mediaType;
			}

			@Override
			public long contentLength() {
				try {
					return inputStream.available();
				} catch (IOException e) {
					return 0;
				}
			}

			@Override
			public void writeTo(BufferedSink sink) throws IOException {
				Source source = null;
				try {
					source = Okio.source(inputStream);
					sink.writeAll(source);
				} finally {
					Util.closeQuietly(source);
				}
			}

		};
	}




	
	



	
	

	public static Request createRequest(Headers headers, String url, RequestBody body, HttpMethod methodType) {
		Request.Builder builder = new Request.Builder();
		if (headers != null) {
			builder.headers(headers);
		}
		builder.url(url);
		logger.debug("Calling {}", url);
		logger.debug("Calling with method {}", methodType);
		switch (methodType) {
		case POST:
			if (body != null) {
				builder.post(body);
			}
			break;
		case DELETE:
			builder.delete(body);
			break;
		case PUT:
			if (body != null) {
				builder.put(body);
			}
			break;
		case GET:
			builder.get();
			break;
		default:
			// default is getmethod;
			break;
		}
		return builder.build();
	}
	
//	public static void main(String[] args) {
//	Map<String, Object> values = new HashMap<>();
//	values.put("a", new String[]{"bac","Dac"});
//	values.put("b", "bad");
//	values.put("bac",Arrays.asList("bac1","Dac2"));
//	values.put("list",Arrays.asList(Arrays.asList("bac31","Dac23")));
//	values.put("Obj",new ApiEntity());
//	Arrays.asList(values,values);
//
//	System.out.println(buildContent(ReflectionUtils.convertToMap(Arrays.asList(values,values))));
//	System.out.println(buildContent(ReflectionUtils.convertToMap(Boolean.FALSE)));
//}


	public static void main(String[] args) throws Exception {
		Map<String,String> result = new HashMap<>();


		OkHttpClient client = getOKHttpClient();
		Map head = new HashMap();
		head.put("token","ad131e0b-0b59-4565-8081-cb468ad6a568");
//		Request request = createRequest(Headers.of(head), "http://10.152.160.56/seeyon/rest/archives/file/download/-2889952317158568573/2021-02-02/test", null, HttpMethod.GET);
//		Request request = createRequest(Headers.of(head), "http://10.152.160.56/seeyon/rest/orgDepartment/-3297552534959010075", null, HttpMethod.GET);
//		Request request = createRequest(Headers.of(head), "http://10.152.160.56/seeyon/rest/archives/form/data/-82908692383314259", null, HttpMethod.GET);
		Request request = createRequest(Headers.of(head), "http://localhost:8081/text/down", null, HttpMethod.GET);
		Response response = null;
		try {
			response = client.newCall(request).execute();
//			String string = response.body().string();
//			System.out.println(string);
//			Map resp = JacksonUtil.fromString(string, Map.class);
//			if (resp.containsKey("data")) {
//				Map data = JacksonUtil.convertValue(resp.get("data"), Map.class);
//				if (data.containsKey("mList")) {
//					List<Map> mLists = JacksonUtil.convertList(data.get("mList"), Map.class);
//					Map mList = mLists.get(0);
//					if (mList.containsKey("Fields")) {
//						Map fields = JacksonUtil.convertValue(mList.get("Fields"), Map.class);
//						if (fields.containsKey("mFieldsInfo")) {
//							List<Map> mFieldsInfo = JacksonUtil.convertList(fields.get("mFieldsInfo"), Map.class);
//							if (!mFieldsInfo.isEmpty() && fields.containsKey("mTableName")) {
//								mFieldsInfo.stream().filter(m ->{return m.containsKey("display") && m.containsKey("name");})
//										.forEach(f -> {result.put((String)f.get("display"),(String)f.get("name"));});
//
//								result.put("tableName",(String)fields.get("mTableName"));
//							}
//						}
//					}
//				}
//			}




//			System.out.println(result);


			//获取返回流，使用FTP上传
			long l = response.body().contentLength();
			if (!Boolean.valueOf(response.header("File-Exist"))) {
				System.out.println("本次没有下载文件");
				return;
			}
			InputStream inputStream = response.body().byteStream();
			DataInputStream in = new DataInputStream(inputStream);
//			FileOutputStream fileOutputStream = new FileOutputStream(new File("F:\\test\\govdoc\\upload\\2021\\01\\19\\aa.docx"));
//			byte [] b = new byte[inputStream.available()];
//			inputStream.read(b);
//			fileOutputStream.write(b);




			List<String> path = new ArrayList();
			path.add("govdoc");
			path.add("upload");
			path.add("2021");
			path.add("01");
			path.add("19");
			FTPTools.upload("10.1.67.176",21,"ftpuser","ftpuser",path,in,"2889952317158568573");







		} catch (IOException e) {
			System.out.println("error ... ");
		} finally {

		}
		System.out.println("success");



//		downloadFile("http://localhost:8081/text/down","F:\\test\\govdoc\\upload\\2021\\01\\19\\idea.docx");
	}


	public static void main1(String[] args) throws Exception {
		downloadFile("http://localhost:8081/text/down","F:\\test\\govdoc\\upload\\2021\\01\\19\\1234.txt");
	}








	/**
	 * TODO 下载文件到本地
	 * @author nadim
	 * @date Sep 11, 2015 11:45:31 AM
	 * @param fileUrl 远程地址
	 * @param fileLocal 本地路径
	 * @throws Exception
	 */
	public static void downloadFile(String fileUrl, String fileLocal) throws Exception {
		URL url = new URL(fileUrl);
		HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
		urlCon.setConnectTimeout(6000);
		urlCon.setReadTimeout(6000);
		int code = urlCon.getResponseCode();
		if (code != HttpURLConnection.HTTP_OK) {
			throw new Exception("文件读取失败");
		}

		//读文件流
		DataInputStream in = new DataInputStream(urlCon.getInputStream());
		DataOutputStream out = new DataOutputStream(new FileOutputStream(fileLocal));
		byte[] buffer = new byte[2048];
		int count = 0;
		while ((count = in.read(buffer)) > 0) {
			out.write(buffer, 0, count);
		}
		out.close();
		in.close();
	}

	
}
