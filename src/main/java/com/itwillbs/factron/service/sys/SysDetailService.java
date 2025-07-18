package com.itwillbs.factron.service.sys;

import com.itwillbs.factron.dto.sys.RequestSysDetailDTO;
import com.itwillbs.factron.dto.sys.ResponseSysDetailDTO;
import com.itwillbs.factron.entity.DetailSysCode;
import jakarta.validation.Valid;

import java.util.List;

public interface SysDetailService {

    List<ResponseSysDetailDTO> getAllDetailByMainCode(String mainCode);

    Void saveSysDetail(@Valid RequestSysDetailDTO requestSysDetailDTO);

    Void updateSysDetail(@Valid RequestSysDetailDTO requestSysDetailDTO);

    String getDetailBySysCode(String sysCode);
}
