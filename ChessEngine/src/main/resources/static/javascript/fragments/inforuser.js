document.addEventListener("DOMContentLoaded", function () {
    // Load image
    var btnChange = document.getElementById("btnChange");
    var fileInput = document.getElementById("fileInput");

    btnChange.addEventListener("click", function () {
        fileInput.click();
    });

    fileInput.addEventListener("change", function () {

        var selectedFile = fileInput.files[0];

        if (selectedFile) {
            var reader = new FileReader();

            reader.onload = function (e) {
                var imageDataUrl = e.target.result;

                document.getElementById("img-user").src = imageDataUrl;
                saveImageToDatabase(imageDataUrl);
            };
            reader.readAsDataURL(selectedFile);
        }
    });
    function saveImageToDatabase(imageDataUrl) {
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/public/save-image", true);
        xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");

        var imageData = { imagePath: imageDataUrl };

        xhr.onload = function () {
            if (xhr.status == 200) {
                console.log("Image path saved successfully");
            } else {
                console.error("Failed to save image path");
            }
        };

        xhr.send(JSON.stringify(imageData));
    }
    // Save Information
    var btnSave = document.getElementById("btnSave");

    btnSave.addEventListener("click", function (event) {
        event.preventDefault();
        var email = document.getElementById("txtEmail").value
        var name = document.getElementById("txtName").value;
        var dateBirth = document.getElementById("dateBirth").value;
        var gender = document.querySelector('input[name="rdoMale"]:checked').value;
        var error = document.getElementById("error_message");
        var userData = {
            email: email,
            name: name,
            dateBirth: dateBirth,
            gender: gender,
        };

        // var formattedDate = new Date(dateBirth).toISOString().split('T')[0];
        // userData.dateBirth = formattedDate;
        console.log(userData);
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/public/update-user", true);
        xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        xhr.onload = function () {
            if (xhr.status == 200) {
                error.textContent = "User information updated successfully";
            } else {
                error.textContent = "Failed to update user information";
            }
        };

        xhr.send(JSON.stringify(userData));
    });

    // Change Password
    var btnChangePassword = document.getElementById("btnChangePassword");

    btnChangePassword.addEventListener("click", function () {
        loadChangePasswordForm();
    });

    function loadChangePasswordForm() {
        var xhr = new XMLHttpRequest();
        xhr.open("GET", "/public/changepassword", true);

        xhr.onload = function () {
            if (xhr.status == 200) {
                document.getElementById("right-content").innerHTML = xhr.responseText;
            } else {
                console.error("Failed to load change password form");
            }
        };

        xhr.send();
    }

    // Back
    var btnback = document.getElementById("btnBack");

    btnback.addEventListener("click", function () {

    });


});
