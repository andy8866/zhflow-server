package com.scyingneng.zhflow.admin.website;

import com.scyingneng.zhflow.response.ResultResponse;
import com.scyingneng.zhflow.suggest.Suggest;
import com.scyingneng.zhflow.utils.IpUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


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
