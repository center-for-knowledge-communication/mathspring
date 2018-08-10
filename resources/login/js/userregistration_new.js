$(document).ready(function() {
    var usernameInput = $('.username-wrapper');
    var passwordInput = $('.password-wrapper');
    var messageBar = $('.msg-bar');
    $('.student-registration-form').on('submit', function(event) {
        event.preventDefault();
        $.ajax({
            url: "${pageContext.request.contextPath}/WoAdmin"
                + "?action=UserRegistrationValidateUsername"
                + "&userName=" + $('#username').val(),
            success: function(responseText) {
                if ($('#password').val() === '') {
                    messageBar.removeClass('hidden');
                    messageBar.text("Please provide a password");
                    passwordInput.addClass('has-error');
                } else if (responseText === "") {
                    var fname = $('#first_name').val();
                    var lname = $('#last_name').val();
                    var uname = $('#username').val();
                    var email = $('#email').val();
                    var age = $('#age').val();
                    var gender = $('#gender').val();
                    var password = $('#password').val();
                    var userType = $('input:radio[name=userType]:checked').val();
                    location.href = "${pageContext.request.contextPath}/WoAdmin?"
                            + "action=UserRegistrationAuthenticationInfo"
                            + "&fname=" + fname
                            + "&lname=" + lname
                            + "&uname=" + uname
                            + "&email=" + email
                            + "&password=" + password
                            + "&age=" + age
                            + "&gender=" + gender
                            + "&userType=" + userType
                            + "&startPage=${startPage}";
                } else {
                    messageBar.removeClass('hidden');
                    messageBar.text(responseText);
                    usernameInput.addClass('has-error');
                }
            },
            error: function() {
                usernameInput.addClass('has-error');
                messageBar.removeClass('hidden');
            },
            async: true});
    });
});

