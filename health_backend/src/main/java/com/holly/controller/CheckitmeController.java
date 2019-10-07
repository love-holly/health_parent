package com.holly.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.holly.constant.MessageConstant;
import com.holly.entity.PageResult;
import com.holly.entity.QueryPageBean;
import com.holly.entity.Result;
import com.holly.pojo.CheckItem;
import com.holly.servcie.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController//它是conrroller和responseBody的合体
@RequestMapping("/checkitem")
public class CheckitmeController {

   //调到接口
    @Reference
    private CheckItemService checkItemService;

    /*新增检查项*/
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){

        System.out.println(checkItem);
        try {

            checkItemService.add(checkItem);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }

        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /*查询检查项*/

   @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
       String queryString = queryPageBean.getQueryString();

       PageResult pageResult=checkItemService.findPage(queryPageBean);

      return pageResult;
    }


    /*删除检查项*/
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")//增加注解权限
    @RequestMapping("/delete")
    public Result deleteById(Integer id){
        try {

            checkItemService.deleteById(id);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }

        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }



    /*查询检查项用于回显*/
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {

         CheckItem checkItem   =checkItemService.findById(id);

            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }


    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkItemService.edit(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }

        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

}
