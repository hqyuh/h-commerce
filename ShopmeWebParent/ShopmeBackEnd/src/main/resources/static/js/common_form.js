
/* account_form.html */

$(document).ready(() => {
    $('#buttonCancel').on('click', () => {
        window.location = moduleURL;
    });

    $('#fileImage').change(function () {
        const fileSize = this.files[0].size;

        if(fileSize > 1048576) {
            this.setCustomValidity('You must choose an image less than 1MB');
            this.reportValidity();
        } else {
            showImageThumbnail(this);
        }
    })
});

// get object from input file
const showImageThumbnail = (fileInput) => {
    // array
    const file = fileInput.files[0];
    const reader = new FileReader();
    // onload: after successfully loaded
    reader.onload = function (e) {
        $('#thumbnail').attr('src', e.target.result);
    }
    // read binary data and encode it as base64 data url
    reader.readAsDataURL(file);
}

const showModalDialog = (title, message) => {
    $('#modalTitle').text(title);
    $('#modalBody').text(message);
    // show dialog
    $('#modalDialog').modal();
}
