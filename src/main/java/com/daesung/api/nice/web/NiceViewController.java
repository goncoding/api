package com.daesung.api.nice.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NiceViewController {

    @GetMapping("/nice/view")
    public String viewNice() {

        return "page/nice";
    }

}
