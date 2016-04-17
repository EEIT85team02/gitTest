package _01_Servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.iiiedu.googleOA2.scribe.builder.api.Google2Api;
import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

@WebServlet("/oauth2callback.do")
public class oauth2callback extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public oauth2callback() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
      
    	// -- 取得google 回傳的 code
     Verifier verifier = new Verifier(req.getParameter("code"));
     String apiKey = "";
     String apiSecret = "";
     String redirect_uri = "";
     String calendarKey = "";
     
     ServletContext context = req.getServletContext();
     System.out.println("Context= "+context.toString());
     String realPath = context.getRealPath("/");
     System.out.println("真實位置= "+ realPath);
     realPath+="\\_02_data\\"+"client_events.json";
     
     BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(realPath)));
     
     // -- 準備OAuthService物件
     OAuthService service = new ServiceBuilder()
     							.provider(Google2Api.class)
     							.apiKey(apiKey)
     							.apiSecret(apiSecret)
     							.callback(redirect_uri)
     							.build();

     // -- 取得驗證 (accessToken)
     Token accessToken = service.getAccessToken(null, verifier);
     
     
     // -- 純粹OAuth2驗證成功的憑證【parameter/code】
     System.out.println(req.getParameter("code"));
     
     
     // -- 根據scope指定的API再發出的憑證【accessToken】
     System.out.println(accessToken.toString());
     
     
     
     
     
     
     
  // -- 請求基本資料流程
     
     // -- 製作請求物件【指定JSON】
     OAuthRequest req01 = new OAuthRequest(Verb.GET, "https://www.googleapis.com/oauth2/v1/userinfo?alt=json");
     // -- 簽署請求物件 【附上accessToken】
     service.signRequest(accessToken, req01);
     // -- 發送請求 取得回應
     Response resp01 = req01.send();
     // Got it! Lets see what we found...



     // -- 格式轉換
     // -- String 轉 JSONObject 
     JSONObject jObject = new JSONObject(resp01.getBody());
     
     
  // -- 取得資料行為
     
     // -- 取email
     String email =jObject.getString("email");
     System.out.println(email);

     
  // -- 請求某服務資料流程【Calendar為例】
     
     // -- 製作請求物件【Google Calendar的事件s】
     OAuthRequest req02 = new OAuthRequest(Verb.GET, "https://www.googleapis.com/calendar/v3/calendars/"+email+"/events/?key={"+calendarKey+"}");
     // -- 簽署請求
     service.signRequest(accessToken, req02);
     // -- 發送請求 同時得回應
     Response resp02 = req02.send();

     
     
     // -- 格式轉換
     // -- String 轉 JSONObject 
     JSONObject jsonObject = new JSONObject(resp02.getBody());
     // -- 從JSONObject取得指定的JSONArray
     JSONArray jsonArray =(JSONArray)jsonObject.get("items");
     
     BufferedReader bf = new BufferedReader(new StringReader(jsonArray.toString())); 
	 String bb;
	 while((bb=bf.readLine())!=null){
		out.write(bb);
	} 
	 out.close();
	

     
     
     
     
     req.getSession().setAttribute("events", jsonArray);
     resp.sendRedirect(req.getContextPath()+"/_01_View/events.jsp");
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
