package pieronegri.RisposteStronze.data_source.Firebase;

import com.google.firebase.database.DataSnapshot;

public interface FBRepositoryCallBack/*<T>*/ {
    void onSuccess(DataSnapshot dataSnapshot);

    void onError(Exception e);
}
