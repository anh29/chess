document.addEventListener("DOMContentLoaded", function () {
    var btnback = document.getElementById("btnBack");

    btnback.addEventListener("click", function () {
        history.back();
    });

    // Save pass
    var btnSavepass = document.getElementById("btnSavepass");

    btnSavepass.addEventListener("click", function (event) {
        event.preventDefault();
        var newPassword = document.getElementById("txtNewpass").value;
        var confirmPassword = document.getElementById("txtConfirmpass").value;
        var error_message = document.getElementById("error_message");

        if (newPassword !== confirmPassword) {
            error_message.textContent = "Passwords do not match";
            return;
        }

        var passwordData = {
            newPassword: newPassword,
            confirmPassword: confirmPassword
        };

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/public/change-password", true);
        xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");

        xhr.onload = function () {
            if (xhr.status === 200) {
                error_message.textContent = "Password changed successfully";
            } else {
                error_message.textContent = "Failed to change password";
            }
        };

        xhr.send(JSON.stringify(passwordData));
    });
});
