package com.andy.zhflow.dept;

import com.andy.zhflow.service.dept.IDeptService;
import com.andy.zhflow.user.User;
import com.andy.zhflow.vo.SelectOutVO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DeptService implements IDeptService {

    public List<DeptOutVO> getList(){
        return Arrays.asList(
                new DeptOutVO("d1","管理部"),
                new DeptOutVO("d2","用户组")
        );
    }

    public List<SelectOutVO> getListToSelect() {
        List<DeptOutVO> list=getList();

        List<SelectOutVO> outList=new ArrayList<>();

        for (DeptOutVO item:list){
            outList.add(new SelectOutVO(item.getName(),item.getId()));
        }

        return outList;
    }

    @Override
    public List<String> getUserDeptIds(String userId){
        return Arrays.asList("d1","d2");
    }

    @Override
    public String getDeptNameById(String deptId) {
        return deptId;
    }

    @Override
    public List<String> getUserIdsByDeptId(String deptId){
        return Arrays.asList(User.getByUserName("andy").getId(),User.getByUserName("hr").getId());
    }
}
