package okhttp.custom.android.getcontacts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactRVAdapter extends RecyclerView.Adapter<ContactRVAdapter.ContactRVAdapterHolder> {

    private List<UploadContactBean> contactList = new ArrayList<>();
    private Context mContext;

    public ContactRVAdapter(List<UploadContactBean> unlockADataLis, Context mContext) {
        this.contactList = unlockADataLis;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ContactRVAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unlock_plana_contact_list, parent, false);
        return new ContactRVAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactRVAdapterHolder holder, int position) {

        LogUtils.d("MainActivity ContactRVAdapter onBindViewHolder position = " + contactList.get(position));
        Glide.with(mContext)
                .load(contactList.get(position).getAvatarUrl())
                .placeholder(R.drawable.icon_normal)
                .fitCenter()
                .dontAnimate()
                .into((holder).mAvatar);

        holder.mName.setText(contactList.get(position).getName());
        holder.mPhoneNumber.setText(contactList.get(position).getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ContactRVAdapterHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cv_avatar_item_unlock_plana_contact_list) CircleImageView mAvatar;
        @BindView(R.id.tv_name_item_unlock_plana_contact_list) TextView mName;
        @BindView(R.id.tv_phone_number_item_unlock_plana_contact_list) TextView mPhoneNumber;
        @BindView(R.id.ll_item_unlock_plana_contact_list) LinearLayout mItem;

        public ContactRVAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
