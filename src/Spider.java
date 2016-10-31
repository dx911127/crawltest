import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class Spider {

	static Connection connect;
	static Statement stmt;
	static Object[] oarr;
	public static void main(String[] args) throws ClientProtocolException, IOException, URISyntaxException, InterruptedException, SQLException {
		// TODO Auto-generated method stub
		connect = null;
		stmt = null;
		oarr = new Object[13];
		 try {
		      connect = DriverManager.getConnection(
		          "jdbc:mysql://localhost:3306/helloWorld","root","dx916562");
		           //����URLΪ   jdbc:mysql//��������ַ/���ݿ���  �������2�������ֱ��ǵ�½�û���������

		      System.out.println("Success connect Mysql server!");
		      PreparedStatement pstmt = connect.prepareStatement("create table if not exists pictures(id int, name char(50))");
		      pstmt.execute();
		      stmt = connect.createStatement();
		 	}
		 	catch (Exception e) {
		      System.out.print("get data error!");
		      e.printStackTrace();
		   }
		RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).setConnectionRequestTimeout(6000).setConnectTimeout(6000).build();
	    CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
	    HttpGet httpget = new HttpGet();
		httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");
	    httpget.addHeader("Cookie","_gat=1; nsfw-click-load=off; gif-click-load=on; _ga=GA1.2.1861846600.1423061484");
	    for(int i=1; i<=13; i++)
		{
			Thread.sleep(5000);
			oarr[i-1] = new Object();
			CloseableHttpResponse response = null;
			try
			{
				httpget.setURI(new URI("http://jandan.net/ooxx/page=" + String.valueOf(i)));
			//System.out.println("http://jandan.net/ooxx/page=" + String.valueOf(i));
			//	System.out.println(httpget.getURI().toString());
				response = httpclient.execute(httpget);
				HttpEntity htmls = response.getEntity();
				new DownLoadT(EntityUtils.toString(htmls), i, connect.createStatement()).run();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				response.close();
			}
			
		/*	try {
			// ��ȡ��Ӧʵ��  
				HttpEntity entity = response.getEntity();
				System.out.println("--------------------------------------");
			// ��ӡ��Ӧ״̬  
				System.out.println(response.getStatusLine());
			if (entity != null) {
				// ��ӡ��Ӧ���ݳ���  
				System.out.println("Response content length: " + entity.getContentLength());
				// ��ӡ��Ӧ����  
				String pageContent = EntityUtils.toString(entity);
			}
			System.out.println("------------------------------------");
			} finally {
				response.close();
			}*/
		}
	    for(int i=0; i<13; i++)
	    {
	    	Object o1 = oarr[i];
	    	synchronized(o1)
	    	{
	    		o1.wait();
	    	}
	    }
	    httpclient.close();
	    stmt = connect.createStatement();
		ResultSet rs = stmt.executeQuery("select * from pictures");
		//user Ϊ��������
		while (rs.next())
		{
			String tmp = "";
			for(int i=1; i<=2; i++)
			{
				tmp += rs.getString(i)+"\t";
			}
			System.out.println(tmp);
		}
	//	connect.close();
	}
}
