package com.giovannisaberon.simplestories;

public class ImageData {

    String title;
    String imagefilename;
    String caption;
    BibleData bibleData;

    public ImageData(String title, String imagefilename, String caption){
        this.title = title;
        this.imagefilename = imagefilename;
        this.caption = caption;
    }

    public ImageData(String title, String imagefilename, String caption, BibleData bibleData){
        this.title = title;
        this.imagefilename = imagefilename;
        this.caption = caption;
        this.bibleData = bibleData;
    }
    public String getTitle(){
        return this.title;
    }

    public String getImagefilename(){
        return this.imagefilename;
    }

    public String getCaption(){
        return this.caption;
    }

    public BibleData getBibleData(){
        return this.bibleData;
    }


}
