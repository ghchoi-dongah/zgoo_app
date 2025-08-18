document.addEventListener("DOMContentLoaded", function() {
    const date = document.getElementById('date');
    const startDateText = document.getElementById('startDateText');
    const endDateText = document.getElementById('endDateText');
    const currentYear = new Date().getFullYear();
    const currentMonth = new Date().getMonth() + 1;
    let targetDate = null;

    // 연도, 월 기본값 설정
    startDateText.textContent = `${currentYear}년 ${currentMonth}월`;
    endDateText.textContent = `${currentYear}년 ${currentMonth}월`;
    startDateText.dataset.value = `${currentYear}-${currentMonth}`;
    endDateText.dataset.value = `${currentYear}-${currentMonth}`;

    window.selectDate = function(target) {
        date.style.display = 'block';
        targetDate = target;
        console.log('target: ', target);
        createDate(target);
    }

    function createDate(target) {
        const currentYear = new Date().getFullYear();
        const currentMonth = new Date().getMonth() + 1;
        const [startYear, startMonth] = startDateText.dataset.value.split("-").map(Number);
        const [endYear, endMonth] = endDateText.dataset.value.split("-").map(Number);

        if (target === 'startDateText') {
            const yearRange = [];
            for (let y = 2024; y <= endYear; y++) yearRange.push(y);

            let monthRange = [];
            if (startYear === endYear) {
                monthRange = Array.from({ length: endMonth }, (_, i) => i + 1);
            } else if (startYear < endYear) {
                monthRange = Array.from({ length: 12 }, (_, i) => i + 1);
            }

            initDropdown('yearDropdown', yearRange, startYear, '년', target);
            initDropdown('monthDropdown', monthRange, startMonth, '월', target);

        } else if (target === 'endDateText') {
            const yearRange = [];
            for (let y = startYear; y <= currentYear; y++) yearRange.push(y);

            let monthRange = [];
            if (startYear < endYear && endYear != currentYear) {
                monthRange = Array.from({ length: 12 }, (_, i) => i + 1);
            }  else if (startYear < endYear && endYear === currentYear) {
                monthRange = Array.from({ length: currentMonth }, (_, i) => i + 1);
            } else if (startYear === endYear && endYear != currentYear) {
                monthRange = Array.from({ length: 12 - startMonth + 1 }, (_, i) => i + startMonth);
            } else if (startYear === endYear && endYear === currentYear) {
                monthRange = Array.from({ length: currentMonth - startMonth + 1 }, (_, i) => i + startMonth);
            }

            initDropdown('yearDropdown', yearRange, endYear, '년', target);
            initDropdown('monthDropdown', monthRange, endMonth, '월', target);
        }
    }

    // 연도 선택 후 월 범위 재생
    function updateMonthDropdown(selectedYear, targetType) {
        selectedYear = Number(selectedYear);
        let monthRange = [];
        const [endYear, endMonth] = endDateText.dataset.value.split('-').map(Number);

        if (targetType === 'startDateText') {
            if (selectedYear < endYear) {
                monthRange = Array.from({ length: 12 }, (_, i) => i + 1);
            } else if (selectedYear === endYear) {
                monthRange = Array.from({ length: endMonth }, (_, i) => i + 1);
            }
        } else if (targetType === 'endDateText') {
            const [startYear, startMonth] = startDateText.dataset.value.split('-').map(Number);
            const currentYear = new Date().getFullYear();

            if (selectedYear > startYear && selectedYear != currentYear) {
                monthRange = Array.from({ length: 12 }, (_, i) => i + 1);
            } else if (selectedYear > startYear && selectedYear === currentYear) {
                monthRange = Array.from({ length: currentMonth }, (_, i) => i + 1);
            } else if (selectedYear === startYear && selectedYear != currentYear) {
                 monthRange = Array.from({ length: 12 - startMonth + 1 }, (_, i) => i + startMonth);
            } else if (selectedYear === startYear && selectedYear === currentYear) {
                 monthRange = Array.from({ length: currentMonth - startMonth + 1 }, (_, i) => i + startMonth);
            } 
        }

        initDropdown('monthDropdown', monthRange, monthRange[0], '월');
    }

    // 드롭다운 초기화
    function initDropdown(dropdownId, items, defaultValue, suffix, target) {
        const dropdown = document.getElementById(dropdownId);
        const selected = dropdown.querySelector('.selected');
        const options = dropdown.querySelector('.options');

        // 기존 옵션 및 이벤트 초기화
        options.innerHTML = '';
        selected.replaceWith(selected.cloneNode(true)); // 기존 이벤트 제거
        const newSelected = dropdown.querySelector('.selected');

        // 기본 값 세팅
        newSelected.textContent = defaultValue + suffix;
        newSelected.dataset.value = defaultValue;

        // 옵션 리스트 생성
        items.forEach(item => {
            const li = document.createElement('li');
            li.textContent = item + suffix;
            li.dataset.value = item;
            options.appendChild(li);

            li.addEventListener('click', e => {
                newSelected.textContent = e.target.textContent;
                newSelected.dataset.value = e.target.dataset.value;
                dropdown.classList.remove('open');

                if (dropdownId  === 'yearDropdown') {
                    console.log('선택한 연도:',e.target.dataset.value);
                    updateMonthDropdown(e.target.dataset.value, target);
                }
            });
        });

        // 드롭다운 열기/닫기
        newSelected.addEventListener('click', e => {
            e.stopPropagation();
            closeAllDropdowns(dropdownId);
            dropdown.classList.toggle('open');
        });
    }

    function closeAllDropdowns(exceptId) {
        document.querySelectorAll('.custom-dropdown').forEach(dropdown => {
            if (dropdown.id !== exceptId) {
                dropdown.classList.remove('open');
            }
        });
    }

    window.checkSelected = function() {
        const year = document.querySelector('#yearDropdown .selected').dataset.value;
        const month = document.querySelector('#monthDropdown .selected').dataset.value;
        alert(`선택된 값: ${year}년 ${month}월`);
    }


    document.addEventListener('click', () => {
        closeAllDropdowns(null);
    });


    window.checkDate = function() {
        const year = document.querySelector('#yearDropdown .selected').dataset.value;
        const month = document.querySelector('#monthDropdown .selected').dataset.value;
        const selectedDate = document.getElementById(targetDate);
        selectedDate.textContent = `${year}년 ${month}월`;
        selectedDate.dataset.value = `${year}-${month}`;
        date.style.display = 'none';
    }

    window.searchHist = function() {
        console.log("startDateText.dataset.value: ", startDateText.dataset.value);
        console.log("endDateText.dataset.value: ", endDateText.dataset.value);
        const startDate = startDateText.dataset.value;
        const endDate = endDateText.dataset.value;

        fetch(`/chghist/get?startDate=${encodeURIComponent(startDate)}&endDate=${encodeURIComponent(endDate)}`, {
            method: 'GET',
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error('Network Error: ' + response.status);
                }
                return response.json();
            })
            .then((data) => {
                const histTable = document.getElementById('histTable');
                histTable.innerHTML = '';

                if (!data || data.length === 0) {
                    const tr = document.createElement('tr');
                    const td = document.createElement('td');
                    td.colSpan = 8;
                    td.className = 'text-center table-border-bottom';
                    td.textContent = '충전 이력이 없습니다.';
                    tr.appendChild(td);
                    histTable.appendChild(tr);
                    return;
                }

                data.forEach((hist, index) => {
                    const tr = document.createElement('tr');
                    appendTd(tr, index + 1); // NO
                    appendTd(tr, hist.stationName); // 충전소명
                    appendTd(tr, formatDateTime(hist.chgStartTime)); // 시작일시
                    appendTd(tr, formatDateTime(hist.chgEndTime)); // 종료일시
                    appendTd(tr, hist.chgTime != null ? formatSeconds(hist.chgTime) : ''); // 충전시간 (초 → HH:mm:ss)
                    appendTd(tr, hist.chgAmount != null ? hist.chgAmount.toFixed(2) : '0.0'); // 충전량
                    appendTd(tr, hist.unitCost); // 적용단가
                    appendTd(tr, hist.realCost != null ? hist.realCost.toLocaleString() : '0'); // 결제금액 (천 단위 콤마)

                    // 최종적으로 histTable에 추가
                    histTable.appendChild(tr);
                });
            })
            .catch((error) => {
                console.error('에러: ', error);
            });
    }
});

// <td> 생성 헬퍼
function appendTd(tr, value) {
    const td = document.createElement('td');
    td.textContent = value;
    tr.appendChild(td);
}

function formatDateTime(dateTimeStr) {
    const d = new Date(dateTimeStr);
    const yyyy = d.getFullYear();
    const MM = String(d.getMonth() + 1).padStart(2, '0');
    const dd = String(d.getDate()).padStart(2, '0');
    const hh = String(d.getHours()).padStart(2, '0');
    const mm = String(d.getMinutes()).padStart(2, '0');
    const ss = String(d.getSeconds()).padStart(2, '0');
    return `${yyyy}-${MM}-${dd} ${hh}:${mm}:${ss}`;
}

// 초 → HH:mm:ss 변환
function formatSeconds(sec) {
    const h = String(Math.floor(sec / 3600)).padStart(2, '0');
    const m = String(Math.floor((sec % 3600) / 60)).padStart(2, '0');
    const s = String(sec % 60).padStart(2, '0');
    return `${h}:${m}:${s}`;
}