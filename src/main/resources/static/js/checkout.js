// const exceptionCode = 417;
var token = localStorage.getItem("token");
async function checkroleUser() {
    var token = localStorage.getItem("token");
    var url = 'http://localhost:8080/api/user/user/check-role-user';
    const response = await fetch(url, {
        method: 'GET',
        headers: new Headers({
            'Authorization': 'Bearer ' + token
        })
    });
    if (response.status > 300) {
        window.location.replace('login')
    }
}
var total = 0;

async function loadCartCheckOut() {
    var url = 'http://localhost:8080/api/cart/user/my-cart' ;
    const response = await fetch(url, {
        method: 'GET',
        headers: new Headers({
            'Authorization': 'Bearer ' + token
        })
    });
    var list = await response.json();
    if (list.length == 0) {
        alert("Bạn chưa có sản phẩm nào trong giỏ hàng!");
        window.location.replace("cart");
    }
    document.getElementById("slcartcheckout").innerHTML = list.length
    var main = ''
    for (i = 0; i < list.length; i++) {
        total += Number(list[i].quantity * list[i].size.price);
        main += `<div class="row">
                    <div class="col-lg-2 col-md-3 col-sm-3 col-3 colimgcheck">
                        <img src="${list[i].size.product.imageBanner}" class="procheckout">
                        <span class="slpro">${list[i].quantity}</span>
                    </div>
                    <div class="col-lg-7 col-md-6 col-sm-6 col-6">
                        <span class="namecheck">${list[i].size.product.name}</span>
                        <span class="colorcheck">Kích thước: ${list[i].size.name}</span>
                    </div>
                    <div class="col-lg-3 col-md-3 col-sm-3 col-3 pricecheck">
                        <span>${formatmoneyCheck(list[i].quantity * list[i].size.price)}</span>
                    </div>
                </div>`
    }
    document.getElementById("listproductcheck").innerHTML = main;
    document.getElementById("totalAmount").innerHTML = formatmoneyCheck(total);
    document.getElementById("totalfi").innerHTML = formatmoneyCheck(total);
}

function formatmoneyCheck(money) {
    const VND = new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND',
    });
    return VND.format(money);
}


function checkout() {
    var con = confirm("Xác nhận đặt hàng!");
    if (con == false) {
        return;
    }
    var paytype = $('input[name=paytype]:checked').val()
    if (paytype == "momo") {
        requestPayMentMomo()
    }
    if (paytype == "cod") {
        paymentCod();
    }
}

async function requestPayMentMomo() {
    window.localStorage.setItem('ghichudonhang', document.getElementById("ghichudonhang").value);
    window.localStorage.setItem('sodiachi', document.getElementById("sodiachi").value);
    var returnurl = 'http://localhost:8080/payment';
    var urlinit = 'http://localhost:8080/api/urlpayment';
    var paymentDto = {
        "content": "thanh toán đơn hàng shoe store",
        "returnUrl": returnurl,
        "notifyUrl": returnurl,
    }
    console.log(paymentDto)
    const res = await fetch(urlinit, {
        method: 'POST',
        headers: new Headers({
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json'
        }),
        body: JSON.stringify(paymentDto)
    });
    var result = await res.json();
    if (res.status < 300) {
        window.open(result.url, '_blank');
    }
    if (res.status == exceptionCode) {
        toastr.warning(result.defaultMessage);
    }

}

async function paymentMomo() {
    var uls = new URL(document.URL)
    var orderId = uls.searchParams.get("orderId");
    var requestId = uls.searchParams.get("requestId");
    var note = window.localStorage.getItem("ghichudonhang");
    var orderDto = {
        "payType": "PAYMENT_MOMO",
        "userAddressId": window.localStorage.getItem("sodiachi"),
        "note": note,
        "requestIdMomo": requestId,
        "orderIdMomo": orderId,
    }
    var url = 'http://localhost:8080/api/invoice/user/create';
    var token = localStorage.getItem("token");
    const res = await fetch(url, {
        method: 'POST',
        headers: new Headers({
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json'
        }),
        body: JSON.stringify(orderDto)
    });
    var result = await res.json();
    if (res.status < 300) {
        document.getElementById("thanhcong").style.display = 'block'
    }
    if (res.status == exceptionCode) {
        document.getElementById("thatbai").style.display = 'block'
        document.getElementById("thanhcong").style.display = 'none'
        document.getElementById("errormess").innerHTML = result.defaultMessage
    }

}



async function paymentCod() {
    var note = document.getElementById("ghichudonhang").value;
    var orderDto = {
        "payType": "PAYMENT_DELIVERY",
        "userAddressId": document.getElementById("sodiachi").value,
        "note": note,
    }
    var url = 'http://localhost:8080/api/invoice/user/create';
    var token = localStorage.getItem("token");
    const res = await fetch(url, {
        method: 'POST',
        headers: new Headers({
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json'
        }),
        body: JSON.stringify(orderDto)
    });
    if (res.status < 300) {
        swal({
                title: "Thông báo",
                text: "Đặt hàng thành công!",
                type: "success"
            },
            function() {
                window.location.replace("account#invoice")
            });
    }
}