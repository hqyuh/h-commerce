
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

    $(".navbar .dropdown").hover(
        function () {
            $(this).find('.dropdown-menu')
                .first()
                .stop(true, true)
                .delay(100)
                .slideDown();
        },
        function () {
            $(this).find('.dropdown-menu')
                .first()
                .stop(true, true)
                .delay(50)
                .slideUp();
        }
    );

    $(".dropdown > a").click(function () {
       location.href = this.href;
    });
}
