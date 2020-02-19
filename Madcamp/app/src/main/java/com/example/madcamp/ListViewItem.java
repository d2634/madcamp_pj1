package com.example.madcamp;
import android.graphics.drawable.Drawable;

public class ListViewItem {
    private Drawable iconDrawable ;
    private String titleStr ;
    private String Contents;
    private int contextInt ;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setCon(String contents) { Contents = contents ; }
    public void setContext(int context) {
        contextInt = context ;
    }

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getCon() {return this.Contents;}
    public int getContext() {
        return this.contextInt ;
    }


}
