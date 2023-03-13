package com.andy.zhflow.role;

import com.andy.zhflow.service.role.IRoleService;
import com.andy.zhflow.user.User;
import com.andy.zhflow.vo.SelectOutVO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class RoleService implements IRoleService {

    public List<RoleOutVO> getList() {
        return Arrays.asList(
                new RoleOutVO("r1","管理员组"),
                new RoleOutVO("r2","用户组")
        );
    }

    public List<SelectOutVO> getListToSelect() {
        List<RoleOutVO> list=getList();

        List<SelectOutVO> outList=new ArrayList<>();

        for (RoleOutVO item:list){
            outList.add(new SelectOutVO(item.getName(),item.getId()));
        }

        return outList;
    }

    @Override
    public List<String> getUserRoleIds(String userId){
        return Arrays.asList("r1","r2");
    }

    @Override
    public String getRoleNameById(String roleId) {
        return roleId;
    }

    @Override
    public List<String> getUserIdsByRoleId(String roleId){
        return Arrays.asList(User.getByUserName("andy").getId(),User.getByUserName("andy领导").getId());
    }
}
