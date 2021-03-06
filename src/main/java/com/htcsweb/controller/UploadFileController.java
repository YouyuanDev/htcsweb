package com.htcsweb.controller;


import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.ContractInfo;
import com.htcsweb.util.FileRenameUtil;
import com.htcsweb.util.PictureCompressorUtil;
import com.htcsweb.util.ResponseUtil;
import com.htcsweb.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.oreilly.servlet.multipart.FileRenamePolicy;
import com.oreilly.servlet.MultipartRequest;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.io.InputStream;
import java.io.FileInputStream;

import com.htcsweb.entity.PipeBasicInfo;
import com.htcsweb.dao.ContractInfoDao;


@Controller
@RequestMapping("/UploadFile")
public class UploadFileController {

    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

    @Autowired
    private ContractInfoDao contractInfoDao;

    public static boolean isServerTomcat = true;//是否服务器为tomcat 还是 本地debug服务器

    /**
     * 获取目录下所有文件
     *
     * @param realpath(目录路径)
     * @param files(文件集合)
     * @return
     */
    public static List<File> getFiles(String realpath, List<File> files) {

        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles();
            for (File file : subfiles) {
                if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath(), files);
                } else {
                    if (file.isFile() && (file.getName().endsWith(".jpg") || file.getName().endsWith(".JPG") || file.getName().endsWith(".png")))
                        files.add(file);
                }
            }
        }
        return files;
    }

    /**
     * 请求最新的10张照片的url(APP使用)
     *
     * @param request
     * @return
     */
    @RequestMapping("/getTopTenPictures")
    @ResponseBody
    public String getTopTenPictures(HttpServletRequest request) {
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        try {
            String basePath = request.getSession().getServletContext().getRealPath("/");
            if (basePath.lastIndexOf('/') == -1) {
                basePath = basePath.replace('\\', '/');
            }
            if (isServerTomcat) {//若果是tomcat需要重新定义upload的入口
                basePath = basePath.substring(0, basePath.lastIndexOf('/'));
                basePath = basePath.substring(0, basePath.lastIndexOf('/'));
            }
            File fileFolder = new File(basePath + "/upload/pictures/");
            if (fileFolder.exists() && fileFolder.isDirectory()) {
                List<File> flist = getFiles(basePath + "/upload/pictures/", new ArrayList<File>());
                Collections.sort(flist, new Comparator<File>() {
                    public int compare(File file, File newFile) {
                        if (file.lastModified() < newFile.lastModified()) {
                            return 1;
                        } else if (file.lastModified() == newFile.lastModified()) {
                            return 0;
                        } else {
                            return -1;
                        }
                    }
                });
                int i = 0;
                for (File f : flist) {
                    HashMap<String, Object> hm = new HashMap<String, Object>();
                    hm.put("pictureName", f.getName());
                    list.add(hm);
                    System.out.println(f.getName() + " : " + f.lastModified());
                    i++;
                    if (i == 10)
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            String map = JSONObject.toJSONString(list);
            return map;
        }
    }

    /**
     * 图片上传
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/uploadPicture")
    public String uploadPicture(HttpServletRequest request, HttpServletResponse response) {
        try {
            String saveDirectory = request.getSession().getServletContext().getRealPath("/");
            if (saveDirectory.lastIndexOf('/') == -1) {
                saveDirectory = saveDirectory.replace('\\', '/');
            }
            //照片放在web目录以外，需要tomcat conf/server.xml增加 <Context path="/upload" docBase="XXXXX/apache-tomcat-8.5.27/webapps/upload" reloadable="false"/>
            //conf/server.xml   <Context path="/upload" docBase="/Users/kurt/Documents/apache-tomcat-8.5.27/webapps/upload" reloadable="false"/>
            saveDirectory = saveDirectory.substring(0, saveDirectory.lastIndexOf('/'));
            if (isServerTomcat) {
                saveDirectory = saveDirectory.substring(0, saveDirectory.lastIndexOf('/'));
            }
            saveDirectory = saveDirectory + "/upload/pictures";
            File uploadPath = new File(saveDirectory);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }
            FileRenameUtil util = new FileRenameUtil();
            MultipartRequest multi = new MultipartRequest(request, saveDirectory, 100 * 1024 * 1024, "UTF-8", util);
            Enumeration files = multi.getFileNames();
            String newName = "";
            while (files.hasMoreElements()) {
                String name = (String) files.nextElement();
                File file = multi.getFile(name);
                if (file != null) {
                    newName = file.getName();
                    //压缩文件 0.5 rate
                    String srcimgfilename = saveDirectory + "/" + newName;
                    String distimgFilename = saveDirectory + "/" + newName;
                    PictureCompressorUtil.reduceImg(srcimgfilename, distimgFilename, 1000, 1000, 0.5f);
                }
            }
            System.out.println(newName + ":newName");
            JSONObject json = new JSONObject();
            json.put("imgUrl", newName);
            ResponseUtil.write(response, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除图片
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/delUploadPicture")
    @ResponseBody
    public String delUploadPicture(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            String imgList = request.getParameter("imgList");
            if (imgList != null && imgList != "") {
                String[] listArr = imgList.split(";");
                String saveDirectory = request.getSession().getServletContext().getRealPath("/");
                if (saveDirectory.lastIndexOf('/') == -1) {
                    saveDirectory = saveDirectory.replace('\\', '/');
                }
                if (isServerTomcat) {
                    saveDirectory = saveDirectory.substring(0, saveDirectory.lastIndexOf('/'));
                    saveDirectory = saveDirectory.substring(0, saveDirectory.lastIndexOf('/'));
                }
                saveDirectory = saveDirectory + "/upload/pictures";
                String imgPath = "";
                for (int i = 0; i < listArr.length; i++) {
                    imgPath = saveDirectory + "/" + listArr[i];
                    File file = new File(imgPath);
                    if (file.isFile() && file.exists()) {
                        file.delete();
                    }
                }
                json.put("success", true);
            } else {
                json.put("success", false);
            }
            ResponseUtil.write(response, json);
        } catch (Exception e) {
            json.put("success", false);
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/delUploadFile")
    @ResponseBody
    public String delUploadFile(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            String fileList = request.getParameter("fileList");
            if (fileList != null && fileList != "") {
                String[] listArr = fileList.split(";");
                String saveDirectory = request.getSession().getServletContext().getRealPath("/");
                if (saveDirectory.lastIndexOf('/') == -1) {
                    saveDirectory = saveDirectory.replace('\\', '/');
                }
                if (isServerTomcat) {
                    saveDirectory = saveDirectory.substring(0, saveDirectory.lastIndexOf('/'));
                    saveDirectory = saveDirectory.substring(0, saveDirectory.lastIndexOf('/'));
                }
                saveDirectory = saveDirectory + "/upload/files";
                String filePath = "";
                for (int i = 0; i < listArr.length; i++) {
                    filePath = saveDirectory + "/" + listArr[i];
                    File file = new File(filePath);
                    if (file.isFile() && file.exists()) {
                        file.delete();
                    }
                }
                json.put("success", true);
            } else {
                json.put("success", false);
            }
            ResponseUtil.write(response, json);
        } catch (Exception e) {
            json.put("success", false);
        }
        return null;
    }

    /**
     * 文件上传
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/uploadFile")
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            //String saveDirectory = request.getSession().getServletContext().getRealPath("/upload/files");
            String saveDirectory = request.getSession().getServletContext().getRealPath("/");
            if (saveDirectory.lastIndexOf('/') == -1) {
                saveDirectory = saveDirectory.replace('\\', '/');
            }
            if (isServerTomcat) {
                saveDirectory = saveDirectory.substring(0, saveDirectory.lastIndexOf('/'));
                saveDirectory = saveDirectory.substring(0, saveDirectory.lastIndexOf('/'));
            }
            saveDirectory = saveDirectory + "/upload/files";
            File uploadPath = new File(saveDirectory);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }
            MultipartRequest multi = new MultipartRequest(request, saveDirectory, 100 * 1024 * 1024, "UTF-8");
            Enumeration files = multi.getFileNames();
            String newName = "";
            while (files.hasMoreElements()) {
                String name = (String) files.nextElement();
                File file = multi.getFile(name);
                if (file != null) {
                    newName = file.getName();
                }
            }
            JSONObject json = new JSONObject();
            json.put("fileUrl", newName);
            ResponseUtil.write(response, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 钢管录入
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/uploadPipeList")
    public String uploadPipeList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String ck_overwrite = request.getParameter("ck_overwrite");
            String entrance = request.getParameter("entrance");
            System.out.println("ck_overwrite=" + ck_overwrite);
            System.out.println("entrance=" + entrance);

            boolean overwrite = false;
            boolean inODBareStorage = true;

            if (ck_overwrite != null && ck_overwrite.equals("1")) {
                overwrite = true;
            }
            if (entrance != null && entrance.equals("1")) {
                inODBareStorage = false;
            }
            //String saveDirectory = request.getSession().getServletContext().getRealPath("/upload/pipes");
            String saveDirectory = request.getSession().getServletContext().getRealPath("/");
            if (saveDirectory.lastIndexOf('/') == -1) {
                saveDirectory = saveDirectory.replace('\\', '/');
            }
            if (isServerTomcat) {
                saveDirectory = saveDirectory.substring(0, saveDirectory.lastIndexOf('/'));
                saveDirectory = saveDirectory.substring(0, saveDirectory.lastIndexOf('/'));
            }
            saveDirectory = saveDirectory + "/upload/pipes";
            File uploadPath = new File(saveDirectory);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }
            //FileRenameUtil util = new FileRenameUtil();
            MultipartRequest multi = new MultipartRequest(request, saveDirectory, 100 * 1024 * 1024, "UTF-8");
            Enumeration files = multi.getFileNames();
            String newName = "";
            File file = null;
            int TotalUploadedPipes = 0;
            int TotalSkippedPipes = 0;
            JSONObject json = new JSONObject();
            if (files.hasMoreElements()) {
                String name = (String) files.nextElement();
                file = multi.getFile(name);
                if (file != null) {
                    newName = file.getName();
                    //处理excel文件
                    HashMap retMap = importExcelInfo(saveDirectory + "/" + newName, overwrite, inODBareStorage);

                    TotalUploadedPipes = Integer.parseInt(retMap.get("uploaded").toString());
                    TotalSkippedPipes = Integer.parseInt(retMap.get("skipped").toString());
                    json.put("fileUrl", newName);
                    json.put("totaluploaded", TotalUploadedPipes);
                    json.put("totalskipped", TotalSkippedPipes);
                    json.put("successList",retMap.get("failList"));
                    json.put("failList",retMap.get("failList"));
                    json.put("updateList",retMap.get("updateList"));
                    json.put("skipList",retMap.get("skipList"));
                }
            }
            json.put("success", true);
            ResponseUtil.write(response, json);
            System.out.print("uploadPipeList成功");
            System.out.println("saveDirectory File=" + saveDirectory + "/" + newName);
            System.out.println("file.length()=" + file.length());

        } catch (Exception e) {
            System.err.println("Exception=" + e.getMessage().toString());
            e.printStackTrace();
            JSONObject json = new JSONObject();
            json.put("success", false);
            ResponseUtil.write(response, json);
        }
        return null;
    }
    /**
     * 钢管录入
     * @param fullfilename(Excel路径)
     * @param overwrite(判断是否已经存在该钢管信息的标识)
     * @param inODBareStorage(判断是否外光管还是内光管标识)
     * @return
     */
    public HashMap importExcelInfo(String fullfilename, boolean overwrite, boolean inODBareStorage) {
        HashMap retMap = new HashMap();//返回值
        List<String>insertList=new ArrayList<String>();//执行插入成功保存钢管编号的集合
        List<String>failList=new ArrayList<String>();//执行更新或插入失败保存钢管编号的集合
        List<String>updateList=new ArrayList<String>();//执行更新成功保存钢管编号的集合
        List<String>skipList=new ArrayList<String>();//执行跳过保存钢管编号的集合
        int TotalUploaded = 0;//成功插入数据库的钢管数量
        int TotalSkipped = 0; //无合同号存在跳过的钢管数量
        try {
            List<List<Object>> listob = ExcelUtil.readFromFiletoList(fullfilename);
            //遍历listob数据，把数据放到List中
            int listObSize=listob.size();
            for (int i = 0; i < listObSize; i++) {
                List<Object> ob = listob.get(i);
                PipeBasicInfo pipe = new PipeBasicInfo();
                //设置编号
                System.out.println(String.valueOf(ob.get(ExcelUtil.PIPE_NO_INDEX)));
                //通过遍历实现把每一列封装成一个model中，再把所有的model用List集合装载
                System.out.println("row:" + String.valueOf(i));
                System.out.println("listob.size():" + String.valueOf(listob.size()));
                if (!ExcelUtil.isNumeric00(String.valueOf(ob.get(ExcelUtil.OD_INDEX)))) {
                    //若od为空或不为数字，则跳过
                    continue;
                }
                if (!ExcelUtil.isNumeric00(String.valueOf(ob.get(ExcelUtil.WT_INDEX)))) {
                    //若wt为空或不为数字，则跳过
                    continue;
                }
                if (!ExcelUtil.isNumeric00(String.valueOf(ob.get(ExcelUtil.WEIGHT_INDEX)))) {
                    //若weight为空或不为数字，则跳过
                    continue;
                }
                if (!ExcelUtil.isNumeric00(String.valueOf(ob.get(ExcelUtil.P_LENGTH_INDEX)))) {
                    //若length为空或不为数字，则跳过
                    continue;
                }

                pipe.setId(0);
                pipe.setPipe_no(String.valueOf(ob.get(ExcelUtil.PIPE_NO_INDEX)));
                pipe.setContract_no(String.valueOf(ob.get(ExcelUtil.CONTRACT_NO_INDEX)));
                pipe.setOd(Float.parseFloat(ob.get(ExcelUtil.OD_INDEX).toString()));
                pipe.setWt(Float.parseFloat(ob.get(ExcelUtil.WT_INDEX).toString()));
                pipe.setHeat_no(String.valueOf(ob.get(ExcelUtil.HEAT_NO_INDEX)));
                pipe.setPipe_making_lot_no(String.valueOf(ob.get(ExcelUtil.PIPE_MAKING_LOT_NO_INDEX)));
                pipe.setWeight(Float.parseFloat(ob.get(ExcelUtil.WEIGHT_INDEX).toString()));
                pipe.setP_length(Float.parseFloat(ob.get(ExcelUtil.P_LENGTH_INDEX).toString()));
                pipe.setStatus("bare1");
                pipe.setLast_accepted_status("bare1");
                //批量插入
                int res = 0;
                //查找该contract是否存在
                List<ContractInfo> conlist = contractInfoDao.getContractInfoByContractNo(pipe.getContract_no());
                if (conlist.size() == 0) {
                    TotalSkipped = TotalSkipped + 1;
                    skipList.add(String.valueOf(ob.get(ExcelUtil.PIPE_NO_INDEX)));
                    continue;//不存在则此钢管不予以录入系统
                }
                //检查pipe的钢种信息是否为空,如果是从contract里得到钢种信息并赋值
                if (pipe.getGrade() == null || pipe.getGrade().equals("")) {
                    pipe.setGrade(((ContractInfo) conlist.get(0)).getGrade());
                }

                //查找该pipebasicinfo是否存在
                List<PipeBasicInfo> pipelist = pipeBasicInfoDao.getPipeNumber(pipe.getPipe_no());
                if (pipelist.size() == 0) {
                    //新钢管入库,如od库或id库
                    if (inODBareStorage) {
                        pipe.setStatus("bare1");
                    } else {
                        pipe.setStatus("bare2");
                    }
                    pipe.setLast_accepted_status(pipe.getStatus());
                    pipe.setRebevel_mark("0");
                    res = pipeBasicInfoDao.addPipeBasicInfo(pipe);
                    if(res>0)
                        insertList.add(pipe.getPipe_no());
                    else
                        failList.add(pipe.getPipe_no());
                    System.out.println("Insert res: " + res);
                } else {
                    if (overwrite) {
                        //更新数据库旧记录
                        PipeBasicInfo oldpipeinfo = pipelist.get(0);
                        pipe.setId(oldpipeinfo.getId());
                        pipe.setStatus(oldpipeinfo.getStatus());
                        pipe.setLast_accepted_status(oldpipeinfo.getLast_accepted_status());
                        res = pipeBasicInfoDao.updatePipeBasicInfo(pipe);
                        if(res>0)
                            updateList.add(pipe.getPipe_no());
                        else
                            failList.add(pipe.getPipe_no());
                        System.out.println("Update res: " + res);
                    }else{
                        //跳过该条记录，不更新
                        skipList.add(pipe.getPipe_no());
                    }
                }
                TotalUploaded = TotalUploaded + res;
            }
            System.out.println("Total pipes: " + TotalUploaded);
            retMap.put("success",true);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("success",false);
        } finally {
            retMap.put("uploaded", TotalUploaded);
            retMap.put("skipped", TotalSkipped);
            retMap.put("insertList",insertList);
            retMap.put("failList",failList);
            retMap.put("updateList",updateList);
            retMap.put("skipList",skipList);
            return retMap;
        }
    }
}

