package com.example.robbie.week06team;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<String> list;

    //private int progressInt = 0;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_list_item_1, list);

        listView = (ListView) findViewById(R.id.lvItems);
        listView.setAdapter(itemsAdapter);

        /** Part 1.03 **/
        // set an event handler to the create button that:
        // a. creates a new file in internal storage called numbers.txt
        // b. Write the numbers 1-10 to the file, one on each line
        // c. After each line, add a thread.sleep(250); to pause for a quarter
        //    of a second to simulate a more difficult task.
        Button createButton = (Button)findViewById(R.id.button_Create);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = "numbers.txt";
                String content = "0";
                String newLine = "\n";
                FileOutputStream outputStream = null;

                ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setProgress(0);
                progressBar.setMax(10);

                try {
                    outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                    for (int i = 1; i < 11; i++) {
                        content = Integer.toString(i);
                        outputStream.write(content.getBytes());
                        outputStream.write(newLine.getBytes());
                        progressBar.setProgress(i);
                        Thread.sleep(250);

                    }
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //display toast
                Toast.makeText(MainActivity.this, "finished saving", Toast.LENGTH_LONG).show();

            }
        });

        /** Part 1.04**/
        // Add an event handler to your Load button that does the following:
        // a. Load the file numbers.txt and read it line by line.
        // b. As you read each line, store each number in a list.
        // c. After reading each line, add a Thread.sleep(250); to pause
        //    for a quarter of a second to simulate a more difficult task.
        // D. Verify that the numbers are being read by displaying each number
        // to the standard output, or creating a toast or something similar,
        // as you read each number.
        Button loadButton = (Button)findViewById(R.id.button_Load);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BufferedReader input = null;
                File file = null;

                ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setProgress(0);
                progressBar.setMax(10);

                try {
                    file = new File(getFilesDir(), "numbers.txt");

                    input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    String line;
                    int i = 0;
                    while ((line = input.readLine()) != null) {

                        list.add(line);

                        //upate progress bar
                        progressBar.setProgress(i);

                        //display toast
                        Toast.makeText(MainActivity.this, list.get(i), Toast.LENGTH_LONG).show();

                        // put the thread to sleep
                        try {
                            Thread.sleep(250);
                        }catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        i++;
                        itemsAdapter.notifyDataSetChanged();
                    }

                }catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });

        Button clearButton = (Button)findViewById(R.id.button_Clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsAdapter.clear();
            }
        });
    }

}
