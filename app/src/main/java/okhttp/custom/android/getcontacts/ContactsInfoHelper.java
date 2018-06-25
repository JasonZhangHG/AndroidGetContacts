package okhttp.custom.android.getcontacts;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsInfoHelper {

    private static volatile ContactsInfoHelper contactsInfoHelper = null;

    public ContactsInfoHelper() {
    }

    public static ContactsInfoHelper getInstance() {
        if (contactsInfoHelper == null) {
            synchronized (ContactsInfoHelper.class) {
                if (contactsInfoHelper == null) {
                    contactsInfoHelper = new ContactsInfoHelper();
                }
            }
        }
        return contactsInfoHelper;
    }

    public void getContactList(final BaseDataSource.GetDataSourceCallback<List<ContactsInfo>> callback) {

        ActivityUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ContactAsyncQueryHandler asyncQueryHandler = new ContactAsyncQueryHandler(CCApplication.getInstance().getContentResolver(), callback);
                Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String[] projection = {
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                        ContactsContract.CommonDataKinds.Phone._ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.DATA1,
                        ContactsContract.CommonDataKinds.Phone.STARRED,
                        ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI,
                        "sort_key"};
                asyncQueryHandler.startQuery(0, null, uri, projection, null, null, "sort_key COLLATE LOCALIZED asc");
            }
        });
    }

    private static class ContactAsyncQueryHandler extends AsyncQueryHandler {

        private BaseDataSource.GetDataSourceCallback<List<ContactsInfo>> mCallback;

        public ContactAsyncQueryHandler(ContentResolver cr, BaseDataSource.GetDataSourceCallback<List<ContactsInfo>> callback) {
            super(cr);
            this.mCallback = callback;
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            Map<Integer, ContactsInfo> contactIdMap;
            List<ContactsInfo> contactsInfos = new ArrayList<ContactsInfo>();
            if (cursor != null && cursor.getCount() > 0) {
                contactIdMap = new HashMap<>();
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    int contactId = cursor.getInt(0);
                    int id = cursor.getInt(1);
                    String name = cursor.getString(2);
                    String number = cursor.getString(3);
                    int isStarted = cursor.getInt(4);
                    String photo = cursor.getString(5);

                    if (!contactIdMap.containsKey(contactId) && name != null && number != null) {
                        ContactsInfo contactsInfo = new ContactsInfo();
                        contactsInfo.setContactId(contactId);
                        contactsInfo.setName(name);
                        contactsInfo.setPhone(number);
                        contactsInfo.setPhoto(photo);
                        contactsInfo.setStared(isStarted == 1);
                        contactsInfos.add(contactsInfo);
                        contactIdMap.put(contactId, contactsInfo);
                    }
                }
                cursor.close();
            }
            mCallback.onLoaded(contactsInfos);
        }
    }
}
