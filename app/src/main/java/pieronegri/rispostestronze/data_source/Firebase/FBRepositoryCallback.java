package pieronegri.rispostestronze.data_source.Firebase;

import com.google.firebase.database.DataSnapshot;

public interface FBRepositoryCallback/*<T>*/ {
    void onSuccess(DataSnapshot dataSnapshot);

    void onError(Exception e);
}
