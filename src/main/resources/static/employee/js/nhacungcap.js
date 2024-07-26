async function loadNhaCC() {
    $('#example').DataTable().destroy();
    var url = 'http://localhost:8080/api/provider/employee/findAll';
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
                </tr>`
    }
    document.getElementById("listnhacc").innerHTML = main
    $('#example').DataTable();
}
