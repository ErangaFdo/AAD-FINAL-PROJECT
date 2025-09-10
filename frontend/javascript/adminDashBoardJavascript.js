// Sidebar navigation
const links = document.querySelectorAll('.sidebar .nav-link');
const sections = document.querySelectorAll('.content-section');
const pageTitle = document.getElementById('pageTitle');

links.forEach(link => {
    link.addEventListener('click', (e) => {
        e.preventDefault();
        links.forEach(l => l.classList.remove('active'));
        link.classList.add('active');
        sections.forEach(sec => sec.classList.add('d-none'));
        const targetId = link.getAttribute('data-target');
        document.getElementById(targetId).classList.remove('d-none');
        pageTitle.textContent = targetId.charAt(0).toUpperCase() + targetId.slice(1);
    });
});

$(document).ready(function () {
    loadMenus()
});

function logout() {
    // optional: localStorage / token clear karanna
    localStorage.removeItem("token");

    // redirect to signin page
    window.location.href = "signin.html";
}


const token = localStorage.getItem('token');
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

$(document).ready(function () {
    loadUser(currentPage);
});

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


// product card

let currentMenuId = null;
const menuModal = new bootstrap.Modal(document.getElementById('menuModal'));


function escapeString(str) {
    if (!str) return '';
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;');
}

function loadMenus() {
    $.ajax({
        url: 'http://localhost:8080/api/v1/product/all',
        type: "GET",
        headers: authHeaders(),
        success: function (res) {
            if (res.status === 200 && res.data && res.data.length > 0) {
                $("#emptyState").hide();
                $("#itemContainer").empty();
                res.data.forEach(product => {
                    const imageUrl = `http://localhost:8080/api/v1/product/images/${escapeString(product.imageUrl)}`;

                    let card = `
                <div class="col-md-4 mb-4">
                  <div class="card shadow-sm rounded-4 h-100">
                    <img src="${imageUrl}"
                         class="card-img-top"
                         alt="${escapeString(product.name)}"
                         onerror="this.onerror=null; this.src='https://via.placeholder.com/286x180?text=Image+not+found';">
                    <div class="action-buttons">
                        <button class="btn btn-sm btn-primary me-1 edit-btn"
                                data-id="${product.productId}"
                                data-name="${escapeString(product.name)}"
                                data-desc="${escapeString(product.description)}"
                                data-cat="${escapeString(product.category)}"
                                data-price="${product.price}"
                                data-img="${escapeString(product.imageUrl)}">
                          <i class="fas fa-edit"></i>
                        </button>
                      </button>
                      <button class="btn btn-sm btn-danger" onclick="deleteMenuItem(${product.productId})">
                        <i class="fas fa-trash"></i>
                      </button>
                    </div>
                    <div class="card-body">
                      <h5 class="card-title">${escapeString(product.name)}</h5>
                      <p class="card-text text-muted">${escapeString(product.description)}</p>
                      <p class="fw-bold">Rs. ${product.price.toFixed(2)}</p>
                      <span class="badge bg-info">${escapeString(product.category)}</span>
                    </div>
                  </div>
                </div>
              `;
                    $("#itemContainer").append(card);
                });
                // loadPagination()
            } else {
                $("#emptyState").show();
            }
        },
        error: function (xhr, status, error) {
            console.error("Error loading menus", error);
            if (xhr.status === 403 || xhr.status === 401) {
                Swal.fire({
                    icon: 'error',
                    title: 'Authentication Error',
                    text: 'Your session has expired. Please login again.',
                    confirmButtonText: 'Go to Login'
                }).then(() => {
                    window.location.href = 'login.html';
                });
            } else {
                Swal.fire("Error", "Failed to load product!", "error");
            }
        }
    });
}

$('#saveItemBtn').click(function() {
    saveMenuItem();
});

$(document).on('click', '.edit-btn', function() {
    const btn = $(this);
    editMenuItem(
        btn.data('id'),
        btn.data('name'),
        btn.data('desc'),
        btn.data('cat'),
        btn.data('price'),
        btn.data('img')
    );
});


function saveMenuItem() {
    // Validate form
    if (!$('#name').val() || !$('#description').val() || !$('#category').val() || !$('#price').val()) {
        Swal.fire("Error", "Please fill all required fields!", "error");
        return;
    }

    // Validate image for new items
    if (!currentMenuId && !$('#image')[0].files[0]) {
        Swal.fire("Error", "Please select an image!", "error");
        return;
    }

    const formData = new FormData();
    formData.append('name', $('#name').val());
    formData.append('description', $('#description').val());
    formData.append('category', $('#category').val());
    formData.append('price', $('#price').val());

    // Only append file if selected
    if ($('#image')[0].files[0]) {
        formData.append('file', $('#image')[0].files[0]);
    }

    // If editing, include the ID
    if (currentMenuId) {
        formData.append('id', currentMenuId);
    }

    const url = currentMenuId
        ? "http://localhost:8080/api/v1/product/updateItem"
        : "http://localhost:8080/api/v1/product/addItem";

    // Show loading state
    $('#saveSpinner').removeClass('d-none');
    $('#saveButtonText').text(currentMenuId ? 'Updating...' : 'Saving...');
    $('#saveItemBtn').prop('disabled', true);

    $.ajax({
        url: url,
        type: currentMenuId ? "PUT" : "POST",
        headers: {
            'Authorization': 'Bearer ' + token
        },
        data: formData,
        processData: false,
        contentType: false,
        success: function (response) {
            Swal.fire("Success", "Product item saved successfully!", "success");
            menuModal.hide();
            // resetForm();
            loadMenus();
        },
        error: function (xhr, status, error) {
            console.error("Error saving menu", error);
            if (xhr.status === 500) {
                Swal.fire("Error", "Server error. Please check if the image format is supported and try again.", "error");
            } else {
                Swal.fire("Error", "Failed to save product item!", "error");
            }
        },
        complete: function() {
            // Reset button state
            $('#saveSpinner').addClass('d-none');
            $('#saveButtonText').text(currentMenuId ? 'Update Item' : 'Save Item');
            $('#saveItemBtn').prop('disabled', false);
        }
    });
}

function editMenuItem(id, name, description, category, price, imageUrl) {
    currentMenuId = id;
    $('#menuId').val(id);
    $('#name').val(name);
    $('#description').val(description);
    $('#category').val(category);
    $('#price').val(price);

    const fullImageUrl = `http://localhost:8080/api/v1/product/images/${escapeString(imageUrl)}`;

    // Show current image
    $('#imagePreview').html(`
      <div class="alert alert-info mt-2">
        <i class="fas fa-info-circle me-2"></i>Current image: ${escapeString(imageUrl)}
      </div>
      <img src="${fullImageUrl}"
           class="img-thumbnail mt-2" style="max-height: 200px;"
           onerror="this.style.display='none'">
    `);

    // Make image optional for editing
    $('#image').removeAttr('required');

    // Change modal title for editing
    $('#menuModalLabel').text('Edit product Item');
    $('#saveButtonText').text('Update Item');

    menuModal.show();
}

function deleteMenuItem(id) {
    Swal.fire({
        title: 'Are you sure?',
        text: "This product item will be permanently deleted!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: `http://localhost:8080/api/v1/product/delete/${id}`,
            type: "DELETE",
                headers: authHeaders(),
                success: function (response) {
                Swal.fire("Deleted!", "Product item has been deleted.", "success");
                loadMenus();
            },
            error: function (xhr, status, error) {
                console.error("Error deleting menu", error);
                if (xhr.status === 403 || xhr.status === 401) {
                    Swal.fire("Error", "You don't have permission to delete menu items.", "error");
                } else if (xhr.status === 500) {
                    Swal.fire("Error", "Server error. Please try again later.", "error");
                } else {
                    Swal.fire("Error", "Failed to delete menu item!", "error");
                }
            }
        });
        }
    });
}