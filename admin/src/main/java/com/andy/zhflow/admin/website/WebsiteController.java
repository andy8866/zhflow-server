package com.andy.zhflow.admin.website;

import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.suggest.Suggest;
import com.andy.zhflow.utils.IpUtil;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequestMapping(value = "/api/admin/website")
public class WebsiteController {

    /**
     * 添加建议
     * @return
     */
    @PermitAll()
    @PostMapping(value="/submitSuggest")
    public ResultResponse<Void> submitSuggest(HttpServletRequest request,
                                              @RequestBody SubmitSuggestIVO submitSuggestIVO) {
        Suggest suggest=new Suggest();
        BeanUtils.copyProperties(submitSuggestIVO,suggest);
        suggest.setIp(IpUtil.getIpAddr(request));
        suggest.save();

        return ResultResponse.success();
    }

}
