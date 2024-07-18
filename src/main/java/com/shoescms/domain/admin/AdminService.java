package com.shoescms.domain.admin;

import com.shoescms.domain.admin.dto.RoleResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    @Transactional(readOnly = true)
    public List<RoleResDto> getRoles() {
        List<RoleResDto> resDtoList = new ArrayList<>();
        return resDtoList;
    }

}
