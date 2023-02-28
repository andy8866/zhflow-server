package com.andy.zhflow.proc.instance;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.response.ResultResponse;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping(value = "/api/proc/instance")
public class InstanceController {

    @Autowired
    private InstanceService instanceService;

    @Resource
    protected HistoryService historyService;

    @PostMapping(value="/startProc")
    public ResultResponse<Void> startProc(@RequestParam("procKey") String procKey,
                                             @RequestBody(required = false) Map<String,Object> vars) throws Exception {
        instanceService.startProc(procKey,vars);
        return ResultResponse.success();
    }

    @GetMapping(value="/getList")
    public ResultResponse<AmisPage<InstanceOutputVO>> getList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        AmisPage<InstanceOutputVO> appPage = instanceService.getList(page, perPage);
        return ResultResponse.success(appPage);
    }

    @GetMapping(value="/cancelProc")
    public ResultResponse<Void> cancelProc(@RequestParam("id") String id) {
        instanceService.cancelProc(id);
        return ResultResponse.success();
    }

    @GetMapping(value="/getProcViewer")
    public ResultResponse<ProcViewerVO> getProcViewer(@RequestParam(value = "procInsId",required = false) String procInsId,
                                                       @RequestParam(value = "taskId",required = false) String taskId
                                          ) {

        if(StringUtils.isEmpty(procInsId) && StringUtils.isNotEmpty(taskId)){
            procInsId=historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult().getProcessInstanceId();
        }

        ProcViewerVO procViewer = instanceService.getProcViewer(procInsId);
        return ResultResponse.success(procViewer);
    }


    @GetMapping(value="/historyProcNodeList")
    public ResultResponse<List<ProcNodeVO>> historyProcNodeList(@RequestParam("taskId") String taskId) {
        List<ProcNodeVO> list = instanceService.historyProcNodeListByTaskId(taskId);
        return ResultResponse.success(list);
    }

    @GetMapping(value="/getProcFlowRecord")
    public ResultResponse<List<ProcFlowRecordOutItemVO>> getProcFlowRecord(@RequestParam("taskId") String taskId) {
        List<ProcFlowRecordOutItemVO> list = instanceService.getProcFlowRecord(taskId);
        return ResultResponse.success(list);
    }

}
