import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.sql.*;

import java.io.*;

public class DownLoadT implements Runnable{

	private String page;
	private Statement tstat;
	private int picid;
	private final static String mregex = "<img src=\"(.+?)\"\\s/>";
	private final static String filebase = "E:/meimei";
	DownLoadT()
	{
		
	}
	DownLoadT(String htmlpage,int tid,Statement mstat) throws SQLException
	{
		page = htmlpage;
		picid = tid;
		tstat = mstat;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub

		File dir = new File(filebase);
		if(!dir.exists())
		{
			dir.mkdir();
		}
		Pattern pattern = Pattern.compile(mregex);
		Matcher match = pattern.matcher(page);
	//	System.out.println(page);
		while(match.find())
		{
			String picaddr = match.group(1);
			 try {
				tstat.executeUpdate("insert into pictures (id,name) values ("
				 		+ String.valueOf(picid) + ",'" + picaddr + "')");
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			System.out.println(picaddr);
			String picname = picaddr.substring(picaddr.lastIndexOf('/') + 1);
			File file = new File(filebase + "/" 
			//+  String.valueOf(picid) 
					+ picname);
			OutputStream os = null;
			URL url = null;
			InputStream is = null;
			try {
				os = new FileOutputStream(file);
				    //创建一个url对象
				url = new URL(picaddr);
				is = url.openStream();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        try
	        {
	            byte[] buff = new byte[1024];
	            while(true) {
	                int readed = is.read(buff, 0, 1024);
	                if(readed == -1) {
	                    break;
	                }
	                //写入文件
	           //     os.write(buff, 0, readed);
	            }
	           
	        }
	        catch(Exception e)
	        {
	            e.printStackTrace();
	        }
	        finally
	        {
	        	try {
					is.close();
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
		}
    	lock mlock = Spider.oarr[picid-1];
    	synchronized(mlock)
    	{
    		mlock.notify();
    		lock.finished = true;
    	}
	}	

}
