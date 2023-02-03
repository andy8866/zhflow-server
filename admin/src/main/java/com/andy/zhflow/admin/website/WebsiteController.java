package com.andy.zhflow.admin.website;

import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.suggest.Suggest;
import com.andy.zhflow.utils.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController()
@RequestMapping(value = "/api/admin/website")
public class WebsiteController {

    /**
     * 添加建议
     * @return
     */
    @PostMapping(value="/submitSuggest")
    public ResultResponse<Void> submitSuggest(HttpServletRequest request,
                                              @RequestBody SubmitSuggestInputVO submitSuggestInputVO) {
        Suggest suggest=new Suggest();
        BeanUtils.copyProperties(submitSuggestInputVO,suggest);
        suggest.setIp(IpUtil.getIpAddr(request));
        suggest.save();

        return ResultResponse.success();
    }

}
