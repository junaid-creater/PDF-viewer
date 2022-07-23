package jbox.skillz.pdfreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;

public class MainActivity extends AppCompatActivity {

    private Button allBtn, recentBtn;
    private ImageButton gridStatus, viewStatus;
    private ListView pdfList, pdfListRecent;
    private GridView pdfGrid, pdfGridRecent;
    private ProgressBar pBar;
    FloatingActionButton fb1, fb2, fb3, fb4;

    private final SimpleDateFormat formator = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");

    private String items[];
    private String s = "x";
    private ArrayList<File> myDocs = new ArrayList<>();
    ArrayList<DocsFile> allPdfFiles = new ArrayList<>();
    ArrayList<DocsFile> recentPdfFiles = new ArrayList<>();

    private PdfListAdapter adapter;
    private PdfGridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        intialize();

        pBar.setVisibility(View.VISIBLE);
        loadData();
        new taskClass().execute();

    }

    @Override
    protected void onStart() {
        super.onStart();
        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s = "x";
                allBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                allBtn.setTextColor(getResources().getColor(R.color.white));

                recentBtn.setBackgroundColor(getResources().getColor(R.color.white));
                recentBtn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                allFiles();
            }
        });
        recentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s = "y";
                recentBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                recentBtn.setTextColor(getResources().getColor(R.color.white));

                allBtn.setBackgroundColor(getResources().getColor(R.color.white));
                allBtn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                recentFiles();
            }
        });

        gridStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s.equals("x")) {
                    pdfList.setVisibility(View.GONE);
                    pdfListRecent.setVisibility(View.GONE);
                    pdfGridRecent.setVisibility(View.GONE);

                    pdfGrid.setVisibility(View.VISIBLE);
                    viewStatus.setVisibility(View.VISIBLE);
                }else {
                    pdfList.setVisibility(View.GONE);
                    pdfListRecent.setVisibility(View.GONE);
                    pdfGrid.setVisibility(View.GONE);

                    pdfGridRecent.setVisibility(View.VISIBLE);
                    viewStatus.setVisibility(View.VISIBLE);
                }
            }
        });
        viewStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s.equals("x")) {
                    pdfGrid.setVisibility(View.GONE);
                    pdfListRecent.setVisibility(View.GONE);
                    pdfGridRecent.setVisibility(View.GONE);

                    pdfList.setVisibility(View.VISIBLE);
                    viewStatus.setVisibility(View.GONE);
                }else {
                    pdfList.setVisibility(View.GONE);
                    pdfGridRecent.setVisibility(View.GONE);
                    pdfGrid.setVisibility(View.GONE);

                    pdfListRecent.setVisibility(View.VISIBLE);
                    viewStatus.setVisibility(View.GONE);
                }
            }
        });
        pdfList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String docName = allPdfFiles.get(position).getName();
                if (recentPdfFiles.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "empity", Toast.LENGTH_SHORT).show();
                    recentPdfFiles.add(allPdfFiles.get(position));
                    saveData();
                }else {
                    int recentTest = 0;
                    for (int c = 0; c < recentPdfFiles.size(); c++)
                    {
                        if (recentPdfFiles.get(c).getName().equals(docName))
                        {
                            recentTest = 1;
                        }
                    }
                    if (recentTest == 0){
                        recentPdfFiles.add(allPdfFiles.get(position));
                        Toast.makeText(MainActivity.this, "Added uiuiu"+recentPdfFiles.size(), Toast.LENGTH_SHORT).show();
                        saveData();
                    }
                }
                Intent i = new Intent(getApplicationContext(), PdfActivity.class);
                i.putExtra("docs", myDocs);
                i.putExtra("pos",position);
                i.putExtra("docName",docName);
                i.putExtra("status",s);
                startActivity(i);
            }
        });
        pdfGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String docName = allPdfFiles.get(position).getName();
                if (recentPdfFiles.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "empity", Toast.LENGTH_SHORT).show();
                    recentPdfFiles.add(allPdfFiles.get(position));
                    saveData();
                }else {
                    int recentTest = 0;
                    for (int c = 0; c < recentPdfFiles.size(); c++)
                    {
                        if (recentPdfFiles.get(c).getName().equals(docName))
                        {
                            recentTest = 1;
                        }
                    }
                    if (recentTest == 0){
                        recentPdfFiles.add(allPdfFiles.get(position));
                        Toast.makeText(MainActivity.this, "Added uiuiu"+recentPdfFiles.size(), Toast.LENGTH_SHORT).show();
                        saveData();
                    }
                }
                Intent i = new Intent(getApplicationContext(), PdfActivity.class);
                i.putExtra("docs", myDocs);
                i.putExtra("pos",position);
                i.putExtra("docName",docName);
                i.putExtra("status",s);
                startActivity(i);
            }
        });
        pdfListRecent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String docName = recentPdfFiles.get(position).getName();
                Intent i = new Intent(getApplicationContext(), PdfActivity.class);
                i.putExtra("docs", myDocs);
                i.putExtra("pos",position);
                i.putExtra("docName",docName);
                i.putExtra("status",s);
                startActivity(i);
            }
        });
        pdfGridRecent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String docName = recentPdfFiles.get(position).getName();
                Intent i = new Intent(getApplicationContext(), PdfActivity.class);
                i.putExtra("docs", myDocs);
                i.putExtra("pos",position);
                i.putExtra("docName",docName);
                i.putExtra("status",s);
                startActivity(i);
            }
        });
        fb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(allPdfFiles, new Comparator<DocsFile>() {
                    @Override
                    public int compare(DocsFile o1, DocsFile o2) {
                        return o2.getDate().compareTo(o1.getDate());
                    }
                });
                Collections.sort(recentPdfFiles, new Comparator<DocsFile>() {
                    @Override
                    public int compare(DocsFile o1, DocsFile o2) {
                        return o2.getDate().compareTo(o1.getDate());
                    }
                });
                update();
            }
        });
        fb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(allPdfFiles, new Comparator<DocsFile>() {
                    @Override
                    public int compare(DocsFile o1, DocsFile o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                Collections.sort(recentPdfFiles, new Comparator<DocsFile>() {
                    @Override
                    public int compare(DocsFile o1, DocsFile o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                update();
            }
        });
        fb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(allPdfFiles, new Comparator<DocsFile>() {
                    @Override
                    public int compare(DocsFile o1, DocsFile o2) {
                        return o2.getName().compareTo(o1.getName());
                    }
                });
                Collections.sort(recentPdfFiles, new Comparator<DocsFile>() {
                    @Override
                    public int compare(DocsFile o1, DocsFile o2) {
                        return o2.getName().compareTo(o1.getName());
                    }
                });
                update();
            }
        });
        fb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(allPdfFiles, new Comparator<DocsFile>() {
                    @Override
                    public int compare(DocsFile o1, DocsFile o2) {
                        return o1.getDate().compareTo(o2.getDate());
                    }
                });
                Collections.sort(recentPdfFiles, new Comparator<DocsFile>() {
                    @Override
                    public int compare(DocsFile o1, DocsFile o2) {
                        return o1.getDate().compareTo(o2.getDate());
                    }
                });
                update();
            }
        });

    }

    private void update(){
        pdfList.setAdapter(null);
        pdfGrid.setAdapter(null);
        pdfGridRecent.setAdapter(null);
        pdfListRecent.setAdapter(null);
        if (s.equals("x")) {
            adapter = new PdfListAdapter(this, R.layout.list_item, allPdfFiles, myDocs);
            pdfList.setAdapter(adapter);

            gridAdapter = new PdfGridAdapter(this, R.layout.grid_item, allPdfFiles, myDocs);
            pdfGrid.setAdapter(gridAdapter);
        }
        else {
            adapter = new PdfListAdapter(this, R.layout.list_item, recentPdfFiles, myDocs);
            pdfListRecent.setAdapter(adapter);

            gridAdapter = new PdfGridAdapter(this, R.layout.grid_item, recentPdfFiles, myDocs);
            pdfGridRecent.setAdapter(gridAdapter);
        }
    }
    private void recentFiles() {
        pdfList.setAdapter(null);
        pdfGrid.setAdapter(null);
        pdfListRecent.setAdapter(null);
        pdfGridRecent.setAdapter(null);

        pdfList.setVisibility(View.GONE);
        pdfGridRecent.setVisibility(View.GONE);
        pdfGrid.setVisibility(View.GONE);

        pdfListRecent.setVisibility(View.VISIBLE);
        viewStatus.setVisibility(View.GONE);

        loadData();
        adapter=new PdfListAdapter(this,R.layout.list_item,recentPdfFiles, myDocs);
        pdfListRecent.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        gridAdapter = new PdfGridAdapter(this,R.layout.grid_item,recentPdfFiles, myDocs);
        pdfGridRecent.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();
    }

    private void allFiles() {
        pdfList.setAdapter(null);
        pdfGrid.setAdapter(null);
        pdfListRecent.setAdapter(null);
        pdfGridRecent.setAdapter(null);

        pdfGrid.setVisibility(View.GONE);
        pdfListRecent.setVisibility(View.GONE);
        pdfGridRecent.setVisibility(View.GONE);

        pdfList.setVisibility(View.VISIBLE);
        viewStatus.setVisibility(View.GONE);

        adapter=new PdfListAdapter(this,R.layout.list_item,allPdfFiles, myDocs);
        pdfList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        gridAdapter = new PdfGridAdapter(this,R.layout.grid_item,allPdfFiles, myDocs);
        pdfGrid.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();
    }

    private void intialize()
    {
        allBtn = findViewById(R.id.all_btn);
        recentBtn = findViewById(R.id.recent_btn);
        gridStatus = findViewById(R.id.grid_view_status);
        viewStatus = findViewById(R.id.view_status);
        pdfGrid = findViewById(R.id.grid_view);
        pdfList = findViewById(R.id.list_view);
        pdfGridRecent = findViewById(R.id.grid_view_recent);
        pdfListRecent = findViewById(R.id.list_view_recent);
        pBar = findViewById(R.id.p_bar);
        fb1 = findViewById(R.id.menu_item1);
        fb2 = findViewById(R.id.menu_item2);
        fb3 = findViewById(R.id.menu_item3);
        fb4 = findViewById(R.id.menu_item4);
    }

    private void saveData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preference",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(recentPdfFiles);
        editor.putString("task list", json);
        editor.apply();
    }
    private void loadData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preference",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<DocsFile>>() {}.getType();
        recentPdfFiles = gson.fromJson(json, type);
        if (recentPdfFiles == null)
        {
            recentPdfFiles = new ArrayList<>();
        }
    }

    void displayDocs(){
        ArrayList<DocsFile> allFiles=new ArrayList<>();
        String somePath;
//        final SimpleDateFormat formator = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
        // myDocs = findDocuments(Environment.getExternalStorageDirectory());
        Collections.sort(myDocs, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return String.valueOf(formator.format(o2.lastModified())).compareTo(String.valueOf(formator.format(o1.lastModified())));
            }
        });
        items = new String[myDocs.size()];
        for(int i = 0; i< myDocs.size() ; i++)
        {
            Date date = new Date(myDocs.get(i).lastModified());
            somePath = myDocs.get(i).getName().substring(myDocs.get(i).getName().lastIndexOf("."));

            DocsFile docsFile = new DocsFile(myDocs.get(i).getName(),somePath,formator.format(date),"no");
            allFiles.add(docsFile);
            allPdfFiles.add(docsFile);
            items[i] = myDocs.get(i).getName();
        }
        adapter=new PdfListAdapter(this,R.layout.list_item,allFiles, myDocs);
        pdfList.setAdapter(adapter);

        gridAdapter = new PdfGridAdapter(this,R.layout.grid_item,allFiles, myDocs);
        pdfGrid.setAdapter(gridAdapter);
    }
    public ArrayList<File> findDocuments(File file)
    {
        ArrayList<File> arrayList = new ArrayList<>();

        File[]   files = file.listFiles();
        for(File singleFile : files)
        {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                arrayList.addAll(findDocuments(singleFile));
            }
            else {
                if(singleFile.getName().endsWith(".pdf"))
                {
                    arrayList.add(singleFile);
                }
            }
        }
        return arrayList;
    }

    public class taskClass extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            myDocs = findDocuments(Environment.getExternalStorageDirectory());
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            displayDocs();
            pBar.setVisibility(View.GONE);
        }
    }
}
