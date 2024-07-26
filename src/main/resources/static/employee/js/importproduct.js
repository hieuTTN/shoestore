async function loadAllProductSelect() {
    var url = 'http://localhost:8080/api/product/public/findAll-list';
    const response = await fetch(url, {
        method: 'GET'
    });
    var list = await response.json();
    var main = '<option selected value="-1">Tất cả sản phẩm</option>'
    for (i = 0; i < list.length; i++) {
        main += `<option value="${list[i].id}">${list[i].name}</option>`
    }
    document.getElementById("sanpham").innerHTML = main
    const ser = $("#sanpham");
    ser.select2({
        placeholder: "Chọn sản phẩm",
    });
}

var listPro = [];
async function loadAllProductSelectAdd() {
    var url = 'http://localhost:8080/api/product/public/findAll-list';
    const response = await fetch(url, {
        method: 'GET'
    });
    var list = await response.json();
    listPro = list;
    var main = ''
    for (i = 0; i < list.length; i++) {
        main += `<option value="${list[i].id}">${list[i].name}</option>`
    }
    document.getElementById("sanpham").innerHTML = main
    const ser = $("#sanpham");
    ser.select2({
        placeholder: "Chọn sản phẩm",
    });
}

async function loadAllNhaCungCapAdd() {
    var url = 'http://localhost:8080/api/provider/employee/findAll';
    const response = await fetch(url, {
        method: 'GET',
        headers: new Headers({
            'Authorization': 'Bearer ' + token
        }),
    });
    var list = await response.json();
    listPro = list;
    var main = ''
    for (i = 0; i < list.length; i++) {
        main += `<option value="${list[i].id}">${list[i].name}</option>`
    }
    document.getElementById("nhacungcap").innerHTML = main
}

async function loadSize() {
    var idPro = document.getElementById("sanpham").value;
    var url = 'http://localhost:8080/api/size/public/find-by-product?idProduct='+idPro;
    const response = await fetch(url, {
        method: 'GET'
    });
    var list = await response.json();
    var main = ''
    for (i = 0; i < list.length; i++) {
        main += `<option value="${list[i].id}">${list[i].name}</option>`
    }
    document.getElementById("sizes").innerHTML = main
    for (i = 0; i < listPro.length; i++) {
        if(listPro[i].id == idPro){
            document.getElementById("gianhap").value = listPro[i].price
        }
    }
}

async function loadImportProduct() {
    $('#example').DataTable().destroy();
    var url = 'http://localhost:8080/api/import-product/admin/findByProductAndDate?oke=2';
    const response = await fetch(url, {
        method: 'GET',
        headers: new Headers({
            'Authorization': 'Bearer ' + token
        }),
    });
    var list = await response.json();
    var main = '';
    for (i = 0; i < list.length; i++) {
        main += `<tr>
                    <td>${list[i].id}</td>
                    <td>${list[i].size.product.name}, <br><strong>Kích thước</strong>: ${list[i].size.name}</td>
                    <td>${list[i].quantity}</td>
                    <td>${formatmoney(list[i].importPrice)}</td>
                    <td>${list[i].importTime}<br>${list[i].importDate}</td>
                    <td>${list[i].provider.name}<br><strong>SDT</strong>: ${list[i].provider.phone}<br><strong>Địa chỉ</strong>: ${list[i].provider.address}<br><strong>Email</strong>: ${list[i].provider.email}</td>
                    <td>${list[i].description}</td>
                    <td class="sticky-col">
                        <i class="fa fa-trash-alt iconaction"></i>
                        <a href="addimportproduct?id=${list[i].id}"><i class="fa fa-edit iconaction"></i></a>
                    </td>
                </tr>`
    }
    document.getElementById("listImport").innerHTML = main
    $('#example').DataTable();
}

async function loadColor(idpro) {
    var url = 'http://localhost:8080/api/product/admin/findById?id=' + idpro;
    const response = await fetch(url, {
        method: 'GET',
        headers: new Headers({
            'Authorization': 'Bearer ' + token
        })
    });
    var result = await response.json();
    document.getElementById("gianhap").value = result.price
    var list = result.productColors;
    var main = '<option selected disabled>Chọn màu sắc</option>';
    for (i = 0; i < list.length; i++) {
        main += `<option value="${list[i].id}">${list[i].colorName}</option>`
    }
    document.getElementById("colors").innerHTML = main
    const ser = $("#colors");
    ser.select2({});
    document.getElementById("colors").onchange = function() {
        var idColor = document.getElementById("colors").value;
        for (i = 0; i < list.length; i++) {
            if (list[i].id == idColor) {
                var listSize = list[i].productSizes;
                var main = '';
                for (j = 0; j < listSize.length; j++) {
                    main += `<option value="${listSize[j].id}">${listSize[j].sizeName}</option>`
                }
                document.getElementById("sizes").innerHTML = main
                const ser = $("#sizes");
                ser.select2({});
                break;
            }
        }
    }
}


async function saveImportPro() {
    var uls = new URL(document.URL)
    var id = uls.searchParams.get("id");
    var url = 'http://localhost:8080/api/import-product/admin/create';
    if (id != null) {
        url = 'http://localhost:8080/api/import-product/admin/update';
    }
    importPro = {
        "id": id,
        "quantity": document.getElementById("soluong").value,
        "importPrice": document.getElementById("gianhap").value,
        "description": tinyMCE.get('editor').getContent(),
        "size": {
            "id":  document.getElementById("sizes").value
        },
        "provider": {
            "id":  document.getElementById("nhacungcap").value
        }
    }
    console.log(importPro);
    const response = await fetch(url, {
        method: 'POST',
        headers: new Headers({
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json'
        }),
        body: JSON.stringify(importPro)
    });
    if (response.status < 300) {
        swal({
                title: "Thông báo",
                text: "thêm/sửa đơn nhập thành công!",
                type: "success"
            },
            function() {
                window.location.href = 'importproduct'
            });
    }
    if (response.status == exceptionCode) {
        var result = await response.json()
        toastr.warning(result.defaultMessage);
    }
}

async function loadAImportProduct() {
    var id = window.location.search.split('=')[1];
    if (id != null) {
        var url = 'http://localhost:8080/api/import-product/admin/findById?id=' + id;
        const response = await fetch(url, {
            method: 'GET',
            headers: new Headers({
                'Authorization': 'Bearer ' + token
            }),
        });
        var result = await response.json();
        document.getElementById("sizes").value = result.size.id
        tinyMCE.get('editor').setContent(result.description)
        document.getElementById("soluong").value = result.quantity
        document.getElementById("nhacungcap").value = result.provider.id
        document.getElementById("gianhap").value = result.importPrice
    }
}
