document.addEventListener('DOMContentLoaded', () => {
    // checkScrollContent();
});

function postSearch() {
    new daum.Postcode({
        oncomplete: function (data) {
            var addr = ''; // 주소 변수

            if (data.userSelectedType === 'R') {
                addr = data.roadAddress;
            } else {
                addr = data.jibunAddress;
            }

            document.getElementById('zipCode').value = data.zonecode;
            document.getElementById('address').value = addr;
            document.getElementById('addressDetail').focus();

            if (typeof validPost === 'function') {
                validPost();
            }
        },
        width: '100%',
        height: '100%'
    }).embed(document.getElementById('postcodeContainer'));

    setTimeout(() => {
        const layer = document.getElementById('__daum__layer_1');
        if (layer) {
            layer.style.position = 'absolute';
            layer.style.top = '0';
            layer.style.left = '0';
            layer.style.zIndex = '10000';
        }
    }, 100); // render 후 적용
}

function maxLengthNum(input, len) {
    input.value = input.value.replace(/\D/g, '');

    if (input.value.length > len) {
        input.value = input.value.substring(0, len);
    }
}

// input[type="number"] maxLength setting function
function maxLengthCheck(object) {
    if (object.value.length > object.maxLength) {
        object.value = object.value.slice(0, object.maxLength);
    }
}

// Regular expression
// 1. email
function isEmail(asValue) {
    var regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
    return regExp.test(asValue);
}

// 2. id(영문으로 시작, 영문+숫자 조합 6~20자)
function isId(asValue) {
    var regExp = /^[a-z][a-z0-9]{5,19}$/
    var result = regExp.test(asValue);
    return result;
}

// 3. password(8~16자 영문, 숫자 조합)
function isPassword(asValue) {
    var regExp = /^(?=.*\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,16}$/;
    return regExp.test(asValue);
}

// 4. password(8~16자 영문, 숫자, 특수문자 최소 한 가지씩 조합)
function isPasswordSpecial(asValue) {
    // 사용가능 특수문자: $~!@%*^?&()-_=+
    var regExp = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/;
    // var regExp = /^(?=.*\d)(?=.*[a-zA-Z])[a-zA-Z0-9]{8,16}$/;
    return regExp.test(asValue);
}

// 5. name(한글, 영문 대소문자만 사용 가능)
function isName(name) {
    const regex = /^[가-힣A-Za-z]+$/;
    return regex.test(name);
}

function closeAlert(target) {
    document.getElementById(target).style.display = 'none';
}

function openAlert(target) {
    document.getElementById(target).style.display = 'block';
}

function closeAlarmAndReplace(target, url) {
    document.getElementById(target).style.display = 'none';
    location.replace(url);
}

function goPageWithoutHistory(url) {
    window.history.replaceState(null, '', url); // URL만 바꾸기 (주소창 변경)
    window.location.reload(); // 강제로 새 페이지 로드
}

// function checkScrollContent() {
//     const headerContent = document.querySelector('.header-scroll');
//     const bodyContent = document.querySelector('.body-scroll');
//     if (bodyContent.scrollTop > 0) {
//         headerContent.classList.add('scrolled');
//     } else {
//         headerContent.classList.remove('scrolled');
//     }
// }

function goPage(url) {
    if (!url) {
        console.error("이동할 URL이 저장되지 않았습니다.");
        return;
    }
    window.location.href = url;
}