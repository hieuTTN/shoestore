async function loadAllUser() {
    $('#example').DataTable().destroy();
    var url = 'http://localhost:8080/api/user/employee/all-customer';
    const response = await fetch(url, {
        method: 'GET',
        headers: new Headers({
            'Authorization': 'Bearer ' + token
        })
    });
    var listUser = await response.json();
    console.log();
    var main = '';
    for (i = 0; i < listUser.length; i++) {
        main += `<tr>
                    <td>${listUser[i].id}</td>
                    <td><img src="${listUser[i].avatar == null?'':listUser[i].avatar}" class="imgtable"></td>
                    <td>${listUser[i].email} ${listUser[i].userType=="GOOGLE"?'<img src="../image/google.png" class="googleicon">':''}</td>
                    <td>${listUser[i].fullName}</td>
                    <td>${listUser[i].createdDate}</td>
                </tr>`
    }
    document.getElementById("listuser").innerHTML = main
    $('#example').DataTable();
}

