package okhttp.custom.android.getcontacts;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_send_message_main_activity) Button mSendMessage;
    @BindView(R.id.btn_intent_sms_main_activity) Button mIntenSMS;
    @BindView(R.id.rlv_main_activity) RecyclerView mRecyclerView;

    private List<UploadContactBean> contactList = new ArrayList<>();
    private List<UploadContactBean> selectContactList = new ArrayList<>();
    private ContactRVAdapter contactRVAdapter;
    private final static String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    private final static String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getPermission();
    }

    public void getPermission() {
        PermissionUtils.permission(PermissionConstants.CONTACTS, PermissionConstants.STORAGE, PermissionConstants.SMS)
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

    @OnClick({R.id.btn_send_message_main_activity, R.id.btn_intent_sms_main_activity})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_message_main_activity:
                if (contactRVAdapter != null) {
                    selectContactList = contactRVAdapter.getSelectContacts();
                    sendGroupMessage("this is group message");
                }
                break;
            case R.id.btn_intent_sms_main_activity:
                if (contactRVAdapter != null) {
                    selectContactList = contactRVAdapter.getSelectContacts();
                    intentGroupSMSMessage("this is group message with Intent to SMS ");
                }
                break;
            default:
                break;
        }
    }

    public void sendGroupMessage(String value) {
        for (UploadContactBean uploadContactBean : selectContactList) {
            Intent deliverIntent = new Intent(SENT_SMS_ACTION);
            PendingIntent deliverPI = PendingIntent.getBroadcast(this, 0, deliverIntent, 0);
            SmsManager smsManager = SmsManager.getDefault();
            List<String> divideContents = smsManager.divideMessage(value);
            for (String text : divideContents) {
                smsManager.sendTextMessage(uploadContactBean.getPhoneNumber(), null, text, null, deliverPI);
            }
            ToastHelper.showShortMessage("Send group message success");
        }
    }

    public void intentGroupSMSMessage(String content) {
        String phoneNumber = "";
        for (UploadContactBean uploadContactBean : selectContactList) {
            phoneNumber = phoneNumber + "," + StringUtils.trim(uploadContactBean.getPhoneNumber());
        }
        sendSMSMessage(content, this, phoneNumber);
    }

    /**
     * 打电话
     *
     * @param tel 电话号码
     */
    private void callPhone1(String tel) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + tel));
        startActivity(intent);
    }

    /**
     * 打电话
     *
     * @param tel 电话号码
     */
    @SuppressLint("MissingPermission")
    private void callPhone2(String tel) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
        startActivity(intent);
    }

    /**
     * 发送短信
     *
     * @param tel     电话号码
     * @param content 短息内容
     */
    private void sendMessage1(String tel, String content) {
        Intent sendIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sendPI = PendingIntent.getBroadcast(this, 0, sendIntent, 0);

        SmsManager smsManager = SmsManager.getDefault();
        List<String> divideContents = smsManager.divideMessage(content);
        for (String text : divideContents) {
            smsManager.sendTextMessage(tel, null, text, sendPI, null);
        }
    }

    /**
     * 发送短信
     *
     * @param tel     电话号码
     * @param content 短息内容
     */
    private void sendMessage2(String tel, String content) {
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent deliverPI = PendingIntent.getBroadcast(this, 0, deliverIntent, 0);

        SmsManager smsManager = SmsManager.getDefault();
        List<String> divideContents = smsManager.divideMessage(content);
        for (String text : divideContents) {
            smsManager.sendTextMessage(tel, null, text, null, deliverPI);
        }
    }

    /**
     * 发送短信(掉起发短信页面)
     *
     * @param tel     电话号码
     * @param content 短息内容
     */
    private void sendMessage3(String tel, String content) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(tel)) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + tel));
            intent.putExtra("sms_body", content);
            startActivity(intent);
        }
    }

    public static void sendSMSMessage(String message, Context mContext, String number) {
        Uri smsToUri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.setData(Uri.parse("smsto:" + number));
        //to do  add Monkey Download url
        intent.putExtra("sms_body", message);
        if (mContext != null) {
            mContext.startActivity(intent);
        }
    }
}
