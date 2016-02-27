package com.nes.intellij.plugin;

import com.intellij.facet.Facet;
import com.intellij.facet.impl.DefaultFacetsProvider;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created with IntelliJ IDEA.
 * User: sergenes
 * Date: 7/28/13
 * Time: 7:58 AM
 */
public class AllProjects extends AnAction {

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        String pluginName = project.getName();
        String sourceRootsList = "";
        VirtualFile[] VFiles = ProjectRootManager.getInstance(project).getContentSourceRoots();
        for (int i = 0; i <= VFiles.length - 1; i++) {
            sourceRootsList += VFiles[i].getUrl() + "\n";

        }
        //System.out.println("sourceRootsList"+sourceRootsList);
        //   Messages.showInfoMessage("Source roots for the " + pluginName + " plugin:\n" + sourceRootsList, "Project Properties");


        Module[] allModules = ModuleManager.getInstance(project).getModules();
        String modulesList = "List of modules and module files in the " + project.getName() + " plugin:\n\n";
        for (int i = 0; i <= allModules.length - 1; i++) {
            modulesList += allModules[i].getName() + " " + allModules[i].getModuleFilePath() + "\n";
        }
        //System.out.println("modulesList"+modulesList);



        // Get a list of all modules the specified project includes
        String facetsList = "List of facets for modules in the " + project.getName() + " plugin:\n\n";
        for (int i = 0; i <= allModules.length - 1; i++) {
            // Get a list of all facets the module allModules[i] includes
            Facet[] facets = DefaultFacetsProvider.INSTANCE.getAllFacets(allModules[i]);
            facetsList += "Facets in the " + allModules[i].getName() + " module:\n";
            for (int j=0; j<= facets.length-1; j++){
                facetsList += facets[j].getName() + "\n";


            }


        }

        //System.out.println("facetsList"+facetsList);

    }
}
