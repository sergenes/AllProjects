package com.nes.intellij.plugin.ui.model;

/**
 * Created with IntelliJ IDEA.
 * User: sergenes
 * Date: 7/28/13
 * Time: 8:41 AM
 */
public class ColumnData {
    public static final int NONE = -1;
    public static final int CENTER = 0;
    public static final int RIGHT = 4;
    public static final int LEFT = 2;

    public String m_title;
    public int m_width;
    public int m_alignment;
    public boolean isEditable = false;
    public boolean isResizable = false;
    public boolean isPrintable = true;

    public ColumnData(String title, int width, int alignment, boolean iseditable, boolean isresizable) {
        m_title = title;
        m_width = width;
        m_alignment = alignment;
        isEditable = iseditable;
        isResizable = isresizable;
        isPrintable = true;
    }

    public ColumnData(String title, int width, int alignment, boolean iseditable, boolean isresizable, boolean isprintable) {
        m_title = title;
        m_width = width;
        m_alignment = alignment;
        isEditable = iseditable;
        isResizable = isresizable;
        isPrintable = isprintable;
    }
}
