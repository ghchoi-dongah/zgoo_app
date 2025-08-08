document.addEventListener("DOMContentLoaded", function() {
    let isTitleChecked = false;
    let isContentChecked = false;
    const type = document.getElementById('type');
    const title = document.getElementById('title');
    const content = document.getElementById('content');
    const saveBtn = document.getElementById('saveBtn');
    const alertSuccess = document.getElementById('alertSuccess');
    const vocForm = document.getElementById('vocForm');

    title.addEventListener('input', function() {
        isTitleChecked = this.value.trim() !== '' ? true : false;
        validateForm();
    });

    content.addEventListener('input', function() {
        isContentChecked = this.value.trim() !== '' ? true : false;
        validateForm();
    });

    function validateForm() {
        console.log('=== validateForm ===');
        console.log('isTitleChecked: ' + isTitleChecked);
        console.log('isContentChecked: ' + isContentChecked);

        saveBtn.disabled = !(isTitleChecked && isContentChecked);
    }

    saveBtn.addEventListener('click', function(e) {
        e.preventDefault();

        const bodyData = {
            type: type.value,
            title: title.value,
            content: content.value
        };

        fetch(`/voc/new`,{
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
                alertSuccess.style.display = 'block';
                vocForm.reset();
                saveBtn.disabled = true;
                console.log(data);
            })
            .catch((error) => {
                console.error('error: ', error);
            });
    });
});