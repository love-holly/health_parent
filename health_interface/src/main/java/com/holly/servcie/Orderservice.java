package com.holly.servcie;

import java.util.Map;

public interface Orderservice {
    Map findById(Integer id) throws Exception;
}
