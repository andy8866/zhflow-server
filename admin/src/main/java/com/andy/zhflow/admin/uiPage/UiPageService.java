package com.andy.zhflow.admin.uiPage;

import com.andy.zhflow.service.uiPage.IUiPageService;
import com.andy.zhflow.uiPage.UiPage;
import org.springframework.stereotype.Component;

@Component
public class UiPageService implements IUiPageService {
    @Override
    public String getContentByCode(String code) {
        return UiPage.getUiByCode(code);
    }
}
