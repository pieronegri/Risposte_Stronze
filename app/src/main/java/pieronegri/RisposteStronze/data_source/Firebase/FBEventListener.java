package pieronegri.RisposteStronze.data_source.Firebase;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class FBEventListener/*<Model>*/ implements ValueEventListener {

    private FBRepositoryCallBack/*<Model>*/ firebaseCallback;

    FBEventListener(FBRepositoryCallBack/*<Model>*/ firebaseCallback) {
        this.firebaseCallback = firebaseCallback;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        firebaseCallback.onSuccess(dataSnapshot);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        firebaseCallback.onError(databaseError.toException());
    }
}
