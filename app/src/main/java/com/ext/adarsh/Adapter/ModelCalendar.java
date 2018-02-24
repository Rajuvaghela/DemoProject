package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-008 on 29-12-2017.
 */

public class ModelCalendar {

    String nameOfToDo;
    String DueDate;

    String bgcolor;
    String fontcolor;
    String personid;
    String PeopleId;
    String eventType;
    String id;
    String EndDate;
    String StartTime;
    String EndTime;
    String PersonName;
    String Address;
    String MatchId;
    String StartDate;

    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    public String getPeopleId() {
        return PeopleId;
    }

    public void setPeopleId(String peopleId) {
        PeopleId = peopleId;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getPersonName() {
        return PersonName;
    }

    public void setPersonName(String personName) {
        PersonName = personName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getMatchId() {
        return MatchId;
    }

    public void setMatchId(String matchId) {
        MatchId = matchId;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }


    public ModelCalendar(String nameOfToDo, String id, String bgcolor, String fontcolor, String eventType, String personid, String PeopleId) {
        this.nameOfToDo = nameOfToDo;
        this.id = id;
        this.bgcolor = bgcolor;
        this.fontcolor = fontcolor;
        this.eventType = eventType;
        this.personid = personid;
        this.PeopleId = PeopleId;

    }

    public String getNameOfToDo() {
        return nameOfToDo;
    }

    public String getPersonid() {
        return personid;
    }

    public void setPersonid(String personid) {
        this.personid = personid;
    }

    public void setNameOfToDo(String nameOfToDo) {
        this.nameOfToDo = nameOfToDo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public String getFontcolor() {
        return fontcolor;
    }

    public void setFontcolor(String fontcolor) {
        this.fontcolor = fontcolor;
    }
}
