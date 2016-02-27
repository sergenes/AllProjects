package com.nes.intellij.plugin;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sergenes
 * Date: 7/28/13
 * Time: 8:04 AM
 */
public class AllProjectsAppComponent implements ApplicationComponent, BulkFileListener {
    //private final MessageBusConnection connection;

    public AllProjectsAppComponent() {
        //connection = ApplicationManager.getApplication().getMessageBus().connect();
    }

    public void initComponent() {
        System.out.println("initComponent");
        // connection.subscribe(VirtualFileManager.VFS_CHANGES, this);
    }

    public void disposeComponent() {
        System.out.println("disposeComponent");
        // connection.disconnect();
    }

    @NotNull
    public String getComponentName() {
        return "TestMyAppComponent";
    }

    @Override
    public void before(@NotNull List<? extends VFileEvent> vFileEvents) {
        for (VFileEvent fe : vFileEvents) {
            if(fe.getFile()!=null){
                System.out.println("before(getCanonicalPath)->" + fe.getFile().getCanonicalPath());
                System.out.println("before(getName)->" + fe.getFile().getName());
                System.out.println("before(getLength)->" + fe.getFile().getLength());
            }

        }
    }

    @Override
    public void after(@NotNull List<? extends VFileEvent> vFileEvents) {
        for (VFileEvent fe : vFileEvents) {
            System.out.println("after(getName)->" + fe.getFile().getName());
            System.out.println("after(getLength)->" + fe.getFile().getLength());
        }
    }
}
