package okhttp.custom.android.getcontacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private List<UploadContactBean> contactList = new ArrayList<>();
    private ContactRVAdapter contactRVAdapter;
    @BindView(R.id.rlv_main_activity) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getPermission();
    }

    public void getPermission() {
        PermissionUtils.permission(PermissionConstants.CONTACTS,PermissionConstants.STORAGE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        shouldRequest.again(true);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        getContactsList();
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            PermissionUtils.launchAppDetailsSettings();
                        }
                    }
                }).request();
    }

    public void getContactsList() {
        ContactsInfoHelper.getInstance().getContactList(new BaseDataSource.GetDataSourceCallback<List<ContactsInfo>>() {
            @Override
            public void onLoaded(@NonNull List<ContactsInfo> data) {
                LogUtils.d("MainActivity getContactsList data = " + data);
                LogUtils.d("MainActivity getContactsList data.size() = " + data.size());
                for (ContactsInfo contactsInfo : data) {
                    if (contactsInfo != null && (!(TextUtils.isEmpty(contactsInfo.getName()))) && validPhoneNumber(contactsInfo.getPhone())) {
                        UploadContactBean uploadContactBean = new UploadContactBean(contactsInfo.getName(), contactsInfo.getPhone());
                        if (contactsInfo.getPhoto() == null) {
                            uploadContactBean.setAvatarUrl("");
                        } else {
                            uploadContactBean.setAvatarUrl(contactsInfo.getPhoto());
                        }
                        contactList.add(uploadContactBean);
                        initRecyclerView();
                    }
                }

                Collections.sort(contactList);
                LogUtils.d("MainActivity  getContactsList  000  uploadContactRequest.size()  = " + contactList.size());
                LogUtils.d("MainActivity  getContactsList  111  uploadContactRequest = " + contactList);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    public void initRecyclerView() {
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            contactRVAdapter = new ContactRVAdapter(contactList, this);
            mRecyclerView.setAdapter(contactRVAdapter);
        }
    }

    private boolean validPhoneNumber(String phoneNum) {
        return phoneNum.length() >= 4 && phoneNum.length() <= 17;
    }

}
