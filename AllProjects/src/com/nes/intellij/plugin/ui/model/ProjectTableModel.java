package com.nes.intellij.plugin.ui.model;

/**
 * Created with IntelliJ IDEA.
 * User: sergenes
 * Date: 7/28/13
 * Time: 8:55 AM
 */
public class ProjectTableModel extends TableListModel {
    public ProjectTableModel(ColumnData[] columns) {
        super(columns);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount())
            return "";
        ProjectData row = (ProjectData) m_vector.elementAt(rowIndex);
        switch (columnIndex) {
            case 0:
                return row.name;
            case 1:
                return row.time;
            case 2:
                return row.timeClosed;
        }
        return "";
    }
}
