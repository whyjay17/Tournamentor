package ykim164cs242.tournamentor;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;

import ykim164cs242.tournamentor.ListItem.MatchListItem;

import static org.junit.Assert.*;

/**
 * Unit Test for the Tournamentor app. Test includes testing the right data from the
 * Firebase real-time database.
 */
public class ExampleUnitTest {

    DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference tournamentReference = rootReference.child("Tournaments");
    DatabaseReference matchReference = tournamentReference.child("Test Tournament").child("Matches");

    @Test
    public void addition_isCorrect() throws Exception {

        DatabaseReference testReference = matchReference.child("Nov 13, 2017 Team Eagles vs Team Tigers");

        testReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    assertEquals(snapshot.child("fieldName").getValue().toString(), "UIUC Sixpack Field");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}