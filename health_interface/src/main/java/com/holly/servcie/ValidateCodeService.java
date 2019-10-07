package com.holly.servcie;

import com.holly.entity.Result;

import java.util.Map;

public interface ValidateCodeService {


    Result submit(Map map) throws Exception;
}
