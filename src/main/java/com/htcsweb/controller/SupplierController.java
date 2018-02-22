package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.htcsweb.dao.SupplierDao;
import com.htcsweb.entity.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("upload")
public class SupplierController {

    @Autowired
    private SupplierDao supplierDao;

    @RequestMapping("addPicture")
    public String addPicture(HttpServletRequest request, MultipartFile file){

        return "od/odcoatingprocess";
    }
    public boolean saveFile(HttpServletRequest request,MultipartFile file){
        //判断文件是否为空
        if(!file.isEmpty()){
            try{
                String filePath=request.getSession().getServletContext().getRealPath("/")+"upload/pictures/"+file.getOriginalFilename();
                System.out.println(filePath);
                File saveDir=new File(filePath);
                if(!saveDir.getParentFile().exists()){
                    saveDir.getParentFile().mkdirs();
                }

            }catch (Exception e){

            }
        }
        return true;
    }
    @ResponseBody
    @RequestMapping("getSupplier")
    public  Map<String,Object> getSupplier(){
        List<Supplier> supplier= supplierDao.getSupplier();
        Map<String,Object>  map= new HashMap<String, Object>();
        map.put("rows",supplier);
        return  map;
    }



    @RequestMapping("editSupplier")
    public  String editSupplier(Supplier supplier){
        System.out.println(supplier);
        supplierDao.updateSupplier(supplier);
        return "od/odcoatingprocess";
    }

    @RequestMapping(value = "selfy")
    @ResponseBody
    public String selfy(HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<Supplier> stus= supplierDao.feny(start,Integer.parseInt(rows));
        int count=supplierDao.getcount();
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",stus);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }


    @RequestMapping("getSupplierById")
    public  String getSupplierById(int supplier_id,HttpServletRequest request){
        Supplier supplier= supplierDao.getSupplierById(supplier_id);
        request.getSession().setAttribute("sa",supplier);
        System.out.println(supplier);
        return  "supplieredit";
    }


}
