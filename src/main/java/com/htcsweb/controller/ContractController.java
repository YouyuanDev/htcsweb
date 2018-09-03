package com.htcsweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.ContractInfoDao;
import com.htcsweb.entity.ContractInfo;
import com.htcsweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Controller
@RequestMapping("/ContractOperation")
public class ContractController {

    @Autowired
    private ContractInfoDao contractInfoDao;
    /**
     * 分页查询合同信息
     * @param project_no(项目编号)
     * @param project_name(项目名称)
     * @param contract_no(合同编号)
     * @param request
     * @return
     */
    @RequestMapping(value = "/getContractInfoByLike")
    @ResponseBody
    public String getContractInfoByLike(@RequestParam(value = "project_no",required = false)String project_no, @RequestParam(value = "project_name",required = false)String project_name,@RequestParam(value = "contract_no",required = false)String contract_no, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=contractInfoDao.getAllByLike(project_no,project_name,contract_no,start,Integer.parseInt(rows));
        int count=contractInfoDao.getCountAllByLike(project_no,project_name,contract_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
    /**
     * 增加或修改合同信息
     * @param contractInfo(合同信息)
     * @param response
     * @return
     */
    @RequestMapping(value = "/saveContract")
    @ResponseBody
    public String saveContract(ContractInfo contractInfo, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
            if(contractInfo.getId()==0){
                //添加
                resTotal=contractInfoDao.addContractInfo(contractInfo);
            }else{
                //修改！
                resTotal=contractInfoDao.updateContractInfo(contractInfo);
            }
            if(resTotal>0){
                json.put("success",true);
                json.put("message","保存成功");
            }else{
                json.put("success",false);
                json.put("message","保存失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());

        }finally {
            try {
                ResponseUtil.write(response, json);
            }catch  (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 删除合同信息
     * @param hlparam(合同信息id集合,","分割)
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/delContract")
    public String delContract(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=contractInfoDao.delContractInfo(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项合同信息删除成功\n");
        if(resTotal>0){
            //System.out.print("删除成功");
            json.put("success",true);
        }else{
            //System.out.print("删除失败");
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        ResponseUtil.write(response,json);
        return null;
    }
    /**
     * 根据合同编号查询合同信息
     * @param request
     * @return
     */
    @RequestMapping(value ="/getContractInfoByContractNo",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getContractInfoByContractNo(HttpServletRequest request){
        String contract_no=request.getParameter("contract_no");
        List<ContractInfo>list=contractInfoDao.getContractInfoByContractNo(contract_no);
        String map= JSONObject.toJSONString(list);
        return map;
    }

}
