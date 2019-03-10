package ch.zhaw.facerecognition.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import ch.zhaw.facerecognition.R;

public class ShowName extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_name);

        TextView shName = (TextView)findViewById(R.id.shNameid);
        TextView shRela = (TextView)findViewById(R.id.rsText);
        ImageView imgView = (ImageView)findViewById(R.id.imageView);

        Bundle abc = getIntent().getExtras();

        String toShowname = abc.getString("shName").toString();
        shName.setText("NAME: "+toShowname);

        /*Bitmap imgShow = abc.getParcelable("photo");
        imgView.setImageBitmap(imgShow);*/

        String toShowRela = abc.getString("shRela").toString();
        shRela.setText("Relationship: "+toShowRela);

    }
}
