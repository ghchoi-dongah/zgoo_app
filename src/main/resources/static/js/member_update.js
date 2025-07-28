let isNameChecked = true;
let isEmailChecked = true;
let isBirthChecked = true;
let isPostChecked = true;

document.addEventListener("DOMContentLoaded", function() {
    const name = document.getElementById('name');
    const isValidName = document.getElementById('isValidName');
    const email = document.getElementById('email');
    const birth = document.getElementById('birth');
    const zipCode = document.getElementById('zipCode');
    const address = document.getElementById('address');
    const addressDetail = document.getElementById('addressDetail');
    
    name.addEventListener('input', function() {
        isValidName.style.display = isName(name.value) ? 'none' : 'block';
        isNameChecked = isName(name.value) ? true : false;
        console.log('name: ' + this.value);
        validateForm();
    });

    email.addEventListener('input', function() {
        isEmailChecked = isEmail(email.value) ? true : false;
        console.log('email: ' + this.value);
        validateForm();
    });

    birth.addEventListener('input', function() {
        isBirthChecked = (this.value.trim() !== '' && this.value.length === 8) ? true : false;
        console.log('birth: ' + this.value);
        validateForm();
    });
});

function validateForm() {
    const updatekBtn = document.getElementById('updatekBtn');

    console.log('=== validateForm ===');
    console.log('isNameChecked: ' + isNameChecked);
    console.log('isEmailChecked: ' + isEmailChecked);
    console.log('isBirthChecked: ' + isBirthChecked);

    if (isNameChecked && isEmailChecked && isBirthChecked) {
        updatekBtn.disabled = false;
    } else {
        updatekBtn.disabled = true;
    }
}

function validPost() {
    const zipCode = document.getElementById('zipCode');
    const address = document.getElementById('address');

    // if (zipCode.value.trim() !== '' && address.value.trim() !== '') {
    //     isPostChecked = true;
    // } else {
    //     isPostChecked = false;
    // }

    isPostChecked = (zipCode.value.trim() !== '' && address.value.trim() !== '') ? true : false;

    validateForm();
}