package com.nes.intellij.plugin.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.components.panels.NonOpaquePanel;
import com.intellij.ui.table.JBTable;
import com.nes.intellij.plugin.ui.model.ColumnData;
import com.nes.intellij.plugin.ui.model.ProjectData;
import com.nes.intellij.plugin.ui.model.ProjectTableModel;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: sergenes
 * Date: 7/28/13
 * Time: 8:06 AM
 */
public class AllProjectsToolWindowFactory implements ToolWindowFactory {

    private ProjectTableModel table_model;
    private TableRowSorter<TableModel> sorter;

    private JBScrollPane sp;
    private JBTable table;
    private JBTextField searchField;
    private ComboBox comboBoxLabel;
    private JBLabel totalSelectedTime;

    private String[] comboBoxModel = {"By Name column", "By Class column", "By Time column"};

    private void makeTable() {
        ColumnData[] columns = {
                new ColumnData("Project", 170, ColumnData.NONE, false, false, true),
                new ColumnData("Class", 200, ColumnData.NONE, false, true, true),
                new ColumnData("Modified", 100, ColumnData.NONE, false, true, true)
        };

        table_model = new ProjectTableModel(columns);
        table = new JBTable();
        table.setModel(table_model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setShowGrid(true);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        sorter = new TableRowSorter<TableModel>(table.getModel());

        Comparator<String> comparator = new Comparator<String>() {
            public int compare(String s1, String s2) {
                String pattern = "dd-MM-yyyy HH:mm:ss";
                DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
                DateTime timeFirst = formatter.parseDateTime(s1.trim());
                DateTime timeLast = formatter.parseDateTime(s2.trim());
                if (timeFirst.isBefore(timeLast)) {
                    return -1;
                }
                if (timeFirst.isAfter(timeLast)) {
                    return 1;
                }
                return 0;
            }
        };

        sorter.setComparator(2, comparator);

        sp = new JBScrollPane(table, JBScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setAutoscrolls(true);

        table.setRowSorter(sorter);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                int first = e.getFirstIndex();
                int last = e.getLastIndex();

                if (first > -1 && last > -1 && first != last) {
                    String timeFirstStr = table.getValueAt(first, 2).toString();
                    String timeLastStr = table.getValueAt(last, 2).toString();

                    String pattern = "dd-MM-yyyy HH:mm:ss";

                    DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);

                    DateTime timeFirst = formatter.parseDateTime(timeFirstStr.trim());
                    DateTime timeLast = formatter.parseDateTime(timeLastStr.trim());

                    long delta = timeFirst.minus(timeLast.getMillis()).getMillis();

                    DateTime jodaTime = new DateTime(delta);

                    if (table.getSelectedRowCount() < 2) {
                        totalSelectedTime.setText("Not selected");
                    } else {
                        totalSelectedTime.setText(String.format("Estimated Time: %02d:%02d", jodaTime.getHourOfDay(), jodaTime.getMinuteOfHour()));
                    }
                } else {
                    totalSelectedTime.setText("Not selected");
                }
            }
        });


        loadDataFromSource(table_model, null);

        sp.setColumnHeaderView(table.getTableHeader());
        table.getRowSorter().toggleSortOrder(2);
        table.getRowSorter().toggleSortOrder(2);
    }

    private void buildFilterComboBox() {
        comboBoxLabel = new ComboBox();
        comboBoxLabel.setModel(new ComboBoxModel() {
            Object selectedItem = null;

            @Override
            public void setSelectedItem(Object anItem) {
                selectedItem = anItem;
            }

            @Override
            public Object getSelectedItem() {
                return selectedItem;
            }

            @Override
            public int getSize() {
                return comboBoxModel.length;
            }

            @Override
            public Object getElementAt(int index) {
                return comboBoxModel[index];
            }

            @Override
            public void addListDataListener(ListDataListener l) {

            }

            @Override
            public void removeListDataListener(ListDataListener l) {

            }
        });

        comboBoxLabel.setSelectedIndex(2);
    }

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        makeTable();

        totalSelectedTime = new JBLabel();
        totalSelectedTime.setText("Not selected");

        searchField = new JBTextField();
        searchField.setToolTipText("Select column in combo box, input text, and press enter");
        searchField.setColumns(50);

        buildFilterComboBox();

        //build UI
        NonOpaquePanel totalPanel = new NonOpaquePanel();

        totalPanel.add(Box.createHorizontalStrut(50), BorderLayout.WEST);
        totalPanel.add(totalSelectedTime, BorderLayout.CENTER);
        totalPanel.add(Box.createHorizontalStrut(50), BorderLayout.EAST);

        NonOpaquePanel rightPanel = new NonOpaquePanel();

        rightPanel.add(comboBoxLabel, BorderLayout.WEST);
        rightPanel.add(Box.createHorizontalStrut(50), BorderLayout.CENTER);
        rightPanel.add(totalPanel, BorderLayout.EAST);

        NonOpaquePanel filterPanel = new NonOpaquePanel();

        filterPanel.add(new JLabel("Filter: "), BorderLayout.WEST);
        filterPanel.add(searchField, BorderLayout.CENTER);
        filterPanel.add(rightPanel, BorderLayout.EAST);

        Component component = toolWindow.getComponent();

        component.getParent().add(filterPanel, BorderLayout.NORTH);
        component.getParent().add(sp, BorderLayout.CENTER);

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();

                    newFilter(searchField.getText(), comboBoxLabel.getSelectedIndex());

                    table.updateUI();
                } else {
                    super.keyPressed(e);
                }
            }
        });

    }

    /**
     * Loads rows from the log file and populate the table
     *
     * @param table_model model of table
     * @param ignore      string pattern to ignore loaded row
     */
    private void loadDataFromSource(ProjectTableModel table_model, String ignore) {
        table_model.deleteAll();
        String userHome = System.getProperty("user.home");
        try {
            FileInputStream input = new FileInputStream(userHome + "/idea.prj.txt");
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(input), 1024);
                String line;
                while ((line = rd.readLine()) != null) {
                    //todo add .ignore config file, and ignore it on write
                    if (line.startsWith("M") && !line.endsWith("R.java]")) {

                        String[] lineArray = line.split(",");

                        if (ignore != null && ignore.trim().length() > 0) {
                            if (comboBoxLabel.getSelectedIndex() == 0 && !lineArray[1].toLowerCase().contains(ignore.toLowerCase()))
                                continue;
                            if (comboBoxLabel.getSelectedIndex() == 1 && !lineArray[4].toLowerCase().contains(ignore.toLowerCase()))
                                continue;
                            if (comboBoxLabel.getSelectedIndex() == 2 && !lineArray[3].toLowerCase().contains(ignore.toLowerCase()))
                                continue;
                        }
                        table_model.insert(0, new ProjectData(lineArray[1], lineArray[4], lineArray[3]));
                    }
                }
                rd.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Applies the filter for table linked with the sorter
     *
     * @param filter the String that looking
     * @param idx    index of column
     */
    private void newFilter(final String filter, int idx) {
        RowFilter<TableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(filter, idx);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
}
