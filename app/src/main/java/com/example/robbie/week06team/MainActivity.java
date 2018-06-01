package com.example.robbie.week06team;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import android.os.AsyncTask;

public class MainActivity extends AppCompatActivity {

    public List<String> list;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String fileName = "numbers.txt";

        list = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_list_item_1, list);

        listView = (ListView) findViewById(R.id.lvItems);
        listView.setAdapter(itemsAdapter);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(10);
        progressBar.setVisibility(View.GONE);



        Button createButton = (Button)findViewById(R.id.button_Create);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AsyncTaskCreate runner = new AsyncTaskCreate();
                runner.execute(fileName);
            }
        });

        Button loadButton = (Button)findViewById(R.id.button_Load);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AsyncTaskLoad loader =  new AsyncTaskLoad();
                loader.execute(fileName);
            }

        });

        Button clearButton = (Button)findViewById(R.id.button_Clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsAdapter.clear();
                Toast.makeText(MainActivity.this, "List cleared", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private class AsyncTaskCreate extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String content = "0";
                String newLine = "\n";
                FileOutputStream outputStream = null;
                String fileName = params[0].toString();

                outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                for (int i = 1; i < 11; i++) {
                    content = Integer.toString(i);
                    outputStream.write(content.getBytes());
                    outputStream.write(newLine.getBytes());

                    Thread.sleep(250);
                    publishProgress(i);

                }
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "File created successfully!";
        }

        @Override
        protected void onPreExecute() {
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    }

    private class AsyncTaskLoad extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            BufferedReader input = null;
            File file = null;

            try {
                file = new File(getFilesDir(), params[0]);

                input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line;
                int i = 0;
                while ((line = input.readLine()) != null) {

                    list.add(line);

                    // put the thread to sleep
                    try {
                        Thread.sleep(250);
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    i++;
                    publishProgress(i);

                }

            }catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... i) {
            progressBar.setProgress(i[0]);
            itemsAdapter.notifyDataSetChanged();
        }
        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
        }
    }
}
