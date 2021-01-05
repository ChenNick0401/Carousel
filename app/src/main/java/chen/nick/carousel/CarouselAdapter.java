package chen.nick.carousel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.HorizontalVpViewHolder>  {

    private static final String TAG = "CarouselAdapter";
    private List<ItemData> mPages = new ArrayList<>();
    private Context mContext;
    private OnActionListener onActionListener;
    private final int infinite = 2;

    public interface OnActionListener {
        default void onClickItem(int item) { }
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    public CarouselAdapter(Context mContext, List<ItemData> mPages){
        this.mContext = mContext;
        this.mPages = mPages;
    }

    class HorizontalVpViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        LinearLayout layout;
        View view;

        HorizontalVpViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            layout = itemView.findViewById(R.id.layout);
            view = itemView;
        }
    }

    @NonNull
    @Override
    public HorizontalVpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HorizontalVpViewHolder(LayoutInflater.from(mContext).inflate((R.layout.carousel_item), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalVpViewHolder holder, int position) {
        if (!(position < 2 || position > (mPages.size() + 1))){
            position = position - 2;
            int finalPosition = position;
            holder.view.setOnClickListener(v -> onActionListener.onClickItem(finalPosition));
            String url = mPages.get(position).getUrl();
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .error(R.drawable.ic_action_warning);
            Glide.with(mContext)
                    .load(url)
                    .apply(options)
                    .fitCenter()
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return mPages.size() + (infinite * 2);
    }
}
