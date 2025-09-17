// // Sidebar navigation
// const links = document.querySelectorAll('.sidebar .nav-link');
// const sections = document.querySelectorAll('.content-section');
// const pageTitle = document.getElementById('pageTitle');
//
// links.forEach(link => {
//     link.addEventListener('click', (e) => {
//         e.preventDefault();
//         links.forEach(l => l.classList.remove('active'));
//         link.classList.add('active');
//         sections.forEach(sec => sec.classList.add('d-none'));
//         const targetId = link.getAttribute('data-target');
//         document.getElementById(targetId).classList.remove('d-none');
//         pageTitle.textContent = targetId.charAt(0).toUpperCase() + targetId.slice(1);
//     });
// });

// $(document).ready(function () {
//     loadMenus()
// });
//
// function logout() {
//     // optional: localStorage / token clear karanna
//     localStorage.removeItem("token");
//
//     // redirect to signin page
//     window.location.href = "signin.html";
// }
//
// // product card
//
// let currentMenuId = null;
// const menuModal = new bootstrap.Modal(document.getElementById('menuModal'));
//
//
// function escapeString(str) {
//     if (!str) return '';
//     return String(str)
//         .replace(/&/g, '&amp;')
//         .replace(/"/g, '&quot;')
//         .replace(/'/g, '&#39;')
//         .replace(/</g, '&lt;')
//         .replace(/>/g, '&gt;');
// }
//
// let currentPageAdmin = 0;
// const pageSizeAdmin = 4;
//
// function loadMenus() {
//     $.ajax({
//         url: `http://localhost:8080/api/v1/product/paginated?page=${currentPageAdmin}&size=${pageSizeAdmin}`,
//         type: "GET",
//         headers: authHeaders(),
//         success: function (res) {
//             if (res.status === 200 && res.data && res.data.length > 0) {
//                 $("#emptyState").hide();
//                 $("#itemContainer").empty();
//
//                 res.data.forEach(product => {
//                     const imageUrl = `http://localhost:8080/api/v1/product/images/${encodeURIComponent(product.imageUrl)}`;
//
//                     let card = `
//                     <div class="col-md-3 mb-4">
//                       <div class="card shadow-sm rounded-4 h-100">
//                         <img src="${imageUrl}"
//                              class="card-img-top"
//                              alt="${escapeString(product.name)}"
//                              onerror="this.onerror=null; this.src='https://via.placeholder.com/286x180?text=Image+not+found';">
//                         <div class="action-buttons">
//                             <button class="btn btn-sm btn-primary me-1 edit-btn"
//                                     data-id="${product.productId}"
//                                     data-name="${escapeString(product.name)}"
//                                     data-desc="${escapeString(product.description)}"
//                                     data-cat="${escapeString(product.category)}"
//                                     data-price="${product.price}"
//                                     data-img="${escapeString(product.imageUrl)}">
//                               <i class="fas fa-edit"></i>
//                             </button>
//                             <button class="btn btn-sm btn-danger" onclick="deleteMenuItem(${product.productId})">
//                               <i class="fas fa-trash"></i>
//                             </button>
//                         </div>
//                         <div class="card-body">
//                           <h5 class="card-title">${escapeString(product.name)}</h5>
//                           <p class="card-text text-muted">${escapeString(product.description)}</p>
//                           <p class="fw-bold">Rs. ${product.price.toFixed(2)}</p>
//                           <span class="badge bg-info">${escapeString(product.category)}</span>
//                         </div>
//                       </div>
//                     </div>
//                     `;
//
//                     $("#itemContainer").append(card);
//
//                 });
//                 loadPagination()
//
//             } else {
//                 $("#emptyState").show();
//             }
//         },
//         error: function (xhr, status, error) {
//             console.error("Error loading menus", error);
//             if (xhr.status === 403 || xhr.status === 401) {
//                 Swal.fire({
//                     icon: 'error',
//                     title: 'Authentication Error',
//                     text: 'Your session has expired. Please login again.',
//                     confirmButtonText: 'Go to Login'
//                 }).then(() => {
//                     window.location.href = 'login.html';
//                 });
//             } else {
//                 Swal.fire("Error", "Failed to load product!", "error");
//             }
//         }
//     });
// }
//
// function loadPagination() {
//     $.ajax({
//         url: `http://localhost:8080/api/v1/product/total-pages?size=${pageSizeAdmin}`,
//         method: "GET",
//         success: function (totalPages) {
//             let paginationHTML = "";
//             for (let i = 0; i < totalPages; i++) {
//                 paginationHTML += `
//                     <li class="page-item ${i === currentPageAdmin ? 'active' : ''}">
//                         <a class="page-link" href="#" onclick="goToPage(${i})">${i + 1}</a>
//                     </li>
//                 `;
//             }
//             $('#productPagination').html(paginationHTML);
//         },
//         error: function (xhr) {
//             console.error("Error loading pagination:", xhr.responseText);
//         }
//     });
// }
//
// function goToPage(page) {
//     currentPageAdmin = page;
//     loadMenus();
// }
//
//
// $('#saveItemBtn').click(function() {
//     saveMenuItem();
// });
//
// $(document).on('click', '.edit-btn', function() {
//     const btn = $(this);
//     editMenuItem(
//         btn.data('id'),
//         btn.data('name'),
//         btn.data('desc'),
//         btn.data('cat'),
//         btn.data('price'),
//         btn.data('img')
//     );
// });
//
//
// function saveMenuItem() {
//     // Validate form
//     if (!$('#name').val() || !$('#description').val() || !$('#category').val() || !$('#price').val()) {
//         Swal.fire("Error", "Please fill all required fields!", "error");
//         return;
//     }
//
//     // Validate image for new items
//     if (!currentMenuId && !$('#image')[0].files[0]) {
//         Swal.fire("Error", "Please select an image!", "error");
//         return;
//     }
//
//     const formData = new FormData();
//     formData.append('name', $('#name').val());
//     formData.append('description', $('#description').val());
//     formData.append('category', $('#category').val());
//     formData.append('price', $('#price').val());
//
//     // Only append file if selected
//     if ($('#image')[0].files[0]) {
//         formData.append('file', $('#image')[0].files[0]);
//     }
//
//     // If editing, include the ID
//     if (currentMenuId) {
//         formData.append('id', currentMenuId);
//     }
//
//     const url = currentMenuId
//         ? "http://localhost:8080/api/v1/product/updateItem"
//         : "http://localhost:8080/api/v1/product/addItem";
//
//     // Show loading state
//     $('#saveSpinner').removeClass('d-none');
//     $('#saveButtonText').text(currentMenuId ? 'Updating...' : 'Saving...');
//     $('#saveItemBtn').prop('disabled', true);
//
//     $.ajax({
//         url: url,
//         type: currentMenuId ? "PUT" : "POST",
//         headers: {
//             'Authorization': 'Bearer ' + token
//         },
//         data: formData,
//         processData: false,
//         contentType: false,
//         success: function (response) {
//             Swal.fire("Success", "Product item saved successfully!", "success");
//             menuModal.hide();
//             // resetForm();
//             loadMenus();
//         },
//         error: function (xhr, status, error) {
//             console.error("Error saving menu", error);
//             if (xhr.status === 500) {
//                 Swal.fire("Error", "Server error. Please check if the image format is supported and try again.", "error");
//             } else {
//                 Swal.fire("Error", "Failed to save product item!", "error");
//             }
//         },
//         complete: function() {
//             // Reset button state
//             $('#saveSpinner').addClass('d-none');
//             $('#saveButtonText').text(currentMenuId ? 'Update Item' : 'Save Item');
//             $('#saveItemBtn').prop('disabled', false);
//         }
//     });
// }
//
// function editMenuItem(id, name, description, category, price, imageUrl) {
//     currentMenuId = id;
//     $('#menuId').val(id);
//     $('#name').val(name);
//     $('#description').val(description);
//     $('#category').val(category);
//     $('#price').val(price);
//
//     const fullImageUrl = `http://localhost:8080/api/v1/product/images/${escapeString(imageUrl)}`;
//
//     // Show current image
//     $('#imagePreview').html(`
//       <div class="alert alert-info mt-2">
//         <i class="fas fa-info-circle me-2"></i>Current image: ${escapeString(imageUrl)}
//       </div>
//       <img src="${fullImageUrl}"
//            class="img-thumbnail mt-2" style="max-height: 200px;"
//            onerror="this.style.display='none'">
//     `);
//
//     // Make image optional for editing
//     $('#image').removeAttr('required');
//
//     // Change modal title for editing
//     $('#menuModalLabel').text('Edit product Item');
//     $('#saveButtonText').text('Update Item');
//
//     menuModal.show();
// }
//
// function deleteMenuItem(id) {
//     Swal.fire({
//         title: 'Are you sure?',
//         text: "This product item will be permanently deleted!",
//         icon: 'warning',
//         showCancelButton: true,
//         confirmButtonColor: '#d33',
//         cancelButtonColor: '#3085d6',
//         confirmButtonText: 'Yes, delete it!'
//     }).then((result) => {
//         if (result.isConfirmed) {
//             $.ajax({
//                 url: `http://localhost:8080/api/v1/product/delete/${id}`,
//             type: "DELETE",
//                 headers: authHeaders(),
//                 success: function (response) {
//                 Swal.fire("Deleted!", "Product item has been deleted.", "success");
//                 loadMenus();
//             },
//             error: function (xhr, status, error) {
//                 console.error("Error deleting menu", error);
//                 if (xhr.status === 403 || xhr.status === 401) {
//                     Swal.fire("Error", "You don't have permission to delete menu items.", "error");
//                 } else if (xhr.status === 500) {
//                     Swal.fire("Error", "Server error. Please try again later.", "error");
//                 } else {
//                     Swal.fire("Error", "Failed to delete menu item!", "error");
//                 }
//             }
//         });
//         }
//     });
// }