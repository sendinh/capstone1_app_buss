package com.team03.dtuevent.helper;
public class StudentAttendDto{
    public String code;
    public String name;
    public String klass; // tieng duc la lop

    public StudentAttendDto() {
    }

    public StudentAttendDto(String code, String name, String klass) {
        this.code = code;
        this.name = name;
        this.klass = klass;
    }

    @Override
    public String toString() {
        return "StudentAttendDto{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", klass='" + klass + '\'' +
                '}';
    }
}