
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
        url: `http://localhost:8080/api/v1/payment/all`,
        type: 'GET',
        headers: authHeaders(),
        success: function (res) {
            const payments = res.data || [];
            let rows = "";
            payments.forEach(payment => {
                rows +=
                    `<tr>
                     <td>${payment.cardHolderName}</td>
                     <td>${payment.cardNumber}</td>
                     <td>${payment.cvv}</td>
                     <td>${payment.email}</td>
                     <td>${payment.expireDate}</td>
                     
                     
                   
                  </tr>`;
            });

            $('#paymentTableBody').html(rows);
            // loadPagination();
        },
        error: function(err){
            console.log(err);
            alert("Failed to load payments");
        }
    });
}
