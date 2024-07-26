async function loadThuongHieu() {
    $('#example').DataTable().destroy();
    var url = 'http://localhost:8080/api/trademark/public/all-trademark';
    const response = await fetch(url, {
        method: 'GET'
    });
    var list = await response.json();
    var main = '';
    for (i = 0; i < list.length; i++) {
        main += `<tr>
                    <td>${list[i].id}</td>
                    <td>${list[i].name}</td>
                    <td class="sticky-col">
                        <i onclick="deleteTrademark(${list[i].id})" class="fa fa-trash-alt iconaction"></i>
                        <i data-bs-toggle="modal" data-bs-target="#addtk" onclick="loadATrademark(${list[i].id})" class="fa fa-edit iconaction"></i>
                    </td>
                </tr>`
    }
    document.getElementById("listtrademark").innerHTML = main
    $('#example').DataTable();
}


function clearData(){
    document.getElementById("idcm").value = ""
    document.getElementById("tencm").value = ""
}


async function loadATrademark(id) {
        var url = 'http://localhost:8080/api/trademark/admin/findById?id=' + id;
        const response = await fetch(url, {
            method: 'GET',
            headers: new Headers({
                'Authorization': 'Bearer ' + token
            })
        });
        var result = await response.json();
        document.getElementById("tencm").value = result.name
        document.getElementById("idcm").value = result.id
}

async function saveTrademark() {
    var uls = new URL(document.URL)
    var id = document.getElementById("idcm").value
    var name = document.getElementById("tencm").value
    var url = 'http://localhost:8080/api/trademark/admin/create';
    var thuonghieu = {
        "id": id,
        "name": name,
    }
    const response = await fetch(url, {
        method: 'POST',
        headers: new Headers({
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json'
        }),
        body: JSON.stringify(thuonghieu)
    });
    if (response.status < 300) {
        swal({
                title: "Thông báo",
                text: "thêm/sửa danh mục thành công!",
                type: "success"
            },
            function() {
                loadThuongHieu();
                $("#addtk").modal("hide")
            });
    }
    if (response.status == exceptionCode) {
        var result = await response.json()
        toastr.warning(result.defaultMessage);
    }
}

async function deleteTrademark(id) {
    var con = confirm("Bạn chắc chắn muốn xóa thương hiệu này?");
    if (con == false) {
        return;
    }
    var url = 'http://localhost:8080/api/trademark/admin/delete?id=' + id;
    const response = await fetch(url, {
        method: 'DELETE',
        headers: new Headers({
            'Authorization': 'Bearer ' + token
        })
    });
    if (response.status < 300) {
        toastr.success("xóa thương hiệu thành công!");
        loadThuongHieu();
    }
    if (response.status == exceptionCode) {
        var result = await response.json()
        toastr.warning(result.defaultMessage);
    }
}



async function loadThuongHieuAddProduct() {
    var url = 'http://localhost:8080/api/trademark/public/all-trademark';
    const response = await fetch(url, {
        method: 'GET'
    });
    var list = await response.json();
    var main = '';
    for (i = 0; i < list.length; i++) {
        main += `<option value="${list[i].id}">${list[i].name}</option>`
    }
    document.getElementById("listtrademark").innerHTML = main
}
