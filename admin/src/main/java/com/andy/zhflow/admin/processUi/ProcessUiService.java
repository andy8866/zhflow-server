package com.andy.zhflow.admin.processUi;

import com.andy.zhflow.processUi.ProcessUi;
import org.springframework.stereotype.Component;

@Component
public class ProcessUiService {

    public String save(ProcessUiInputVO inputVO) throws Exception {
        return  ProcessUi.save(inputVO.getId(),inputVO.getContent());
    }
}
