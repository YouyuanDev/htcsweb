package com.htcsweb.util;

import com.oreilly.servlet.multipart.FileRenamePolicy;

import java.io.File;

public class FileRenameUtil implements FileRenamePolicy{

    public File rename(File file) {
         String fileName=file.getName();
         String extName=fileName.substring(fileName.lastIndexOf('.'));
         fileName=System.currentTimeMillis()+extName;
         file=new File(file.getParent(),fileName);
         return file;
    }
}
