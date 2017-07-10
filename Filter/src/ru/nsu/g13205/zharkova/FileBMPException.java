package ru.nsu.g13205.zharkova;

/**
 * Created by Yana on 13.03.16.
 */
public class FileBMPException extends Throwable {

    String reason;

    public FileBMPException(String reason){
        super(reason);
        this.reason = reason;
    }
}
