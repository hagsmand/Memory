package ch.zhaw.facerecognition.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import ch.zhaw.facerecognition.R;
import ch.zhaw.facerecognitionlibrary.Helpers.FileHelper;

public class DeletePerson extends AppCompatActivity {


    FileHelper fiHelper;
    String realName[] = new String[300];
    int i=0;

    public String[] getRealName() {
        return realName;
    }

    public void setRealName(String k) {
        realName[i] = k;
        i++;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_person);

        ListView nameList = (ListView) findViewById(R.id.ListView1);
        final TextView inputName = (TextView) findViewById(R.id.inputNm);
        Button submit = (Button)findViewById(R.id.btt_del);

        fiHelper = new FileHelper();
        File folder = new File(fiHelper.getFolderPath());

        for(int i=0;i<=299;i++){
            realName[i]="";
        }

        File name[] = fiHelper.getTrainingList();
        if (name.length > 0) {
            for (File person : name) {
                String[] tokens = person.getAbsolutePath().split("\\W+");
                final String foldername = tokens[tokens.length - 1];
                //setRealName(foldername);
                realName[i++]=foldername;
                Log.i("11111111111",String.valueOf(name.length) );
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, getRealName());
        nameList.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String k = inputName.getText().toString();  // get name to delete
                File name[] = fiHelper.getTrainingList();
                if (name.length > 0) {
                    for (File person : name) {
                        String[] tokens = person.getAbsolutePath().split("\\W+");
                        final String foldername = tokens[tokens.length - 1];
                        if(foldername.equals(k)){
                            Log.i("555555555","deleteeeeeeeeee" );
                            deleteRecursive(person);
                            Toast.makeText(getApplicationContext(),"Deleted." ,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DeletePerson.this,MainFace.class);
                            startActivity(intent);
                        }
                        else{Toast.makeText(getApplicationContext(),"Can't find this person." ,Toast.LENGTH_SHORT).show();}
                    }
                }
            }
        });

    }

    public void deleteRecursive(File file){
        if(file.isDirectory()){
            for(File child:file.listFiles()){
                deleteRecursive(child);
            }
        }
        file.delete();
    }
}
