package com.team03.dtuevent.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.team03.dtuevent.objects.Type;
import com.team03.dtuevent.objects.data.Data;

import java.util.Date;

@Entity(tableName = "code_memento_table")
public class CodeMemento {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "data_type")
    private Type dataType;

    @ColumnInfo(name = "format")
    private int format;
//
//    @Embedded
//    private Contents contents;
    @ColumnInfo
    private Data data;

    @ColumnInfo(name = "date")
    private Date date;


    public CodeMemento(Type dataType, int format, Data data, Date date) {
        this.date = date;
        this.dataType = dataType;
        this.format = format;
        this.data = data;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Type getDataType() {
        return dataType;
    }

    public void setDataType(Type dataType) {
        this.dataType = dataType;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }


    public Date getDate() {
        return date;
    }


    public void setDate(Date date) {
        this.date = date;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getDisplayData() {
        return data.getStringRepresentation();
    }
}
