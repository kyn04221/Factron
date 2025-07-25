package com.itwillbs.factron.controller.outbound;

import com.itwillbs.factron.dto.ResponseDTO;
import com.itwillbs.factron.dto.outbound.RequestOutboundCompleteDTO;
import com.itwillbs.factron.dto.outbound.RequestSearchOutboundDTO;
import com.itwillbs.factron.dto.outbound.ResponseSearchOutboundDTO;
import com.itwillbs.factron.service.outbound.OutboundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/outbound")
@RequiredArgsConstructor
public class OutboundRestController {
    private final OutboundService outboundService;

    // 출고 전체 조회
    @GetMapping("")
    public ResponseDTO<List<ResponseSearchOutboundDTO>> getOutboundsList(RequestSearchOutboundDTO requestSearchOutboundDTO) {
        try {
            return ResponseDTO.success(outboundService.getOutboundsList(requestSearchOutboundDTO));
        } catch (Exception e) {
            return ResponseDTO.fail(800,"조회된 결과가 없습니다.",outboundService.getOutboundsList(requestSearchOutboundDTO));
        }
    }

    // 출고 처리
    @PutMapping("")
    public ResponseDTO<Void> updateOutbound(@RequestBody RequestOutboundCompleteDTO requestOutboundCompleteDTO) {
        try{
            outboundService.updateOutbound(requestOutboundCompleteDTO);
            return ResponseDTO.success("출고에 성공했습니다.",null);
        } catch (Exception e) {
            return ResponseDTO.fail(800,e.getMessage(),null);
        }
    }
}
