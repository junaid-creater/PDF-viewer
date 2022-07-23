package jbox.skillz.pdfreader;

/**
 * Created by joni on 9/1/2019.
 */

public class DocsFile {
    private String name;
    private String extension;
    private String date;
    private String chk;

    public DocsFile(String name, String extension, String date, String chk) {
        this.name = name;
        this.extension = extension;
        this.date = date;
        this.chk = chk;
    }

    public String getName() {return name;}

    public String getExtension() {
        return extension;
    }

    public String getDate() {
        return date;
    }

    public String getChk() {
        return chk;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setChk(String chk) {
        this.chk = chk;
    }
}
