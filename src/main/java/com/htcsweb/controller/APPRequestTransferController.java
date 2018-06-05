package com.htcsweb.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/APPRequestTransfer")
public class APPRequestTransferController {



    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

    //用于APP请求重定向
    @RequestMapping(value = "/getCoatingInfoByPipeNo",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getCoatingInfoByPipeNo(@RequestParam(value = "pipe_no",required = false)String pipe_no, HttpServletRequest request,HttpServletResponse response){


        //List<HashMap<String,Object>> list=coatingStripDao.getCoatingStripInfoByLike(pipe_no,operator_no,project_no,contract_no,beginTime,endTime,mill_no,start,Integer.parseInt(rows));
        //int count=coatingStripDao.getCountCoatingStripInfoByLike(pipe_no,operator_no,project_no,contract_no,beginTime,endTime,mill_no);

        Map<String,Object> maps=new HashMap<String,Object>();
        List<HashMap<String,Object>>list=pipeBasicInfoDao.getPipeInfoByNo(pipe_no);

        if(list.size()>0){
            //可跳转的url表
            HashMap<String,Object> urloptions=new HashMap<String,Object>();
            String status=(String)list.get(0).get("status");
            String external_coating=(String)list.get(0).get("external_coating");
            String rebevel_mark=(String)list.get(0).get("rebevel_mark");
            maps.put("success",true);
            maps.put("pipeinfo",list.get(0));
            maps.put("message","存在钢管"+pipe_no+"的信息");
            //根据status，计算可跳转的状态
            //外防
            if(status.equals("bare1")){
                urloptions.put("1","od/odblast");
                urloptions.put("2","storage/barepaipemovement");
                urloptions.put("3","storage/instoragetransfer");
                urloptions.put("4","storage/productionstockout");
            }
            else if(status.equals("od1")){
                urloptions.put("1","od/odblastinspection");
            }
            else if(status.equals("od2")){
                if(external_coating.equals("2FBE")) {
                    urloptions.put("1","od/odcoating2FBE" );
                }else if(external_coating.equals("3LPE")){
                    urloptions.put("1","od/odcoating3LPE");
                }
            }
            else if(status.equals("od3")){
                if(external_coating.equals("2FBE")) {
                    urloptions.put("1","od/odcoatinginspection2FBE");
                }else if(external_coating.equals("3LPE")){
                    urloptions.put("1","od/odcoatinginspection3LPE");
                }
            }
            else if(status.equals("od4")){
                urloptions.put("1","od/odstencil");
            }
            else if(status.equals("od5")){
                urloptions.put("1","od/odfinalinspection");
            }
            else if(status.equals("od6")){
                urloptions.put("1","storage/stockin");
            }
            else if(status.equals("odstockin")){
                urloptions.put("1","id/idblast");
                urloptions.put("2","storage/instoragetransfer");
                //需倒棱
                if(rebevel_mark!=null&&rebevel_mark.equals("1")){
                    urloptions.put("3","addition/piperebevel");
                }
                //不需要倒棱
                else if(rebevel_mark==null||!rebevel_mark.equals("1")) {
                    urloptions.put("3", "storage/productionstockout");
                }

            }
            //内防
            else if(status.equals("bare2")){
                urloptions.put("1","id/idblast");
                urloptions.put("2","storage/barepaipemovement");
                urloptions.put("3","storage/instoragetransfer");
                urloptions.put("4","storage/productionstockout");
            }
            else if(status.equals("id1")){
                urloptions.put("1","id/idblastinspection");
            }
            else if(status.equals("id2")){
                urloptions.put("1","id/idcoating");
            }
            else if(status.equals("id3")){
                urloptions.put("1","id/idcoatinginspection");
            }
            else if(status.equals("id4")){
                urloptions.put("1","id/idstencil");
            }
            else if(status.equals("id5")){
                urloptions.put("1","id/idfinalinspection");
            }
            else if(status.equals("id6")){
                urloptions.put("1","storage/stockin");
            }
            else if(status.equals("idstockin")){
                urloptions.put("1","storage/instoragetransfer");

                //需倒棱
                 if(rebevel_mark!=null&&rebevel_mark.equals("1")){
                    urloptions.put("2","addition/piperebevel");
                }
                //不需要倒棱
                else if(rebevel_mark==null||!rebevel_mark.equals("1")) {
                    urloptions.put("2", "storage/productionstockout");
                }
            }

            //修补
            else if(status.equals("odrepair1")||status.equals("odrepair2")||status.equals("idrepair1")||status.equals("idrepair2")){
                urloptions.put("1","addition/coatingrepair");
            }

            //扒皮
            else if(status.equals("odstrip1")||status.equals("idstrip1")){
                urloptions.put("1","addition/coatingstrip");
            }

            //修磨或切割
            else if(status.equals("onhold")){
                urloptions.put("1","addition/barepipegrinding");
            }

            maps.put("urloptions",urloptions);

        }else{
            maps.put("success",false);
            maps.put("message","不存钢管"+pipe_no+"的信息");
        }


        String mmp= JSONArray.toJSONString(maps);


//
//        try{
//            ResponseUtil.write(response, mmp);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//
        //System.out.print("mmp:"+mmp);
        return mmp;
    }











}
