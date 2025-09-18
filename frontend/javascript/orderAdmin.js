const token = localStorage.getItem('token');

function authHeaders() {
    return {
        'Authorization': 'Bearer ' + token,
        'Content-Type': 'application/json'
    };
}

$(document).ready(function () {
    loadOder();
});

// let currentPage = 0;
// const pageSize = 8;

function loadOder() {
    $.ajax({
        url: `http://localhost:8080/api/v1/orders/all`,
        type: 'GET',
        headers: authHeaders(),
        success: function (res) {
            const orders = res.data || [];
            let rows = "";
            orders.forEach(order => {
                rows +=
                    `<tr>
                     <td>${order.name}</td>
                     <td>${order.email}</td>
                     <td>${order.orderDatetime}</td>
                     <td>${order.orderQty}</td>
                     <td>${order.orderType}</td>
                     <td>${order.price}</td>
                     <td>${order.status}</td>
                     <td>${order.total}</td>
                     <td><button class="btn btn-success btn-sm mark-delivered" data-id="${order.orderId}">Complete</button></td>
                  </tr>`;
            });

            $('#orderTableBody').html(rows);
            // loadPagination();
        },
        error: function(err){
            console.log(err);
            alert("Failed to load Orders");
        }
    });
}


$(document).on("click", ".mark-delivered", function() {
    const orderId = $(this).data("id");
    const token = localStorage.getItem("token");

    Swal.fire({
        title: 'Change Status?',
        text: "Do you want to mark this order as complete?",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, Complete',
        cancelButtonText: 'Cancel'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: `http://localhost:8080/api/v1/orders/status/${orderId}`,
            type: "PATCH",
                headers: {
                "Authorization": "Bearer " + token
            },
            success: function(res) {
                Swal.fire({
                    icon: 'success',
                    title: 'Status Updated!',
                    text: 'Order has been marked as complete.'
                });

                loadOder();
            },
            error: function(xhr) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Failed to update status!'
                });
            }
        });
        }
    });
});

// function loadPagination() {
//     $.ajax({
//         url: `http://localhost:8080/api/v1/orders/total-pages?size=${pageSize}`,
//         method: "GET",
//         headers: authHeaders(),
//         success: function (totalPages) {
//             let paginationHTML = "";
//             for (let i = 0; i < totalPages; i++) {
//                 paginationHTML += `
//                     <li class="page-item ${i === currentPage ? 'active' : ''}">
//                         <a class="page-link" href="#" onclick="goToPage(${i})">${i + 1}</a>
//                     </li>
//                 `;
//             }
//             $('.pagination').html(paginationHTML);
//         },
//         error: function (xhr) {
//             console.error("Error loading pagination:", xhr.responseText);
//         }
//     });
// }
//
// function goToPage(page) {
//     currentPage = page;
//     loadOder();
// }
