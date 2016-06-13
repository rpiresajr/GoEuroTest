/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package goeurotest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import org.json.*;

/**
 *
 * @author Renato
 */
public class GoEuroTest {

    
    /**
        * Get Source HTML
        *
        * @param URL description 
        * @return HTML Source
    */
    public static String getHTML(String urlToGetSource) throws Exception {
      
      StringBuilder source = new StringBuilder();
      
      URL url = new URL(urlToGetSource);
      
      HttpURLConnection cn = (HttpURLConnection) url.openConnection();
      // get html source
      cn.setRequestMethod("GET");
      
      BufferedReader br = new BufferedReader(new InputStreamReader(cn.getInputStream()));
      
      String line;
      // read all lines
      while ((line = br.readLine()) != null) {
         source.append(line);
      }
      
      // close Buffered Reader
      br.close();
      
      return source.toString();
   }
    
    /**
        * Constructor function
        *
        * @param CITY NAME 
        * @return none 
    */
    public GoEuroTest(String city){
            // Declare variables
           
            JSONArray  jsonRoot = null;
            PrintWriter filewriter = null;
            
            String step ="0";
            try{
                
                String json_str = getHTML("http://api.goeuro.com/api/v2/position/suggest/en/"+city);
                
                jsonRoot = new JSONArray(json_str);
                
                // Creating the file File_CITY.csv ..
                filewriter = new PrintWriter("File_"+city+".csv", "UTF-8");
                
                // Wirte the header, using ";" like a sep
                filewriter.println("_id;name;type;latitude;longitude");
                
                // Get positions
                for(int i=0; i < jsonRoot.length(); i++) {
                   
                   // Get All objects per line
                   JSONObject jsonItem = jsonRoot.getJSONObject(i);
                   
                   // Get geo position
                   JSONObject geoLoc = jsonItem.getJSONObject("geo_position");
                   
                   // Write info into CSV FILE
                   filewriter.println(jsonItem.optString("_id").toString() + 
                            ";"+jsonItem.getString("name")+
                            ";"+jsonItem.getString("type")+
                            ";"+geoLoc.optString("latitude").toString() +
                            ";"+geoLoc.optString("longitude").toString());

                }

            }catch(Exception e){
                System.out.println(e.toString());
            }finally{
                // Closing the CSV File
                filewriter.close();
            }
        
    }
    
    /**
     * Main Funtion
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Check arguments
        if (args.length == 0){
            System.out.println("Syntax error. Please inform the city name!");
            System.out.println("Syntax: java -jar GoEuroTest \"CITY_NAME\"");
        }else{
            // build the city name ... put under line between the name
            String city = "";
            for (int i=0;i<args.length;i++)
                city+=args[i].toString()+"_";
            
            city=city.substring(0, city.length() -1);
            
            new GoEuroTest(""+city);
        }
    }
    
}
