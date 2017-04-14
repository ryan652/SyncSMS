package svyp.syncsms.archived;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import svyp.syncsms.Constants;
import svyp.syncsms.R;
import svyp.syncsms.chat.ChatActivity;
import svyp.syncsms.models.Message;

class ArchivedAdapter extends RecyclerView.Adapter<ArchivedAdapter.ViewHolder> {

    private List<Message> mDataset;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvName, tvMessage, tvDate, tvUnreadCount;
        ImageView ivUserPicture;
        ViewHolder(CardView v) {
            super(v);
            cardView = v;
            ivUserPicture = (ImageView) v.findViewById(R.id.iv_user_picture);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvMessage = (TextView) v.findViewById(R.id.tv_message);
            tvDate = (TextView) v.findViewById(R.id.tv_date);
            tvUnreadCount = (TextView) v.findViewById(R.id.tv_unread_count);
        }
    }

    ArchivedAdapter(List<Message> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public ArchivedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_messages, parent, false);
        return new ArchivedAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ArchivedAdapter.ViewHolder holder, int position) {

        final Message message = mDataset.get(position);
        final int pos = position;

        holder.ivUserPicture.setImageResource(R.mipmap.ic_launcher);
        final StringBuilder names = new StringBuilder();
        for (int i = 0; i < message.contacts.size(); i++) {
            names.append(message.contacts.get(i).name);
            if (i < message.contacts.size() - 1) names.append(", ");
        }
        holder.tvName.setText(names.toString());
        holder.tvMessage.setText(message.message);
        holder.tvDate.setText(message.date);

        if (message.unreadCount > 0) {
            holder.tvUnreadCount.setText(String.valueOf(message.unreadCount));
        } else {holder.tvUnreadCount.setVisibility(View.GONE);}

        if (!message.read) {
            holder.tvName.setTypeface(Typeface.DEFAULT_BOLD);
            holder.tvMessage.setTypeface(Typeface.DEFAULT_BOLD);
            holder.tvDate.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            holder.tvName.setTypeface(Typeface.DEFAULT);
            holder.tvMessage.setTypeface(Typeface.DEFAULT);
            holder.tvDate.setTypeface(Typeface.DEFAULT);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!message.read) {
                    message.read = true;
                    message.unreadCount = 0;
                    mDataset.set(pos, message);
                    notifyItemChanged(pos);
                    notifyItemRangeChanged(pos, getItemCount());
                }
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra(Constants.KEY_TITLE, names.toString());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset != null ? mDataset.size() : 0;
    }

    Message removeMessage(int position) {
        Message removed = mDataset.get(position);
        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        return removed;
    }

    void addMessage(Message message) {
        mDataset.add(message);
        notifyItemInserted(mDataset.indexOf(message));
        notifyItemRangeChanged(mDataset.indexOf(message), getItemCount());
    }
}
