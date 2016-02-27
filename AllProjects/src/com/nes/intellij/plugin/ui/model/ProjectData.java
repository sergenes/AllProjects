package com.nes.intellij.plugin.ui.model;

/**
 * Created with IntelliJ IDEA.
 * User: sergenes
 * Date: 7/28/13
 * Time: 8:52 AM
 */
public class ProjectData {
    public String name;
    public String time;
    public String timeClosed;

    public ProjectData(String name, String time, String closed) {
        this.name = name;
        this.time = time;
        this.timeClosed  = closed;
    }
}
