package chen.nick.carousel;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toast toast;

    String [] urlData = {
            "https://www.google.com.tw/images/branding/googlelogo/2x/googlelogo_color_160x56dp.png",
            "https://developer.apple.com/news/images/og/safari-og-twitter.jpg",
            "https://www.mozilla.org/media/protocol/img/logos/firefox/browser/logo-lg-high-res.fbc7ffbb50fd.png",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/1/18/Internet_Explorer_10%2B11_logo.svg/1200px-Internet_Explorer_10%2B11_logo.svg.png",
            "https://img-prod-cms-rt-microsoft-com.akamaized.net/cms/api/am/imageFileData/RE4nqTh"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<ItemData> dataList = new ArrayList<>();
        for(int i = 0 ; i < urlData.length ; i ++){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("url",urlData[i]);
                dataList.add(new ItemData(jsonObject));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Carousel carousel = findViewById(R.id.carousel);
        carousel.setListData(dataList);
        carousel.setOnActionListener(new Carousel.OnActionListener() {
            @Override
            public void onClickItem(ItemData item) {
                sendToast(item.getUrl());
            }
        });
        carousel.init();

        Carousel carousel2 = findViewById(R.id.carousel2);
        carousel2.setListData(dataList);
        carousel2.setOnActionListener(new Carousel.OnActionListener() {
            @Override
            public void onClickItem(ItemData item) {
                sendToast(item.getUrl());
            }
        });
        carousel2.setDotSelectedResource(R.drawable.bg_dotselected);
        carousel2.setDotUnselectedResource(R.drawable.bg_unselected);
        carousel2.setOppositeDirection(true);
        carousel2.init();

        Carousel carousel3 = findViewById(R.id.carousel3);
        carousel3.setListData(dataList);
        carousel3.setOnActionListener(new Carousel.OnActionListener() {
            @Override
            public void onClickItem(ItemData item) {
                sendToast(item.getUrl());
            }
        });
        carousel3.setDelayInMilliseconds(6000);
        carousel3.setDotShow(false);
        carousel3.init();
    }


    @SuppressLint("WrongConstant")
    public void sendToast(String str){
        sendToast(str, Toast.LENGTH_SHORT);
    }
    @SuppressLint("WrongConstant")
    public void sendToast(@StringRes int str){
        sendToast(str,Toast.LENGTH_SHORT);
    }
    public void sendToast(String str,@BaseTransientBottomBar.Duration int duration){
        if(toast != null) toast.cancel();
        toast = Toast.makeText(this, str, duration);
        toast.show();
    }
    public void sendToast(@StringRes int str,@BaseTransientBottomBar.Duration int duration){
        if(toast != null) toast.cancel();
        toast = Toast.makeText(this, getString(str), duration);
        toast.show();
    }
}