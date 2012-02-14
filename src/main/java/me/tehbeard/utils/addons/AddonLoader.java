package me.tehbeard.utils.addons;

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
public abstract class AddonLoader<T> {

    /**
     * Directory addons are located under
     * NOTE: sub-directories are not searched. 
     */
    private File dir;
    private URLClassLoader loader;
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

                classList.addAll(getClassList(addon));

                loader = new URLClassLoader(urls,getClass().getClassLoader());

                for(String t :classList){
                    try {
                        Class<?> c = loader.loadClass(t);
                        if(c!=null && addonClass.isAssignableFrom(c) ){
                            @SuppressWarnings("unchecked")
                            Class<T> tc = (Class<T>) c;
                            makeClass(tc);
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
     * Makes a class of classType
     * @param classType
     */
    public abstract void makeClass(Class<? extends T> classType);

    public abstract List<String> getClassList(ZipFile file);

}