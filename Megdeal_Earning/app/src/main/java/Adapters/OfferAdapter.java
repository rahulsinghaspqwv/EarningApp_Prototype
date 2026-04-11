package Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.megdeal_earning.Models.Offer;
import com.example.megdeal_earning.R;

import java.text.DecimalFormat;
import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder>{
    private List<Offer> offerList;
    private OnOfferClickListener listener;
    private DecimalFormat df = new DecimalFormat("#0.00");
    public interface OnOfferClickListener{
        void onOfferClick(Offer offer);
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView ivIcon;
        TextView tvTitle, tvDescription, tvReward;
        ViewHolder(@NonNull View itemView){
            super(itemView);
            cardView = itemView.findViewById(R.id.card_offer);
            ivIcon = itemView.findViewById(R.id.iv_offer_icon);
            tvTitle = itemView.findViewById(R.id.tv_offer_title);
            tvDescription = itemView.findViewById(R.id.tv_offer_description);
            tvReward = itemView.findViewById(R.id.tv_offer_reward);
        }
    }
    public OfferAdapter(android.content.Context context, List<Offer> offerList, OnOfferClickListener listener){
        this.offerList=offerList;
        this.listener=listener;
    }

    @NonNull
    @Override
    public OfferAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offer, parent, false);
        return new ViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Offer offer = offerList.get(position);
        holder.tvTitle.setText(offer.getTitle());
        holder.tvDescription.setText(offer.getDescription());
        holder.tvReward.setText("rs " + df.format(offer.getReward()));
        // Load image using Glide (add dependency first)
        if (offer.getImageUrl() != null && !offer.getImageUrl().isEmpty()){
            Glide.with(holder.itemView.getContext()).load(offer.getImageUrl()).placeholder(R.drawable.ic_offer_placeholder).into(holder.ivIcon);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onOfferClick(offer);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return offerList != null ? offerList.size() : 0;
    }
}
