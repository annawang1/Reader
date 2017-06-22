package com.learning.annawang.reader;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnClick = (Button) this.findViewById(R.id.btn_click);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File file = Environment.getExternalStorageDirectory();
                    String path = file.getAbsolutePath() + "/Download/currency.txt";
                    InputStream inputStream = new FileInputStream(path);
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    String pathOut = file.getAbsolutePath() + "/currency_out.txt";
                    OutputStream outputStream = new FileOutputStream(pathOut, true);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

                    String line;
                    int i = 0;


                    Map<String, String> map = new HashMap<String, String>();
                    try {
                        // ISO code
                        String key = "";
                        // num
                        String value = "";
                        String comments;
                        StringBuilder stringBuilder = new StringBuilder();

                        while (null != (line = bufferedReader.readLine())) {
                            String regex = "[0-9]{3}";
                            Pattern pattern = Pattern.compile(regex);
                            Matcher matcher = pattern.matcher(line);
                            if (matcher.matches()) {
                                // is the value
                                value = matcher.group();
                                map.put("v", value);
                            } else {
                                comments = line.substring(0, line.length() - 3);
                                key = line.substring(line.length() - 3, line.length());
                                map.put("k", key);
                            }

                            i++;

                            if (i % 2 == 0) {
                                String generate = "gCurrencyMap.put(\"" + map.get("k") + "\", \"" + map.get("v") + "\");\r\n";
                                map.clear();

                                bufferedWriter.write(generate);
                                bufferedWriter.newLine();
                                bufferedWriter.flush();


                                Log.d("********", generate + "\r\n");
                            }
                        }
                        inputStream.close();
                        inputStreamReader.close();
                        bufferedReader.close();

                        bufferedWriter.close();
                        outputStreamWriter.close();
                        outputStream.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
