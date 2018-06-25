package okhttp.custom.android.getcontacts;

import android.support.annotation.NonNull;

public class BaseDataSource {

    interface GetDataSourceCallback<T> {

        void onLoaded(@NonNull T data);

        void onDataNotAvailable();
    }

    interface SetDataSourceCallback<T> {

        void onUpdated(@NonNull T newData);

        void onError();

    }
}
