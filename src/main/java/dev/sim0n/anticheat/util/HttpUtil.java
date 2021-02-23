package dev.sim0n.anticheat.util;

import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@UtilityClass
public class HttpUtil {
    public String getPastie(String body) {
        try {
            URL url = new URL("https://pastie.io/documents");

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.109 Safari/537.36");
            connection.setRequestProperty("Content-type", "text/plain");

            connection.setInstanceFollowRedirects(false);

            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(body);
            os.close();

            StringBuilder sb = new StringBuilder();

            try (InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                String string;
                while ((string = bufferedReader.readLine()) != null) {
                    sb.append(string);
                }
            }

            return new JsonParser().parse(sb.toString()).getAsJsonObject().get("key").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
