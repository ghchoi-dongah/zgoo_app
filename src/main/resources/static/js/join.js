let isNameChecked = false;
let isIdChecked  = false;
let isPwChecked = false;
let isPhoneChecked = false;
let isEmailChecked = false;
let isPayChecked = false;
let isBirthChecked = false;
let isPostChecked = false;
let isCarChecked = true;

document.addEventListener("DOMContentLoaded", function() {
    const memType = document.getElementById('memType');
    const bizType = document.getElementById('bizType');
    const bizName = document.getElementById('bizName');
    const bizId = document.getElementById('bizId');
    const bizList = document.getElementById('bizList');
    const bizContent = document.getElementById('bizContent');
    const searchBizName = document.getElementById('searchBizName');
    const paymentContent = document.getElementById('paymentContent');
    ///////////////////////////////////////////////////////////////////
    const name = document.getElementById('name');
    // id
    const memLoginId = document.getElementById('memLoginId');
    const invalidId = document.getElementById('invalidId');
    const validId = document.getElementById('validId');
    const isValidId = document.getElementById('isValidId');
    const idCheckBtn = document.getElementById('idCheckBtn');
    // password
    const pw = document.getElementById('password');
    const rePw = document.getElementById('rePassword');
    const isValidPw = document.getElementById('isValidPw');
    const invalidPw = document.getElementById('invalidPw');

    const phoneNo = document.getElementById('phoneNo');
    const verifiNum = document.getElementById('verifiNum');
    const email = document.getElementById('email');
    const birth = document.getElementById('birth');
    const zipCode = document.getElementById('zipCode');
    const address = document.getElementById('address');
    const addressDetail = document.getElementById('addressDetail');
    const saveBtn = document.getElementById('saveBtn');
    const savePayBtn = document.getElementById('savePayBtn');
    const cardDelBtn = document.getElementById('cardDelBtn');

    const carNum = document.getElementById('carNum');
    const model = document.getElementById('model');
    ///////////////////////////////////////////////////////////////////

    document.getElementById('joinForm').reset();
    document.querySelector('.mem-body').addEventListener('scroll', checkScroll);
    checkScroll();

    ///////////////////////////////////////////////////////////////////
    // 약관처리
    document.querySelectorAll('.checkbox-terms').forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            const checkboxes = document.querySelectorAll('.checkbox-terms');
            const checkedCount = Array.from(checkboxes).filter(cb => cb.checked).length;

            document.getElementById('termsAll').checked = (checkboxes.length === checkedCount);

            checkRequiredTerms();
        });
    });

    ///////////////////////////////////////////////////////////////////
    // 회원구분(개인/법인)
    document.getElementById('typePB').addEventListener('click', function() {
        bizContent.style.display = 'none';
        memType.innerText = "개인 회원";
        bizType.value = "PB";
        paymentContent.style.display = 'block';
        document.getElementById('slideBtn2').disabled = false;
        isPayChecked = false;
    });

    document.getElementById('typeCB').addEventListener('click', function() {
        bizContent.style.display = 'block';
        memType.innerText = "법인 회원";
        bizType.value = "CB";
        paymentContent.style.display = 'none';

        document.getElementById('slideBtn2').disabled = true;
        isPayChecked = true;
        clearBizInfo();
    });

    ///////////////////////////////////////////////////////////////////
    // 계정정보 확인
    name.addEventListener('input', function() {
        isNameChecked = this.value.trim() !== '';
        console.log('name: ' + this.value);
        validateForm();
    });

    memLoginId.addEventListener('input', function() {
        isValidId.style.display = isId(memLoginId.value) ? 'none' : 'block';
        invalidId.style.display = 'none';
        validId.style.display = 'none';
        isIdChecked = false;
        console.log('memLoginId: ' + this.value);
        console.log('isIdChecked: ' + isIdChecked);
        validateForm();
    });

    // ID 중복확인
    idCheckBtn.addEventListener('click', function() {
        if (memLoginId.value === '' || !isId(memLoginId.value)) {
            return;
        }

        fetch(`/member/dupcheck?memLoginId=${encodeURIComponent(memLoginId.value)}`, {
            method: 'GET',
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error('서버 오류: ' + response.status);
                }
                return response.json();
            })
            .then((data) => {
                invalidId.style.display = data ? 'block' : 'none';
                validId.style.display = data ? 'none' : 'block';
                isIdChecked = !data;
                console.log('ID 중복확인: ' + isIdChecked);
                validateForm();
            })
            .catch((error) => {
                console.error('에러:', error);
                alert('오류가 발생하여 회원가입을 할 수 없습니다.');
            });
    });

    pw.addEventListener('input', function() {
        invalidPw.style.display = pw.value === rePw.value ? 'none' : 'block';

        if (isPasswordSpecial(pw.value)) {
            isValidPw.style.display = 'none';
            isPwChecked = true;
        } else {
            isValidPw.style.display = 'block';
            isPwChecked = false;
        }
        
        console.log('pw: ' + this.value);
        console.log('isPwChecked: ' + isPwChecked);

        if (pw.value !== rePw.value) return;
        validateForm();
    });

    rePw.addEventListener('input', function() {
        invalidPw.style.display = pw.value === rePw.value ? 'none' : 'block';

        if (isPasswordSpecial(pw.value)) {
            isPwChecked = true;
        } else {
            isPwChecked = false;
        }

        console.log('rePw: ' + this.value);
        console.log('isPwChecked: ' + isPwChecked);

        if (pw.value !== rePw.value) return;
        validateForm();
    });

    phoneNo.addEventListener('input', function() {
        if (this.value.trim() !== '' && this.value.length === 11) {
            isPhoneChecked = true;
        } else {
            isPhoneChecked = false;
        }

        console.log('phoneNo: ' + this.value);
        console.log('isPhoneChecked: ' + isPhoneChecked);
        validateForm();
    });

    email.addEventListener('input', function() {
        if (isEmail(email.value)) {
            isEmailChecked = true;  
        } else {
            isEmailChecked = false;
        }

        console.log('email: ' + this.value);
        console.log('isEmailChecked: ' + isEmailChecked);
        validateForm();
    });

    // --------------------------------------------------- //
    // 결제수단
    savePayBtn.addEventListener('click', function() {
        isPayChecked = true;
        savePaymentInfo();
        document.getElementById('paymentForm').reset();
        validateForm();
    });

    cardDelBtn.addEventListener('click', function() {
        isPayChecked = false;
        delPaymentInfo();
        document.getElementById('paymentForm').reset();
        validateForm();
    });

    const cardNum1  = document.getElementById('cardNum1');
    const cardNum2  = document.getElementById('cardNum2');
    const cardNum3  = document.getElementById('cardNum3');
    const cardNum4  = document.getElementById('cardNum4');
    const cardExpireMonth  = document.getElementById('cardExpireMonth');
    const cardExpireYear  = document.getElementById('cardExpireYear');
    const cvc  = document.getElementById('cvc');
    const cardPw  = document.getElementById('cardPw');

    const cardInputs = [cardNum1, cardNum2, cardNum3, cardNum4];
    cardInputs.forEach((input, index) => {
        input.addEventListener('input', function() {
            if (this.value.length === 4 && index < cardInputs.length - 1) {
                cardInputs[index + 1].focus();
            }
            validPayment();
        });
    });
    cardExpireMonth.addEventListener('input', function() {
        if (this.value.length === 2) {
            cardExpireYear.focus();
        }
        validPayment();
    });
    cardExpireYear.addEventListener('input', validPayment);
    cvc.addEventListener('input', validPayment);
    cardPw.addEventListener('input', validPayment);
    
    function validPayment() {
        if ((cardNum1.value.trim() !== '' && cardNum1.value.length === 4) &&
            (cardNum2.value.trim() !== '' && cardNum2.value.length === 4) &&
            (cardNum3.value.trim() !== '' && cardNum3.value.length === 4) &&
            (cardNum4.value.trim() !== '' && cardNum4.value.length === 4) &&
            (cardExpireMonth.value.trim() !== '' && cardExpireMonth.value.length === 2) &&
            (cardExpireYear.value.trim() !== '' && cardExpireYear.value.length === 2) &&
            (cvc.value.trim() !== '' && cvc.value.length === 3) &&
            (cardPw.value.trim() !== '' && cardPw.value.length === 2)) {
            savePayBtn.disabled = false;
        } else {
            savePayBtn.disabled = true;
        }
    }

    // --------------------------------------------------- //
    birth.addEventListener('input', function() {
        if (this.value.trim() !== '' && this.value.length === 8) {
            isBirthChecked = true;
        } else {
            isBirthChecked = false;
        }

        console.log('birth: ' + this.value);
        console.log('isBirthChecked: ' + isBirthChecked);
        validateForm();
    });

    carNum.addEventListener('input', validCar);
    model.addEventListener('input', validCar);

    // 회원정보 저장
    saveBtn.addEventListener('click', function(e) {
        e.preventDefault();
        
        const bodyData = {
            condition: getConditionList(),
            bizType: bizType.value,
            bizId: bizType.value === 'CB' ? bizId.value : null,
            name: name.value,
            memLoginId: memLoginId.value,
            password: pw.value,
            phoneNo: phoneNo.value,
            verifiNum: verifiNum.value,
            email: email.value,
            birth: birth.value,
            zipCode: zipCode.value,
            address: address.value,
            addressDetail: addressDetail.value,
            card: getCardInfo(bizType.value),
            car: getCarList(),
        };

        fetch(`/member/new`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(bodyData)
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error('서버 오류: ' + response.status);
                }
                return response.text();
            })
            .then((data) => {
                alert(data);
                location.replace('/login');
            })
            .catch((error) => {
                console.error('에러:', error);
                alert('네트워크 오류가 발생했습니다. 다시 시도해 주세요.');
            });
    });

    // 법인조회: 영역 외 클릭 시 보이지 않도록 설정
    document.addEventListener('click', function (event) {
        const bizList = document.getElementById('bizList');
        
        // bizList 또는 그 자식 요소를 클릭한 경우는 무시
        if (bizList.contains(event.target)) return;

        // 검색 input도 무시 (검색창 클릭 시 닫히지 않도록)
        const searchBizName = document.getElementById('searchBizName');
        if (searchBizName.contains(event.target)) return;

        // bizList 보이면 닫기
        if (bizList.style.display === 'block') {
            bizList.style.display = 'none';
        }
    });

    bizList.addEventListener('click', e => e.stopPropagation());
    searchBizName.addEventListener('click', e => e.stopPropagation());
});

// next page
function goToNext(target) {
    document.getElementById('content' + target).classList.remove('active');
    document.getElementById('content' + (target + 1)).classList.add('active');
}

// terms all checked
function allChecked() {
    console.log('checked');
    const termsAllCheckbox = document.getElementById('termsAll');
    const isChecked = termsAllCheckbox.checked;

    document.querySelectorAll('.checkbox-terms').forEach(checkbox => {
        checkbox.checked = termsAllCheckbox.checked;
    });

    document.querySelectorAll('.term-agreedt').forEach(dt => {
        dt.value = isChecked ? formatTimestamp() : '';
    });

    checkRequiredTerms();
}

// page scroll check
function checkScroll() {
    const memBody = document.querySelector('.mem-body');
    const memHeader = document.querySelector('.mem-header');
    if (memBody.scrollTop > 0) {
        memHeader.classList.add('scrolled');
    } else {
        memHeader.classList.remove('scrolled');
    }
}

// 약관동의 필수항목 check
function checkRequiredTerms() {
    // 필수 항목 총 개수
    const yCheckboxes = document.querySelectorAll('.checkbox-terms[id^="Y"]');
    const count = yCheckboxes.length;

    // 선택된 필수 항목 개수
    const yChecked = Array.from(yCheckboxes).filter(cb => cb.checked).length;

    console.log("필수 항목 총 개수: " + count);
    console.log("체크된 필수 항목 개수: " + yChecked);

    document.getElementById('slideBtn1').disabled = count === yChecked ? false : true;
}
// 회원 약관정보 얻기
function getConditionList() {
    const result = Array.from(document.querySelectorAll('.checkbox-terms')).map(cb => {
        const [section, code] = cb.id.split('-');
        const agreedt = document.getElementById('agreeDt' + code);
        return {
            conditionCode: code,
            agreeVersion: cb.value,
            agreeYn: cb.checked ? 'Y' : 'N', 
            stringAgreeDt: agreedt.value
        };
    });
    console.log("conditionList >> ", result);
    return result;
}
// 회원 차량정보 얻기
function getCarList() {
    const carInfoList = [];
    const carNum = document.getElementById('carNum').value.trim();
    const model = document.getElementById('model').value.trim();

    if (carNum && model) {
        carInfoList.push({
            carNum: carNum,
            model: model
        });
    }

    return carInfoList;
}
// 카드정보
function getCardInfo(bizType) {
    if (bizType === 'CB') {
        return null;
    }

    const card = { cardNum: '1234-5678-****-****', fnCode: '11', tid: '12' };
    return card;
}
function formatTimestamp() {
    const date = new Date();
    const pad = (n) => n.toString().padStart(2, '0');
    const year = date.getFullYear();
    const month = pad(date.getMonth() + 1);  // 0~11
    const day = pad(date.getDate());
    const hour = pad(date.getHours());
    const minute = pad(date.getMinutes());
    const second = pad(date.getSeconds());
    return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
}

function paymentClick(prop) {
    const layer = document.getElementById('paymentLayer');
    document.getElementById('paymentForm').reset();

    if (prop === 'block') {
        layer.classList.add('show');
    } else {
        layer.classList.remove('show');
    }
}
// 법인정보 조회
function findBizList() {
    const searchBizName = document.getElementById('searchBizName');
    const bizNameInput = document.getElementById("bizName");
    const bizName = searchBizName.value;
    const bizList = document.getElementById('bizList');
    const bizId = document.getElementById('bizId');

    if (!bizName || bizName === "") {
        bizList.innerHTML = "";
        bizList.style.display = "none";
        return;
    }

    fetch(`/member/search?bizName=${encodeURIComponent(bizName)}`, {
        method: 'GET',
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error('서버 오류: ' + response.status);
            }
            return response.json();
        })
        .then((data) => {
            bizList.innerHTML = "";
            bizList.style.display = "block";

            data.forEach(biz => {
                if (!biz.bizName) return;
                const div = document.createElement("div");
                div.textContent = biz.bizName;
                div.id = biz.bizId;

                div.addEventListener('click', function() {
                    bizNameInput.value = biz.bizName;
                    bizList.style.display = "none";
                    searchBizName.value = "";
                    bizId.value = biz.bizId;
                    console.log("법인명: " + bizNameInput.value);
                    console.log("법인ID: " + bizId.value);
                    checkBizInfo();
                });

                bizList.appendChild(div);
            });

            if (data.length === 0) {
                bizList.innerHTML = "<div>검색 결과가 없습니다.</div>";
            }
        })
        .catch((error) => {
            console.error('에러:', error);
            alert('네트워크 오류가 발생했습니다. 다시 시도해 주세요.');
        });
}
// 법인정보 선택 시 버튼 활성화
function checkBizInfo() {
    const bizNameInput = document.getElementById('bizName');
    const slideBtn = document.getElementById('slideBtn2');
    const isValid = bizNameInput && bizNameInput.value && bizNameInput.value.trim() !== "";
    slideBtn.disabled = !isValid;
}
// 법인정보 삭제
function clearBizInfo() {
    const searchBizName = document.getElementById('searchBizName');
    const bizName = document.getElementById("bizName");
    const bizList = document.getElementById('bizList');
    const bizId = document.getElementById('bizId');

    searchBizName.value = '';
    bizName.value = '';
    bizList.innerHTML = '';
    bizList.style.display = 'none';
    bizId.value = '';
}
// 결제정보 등록
function savePaymentInfo() {
    const cardSaveBtn = document.getElementById('cardSaveBtn');
    const cardDelBtn = document.getElementById('cardDelBtn');
    cardSaveBtn.style.visibility = 'hidden';
    cardDelBtn.style.visibility = 'visible';

    const cardNum1 = document.getElementById('cardNum1').value;
    const cardNum2 = document.getElementById('cardNum2').value;
    const card = document.getElementById('card');
    var cardNum = cardNum1 + " - " + cardNum2 +  " - **** - ****";
    card.innerText = cardNum;

    paymentClick('none');
}
// 결제정보 삭제
function delPaymentInfo() {
    const cardSaveBtn = document.getElementById('cardSaveBtn');
    const cardDelBtn = document.getElementById('cardDelBtn');
    cardSaveBtn.style.visibility = 'visible';
    cardDelBtn.style.visibility = 'hidden';

    const card = document.getElementById('card');
    const cardName = document.getElementById('cardName');
    card.innerText = "";
    cardName.innerText = "";
}

function validateForm() {
    const saveBtn = document.getElementById('saveBtn');

    console.log('=== validateForm ===');
    console.log('isNameChecked: ' + isNameChecked);
    console.log('isIdChecked: ' + isIdChecked);
    console.log('isPwChecked: ' + isPwChecked);
    console.log('isPhoneChecked: ' + isPhoneChecked);
    console.log('isEmailChecked: ' + isEmailChecked);
    console.log('isPayChecked: ' + isPayChecked);
    console.log('isBirthChecked: ' + isBirthChecked);
    console.log('isPostChecked: ' + isPostChecked);
    console.log('isCarChecked: ' + isCarChecked);

    if (isNameChecked && isIdChecked && isPwChecked && isPhoneChecked && isEmailChecked && 
        isPayChecked && isBirthChecked && isPostChecked && isCarChecked
    ) {
        saveBtn.disabled = false;
    } else {
        saveBtn.disabled = true;
    }
}

function validPost() {
    const zipCode = document.getElementById('zipCode');
    const address = document.getElementById('address');

    if (zipCode.value.trim() !== '' && address.value.trim() !== '') {
        isPostChecked = true;
    } else {
        isPostChecked = false;
    }

    validateForm();
}

function validCar() {
    const carNum = document.getElementById('carNum').value.trim();
    const model = document.getElementById('model').value.trim();

    const isCarNumEmpty = carNum === '';
    const isModelEmpty = model === '';

    if ((isCarNumEmpty && isModelEmpty) || (!isCarNumEmpty && !isModelEmpty)) {
        isCarChecked = true;
    } else {
        isCarChecked = false;
    }

    validateForm();
}