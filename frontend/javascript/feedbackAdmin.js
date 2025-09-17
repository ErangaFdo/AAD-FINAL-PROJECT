
const token = localStorage.getItem("token");

$(document).ready(function () {
    loadFeedback();
});

function authHeaders() {
    return {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
    };
}


// let currentPage = 0;
// const pageSize = 8;

function loadFeedback(page = 0) {
    $.ajax({
        url: `http://localhost:8080/api/v1/feedback/all`,
        type: 'GET',
        headers: authHeaders(),
        success: function (res) {
            const feedbacks = res.data || [];
            let rows = "";
            feedbacks.forEach(feedback => {
                rows += `
                    <tr>
                        
                        <td>${feedback.fullName}</td>
                        <td>${feedback.email}</td>
                        <td>${feedback.message}</td>
                        <td>${feedback.ratings}</td>
                        <td>${feedback.services}</td>
                    </tr>`;
            });

            // currentPage = page;
            $('#feedbackTableBody').html(rows);
        },
        error: function (err) {
            console.error(err);
            if (err.status === 401) {
                alert("Unauthorized! Please login again.");
            } else {
                alert("Failed to load feedback");
            }
        }
    });
}
