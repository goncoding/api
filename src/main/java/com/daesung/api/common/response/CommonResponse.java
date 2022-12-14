package com.daesung.api.common.response;

import java.util.List;

public class CommonResponse<T> extends BasicResponse{

    private int count;
    private T data;

    public CommonResponse(T data) {
        this.data = data;
        if(data instanceof List) {
            this.count = ((List<?>)data).size();
        } else {
            this.count = 1;
        }
    }

}
