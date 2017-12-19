package JsonBean;

import com.google.gson.Gson;

/**
 * Created by fulxtaw on 2017/12/19.
 */

public class JsonUtils {
    //将json数据转化为字符串
    public static  Response getResponse(String result) {
        try{
            Gson gson= new Gson();
            Response response= gson.fromJson(result,Response.class);
            return response;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
