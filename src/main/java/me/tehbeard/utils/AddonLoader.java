package me.tehbeard.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


/**
 * Represents a generic addon loader 
 * An addon loader must be for a specific type of class
 */
public class AddonLoader<T> {

    /**
     * Directory addons are located under
     * NOTE: sub-directories are not searched. 
     */
    private File dir;
    private URLClassLoader loader;
    /**
     * class name provider for this addon loader,
     * This is responsible for finding the name of classes to load,
     * and can made work in partnership with the boot strapper for
     * dependency resolution 
     */
    private ClassNameProvider classNameProvider;
    
    /**
     * class boot strapper, this will be responsible for loading the classes
     */
    private ClassBootStrapper<T> classBootStraper;
    private Class<T> addonClass;
    /**
     * 
     * @param dir directory addons are located in
     * @param addonClass main class/interface Addons are based off of 
     */
    public AddonLoader(File dir,Class<T> addonClass) {
        this.addonClass = addonClass;
        this.dir = dir;

        if (!dir.isDirectory()) {
            throw new IllegalStateException("dir must be a directory!");
        }
    }

    /**
     * Loads the addons
     */
    public void loadAddons() {
        // get list of files
        String[] flist = dir.list(new FilenameFilter() {

            public boolean accept(File file, String filename) {
                return !(file.isFile() && filename.endsWith(".jar"));
            }
        });
        List<String> classList = new ArrayList<String>();
        ZipFile addon;
        URL[] urls = new URL[flist.length];
        int i =0;

        for (String file : flist) {

            File addonFile = new File(dir, file);

            try {
                urls[i] = addonFile.toURI().toURL();
            } catch (MalformedURLException e) {
                System.out.println("Could not parse a path");
                e.printStackTrace();
                return;
            }
            i++;


            try {
                addon = new ZipFile(addonFile);
                if(classNameProvider != null){
                    classList.addAll(classNameProvider.getClassList(addon));
                }
                else
                {
                    throw new IllegalStateException("class name provider not started.");
                }
                loader = new URLClassLoader(urls,getClass().getClassLoader());

                for(String t :classList){
                    try {
                        Class<?> c = loader.loadClass(t);
                        if(c!=null && addonClass.isAssignableFrom(c) ){
                            @SuppressWarnings("unchecked")
                            Class<T> tc = (Class<T>) c;
                            classBootStraper.makeClass(tc);
                        }
                    } catch (ClassNotFoundException e) {
                        System.out.println("Could not find class! " + t);
                        e.printStackTrace();
                    }


                }

            } catch (ZipException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the boot strapper for this loader
     * @param boot boot strapper to use
     */
    public void setBootStrap(ClassBootStrapper<T> boot){
        classBootStraper = boot;
    }

    /**
     * Sets the class name provider for this addon loader
     * @param provider
     */
    public void setClassNameProvider(ClassNameProvider provider){
        classNameProvider = provider;
    }

}