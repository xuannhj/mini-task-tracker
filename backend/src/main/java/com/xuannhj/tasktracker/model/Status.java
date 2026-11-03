package com.xuannhj.tasktracker.model;

public enum Status {
    TODO,
    IN_PROGRESS,
    DONE
}

/** public static final Status TODO = new Status();
 public static final Status IN_PROGRESS = new Status();
 public static final Status DONE = new Status();
 * that is behind the stage Java compiler will change it into static final (cac hang so tinh)
 * vì có từ static -> tên lớp.tên hằng số
 * ENUM CONSTANT
 */
