package com.nes.intellij.plugin.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.components.panels.NonOpaquePanel;
import com.intellij.ui.table.JBTable;
import com.nes.intellij.plugin.ui.model.ColumnData;
import com.nes.intellij.plugin.ui.model.ProjectData;
import com.nes.intellij.plugin.ui.model.ProjectTableModel;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: sergenes
 * Date: 7/28/13
 * Time: 8:06 AM
 */
public class AllProjectsToolWindowFactory implements ToolWindowFactory {

    private JBScrollPane sp;
    private JBTable table;
    private JBTextField searchField;
    private ComboBox comboBoxLabel;

    private String[] comboboxModel = {"By Name column", "By Class column", "By Time column"};

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {

        ColumnData[] columns = {
                new ColumnData("Project", 170, ColumnData.NONE, false, false, true),
                new ColumnData("Class", 200, ColumnData.NONE, false, true, true),
                new ColumnData("Modified", 100, ColumnData.NONE, false, true, true)
        };

        final ProjectTableModel table_model = new ProjectTableModel(columns);


        searchField = new JBTextField();
        searchField.setToolTipText("Select column in combo box, input text, and press enter");
        searchField.setColumns(100);

        comboBoxLabel = new ComboBox();
        comboBoxLabel.setModel(new ComboBoxModel() {
            Object selectedItem = null;

            @Override
            public void setSelectedItem(Object anItem) {
                System.out.println("setSelectedItem->" + anItem);
                selectedItem = anItem;
            }

            @Override
            public Object getSelectedItem() {
                return selectedItem;
            }

            @Override
            public int getSize() {
                return comboboxModel.length;
            }

            @Override
            public Object getElementAt(int index) {
                return comboboxModel[index];
            }

            @Override
            public void addListDataListener(ListDataListener l) {

            }

            @Override
            public void removeListDataListener(ListDataListener l) {

            }
        });

        comboBoxLabel.setSelectedIndex(2);

        table = new JBTable();

        sp = new JBScrollPane(table, JBScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setAutoscrolls(true);

        table.setModel(table_model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setShowGrid(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        loadDataFromSource(table_model, null);

        sp.setColumnHeaderView(table.getTableHeader());


        NonOpaquePanel rightPanel = new NonOpaquePanel();
        rightPanel.add(comboBoxLabel, BorderLayout.CENTER);
        rightPanel.add(Box.createHorizontalStrut(250), BorderLayout.EAST);

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

                    loadDataFromSource(table_model, searchField.getText());
                    table.updateUI();
                } else {
                    super.keyPressed(e);
                }
            }
        });
    }

    private void loadDataFromSource(ProjectTableModel table_model, String filter) {
        table_model.deleteAll();
        String userHome = System.getProperty("user.home");
        try {
            FileInputStream input = new FileInputStream(userHome + "/idea.prj.txt");
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(input), 1024);
                String line;
                while ((line = rd.readLine()) != null) {
//                    System.out.println("line->"+line);
                    //todo add .ignore config file
                    if (line.startsWith("M") && !line.endsWith("R.java]")) {

                        String[] lineArray = line.split(",");

                        if (filter != null) {
                            if (comboBoxLabel.getSelectedIndex() == 0 && !lineArray[1].toLowerCase().contains(filter.toLowerCase()))
                                continue;
                            if (comboBoxLabel.getSelectedIndex() == 1 && !lineArray[4].toLowerCase().contains(filter.toLowerCase()))
                                continue;
                            if (comboBoxLabel.getSelectedIndex() == 2 && !lineArray[3].toLowerCase().contains(filter.toLowerCase()))
                                continue;
                        }
                        table_model.add(new ProjectData(lineArray[1], lineArray[4], lineArray[3]));
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
}
