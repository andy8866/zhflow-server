package com.scyingneng.zhflow.admin.uiPage;

import com.scyingneng.zhflow.service.uiPage.IUiPageService;
import com.scyingneng.zhflow.uiPage.UiPage;
import org.springframework.stereotype.Component;

@Component
public class UiPageService implements IUiPageService {
    @Override
    public String getContentByCode(String code) {
        return UiPage.getUiByCode(code);
    }
}
