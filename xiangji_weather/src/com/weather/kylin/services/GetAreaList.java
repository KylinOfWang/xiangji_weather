package com.weather.kylin.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.weather.kylin.model.AreaListJson;
import com.weather.kylin.model.AreaResult;

public class GetAreaList {
	public static String getAreaList() {

		String url_path = "http://v.juhe.cn/xiangji_weather/areaList.php?key=01578cfce21f1cd007c9b3b30a5b8fd8";
		int responseCode = 0;
		String response_areaList = "";
		URL realUrl;
		try {
			realUrl = new URL(url_path);
			HttpURLConnection connection = (HttpURLConnection) realUrl
					.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setRequestProperty("content-type", "text/html");
			connection.setRequestMethod("GET");
			connection.connect();
			responseCode = connection.getResponseCode();
			if (responseCode == 200) {

				BufferedReader br = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				String line;
				StringBuilder sb = new StringBuilder();
				while ((line = br.readLine()) != null) {// 循环读取流
					sb.append(line);
				}
				response_areaList = sb.toString();
				br.close();// 关闭流
			}

			connection.disconnect();// 断开连接

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response_areaList;

	}

	public static int insert_mysql(String sql) {
		Connection conn = null;

		// MySQL的JDBC URL编写方式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值

		// 避免中文乱码要指定useUnicode和characterEncoding

		// 执行数据库操作之前要在数据库管理系统上创建一个数据库，名字自己定，

		// 下面语句之前就要先创建javademo数据库

		String url = "jdbc:mysql://localhost:3306/wang_weather?"

		+ "user=root&password=930710&useUnicode=true&characterEncoding=UTF8";
		int result = 0;
		try {

			// 之所以要使用下面这条语句，是因为要使用MySQL的驱动，所以我们要把它驱动起来，

			// 可以通过Class.forName把它加载进去，也可以通过初始化来驱动起来，下面三种形式都可以

			Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动

			// or:

			// com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();

			// or：

			// new com.mysql.jdbc.Driver();

			System.out.println("成功加载MySQL驱动程序");

			// 一个Connection代表一个数据库连接

			conn = DriverManager.getConnection(url);

			// Statement里面带有很多方法，比如executeUpdate可以实现插入，更新和删除等

			Statement stmt = conn.createStatement();

			result = stmt.executeUpdate(sql);

		} catch (SQLException e) {

			System.out.println("MySQL操作错误");

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return result;
	}

	public static void main(String[] args) {
		Gson gson = new Gson();
		AreaListJson areaListJson = gson.fromJson(getAreaList(),
				AreaListJson.class);
		String mysql = "";
		List<AreaResult> areaResults = null;
		areaResults = areaListJson.getResult();
		int result_number;
		if (areaResults != null && !areaResults.equals("")) {
			for (Iterator<AreaResult> iterator = areaResults.iterator(); iterator
					.hasNext();) {
				AreaResult areaResult = (AreaResult) iterator.next();

				mysql = "INSERT INTO t_area_info VALUES (null,\""
						+ areaResult.getProvince() + "\",\""
						+ areaResult.getCity() + "\",\"" + areaResult.getArea()
						+ "\",\"" + areaResult.getAreaid() + "\",\""
						+ areaResult.getLon() + "\",\"" + areaResult.getLat()
						+ "\")";
				result_number = insert_mysql(mysql);
				if(result_number==-1){
					System.out.println(result_number);
				}

			}
		}

	}
}
