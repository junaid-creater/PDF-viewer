package jbox.skillz.pdfreader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by joni on 9/1/2019.
 */

public class PdfGridAdapter extends ArrayAdapter<DocsFile> {

    private static final String TAG = "PersonListAdapter";
    private Context mContext;
    int mResource;
    ArrayList<File> myDocsPdf;

    public PdfGridAdapter(@NonNull MainActivity pdfDocument, int custom_layout, ArrayList<DocsFile> docsFiles, ArrayList<File> myDocs) {
        super(pdfDocument, custom_layout, docsFiles);
        mContext = pdfDocument;
        mResource = custom_layout;
        myDocsPdf = myDocs;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final String name=getItem(position).getName();
        String extension=getItem(position).getExtension();
        String date=getItem(position).getDate();
        String chk = getItem(position).getChk();

        DocsFile dFile=new DocsFile(name,extension,date, chk);

        LayoutInflater inflater= LayoutInflater.from(mContext);
        convertView=inflater.inflate(mResource,parent,false);

        TextView fileName=(TextView) convertView.findViewById(R.id.grid_file_name);
        TextView fileDate=(TextView) convertView.findViewById(R.id.grid_file_date);
        ImageView fileImage = convertView.findViewById(R.id.grid_file_img);
        final ImageView fileOptions = convertView.findViewById(R.id.grid_file_options);

        fileName.setText(name);
        fileDate.setText(date);
        if (extension.equals(".pdf")){
            fileImage.setBackgroundResource(R.drawable.pdf_img);
        }

        fileOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                File file = new File(myDocsPdf.get(position).getAbsolutePath());
                if (file.exists()){
                    shareIntent.setType("application/pdf");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+myDocsPdf.get(position).getAbsolutePath()));
                    mContext.startActivity(Intent.createChooser(shareIntent,"Share File"));
                }
            }
        });

        return convertView;
    }
}