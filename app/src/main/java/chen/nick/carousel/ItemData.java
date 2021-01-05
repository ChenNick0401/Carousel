package chen.nick.carousel;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemData {

    private String url ;

    public ItemData(String index){
        try {
            JSONObject jsonObject = new JSONObject(index);
            this.url = jsonObject.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public ItemData(JSONObject index){
        try {
            this.url = index.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl(){
        return url;
    }


    public JSONObject getJSON(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url",url);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
