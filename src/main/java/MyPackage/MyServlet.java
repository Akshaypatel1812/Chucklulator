package MyPackage;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public MyServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String str1 = request.getParameter("num1");
        String str2 = request.getParameter("num2");
        String str3 = request.getParameter("bt1");

        int num1 = Integer.parseInt(str1);
        int num2 = Integer.parseInt(str2);
        int ans;

        if (str3.equals("1")) {
            ans = num1 + num2;
        } else if (str3.equals("2")) {
            ans = num1 - num2;
        } else if (str3.equals("3")) {
            ans = num1 * num2;
        } else {
            ans = num1 / num2;
        }

        // Fetch a random joke from the API
        String joke = fetchRandomJoke();

        response.sendRedirect("index.jsp?ans=" + ans + "&joke=" + joke);
    }

    // Method to fetch a random joke from the API
    private String fetchRandomJoke() {
        String api_url = "https://api.api-ninjas.com/v1/jokes";
        String api_key = "l5FBXM3qcL9xeHatRjMu6A==1izWXZNJmc0klUUN"; // Replace with your actual API key
        int limit = 1;

        String apiUrlWithParams = api_url + "?limit=" + limit;

        try {
            URL url = new URL(apiUrlWithParams);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-API-KEY", api_key);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                // Assuming the response is in JSON format
                Gson gson = new Gson();
                JsonArray jsonArray = gson.fromJson(content.toString(), JsonArray.class);

                if (jsonArray.size() > 0) {
                    JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
                    return jsonObject.getAsJsonPrimitive("joke").getAsString();
                } else {
                    return "No jokes found in the response.";
                }
            } else {
                return "Failed to fetch joke. Error " + responseCode + ": " + connection.getResponseMessage();
            }
        } catch (Exception e) {
            return "Failed to fetch joke. Error: " + e.getMessage();
        }
    }
}
