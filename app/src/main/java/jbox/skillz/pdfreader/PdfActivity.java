package jbox.skillz.pdfreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class PdfActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {

    private PDFView pdf_file;
    private ArrayList<File> myDocs;
    private int position;
    private String docName;
    private String docNameRecent;

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        pdf_file = findViewById(R.id.pdfView);

        int x = 0;
        String s = null;
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        myDocs = (ArrayList) bundle.getParcelableArrayList("docs");
        docName = i.getStringExtra("docName");
        s = i.getStringExtra("status");
        position = bundle.getInt("pos");
        for (int y = 0; y < myDocs.size(); y++) {
            if (myDocs.get(y).getName().equals(docName)) {
                x = y;
            }
        }
        Uri uri = Uri.fromFile(myDocs.get(x));
        try
        {
            pdf_file.fromUri(uri)
                    .defaultPage(0)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .onPageChange(this)
                    .enableAnnotationRendering(true)
                    .onLoad(this)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .load();

        }catch (Exception e){
            Log.e(TAG, "onCreate: DocsViewer"+e.toString() );
        }
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

}
