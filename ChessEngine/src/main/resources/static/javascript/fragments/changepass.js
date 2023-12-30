document.addEventListener("DOMContentLoaded", function () {
    // Save pass
    var btnSavepass = document.getElementById("btnSavepass");
    btnSavepass.addEventListener("click", function (event) {
        event.preventDefault(); // Prevent the default form submission
        var newPassword = document.getElementById("txtNewpass").value;
        var confirmPassword = document.getElementById("txtConfirmpass").value;
        var error_message = document.getElementById("error_message");
        if (newPassword !== confirmPassword) {
            error_message.textContent = "Passwords do not match";
            return;
        }
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/public/change-password", true);
        xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        xhr.onload = function () {
            if (xhr.status === 200) {
                console.log("Password changed successfully");
                window.location.href = '/public/inforuser'; // Redirect on success
            } else {
                console.error("Failed to change password");
                error_message.textContent = xhr.responseText; // Display error message from server
            }
        };
        var passwordData = {
            newPassword: newPassword,
            confirmPassword: confirmPassword
        };
        xhr.send(JSON.stringify(passwordData));
    });
    // Back
    var btnBack = document.getElementById("btnBack");
    btnBack.addEventListener("click", function (event) {
        event.preventDefault();
        window.location.href = '/public/inforuser'; // Correct redirect
    });
});
