package ch.zhaw.facerecognition.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.locationlibary.MapsActivity;

import java.util.ArrayList;

public class FriendActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> idList;
    String useName;
    Button showBtt;
    EditText findLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.locationlibary.R.layout.activity_friend);

        showBtt = (Button)findViewById(com.example.locationlibary.R.id.button_addfriend);
        findLoc = (EditText)findViewById(com.example.locationlibary.R.id.text_addfriend);

        showBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ch.zhaw.facerecognition.Activities.MapsActivity.class);
                String k = findLoc.getText().toString().replace(".", "?");
                intent.putExtra("emailFriend",k);
               // Log.i("111111111111",k.replace(".","?"));
              //  Toast.makeText(getApplicationContext(), findLoc.getText().toString().replace(".","?"),Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}
