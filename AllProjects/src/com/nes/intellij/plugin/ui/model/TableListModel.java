package com.nes.intellij.plugin.ui.model;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: sergenes
 * Date: 7/28/13
 * Time: 8:40 AM
 */
public class TableListModel extends AbstractTableModel {
    protected ColumnData[] m_columns;
    protected Vector m_vector = new Vector(1);

    public TableListModel(ColumnData[] columns) {
        m_columns = columns;
    }

    public Object getRowAt(int idx){
        return m_vector.elementAt(idx);
    }

    public int getRowCount() {
        return m_vector == null ? 0 : m_vector.size();
    }

    public int getColumnCount() {
        return m_columns.length;
    }

    public void setAlignment(int col, int al) {
        m_columns[col].m_alignment = al;
    }

    public String getColumnName(int col) {
        return m_columns[col].m_title;
    }

    public void setColumnName(int col, String name) {
        m_columns[col].m_title = name;
    }

    public boolean isCellEditable(int nRow, int nCol) {
        return m_columns[nCol].isEditable;
    }

    @Override
    public Object getValueAt(int nRow, int nCol) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public void setColumnPrintable(int c, boolean print) {
        m_columns[c].isPrintable = print;
    }

    public boolean getColumnPrintable(int c) {
        return m_columns[c].isPrintable;
    }

    public void insert(int row, Object obj) {
        if (row < 0)
            row = 0;
        if (row > m_vector.size())
            row = m_vector.size();
        m_vector.add(row, obj); //);.insertElementAt(
    }

    public void add(Object obj) {
        m_vector.addElement(obj);
    }

    public void deleteAll() {
        m_vector.removeAllElements();
    }

    public boolean delete(int row) {
        if (row < 0 || row >= m_vector.size())
            return false;
        m_vector.remove(row);
        return true;
    }
}
