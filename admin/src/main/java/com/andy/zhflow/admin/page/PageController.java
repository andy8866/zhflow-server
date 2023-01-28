package com.andy.zhflow.admin.page;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/page")
public class PageController {

    @Autowired
    private PageService pageService;

    @GetMapping(value="/getPage")
    public void getPage(HttpServletResponse response,
                        @RequestParam("pageName") String pageName) {
        pageService.getPage(response,pageName);
    }
}
