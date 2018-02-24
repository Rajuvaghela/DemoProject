package com.desai.vatsal.mydynamiccalendar;

/**
 * Created by HCL on 02-10-2016.
 */
public class EventModel {

    private String strDate;
    private String strStartTime;
    private String strEndTime;
    private String strName;
    private int image = -1;
    private int image1 = 0;

   /* private int image = 0;
    private int image1 = 0;*/



    private String tmpDate, tmpStartTime, tmpEndTime, nameOfToDo, personId, peopleId, id, eventType, fontcolor,bgcolor;



    public EventModel(String strDate, String strStartTime, String strEndTime, String strName) {
        this.strDate = strDate;
        this.strStartTime = strStartTime;
        this.strEndTime = strEndTime;
        this.strName = strName;
    }

    public EventModel(String strDate, String strStartTime, String strEndTime, String strName, int image) {
        this.strDate = strDate;
        this.strStartTime = strStartTime;
        this.strEndTime = strEndTime;
        this.strName = strName;
        this.image = image;
    }

    public EventModel(String tmpDate, String tmpStartTime, String tmpEndTime, String nameOfToDo, int image ,int image1, String personId,String peopleId,String id,String eventType,String fontcolor,String bgcolor) {
        this.strDate = tmpDate;
        this.strStartTime = tmpStartTime;
        this.strEndTime = tmpEndTime;
        this.strName = nameOfToDo;
        this.image = image;
        this.image1 = image1;
        this.personId = personId;
        this.peopleId = peopleId;
        this.id = id;
        this.eventType = eventType;
        this.fontcolor = fontcolor;
        this.bgcolor = bgcolor;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getStrStartTime() {
        return strStartTime;
    }

    public void setStrStartTime(String strStartTime) {
        this.strStartTime = strStartTime;
    }

    public String getStrEndTime() {
        return strEndTime;
    }

    public void setStrEndTime(String strEndTime) {
        this.strEndTime = strEndTime;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage1() {
        return image1;
    }

    public void setImage1(int image1) {
        this.image1 = image1;
    }

    public String getTmpDate() {
        return tmpDate;
    }

    public void setTmpDate(String tmpDate) {
        this.tmpDate = tmpDate;
    }

    public String getTmpStartTime() {
        return tmpStartTime;
    }

    public void setTmpStartTime(String tmpStartTime) {
        this.tmpStartTime = tmpStartTime;
    }

    public String getTmpEndTime() {
        return tmpEndTime;
    }

    public void setTmpEndTime(String tmpEndTime) {
        this.tmpEndTime = tmpEndTime;
    }

    public String getNameOfToDo() {
        return nameOfToDo;
    }

    public void setNameOfToDo(String nameOfToDo) {
        this.nameOfToDo = nameOfToDo;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(String peopleId) {
        this.peopleId = peopleId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getFontcolor() {
        return fontcolor;
    }

    public void setFontcolor(String fontcolor) {
        this.fontcolor = fontcolor;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }
}
