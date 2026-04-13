package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.megdeal_earning.Models.Transaction;
import com.example.megdeal_earning.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> transactions;
    private SimpleDateFormat dateFormat;

    public TransactionAdapter(List<Transaction> transactions){
        this.transactions=transactions;
        this.dateFormat=new SimpleDateFormat("dd MM yyyy, hh:mm a", Locale.getDefault());
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.tvTitle.setText(transaction.getDescription());
        holder.tvDate.setText(dateFormat.format(new Date(transaction.getTimeStamp())));
        String amountText = (transaction.getType().equals("earning") ? "+" : "-") + "₹" + String.format("%.2f", transaction.getAmount());
        holder.tvAmount.setText(amountText);
        if (transaction.getType().equals("earning")){
            holder.tvAmount.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.green_500));
        } else {
            holder.tvAmount.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.red_500));
        }
    }

    public int getItemCount(){
        return transactions.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvDate, tvAmount;
        ViewHolder(@NonNull View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvAmount = itemView.findViewById(R.id.tv_amount);
        }
    }
}
