$(document).ready(function () {
    const selectedName = localStorage.getItem("selectedMenuName");
    const selectedPrice = localStorage.getItem("selectedMenuPrice");

    if (selectedName && selectedPrice) {
        if ($("#menuName").length === 0) {
            $("#orderForm").prepend(`
                <div class="mb-3">
                    <label for="menuName" class="form-label">Menu Item</label>
                    <input type="text" class="form-control" id="menuName" value="${selectedName}" readonly>
                </div>
                <div class="mb-3">
                    <label for="menuPrice" class="form-label">Price</label>
                    <input type="text" class="form-control" id="menuPrice" value="Rs. ${selectedPrice}" readonly>
                </div>
            `);
        }
    }
});

$("#orderSave").on("click", function(e) {
    e.preventDefault();

    const token = localStorage.getItem("token");

    const price = parseFloat($("#menuPrice").val().replace("Rs.", "").trim());
    const qty = parseInt($("#qty").val());

    const total = price * qty;

    // Collect form values
    const orderData = {
        name: $("#menuName").val().trim(),
        price: parseFloat($("#menuPrice").val().replace("Rs.", "").trim()),
        orderType: $("#orderType").val().trim(),
        orderQty: parseInt($("#qty").val()),
        orderDatetime: $("#orderDatetime").val().trim(),
        status: 'pending',
        email: $("#email").val().trim(),
        total: total,
    };

    // Validation
    if (!orderData.name || !orderData.price || !orderData.orderType ||
        !orderData.orderQty || !orderData.orderDatetime || !orderData.email) {
        Swal.fire({
            icon: 'warning',
            title: 'Missing Fields',
            text: 'Please fill all required fields before placing the order.'
        });
        return;
    }

    // Confirmation dialog
    Swal.fire({
        title: 'Are you sure?',
        text: "Do you want to submit this order?",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, Submit Order',
        cancelButtonText: 'Cancel'
    }).then((result) => {
        if (result.isConfirmed) {
            // AJAX request only runs if user confirms
            $.ajax({
                url: "http://localhost:8080/api/v1/orders/place",
                type: "POST",
                contentType: "application/json",
                headers: {
                    "Authorization": "Bearer " + token
                },
                data: JSON.stringify(orderData),
                success: function(res) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Order Placed!',
                        text: 'Your order has been saved successfully.',
                        confirmButtonText: 'Proceed to Payment'
                    }).then(() => {
                        // Save total to session storage
                        sessionStorage.setItem("lastOrderTotal", total);

                        // Redirect to payment page
                        window.location.href = "payment.html";
                    });

                    console.log("Order saved:", res.data);

                    $("#orderForm")[0].reset();
                    localStorage.removeItem("selectedMenuName");
                    localStorage.removeItem("selectedMenuPrice");
                },
                error: function(xhr) {
                    console.error("Order save failed:", xhr);
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: 'Failed to place the order!'
                    });
                }
            });
        }
    });
});

$("#qty").on("input", function () {
    const price = parseFloat($("#menuPrice").val().replace("Rs.", "").trim());
    const qty = parseInt($("#qty").val()) || 0;
    const total = price * qty;

    if ($("#total").length === 0) {
        $("#orderForm .d-flex").before(`
        <div class="mb-3">
            <label for="total" class="form-label">Total</label>
            <input type="text" class="form-control" id="total" value="Rs. ${total}" readonly>
        </div>
    `);
    } else {
        $("#total").val("Rs. " + total);
    }

});