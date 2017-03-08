package com.nes.intellij.plugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.util.messages.MessageBusConnection;
import org.apache.commons.lang.time.FastDateFormat;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sergenes
 * Date: 7/28/13
 * Time: 8:03 AM
 */
public class AllProjectsProjectComponent implements ProjectComponent, BulkFileListener {
    private final MessageBusConnection connection;

    Project _project;

    public AllProjectsProjectComponent(Project project) {
        _project = project;

        connection = ApplicationManager.getApplication().getMessageBus().connect();
    }

    public void initComponent() {
        System.out.println("TestMyProjectComponent.initComponent");
        connection.subscribe(VirtualFileManager.VFS_CHANGES, this);
//        Properties props = new Properties();
//
//        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("mapProviders.properties")) ;
//        for (Enumeration en = props.keys(); en.hasMoreElements();) {
//            String sendersPrefix = (String) en.nextElement() ;
//            String className = props.getProperty(sendersPrefix) ;
//            if (sendersPrefix.equals("default")) {
//                defaultProvider = (IMapProvider) Class.forName(className).newInstance() ;
//                _logger.debug("set default: " + className);
//            } else {
//                _providers.put(sendersPrefix, (IMapProvider) Class.forName(className).newInstance()) ;
//                _logger.debug("set map provider: " + sendersPrefix + "=" + className);
//            }
//        }

    }

    public void disposeComponent() {
        //System.out.println("TestMyProjectComponent.disposeComponent");
        connection.disconnect();
    }

    @NotNull
    public String getComponentName() {
        return "TestMyProjectComponent";
    }

    public void projectOpened() {
        //System.out.println("TestMyProjectComponent.projectOpened" + _project.getBaseDir());

        String pluginName = _project.getName();

        String userHome = System.getProperty("user.home");
        try {
            PrintWriter output = new PrintWriter(new FileWriter(userHome + "/idea.prj.txt", true));

            output.printf("O, %s, %d\r\n", pluginName, System.currentTimeMillis());

            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void projectClosed() {
        //System.out.println("TestMyProjectComponent.projectClosed");

        String pluginName = _project.getName();

        String userHome = System.getProperty("user.home");
        try {
            PrintWriter output = new PrintWriter(new FileWriter(userHome + "/idea.prj.txt", true));

            output.printf("C, %s, %d\r\n", pluginName, System.currentTimeMillis());

            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void before(@NotNull List<? extends VFileEvent> vFileEvents) {
//        for (VFileEvent fe : vFileEvents) {
//            if (fe.getFile() != null && fe.getFile().getCanonicalPath() != null && fe.getFile().getCanonicalPath().contains(_project.getName())) {
//                System.out.println("before(getCanonicalPath)->" + fe.getFile().getCanonicalPath());
//
//                System.out.println("before(getName)->" + fe.getFile().getName());
//                System.out.println("before(getLength)->" + fe.getFile().getLength());
//            }

//        }
    }

    @Override
    public void after(@NotNull List<? extends VFileEvent> vFileEvents) {
        for (VFileEvent fe : vFileEvents) {
            if (fe == null || fe.getFile() == null) continue;

            try {
                String file = fe.getFile().getCanonicalPath();
//                System.out.println("after(file)->" + file);
                if (fe.getFile().getCanonicalPath().contains(_project.getName())
                        && !file.endsWith("/BuildConfig.java")
                        && !file.endsWith("/R.java")
                        && ((file.endsWith("AndroidManifest.xml")
                        || (file.endsWith(".xml") && file.contains("res/layout"))
                        || file.endsWith(".gradle")
                        || file.endsWith(".java")
                        || file.endsWith(".py")
                        || file.endsWith(".kt")
                        || file.endsWith(".js")
                        || file.endsWith(".html")
                        || file.endsWith(".mm")
                        || file.endsWith(".c")
                        || file.endsWith(".cpp")
                        || file.endsWith(".swift")
                        || file.endsWith(".storyboard")
                        || file.endsWith(".m")
                        || file.endsWith(".h")))) {

//                    System.out.println("after(getName)->" + fe.getFile().getName());
//                    System.out.println("after(getLength)->" + fe.getFile().getLength());
                    String userHome = System.getProperty("user.home");
                    try {
                        PrintWriter output = new PrintWriter(new FileWriter(userHome + "/idea.prj.txt", true));
                        if (fe.getFile() != null) {
                            String date = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss").format(fe.getFile().getTimeStamp());
                            output.printf("M, %s, %d, %s, %s, [%s]\r\n", _project.getName(), fe.getFile().getTimeStamp(), date, fe.getFile().getName(), fe.getFile().getCanonicalPath());
                        }
                        output.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

}