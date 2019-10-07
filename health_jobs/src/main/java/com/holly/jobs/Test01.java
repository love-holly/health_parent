package com.holly.jobs;

import com.holly.util.QiniuUtil;
import org.junit.Test;

public class Test01 {


    @Test
    public void show(){
        QiniuUtil.deleteFileFromQiniu("701cd6b1-3962-4de1-afcf-03599dd1c0191.jpg");
    }
}
