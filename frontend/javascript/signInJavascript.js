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

$('#signIn').on('click', function () {
    var username = $('#fullName').val();
    var password = $('#password').val();

    if (username && password) {
        $.ajax({
            method: 'POST',
            url: 'http://localhost:8080/auth/login',
            contentType: 'application/json',
            data: JSON.stringify({
                username: username,
                password: password
            }),
            success: function(response) {
                console.log("Login API Response:", response);

                const token = response.data?.accessToken;
                if (token) {
                    localStorage.setItem('username', username);
                    localStorage.setItem('token', token);

                    $.ajax({
                        method: 'GET',
                        url: 'http://localhost:8080/hello/api/user-info',
                        headers: {
                            'Authorization': 'Bearer ' + token
                        },
                        success: function(userInfo) {
                            const role = userInfo.role;
                            if (role === 'ADMIN') {
                                console.log("ADMIN Token:", token);
                                alert('Successfully Login Admin');
                                window.location.href = 'adminDashBoard.html';
                            } else if (role === 'USER') {
                                console.log("USER Token:", token);
                                alert('Successfully Login User');
                                window.location.href = 'index.html';
                            } else {
                                alert('Unknown user role!');
                            }
                        },
                        error: function () {
                            alert('Session expired or unauthorized');
                        }
                    });
                } else {
                    alert('Login failed: No token received');
                }
            },
            error: function (xhr) {
                let errorMessage = xhr.responseJSON?.message || 'Login failed. Please check your username and password.';
                alert(errorMessage);
            }
        });
    } else {
        alert('Please enter both username and password');
    }
});
