const token = localStorage.getItem('token');
function authHeaders() {
    return {
        'Authorization': 'Bearer ' + token,
        'Content-Type': 'application/json'
    };
}

$('#submitFeedback').on('click', function (e) {
    e.preventDefault();

    if (!token) {
        Swal.fire({
            icon: 'warning',
            title: 'Login Required',
            text: 'Please login first to submit feedback.',
            confirmButtonText: 'Go to Login'
        }).then(() => {
            window.location.href = "signInPage.html"; // redirect to login page
        });
        return;
    }

    if (!$('#fullName').val() || !$('#email').val() || !$('#message').val()) {
        Swal.fire({
            icon: 'warning',
            title: 'Please Fill the Fields',
            text: 'Please fill required fields before submitting.'
        });
        return;
    }


    let feedBackData = {
        fullName: $('#fullName').val(),
        email: $('#email').val(),
        services: $('#service').val(),
        ratings: $('#ratings').val(),
        message: $('#message').val()
    };

    console.log("Sending feedback data:", feedBackData);

    $.ajax({
        url: "http://localhost:8080/api/v1/feedback/save",
        type: "POST",
        headers: authHeaders(),
        data: JSON.stringify(feedBackData),
        success: function (response) {
            Swal.fire({
                icon: 'success',
                title: 'Success!',
                text: 'Feedback submitted successfully!',
                showConfirmButton: false,
                timer: 2000
            });
            $('#feedbackform')[0].reset();
        },
        error: function (xhr) {
            if (xhr.status === 403) {
                Swal.fire({
                    icon: 'error',
                    title: 'Unauthorized',
                    text: 'You are not authorized. Please login.',
                    confirmButtonText: 'Login'
                }).then(() => {
                    window.location.href = "signInPage.html";
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: xhr.responseText || 'Something went wrong!',
                });
            }
        }
    });
});