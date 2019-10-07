package com.holly.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.holly.constant.MessageConstant;
import com.holly.entity.PageResult;
import com.holly.entity.QueryPageBean;
import com.holly.entity.Result;
import com.holly.pojo.CheckGroup;
import com.holly.pojo.CheckItem;
import com.holly.servcie.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.util.List;

@RequestMapping("/checkgroup")
@RestController
public class CheckGroupController {

    @Reference
    private CheckGroupService service;


    @RequestMapping("/findAll")
    public Result findAll(){
            List<CheckItem> checkItems=service.findAll();
            //判断集合是否为空;
        if (checkItems !=null &&checkItems.size()>0){
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItems);

        }
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }



        @RequestMapping("/add")
        public Result add(@RequestBody CheckGroup checkGroup ,Integer[] checkitemIds){

            try {

                service.add(checkGroup,checkitemIds);

            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
            }
            return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);

        }

    /**
     * 查询所有
     * @param queryPageBean
     * @return
     */
          @RequestMapping("/findPage")
        public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

              PageResult page=service.findPage(queryPageBean);


              return page;
        }

    /**
     * 编辑框中根据Id查询
     * @param id
     * @return
     */
    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id){


        try {
            List<Integer>  Ids = service.findCheckItemIdsByCheckGroupId(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,Ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
            }

    /**
     * 回传修改内容
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup ,Integer[] checkitemIds){
   /*     for (Integer checkitemId : checkitemIds) {
            System.out.println("检查项id: "+checkitemId);
        }*/
       /* Integer id = checkGroup.getId();
        System.out.println("检查组id"+id);*/
        try {
            service.edit(checkGroup,checkitemIds);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
             return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    /**
     * 根据id删除所选项
     * @param id
     * @return
     */
    @RequestMapping("/deleteyId")
    public Result deleteyId(Integer id){

        try {
            service.deleteyId(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

  }




