package com.andy.zhflow.admin.htmlpage;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/htmlpage")
public class HtmlPageController {

    @GetMapping(value="/getPage")
    public void getPage(HttpServletResponse response,
                        @RequestParam("pageName") String pageName) {
    }
}
