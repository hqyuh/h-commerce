
/* index.html */
$(document).ready(() => {
    $("#logoutLink").on("click", (e) => {
        e.preventDefault();
        document.logoutForm.submit();
    });

    customizeDropDownMenu();
});

/* navigation.html */
const customizeDropDownMenu = () => {
    $(".dropdown > a").click(function () {
       location.href = this.href;
    });
}
