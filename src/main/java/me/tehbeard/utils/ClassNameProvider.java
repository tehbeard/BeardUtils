package me.tehbeard.utils;
import java.util.List;
import java.util.zip.ZipFile;


/**
 * Provides A list of class names to load from an addon
 * @author james
 *
 */
public abstract class ClassNameProvider{
    public abstract List<String> getClassList(ZipFile file);
}

