package server;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class APICurrency {

	private static String direccion = "https://trustpilot.digitalshopuy.com/currency/"; 
	private static String error = "{\"error\":\"Cannot read properties of undefined (reading 'from')\"}";
    
	
    public static Double conversor(String moneda1, String moneda2, Double cantidad) throws IOException {
    	
    	//iniciasion del URL de la api
    	URL url = new URL(direccion + "convert?from="+ moneda1 + "&to=" + moneda2 + "&amount="+ cantidad);
    	
    	//conexion http
    	HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");
        httpConn.setRequestProperty("accept", "application/json");
        
        
        //input de la respuesta
        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream() : httpConn.getErrorStream();
        
        //lectura de la respuesta
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        
        //Error si la moneda no coicide
        if (response.equals(error)){
        	return -1.0;
        }
        
        //conversion a double
        Double ret = Double.parseDouble(response.split(",")[0].split(":")[1]);
        
    	
    	return ret;
    }

}
