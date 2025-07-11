const init = () => {
    const vacationGrid = initGrid(
        document.getElementById('vacationGrid'),
        400,
        [
            { header: '사원번호', name: 'empId', align: 'center' },
            { header: '이름', name: 'empName', align: 'center' },
            { header: '직급', name: 'positionName', align: 'center' },
            { header: '부서', name: 'deptName', align: 'center' },
            { header: '시작날짜', name: 'vacationStartDate', align: 'center' },
            { header: '종료날짜', name: 'vacationEndDate', align: 'center' },
            { header: '비고', name: 'remark', align: 'center' }
        ]
    );

    // 검색 초기 세팅
    document.querySelector("input[name='srhIdOrName']").value = user.id;

    // 검색 버튼 클릭 이벤트
    document.querySelector(".srhBtn").addEventListener("click", function(e) {
        e.preventDefault();
        e.stopPropagation();

        fetchData().then(res => {
            vacationGrid.resetData(res.data);
        });

    }, false);

    // 엔터 시 검색
    document.querySelector(".search__form").addEventListener("submit", function(e) {
        e.preventDefault();

        fetchData().then(res => {
            vacationGrid.resetData(res.data);
        });
    });

    // 조회 함수
    async function fetchData() {
        // validation
        const startDate = document.querySelector("input[name='startDate']").value;
        const endDate = document.querySelector("input[name='endDate']").value;
        if ((startDate && !endDate) || (!startDate && endDate)) {
            alert("시작 및 종료 날짜를 모두 입력해주세요.");
            return { data: [] };
        }

        if (startDate && endDate) {
            const dateRegex = /^\d{4}-\d{2}-\d{2}$/;

            if (!dateRegex.test(startDate) || isNaN(Date.parse(startDate))
                || !dateRegex.test(endDate) || isNaN(Date.parse(endDate))) {
                alert("날짜 형식이 올바르지 않습니다.");
                return { data: [] };
            }
            if (new Date(startDate) > new Date(endDate)) {
                alert("시작 날짜는 종료 날짜보다 이전이어야 합니다.");
                return { data: [] };
            }
        }

        const data = new URLSearchParams({
            srhIdOrName: document.querySelector("input[name='srhIdOrName']").value,
            vacationStartDate: startDate,
            vacationEndDate: endDate,
            deptCode: document.querySelector("select[name='srhDeptCode']").value
        });

        try {
            const res = await fetch(`/api/vacation?${data.toString()}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json"
                }
            });
            return res.json();

        } catch (err) {
            console.error("fetch error", err);
        }
    }

    // 휴가 신청 팝업 오픈
    document.querySelector(".registVacation").addEventListener("click", function(e) {
        const popup = window.open('/vacation/save', '_blank', 'width=800,height=450');

        const messageHandler = (event) => {
            if (event.data === 'ready') {
                popup.postMessage({}, "*");
                window.removeEventListener("message", messageHandler);
            }
        };
        window.addEventListener("message", messageHandler);
    });

    // 공통코드 세팅
    setSelectBox("DEP", "srhDeptCode");

    // 페이지 진입 시 바로 리스트 호출
    fetchData().then(res => {
        vacationGrid.resetData(res.data);
    });
};

window.onload = () => {
    init();
};
