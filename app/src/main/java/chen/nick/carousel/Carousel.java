package chen.nick.carousel;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Carousel extends ConstraintLayout{

    static final String TAG = "Carousel";

    private ViewPager2 viewpager;
    private LinearLayout bottomLayout;
    private List<ImageView> tabViews = new ArrayList<>();
    private int nowInt = 0;
    private Context context;
    private OnActionListener onActionListener;
    private List<ItemData> pages;
    private Timer timer;
    private TimerTask timerTask;
    private int delayInMilliseconds = 3000;
    private boolean isShowDot = true;
    private final int infinite = 2;

    private int dotSizeDP = 17;
    private int dotSpacingDP = 3;
    private int dotStrokeSizeDP = 0;

    private @ColorInt int dotSelectedColor = Color.parseColor("#60ffffff");
    private @ColorInt int dotSelectedStrokeColor = Color.parseColor("#60ffffff");
    private @ColorInt int dotUnselectedColor = Color.parseColor("#60000000");
    private @ColorInt int dotUnselectedStrokeColor = Color.parseColor("#60000000");
    private GradientDrawable gdDotSelected;
    private GradientDrawable gdDotUnselected;
    private @DrawableRes int ridDotSelected = -1;
    private @DrawableRes int ridDotUnselected = -1;

    private boolean isOppositeDirection = false;

    public interface OnActionListener {
        default void onClickItem(ItemData item) { }
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    public Carousel(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public Carousel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public Carousel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public Carousel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.carousel_main, this, true);
        viewpager =  findViewById(R.id.viewpager);
        bottomLayout = findViewById(R.id.bottomLayout);
        this.context = context;
    }

    public void setListData(List<ItemData> list) {
        pages = list;
    }

    public void init() {
        if(timer != null){
            timerTask.cancel();
            timer.cancel();
        }
        if(pages.size() == 0)
            return;
        nowInt = infinite;
        CarouselAdapter adapter = new CarouselAdapter(context,pages);
        adapter.setOnActionListener(new CarouselAdapter.OnActionListener() {
            @Override
            public void onClickItem(int item) {
                if(onActionListener != null) onActionListener.onClickItem(pages.get(item));
            }
        });
        viewpager.setAdapter(adapter);
        viewpager.setPageTransformer((page, position) -> {
            final float normalizedPosition = Math.abs(Math.abs(position) - 1);
            page.setAlpha(normalizedPosition);
        });
        viewpager.setCurrentItem(nowInt);
        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                updateTheDotBelow(position);
            }
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position < infinite){
                    position = ( (pages.size() + infinite) - 1);
                    viewpager.setCurrentItem(position);
                }else if(position >= (pages.size() + infinite)){
                    position = infinite;
                    viewpager.setCurrentItem(position);
                }else {
                    updateTheDotBelow(position);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        initTimer();
        for(int i = 0 ; i < pages.size();i++){
            ImageView imageView = new ImageView(context);
            LayoutParams layoutParams = new LayoutParams((int)(pxFromDp(context,dotSizeDP)), (int)(pxFromDp(context,dotSizeDP)));
            ((LayoutParams)layoutParams).setMargins(i == 0 ? 0 : (int)(pxFromDp(context,dotSpacingDP)),0,(int)(pxFromDp(context,dotSpacingDP)),(int)(pxFromDp(context,dotSpacingDP)));
            imageView.setLayoutParams(layoutParams);
            int finalI = i;
            imageView.setOnClickListener(v -> {
                nowInt = finalI;
                timerTask.cancel();
                timer.cancel();
                viewpager.setCurrentItem(finalI + infinite);
                initTimer();
            });
            bottomLayout.addView(imageView);
            tabViews.add(imageView);
        }
    }

    private void initTimer(){
        if(timer != null){
            timerTask.cancel();
            timer.cancel();
        }
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                viewpager.post(() -> {
                    if(isOppositeDirection) {
                        if(infinite >= nowInt){
                            nowInt = (pages.size() + infinite - 1);
                        }else{
                            nowInt--;
                        }
                    }else {
                        if((pages.size() + infinite - 1) <= nowInt){
                            nowInt = infinite;
                        }else{
                            nowInt++;
                        }
                    }
                    viewpager.setCurrentItem(nowInt);
                });
            }
        };
        timer.schedule(timerTask, delayInMilliseconds, delayInMilliseconds);
    }

    private void updateTheDotBelow(int index){
        nowInt = index;
        index = index - infinite;
        for(int i = 0 ; i < tabViews.size();i++){
            if(index==i){
                if(ridDotSelected != -1){
                    tabViews.get(i).setImageResource(ridDotSelected);
                }else {
                    tabViews.get(i).setImageDrawable(getDotSelected());
                }
            }else {
                if(ridDotUnselected != -1) {
                    tabViews.get(i).setImageResource(ridDotUnselected);
                }else {
                    tabViews.get(i).setImageDrawable(getDotUnselected());
                }
            }
        }
    }

    private GradientDrawable getDotSelected(){
        if(gdDotSelected == null) {
            gdDotSelected = new GradientDrawable();
            return initGradientDrawable(true);
        }else {
            return gdDotSelected;
        }
    }

    private GradientDrawable getDotUnselected(){
        if(gdDotSelected == null) {
            gdDotUnselected = new GradientDrawable();
            return initGradientDrawable(false);
        }else {
            return gdDotUnselected;
        }
    }

    public void setDotSelectedResource(@DrawableRes int dotSelected){
        this.ridDotSelected = dotSelected;
    }

    public void setDotUnselectedResource(@DrawableRes int dotUnselected){
        this.ridDotUnselected = dotUnselected;
    }

    private GradientDrawable initGradientDrawable(boolean isSelected){
        GradientDrawable gd = isSelected ? gdDotSelected : gdDotUnselected;
        int strokeWidth = (int)pxFromDp(context,dotStrokeSizeDP);
        int roundRadius = (int)pxFromDp(context,dotSizeDP);
        int strokeColor = isSelected ? dotSelectedStrokeColor : dotUnselectedStrokeColor;
        int fillColor = isSelected ? dotSelectedColor : dotUnselectedColor;
        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, strokeColor);
        return gd;
    }

    private static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public void setDelayInMilliseconds(int delayInMilliseconds){
        this.delayInMilliseconds = delayInMilliseconds;
        initTimer();
    }

    public int getDelayInMilliseconds(){
        return delayInMilliseconds;
    }

    public void setDotShow(boolean isShowDot) {
        this.isShowDot = isShowDot;
        bottomLayout.setVisibility(isShowDot ? VISIBLE : GONE);
    }

    public boolean getDotShow() {
        return isShowDot;
    }

    public void setOppositeDirection(boolean isOppositeDirection) {
        this.isOppositeDirection = isOppositeDirection;
    }

    public boolean getOppositeDirection() {
        return isOppositeDirection;
    }
}
