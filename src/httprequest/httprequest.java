/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httprequest;

/**
 *
 * @author Castiel
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

public class httprequest {
    
	private final String USER_AGENT = "Mozilla/5.0";
        private int responseCode;
        
        public int getResponseCode(){
            return responseCode;
        }
	public String sendGet(String url) throws Exception {
 
		URL obj = new URL(url);//convert String sang object URL
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
                
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
 
		int responseCode = con.getResponseCode();

		//System.out.println("\nSending 'GET' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		//StringBuffer response = new StringBuffer();
                //String response = new String();
                StringBuilder response = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
		}
		in.close();
                
                //System.out.println(response);
                return response.toString();
	}
 
	// HTTP POST request
	public String sendPost(String url,String urlParameters ) throws Exception {
 
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
 
		//String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
 
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
                //System.out.println( response.toString() );
		return response.toString();
 
	}

	public static void main(String[] args) throws Exception {
            httprequest h = new httprequest();
            System.out.println( h.sendGet("http://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=vi&dt=t&q=Hello+World&ie=UTF-8&oe=UTF-8%22") );
	}
      

}