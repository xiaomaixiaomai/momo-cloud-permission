/**
 * Copyright (c) 2018-2019, Jie Li 李杰 (mqgnsds@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.momo.mapper.res.authority;

import com.google.common.collect.Lists;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * @ProjectName: momo-cloud-permission
 * @Package: com.momo.mapper.res.authority
 * @Description: 比较两个list集合里的值 大小 ,并知道最大的那个值在哪个list里
 * @Author: Jie Li
 * @CreateDate: 2019/8/24 0024 15:05
 * @UpdateDate: 2019/8/24 0024 15:05
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"roleType"})
public class CheckTwoSetSizeRes {
    //set类型  1 当前登录用户所拥有的 角色类型
    //        2 为用户授权所拥有的 角色类型
    private Integer whichType;
    //需要比较的值
    private Integer roleType;

    /**
     * @param currentLoginRoleType 1 当前登录用户所拥有的 角色类型
     * @param authorRoleType  2 为用户授权所拥有的 角色类型
     * @return
     */
    public static boolean CheckTwoSetSize(Set<Integer> currentLoginRoleType, Set<Integer> authorRoleType) {
        if (CollectionUtils.isEmpty(currentLoginRoleType)) {
            return true;
        }
        if (CollectionUtils.isEmpty(authorRoleType)) {
            return false;
        }
        //角色的类型，0：管理员(老板)，1：管理员(员工)  2:普通员工 3:其他
        if (currentLoginRoleType.contains(0)) {
            return false;
        }
        List<CheckTwoSetSizeRes> checkTwoSetSizeRes = Lists.newArrayList();
        currentLoginRoleType.forEach(integer -> {
            CheckTwoSetSizeRes setSizeRes = new CheckTwoSetSizeRes();
            setSizeRes.setWhichType(1);
            setSizeRes.setRoleType(integer);
            checkTwoSetSizeRes.add(setSizeRes);
        });
        authorRoleType.forEach(integer -> {
            CheckTwoSetSizeRes setSizeRes = new CheckTwoSetSizeRes();
            setSizeRes.setWhichType(2);
            setSizeRes.setRoleType(integer);
            checkTwoSetSizeRes.add(setSizeRes);
        });
        checkTwoSetSizeRes.sort(deptSeqComparator);
        return !checkTwoSetSizeRes.get(0).getWhichType().equals(1);
    }

    //由小到大
    //优先保证 当前登录用户所拥有的 角色类型  在前面
    private static Comparator<CheckTwoSetSizeRes> deptSeqComparator = new Comparator<CheckTwoSetSizeRes>() {
        @Override
        public int compare(CheckTwoSetSizeRes o1, CheckTwoSetSizeRes o2) {
            Integer size = o1.getRoleType() - o2.getRoleType();
            if (size.equals(0)) {
                return o1.getWhichType() - o2.getWhichType();
            }
            return o1.getRoleType() - o2.getRoleType();
        }
    };
}
