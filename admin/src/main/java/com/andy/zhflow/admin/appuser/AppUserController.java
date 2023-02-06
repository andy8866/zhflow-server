package com.andy.zhflow.admin.appuser;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.app.App;
import com.andy.zhflow.appuser.AppUser;
import com.andy.zhflow.response.ResultResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(value = "/api/admin/appUser")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping(value="/save")
    public ResultResponse<String> save(@RequestBody() AppUserInputVO inputVO) throws Exception {
        AppUser appUser=new AppUser();
        if(StringUtils.isNoneEmpty(inputVO.getId())){
            appUser = AppUser.getById(inputVO.getId());
            if(appUser==null){
                throw new Exception("未找到ID数据");
            }
        }

        BeanUtils.copyProperties(inputVO,appUser);

        String id=AppUser.save(appUser);
        return ResultResponse.success(id);
    }

    @GetMapping(value="/getList")
    public ResultResponse<AmisPage<AppUser>> getList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        IPage<AppUser> appPage = AppUser.selectPage(page, perPage);

        return ResultResponse.success(AmisPage.transitionPage(appPage));
    }

    @GetMapping(value="/getSelectApp")
    public ResultResponse<App> getSelectApp() {
         App app= appUserService.getSelectApp();

        return ResultResponse.success(app);
    }

    @GetMapping(value="/setSelectApp")
    public ResultResponse<Void> setSelectApp(@RequestParam("appId") String appId) {
        appUserService.setSelectApp(appId);

        return ResultResponse.success();
    }
}
