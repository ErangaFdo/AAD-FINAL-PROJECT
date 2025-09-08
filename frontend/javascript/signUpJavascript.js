function togglePassword() {
    const passwordField = document.getElementById('password');
    const toggleIcon = document.getElementById('toggleIcon');

    if (passwordField.type === 'password') {
        passwordField.type = 'text';
        toggleIcon.className = 'bi bi-eye-slash';
    } else {
        passwordField.type = 'password';
        toggleIcon.className = 'bi bi-eye';
    }
}

$('#signUp').on('click', function () {
    var username = $('#fullName').val();
    var email = $('#emailAddress').val();
    var password = $('#password').val();

    if (username && email && password) {
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/auth/register',
            data: JSON.stringify({
                userName: username,
                emailAddress: email,
                userPassword: password,
                role: "USER"
            }),
            contentType: 'application/json',
            success: function (response) {
                alert(response.message);
                window.location.href = 'signin.html';
            },
            error: function (xhr) {
                let errorMessage = xhr.responseJSON?.message || 'Error occurred';
                alert(errorMessage);
            }
        });
    } else {
        alert('Please fill in all fields');
    }
});
