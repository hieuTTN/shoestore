async function loadNhaCC() {
    $('#example').DataTable().destroy();
    var url = 'http://localhost:8080/api/provider/admin/findAll';
    const response = await fetch(url, {
        method: 'GET',
        headers: new Headers({
            'Authorization': 'Bearer ' + token
        })
    });
    var list = await response.json();

    var main = '';
    for (i = 0; i < list.length; i++) {
        main += ` <tr>
                    <td>${list[i].id}</td>
                    <td>${list[i].name}</td>
                    <td>${list[i].address}</td>
                    <td>${list[i].phone}</td>
                    <td>${list[i].email}</td>
                    <td class="sticky-col">
                        <i onclick="deleteNhaCC(${list[i].id})" class="fa fa-trash-alt iconaction"></i>
                        <a href="addnhacungcap?id=${list[i].id}"><i class="fa fa-edit iconaction"></i></a>
                    </td>
                </tr>`
    }
    document.getElementById("listnhacc").innerHTML = main
    $('#example').DataTable();
}

async function saveNhaCC() {
    var uls = new URL(document.URL)
    var id = uls.searchParams.get("id");
    var url = 'http://localhost:8080/api/provider/admin/create';
    var ncc = {
        "id": id,
        "name": document.getElementById("tenncc").value,
        "address": document.getElementById("address").value,
        "phone": document.getElementById("phone").value,
        "email": document.getElementById("email").value,
    }

    const response = await fetch(url, {
        method: 'POST',
        headers: new Headers({
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json'
        }),
        body: JSON.stringify(ncc)
    });
    if (response.status < 300) {
        swal({
                title: "Thông báo",
                text: "thêm/sửa nhà cung cấp thành công!",
                type: "success"
            },
            function() {
                window.location.replace('nhacungcap')
            });
    }
    if (response.status == exceptionCode) {
        var result = await response.json()
        toastr.warning(result.defaultMessage);
    }
}

async function loadANhacc() {
    var id = window.location.search.split('=')[1];
    if (id != null) {
        var url = 'http://localhost:8080/api/provider/admin/findById?id=' + id;
        const response = await fetch(url, {
            method: 'GET',
            headers: new Headers({
                'Authorization': 'Bearer ' + token
            })
        });
        var result = await response.json();
        document.getElementById("tenncc").value = result.name
        document.getElementById("address").value = result.address
        document.getElementById("phone").value = result.phone
        document.getElementById("email").value = result.email
    }
}


async function deleteNhaCC(id) {
    var con = confirm("Xác nhận xóa nhà cung cấp này?")
    if (con == false) {
        return;
    }
    var url = 'http://localhost:8080/api/provider/admin/delete?id=' + id;
    const response = await fetch(url, {
        method: 'DELETE',
        headers: new Headers({
            'Authorization': 'Bearer ' + token
        })
    });
    if (response.status < 300) {
        swal({
                title: "Thông báo",
                text: "xóa nhà cung cấp thành công!",
                type: "success"
            },
            function() {
                loadNhaCC();
            });
    }
    if (response.status == exceptionCode) {
        var result = await response.json()
        toastr.warning(result.defaultMessage);
    }
}