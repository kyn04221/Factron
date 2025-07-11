package com.itwillbs.factron.service.machine;

import com.itwillbs.factron.common.component.AuthorizationChecker;
import com.itwillbs.factron.dto.machine.RequestAddMachineDTO;
import com.itwillbs.factron.dto.machine.RequestMachineInfoDTO;
import com.itwillbs.factron.dto.machine.RequestUpdateMachineDTO;
import com.itwillbs.factron.dto.machine.ResponseMachineInfoDTO;
import com.itwillbs.factron.entity.Machine;
import com.itwillbs.factron.entity.Process;
import com.itwillbs.factron.mapper.machine.MachineMapper;
import com.itwillbs.factron.repository.process.MachineRepository;
import com.itwillbs.factron.repository.process.ProcessRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MachineServiceImpl implements MachineService {

    private final MachineRepository machineRepository;
    private final MachineMapper machineMapper;
    private final ProcessRepository processRepository;

    private final AuthorizationChecker authorizationChecker;

    /**
     * 설비 추가
     *
     * @param requestDto 요청 DTO
     */
    @Override
    @Transactional
    public void addMachine(RequestAddMachineDTO requestDto) {

        // 관리자 권한 체크
        authorizationChecker.checkAnyAuthority("ATH003", "ATH007");

        // 공정 조회
        Process process = processRepository.findById(requestDto.getProcessId())
                .orElseThrow(() -> new EntityNotFoundException("해당 공정이 존재하지 않습니다."));

        // hasMachine 검증
        if ("Y".equals(process.getHasMachine())) {
            throw new IllegalStateException("이미 설비가 등록된 공정입니다.");
        }

        // Machine 생성 및 저장
        Machine machine = Machine.builder()
                .process(process)
                .name(requestDto.getMachineName())
                .manufacturer(requestDto.getManufacturer())
                .buyDate(requestDto.getBuyDate())
                .build();

        machineRepository.save(machine);

        // 공정 정보 업데이트
        process.updateHasMachine("Y");
    }

    /**
     * 설비 수정
     *
     * @param requestDto 요청 DTO
     */
    @Override
    @Transactional
    public void updateMachine(RequestUpdateMachineDTO requestDto) {

        // 관리자 권한 체크
        authorizationChecker.checkAnyAuthority("ATH003", "ATH007");

        // Machine 조회
        Machine machine = machineRepository.findById(requestDto.getMachineId())
                .orElseThrow(() -> new EntityNotFoundException("해당 설비가 존재하지 않습니다."));

        machine.updateMachineInfo(
                requestDto.getMachineName(),
                requestDto.getManufacturer(),
                requestDto.getBuyDate()
        );
    }

    /**
     * 설비 목록 조회
     *
     * @param requestDto 요청 DTO
     * @return 설비 목록
     */
    @Override
    public List<ResponseMachineInfoDTO> getMachineList(RequestMachineInfoDTO requestDto) {

        return machineMapper.selectMachines(requestDto);
    }
}
