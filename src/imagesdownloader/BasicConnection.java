package imagesdownloader;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;

public class BasicConnection{
        private int id;
        public BasicConnection(int id){
            this.id=id;
        }
	public void Download(ArrayList<URL> links,ArrayList<URL> mediaLinks) throws IOException {
//			URL url = new URL("http://scontent-fra3-1.cdninstagram.com/t50.2886-16/12786684_915801225205365_627508876_n.mp4");
//			URL url = new URL("https://www.instagram.com/p/BD3QalZJfCw/");
//			URL url = new URL("https://www.instagram.com/p/BAb5Li4JfF9/");
//			URL url = new URL("http://re11.anidub.ru/t/storage4/143/443/n_1_0_0.ts");
//			URL url = new URL("https://scontent-fra3-1.cdninstagram.com/t51.2885-15/e35/12328010_489114607956639_340330620_n.jpg?ig_cache_key=MTIyMjUxODAxOTQ1NTMxNjE0NA%3D%3D.2");
			
            for (URL url : links) {
		URLConnection connection=(HttpURLConnection) url.openConnection();
		connection.connect();
		String type = connection.getContentType();
		mediaLinks.add(readText(connection));
            }
            for (URL url : mediaLinks) {
		URLConnection connection=(HttpURLConnection) url.openConnection();
		connection.connect();
		String type = connection.getContentType();
		if(type.equals("text/html"))
                    readText(connection);
		else if(type.equals("video/mp4"))
                    saveMedia(connection,1);
                else if(type.equals("image/jpeg"))
                    saveMedia(connection,2);
//              System.out.println(connection.getHeaderFields());
		}
	}
	private void saveMedia(URLConnection connection, int type) throws IOException {
//		byte[] v1 = (byte[]) connection.getContent();
            InputStream is = connection.getInputStream();
            FileOutputStream fos = null;
            switch (type) {
            case 1:
                String g1 = randomString(8); 
		fos = new FileOutputStream(g1+".mp4");
                break;
            case 2:
                String g2 = randomString(8);
		fos = new FileOutputStream(g2+".jpg");
		break;
            default:
		break;
            }
            int b = is.read();
            long counter=1;
            String str;
            str = Integer.toBinaryString(b);
            while(b != -1){
//        while(counter != 200){
//        	System.out.println(str);
                fos.write(b);
                b = is.read();
                if(b != -1)
                    counter++;
                str = Integer.toBinaryString(b);
            }
            System.out.println("Total amount of bytes: "+counter);
        }
	private URL readText(URLConnection connection) throws IOException{
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            URL mediaLink=null;
            String strTemp = "";
            while (null != (strTemp = br.readLine())) {
//		System.out.println(strTemp);
		if(strTemp.contains("og:image")){
                    int start = strTemp.indexOf("og:image")+19;
                    int end = strTemp.lastIndexOf("/>")-2;
                    mediaLink = new URL(strTemp.substring(start, end));
                }
                else if(strTemp.contains("og:video")){
                    int start = strTemp.indexOf("og:video")+19;
                    int end = strTemp.lastIndexOf("/>")-2;
                    mediaLink = new URL(strTemp.substring(start, end));
                    break;
		}
            }
            System.out.println("New media link was found: "+mediaLink);
            return mediaLink;
	}

        String randomString( int len ){
            String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
            SecureRandom rnd = new SecureRandom();
            StringBuilder sb = new StringBuilder( len );
            for( int i = 0; i < len; i++ ) 
                sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
            return sb.toString();
        }

}
