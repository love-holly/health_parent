package com.holly.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.holly.dao.CheckGroupDao;
import com.holly.entity.PageResult;
import com.holly.entity.QueryPageBean;
import com.holly.pojo.CheckGroup;
import com.holly.pojo.CheckItem;
import com.holly.servcie.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    public List<CheckItem> findAll() {

       List<CheckItem> checkItems=checkGroupDao.findAll();
       // System.out.println(checkItems+"3434");
       return checkItems;
    }

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
      checkGroupDao.add(checkGroup);

        Integer checkgroup_id = checkGroup.getId();

        this.addById(checkgroup_id,checkitemIds);
      /*  if (checkitemIds!=null&&checkitemIds.length>0) {
            for (Integer checkitem_id : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroup_id",checkgroup_id);
                map.put("checkitem_id",checkitem_id);
                checkGroupDao.addById(map);

            }
        }*/
    }

    /**
     * 查询所有信息
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //获取当前页
        Integer currentPage = queryPageBean.getCurrentPage();
        //获取每页条数
        Integer pageSize = queryPageBean.getPageSize();
        //获取查询语句
        String queryString = queryPageBean.getQueryString();
        System.out.println("当前页"+currentPage);
        //将当前页和每页条数封装至Pagehelp插件中
      PageHelper.startPage(currentPage, pageSize);

        Page<CheckGroup> page = checkGroupDao.findPage(queryString);

        return new PageResult(page.getTotal(),page.getResult());
    }


    /**
     * 根据id查询已经选中的检查项id
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        List<Integer> Ids=checkGroupDao.findCheckItemIdsByCheckGroupId(id);
        return Ids;
    }

    /**
     * 回传编辑内容
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //修改检查组
        checkGroupDao.edit(checkGroup);
       //根据id删除关联表
        checkGroupDao.deleteById(checkGroup.getId());
        //获取id
        Integer checkgroup_id = checkGroup.getId();
              this.addById(checkgroup_id,checkitemIds);


    }

    /**
     * 根据id删除检查组
     * @param id
     */
    @Override
    public void deleteyId(Integer id) {
        checkGroupDao.deleteById(id);
        checkGroupDao.deleteByCheckgroup(id);
    }

    public void addById(Integer checkgroup_id, Integer[] checkitemIds){
        //添加关联表信息
        if(checkitemIds!=null&&checkitemIds.length>0){
            for (Integer checkitem_id : checkitemIds) {
                Map<String,Integer> maps=new HashMap<>();
                maps.put("checkgroup_id",checkgroup_id);
                maps.put("checkitem_id",checkitem_id);
                checkGroupDao.addById(maps);
            }

        }

    }
}
