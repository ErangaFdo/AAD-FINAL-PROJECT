
const token = localStorage.getItem('token');

$(document).ready(function () {
    loadUser(currentPage);
});
function authHeaders() {
    return {
        'Authorization': 'Bearer ' + token,
        'Content-Type': 'application/json'
    };
}



let currentPage = 0;
const pageSize = 8;

function loadUser( page=0,) {
    $.ajax({
        url: `http://localhost:8080/api/v1/user/paginated?page=${page}&size=${pageSize}`,
        type: 'GET',
        headers: authHeaders(),
        success: function (res) {
            const users = res.data || []; // <-- important
            let rows = "";
            users.forEach(user => {
                rows +=
                    `<tr>
                     <td>${user.userName}</td>
                     <td>${user.emailAddress}</td>
                     <td>${user.userPassword}</td>
                     <td>${user.role}</td>
                     <td>
<!--                         <button class="btn btn-warning me-2">Edit</button>-->
                         <button class="btn btn-sm btn-danger" onclick="deleteUser('${user.userId}')">Delete</button>
                     </td>
                  </tr>`;
            });

            currentPage = page
            loadPagination(page)

            $('#userTableBody').html(rows);

        },
        error: function(err){
            console.log(err);
            alert("Failed to load customers");
        }
    });
}



function deleteUser(userId) {
    if(!confirm("Are you sure you want to delete this customer?")) return;

    $.ajax({
        url: `http://localhost:8080/api/v1/user/delete/${userId}`,
        type: "DELETE",
        headers: authHeaders(),
        success: function(response) {
            alert("Customer deleted successfully!");
            loadUser();
        },
        error: function(xhr) {
            alert("Error deleting customer: " + xhr.responseText);
        }
    });
}

function loadPagination(current) {
    $.ajax({
        url: `http://localhost:8080/api/v1/user/total-pages?size=${pageSize}`,
        method: 'GET',
        success: function (totalPages) {
            const pagination = $('.pagination');
            pagination.empty();

            for (let i = 0; i < totalPages; i++) {
                const activeClass = i === current ? 'active' : '';
                const pageBtn = `
                    <li class="page-item ${activeClass}">
                        <a class="page-link" href="#" onclick="loadUser(${i})">${i + 1}</a>
                    </li>`;
                pagination.append(pageBtn);
            }
        }
    });
}


$('#searchForm').on('submit', function (e) {
    e.preventDefault(); // form reload avoid

    const keyword = $('#searchInput').val().trim();
    if (!keyword) {
        loadUser(0);
        return;
    }

    $.ajax({
        url: `http://localhost:8080/api/v1/user/search/${keyword}`,
        method: 'GET',
        headers: authHeaders(),
        success: function (res) {
            const users = res.data || [];
            let rows = "";

            if (users.length === 0) {
                rows = `<tr><td colspan="5" class="text-center">No results found</td></tr>`;
            } else {
                users.forEach(user => {
                    rows += `
                        <tr>
                            <td>${user.userName}</td>
                            <td>${user.emailAddress}</td>
                            <td>${user.userPassword}</td>
                            <td>${user.role}</td>
                            <td>
                                <button class="btn btn-sm btn-danger" onclick="deleteUser('${user.userId}')">Delete</button>
                            </td>
                        </tr>`;
                });
            }

            $('#userTableBody').html(rows);

            // hide pagination when searching
            $('.pagination').empty();
        },
        error: function (err) {
            console.log(err);
            alert("Search failed!");
        }
    });
});
