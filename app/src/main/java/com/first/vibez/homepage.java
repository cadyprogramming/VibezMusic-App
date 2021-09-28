package com.first.vibez;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class homepage extends AppCompatActivity {
    ListView listView;
    String[]items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        listView=findViewById(R.id.listviewsong);

        runtimePermission();


    }

    public void runtimePermission()
    {
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                displaySongs();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();

            }
        }).check();

    }
    public ArrayList<File> findsong (File file)
    {
        ArrayList<File>arrayList=new ArrayList<>();
        File[] files=file.listFiles();

        for (File singlefile:files)
        {
            if (singlefile.isDirectory()&& !singlefile.isHidden())
            {
                arrayList.addAll(findsong(singlefile));

            }
            else
            {
                if (singlefile.getName().endsWith(".mp3")||singlefile.getName().endsWith(".wav"))
                {
                    arrayList.add(singlefile);
                }
            }

        }
        return arrayList;

    }

    void displaySongs()
    {
        final ArrayList<File> mysongs = findsong(Environment.getExternalStorageDirectory());

        items=new String[mysongs.size()];
        for (int i = 0;i<mysongs.size();i++)
        {
            items[i]=mysongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
        }

       /* ArrayAdapter<String> myAdapter =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        listView.setAdapter(myAdapter);*/

        customAdapter customAdapter=new customAdapter();
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String songName = (String) listView.getItemAtPosition(position);
                startActivity(new Intent(getApplicationContext(),player.class)
                        .putExtra("songs",mysongs)
                        .putExtra("songname",songName)
                        .putExtra("pos",position));
            }
        });


    }

    class customAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            View myView = getLayoutInflater().inflate(R.layout.list_item,null);
            TextView textsong =myView.findViewById(R.id.txtsongname);
            textsong.setSelected(true);
            textsong.setText(items[i]);

            return myView;
        }
    }


}