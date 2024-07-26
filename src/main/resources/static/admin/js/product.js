const listFile = [];

var listsize = [];


async function loadProduct() {
    $('#example').DataTable().destroy();
    var url = 'http://localhost:8080/api/product/public/findAll-list';
    const response = await fetch(url, {
        method: 'GET'
    });
    var list = await response.json();
    var main = '';
    for (i = 0; i < list.length; i++) {
        var total = 0;
        var ms = '';
        for(j=0; j<list[i].sizes.length; j++){
            total += Number(list[i].sizes[j].quantity)
            ms += `<br><strong class="sizetable">${list[i].sizes[j].name}</strong>`+": "+list[i].sizes[j].quantity
        }
        main += `<tr>
                    <td>${list[i].id}</td>
                    <td><img src="${list[i].imageBanner}" style="width: 100px;"></td>
                    <td>${list[i].code}</td>
                    <td>${list[i].category.name}</td>
                    <td>${list[i].name}</td>
                    <td>${formatmoney(list[i].price)}</td>
                    <td>${list[i].createdDate}</td>
                    <td>${list[i].quantitySold}</td>
                    <td><span><strong>Tổng:</strong> ${total}</span>${ms}</td>
                    <td class="sticky-col">
                        <i onclick="deleteProduct(${list[i].id})" class="fa fa-trash-alt iconaction"></i>
                        <a href="addproduct?id=${list[i].id}"><i class="fa fa-edit iconaction"></i><br></a>
                    </td>
                </tr>`
    }
    document.getElementById("listproduct").innerHTML = main
    $('#example').DataTable();
}

async function loadAProduct() {
    var uls = new URL(document.URL)
    var id = uls.searchParams.get("id");
    if (id != null) {
        document.getElementById("btnaddpro").innerHTML = `<i class="fa fa-edit"></i> Cập nhật sản phẩm`
        var url = 'http://localhost:8080/api/product/public/findById?id=' + id;
        const response = await fetch(url, {
            method: 'GET'
        });
        var result = await response.json();
        console.log(result)
        document.getElementById("tensp").value = result.name
        document.getElementById("price").value = result.price
        linkbanner = result.imageBanner
        document.getElementById("imgpreview").src = result.imageBanner
        tinyMCE.get('editor').setContent(result.description)
        tinyMCE.get('editorchonsize').setContent(result.instructionSize!=null?result.instructionSize:'')
        document.getElementById("listcategory").value = result.category.id
        document.getElementById("listtrademark").value = result.trademark.id
        document.getElementById("masp").value = result.code
        var main = ''
        for (i = 0; i < result.productImages.length; i++) {
            main += `<div id="imgdathem${result.productImages[i].id}" class="col-md-2 col-sm-4 col-6">
                        <img src="${result.productImages[i].linkImage}" class="image-uploaded">
                        <button onclick="deleteProductImage(${result.productImages[i].id})" class="btn btn-danger form-control">Xóa ảnh</button>
                    </div>`
        }
        document.getElementById("preview").innerHTML = main
        

        var mainkichthuoc = ''
        for (i = 0; i < result.sizes.length; i++) {
            mainkichthuoc += `<div class="singlekichthuocdathem" id="sizedathem${result.sizes[i].id}">
                Tên kích thước: ${result.sizes[i].name} - Số lượng: ${result.sizes[i].quantity} - giá bán: ${formatmoney(result.sizes[i].price)} 
                <i onclick="deleteSize(${result.sizes[i].id})" class="fa fa-trash iconaction"></i>
                <i onclick="loadSizeUpdate(${result.sizes[i].id}, '${result.sizes[i].name}',${result.sizes[i].price},${result.sizes[i].quantity})" data-bs-toggle="modal" data-bs-target="#modalcapnhatkichthuoc" onclick="" class="fa fa-edit iconaction"></i>
            </div>`
        }
        document.getElementById("listkichthuocdathem").innerHTML = mainkichthuoc
    }
}


var linkbanner = '';
async function saveProduct() {
    var uls = new URL(document.URL)
    var id = uls.searchParams.get("id");
    var url = 'http://localhost:8080/api/product/admin/create';
    if (id != null) {
        url = 'http://localhost:8080/api/product/admin/update';
    }
    document.getElementById("loading").style.display = 'block'
    await uploadFile(document.getElementById("anhdaidien"));
    var listLinkImg = await uploadMultipleFile();
    var product = {
        "id": id,
        "code": document.getElementById("masp").value,
        "name": document.getElementById("tensp").value,
        "imageBanner":linkbanner,
        "oldPrice": document.getElementById("oldprice").value,
        "price": document.getElementById("price").value,
        "description": tinyMCE.get('editor').getContent(),
        "instructionSize": tinyMCE.get('editorchonsize').getContent(),
        "category": {"id":document.getElementById("listcategory").value},
        "trademark": {"id":document.getElementById("listtrademark").value},
        "productImages": listLinkImg,
        "sizes": listsize,
    }
    console.log(product)
    const response = await fetch(url, {
        method: 'POST',
        headers: new Headers({
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json'
        }),
        body: JSON.stringify(product)
    });
    var result = await response.json();
    console.log(result)

    if (response.status < 300) {
        swal({ title: "Thông báo", text: "thêm/sửa sản phẩm thành công", type: "success" },
            function() { window.location.href = 'product' });
    } else {
        swal({title: "Thông báo",text: "thêm/sửa sản phẩm thất bại",type: "error"},
            function() { document.getElementById("loading").style.display = 'none' });
    }
}


function loadSizeUpdate(id, name, price, quantity){
    document.getElementById("idsizeupdate").value = id
    document.getElementById("tensizeupdate").value = name
    document.getElementById("giabanupdate").value = price
    document.getElementById("soluongupdate").value = quantity
}


async function capNhatKichThuoc() {
    var url = 'http://localhost:8080/api/size/admin/update';
    var kichthuoc = {
        "id": document.getElementById("idsizeupdate").value,
        "name": document.getElementById("tensizeupdate").value,
        "quantity": document.getElementById("soluongupdate").value,
        "price":document.getElementById("giabanupdate").value,
    }
    const response = await fetch(url, {
        method: 'POST',
        headers: new Headers({
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json'
        }),
        body: JSON.stringify(kichthuoc)
    });
    if (response.status < 300) {
        toastr.success("Đã cập nhật kích thước")
        loadAProduct()
        $("#modalcapnhatkichthuoc").modal("hide")
    } else {
        toastr.error("Thất bại")
    }
}


function loadInit() {
    $('input#choosefile').change(function() {
        var files = $(this)[0].files;
    });
    document.querySelector('#choosefile').addEventListener("change", previewImages);

    function previewImages() {
        var files = $(this)[0].files;
        for (i = 0; i < files.length; i++) {
            listFile.push(files[i]);
        }

        var preview = document.querySelector('#preview');

        for (i = 0; i < files.length; i++) {
            readAndPreview(files[i]);
        }

        function readAndPreview(file) {

            // if (!/\.(jpe?g|png|gif|webp)$/i.test(file.name)) {
            //     return alert(file.name + " is not an image");
            // }

            var reader = new FileReader(file);

            reader.addEventListener("load", function() {
                var div = document.createElement('div');
                div.className = 'col-lg-2 col-md-3 col-sm-6 col-6';
                div.style.height = '120px';
                div.style.paddingTop = '5px';
                div.marginTop = '100px';
                preview.appendChild(div);

                var img = document.createElement('img');
                img.src = this.result;
                img.style.height = '85px';
                img.style.width = '90%';
                img.className = 'image-upload';
                img.style.marginTop = '5px';
                div.appendChild(img);

                var button = document.createElement('button');
                button.style.height = '30px';
                button.style.width = '90%';
                button.innerHTML = 'xóa'
                button.className = 'btn btn-warning';
                div.appendChild(button);

                button.addEventListener("click", function() {
                    div.remove();
                    console.log(listFile.length)
                    for (i = 0; i < listFile.length; i++) {
                        if (listFile[i] === file) {
                            listFile.splice(i, 1);
                        }
                    }
                    console.log(listFile.length)
                });
            });

            reader.readAsDataURL(file);

        }

    }

}


function addKichThuocTam(){
    var obj = {
        "name":document.getElementById("tensize").value,
        "quantity":document.getElementById("soluong").value,
        "price":document.getElementById("giaban").value
    }
    listsize.push(obj);
    document.getElementById("tensize").value = "";
    document.getElementById("soluong").value = "";
    setPreViewKichThuoc();
}

function setPreViewKichThuoc(){
    var main = '';
    for(i=0; i< listsize.length; i++){
        main += `
        <div class="singlekichthuoc">
            Tên kích thước: ${listsize[i].name} - Số lượng: ${listsize[i].quantity} - giá bán: ${formatmoney(listsize[i].price)} <i onclick="deleteKichThuocTam(${i})" class="fa fa-trash iconxoakt"></i>
        </div>
        `
    }
    document.getElementById("listkichthuoctam").innerHTML = main;
}


function deleteKichThuocTam(position){
    listsize.splice(position, 1);
    toastr.success("Đã xóa kích thước ra khỏi bộ nhớ tạm");
    setPreViewKichThuoc();
}


async function uploadMultipleFile() {
    const formData = new FormData()
    for (i = 0; i < listFile.length; i++) {
        formData.append("file", listFile[i])
    }
    var urlUpload = 'http://localhost:8080/api/public/upload-multiple-file';
    const res = await fetch(urlUpload, {
        method: 'POST',
        body: formData
    });
    if (res.status < 300) {
        var arr = await res.json();
        var listrt = [];
        for(i=0; i<arr.length; i++){
            var oj = {
                "linkImage":arr[i]
            }
            listrt.push(oj)
        }
        return listrt
    } else {
        return [];
    }
}

async function uploadFile(filePath) {
    const formData = new FormData()
    formData.append("file", filePath.files[0])
    var urlUpload = 'http://localhost:8080/api/public/upload-file';
    const res = await fetch(urlUpload, {
        method: 'POST',
        body: formData
    });
    if (res.status < 300) {
        linkbanner = await res.text();
    }
}


async function deleteSize(id) {
    var con = confirm("Bạn chắc chắn muốn xóa kích thước sản phẩm này?");
    if (con == false) {
        return;
    }
    var url = 'http://localhost:8080/api/size/admin/delete?id=' + id;
    const response = await fetch(url, {
        method: 'DELETE',
        headers: new Headers({
            'Authorization': 'Bearer ' + token
        })
    });
    if (response.status < 300) {
        toastr.success("xóa kích thước sản phẩm thành công!");
        document.getElementById("sizedathem"+id).style.display = 'none'
    }
}


async function deleteProductImage(id) {
    var con = confirm("Bạn chắc chắn muốn xóa ảnh sản phẩm này?");
    if (con == false) {
        return;
    }
    var url = 'http://localhost:8080/api/product-image/admin/delete?id=' + id;
    const response = await fetch(url, {
        method: 'DELETE',
        headers: new Headers({
            'Authorization': 'Bearer ' + token
        })
    });
    if (response.status < 300) {
        toastr.success("xóa ảnh sản phẩm thành công!");
        document.getElementById("imgdathem"+id).style.display = 'none'
    }
}

async function deleteProduct(id) {
    var con = confirm("Bạn chắc chắn muốn xóa sản phẩm này?");
    if (con == false) {
        return;
    }
    var url = 'http://localhost:8080/api/product/admin/delete?id=' + id;
    const response = await fetch(url, {
        method: 'DELETE',
        headers: new Headers({
            'Authorization': 'Bearer ' + token
        })
    });
    if (response.status < 300) {
        toastr.success("xóa sản phẩm thành công!");
        loadProduct()
    }
    if (response.status == exceptionCode) {
        var result = await response.json()
        toastr.warning(result.defaultMessage);
    }
}




async function xuatExcel(type) {
    var url = 'http://localhost:8080/api/product/admin/export-excel?type='+type;
    const response = await fetch(url, {
        method: 'GET',
        headers: new Headers({
            'Authorization': 'Bearer ' + token
        })
    });
    var list = await response.json();
    const workbook = XLSX.utils.book_new();

    for (i = 0; i < list.length; i++) {
        // Tạo dữ liệu cho bảng tính
        let worksheet_data = [
            ["Ngày "+currentDate()],
            ["Thời gian: "+currentTime()],
            ["ID", "Mã giày", "Tên giày", "Giá bán", "Giá cũ", "Ngày tạo", "Danh mục", "Thương hiệu", "Số lượng bán", "Số lượng tồn"], // Row 8
        ];
        let worksheet = XLSX.utils.aoa_to_sheet(worksheet_data);

        worksheet['!cols'] = [
            { wch: 6 }, { wch: 16.11 },{ wch: 52 },{ wch: 13.56 },{ wch: 13.56 },{ wch: 20 },{ wch: 15.11 },{ wch: 15.44 },{ wch: 15 },{ wch: 15 }
        ];

        // Định nghĩa kiểu đường viền
        const borderStyle = {
            top: { style: "thin", color: { rgb: "000000" } },
            bottom: { style: "thin", color: { rgb: "000000" } },
            left: { style: "thin", color: { rgb: "000000" } },
            right: { style: "thin", color: { rgb: "000000" } }
        };
        const range = XLSX.utils.decode_range('A3:J3');
        for (let R = range.s.r; R <= range.e.r; ++R) {
            for (let C = range.s.c; C <= range.e.c; ++C) {
                const cell_address = { c: C, r: R };
                const cell_ref = XLSX.utils.encode_cell(cell_address);

                if (!worksheet[cell_ref]) worksheet[cell_ref] = {};
                worksheet[cell_ref].s = {
                    border: borderStyle,
                    alignment: { vertical: "center", horizontal: "center",wrapText:true },
                    font: { bold: true, sz: 12}
                };
            }
        }

        // const dataToAdd = list[i].products.map(item => [item.id, item.code, item.name, formatmoney(item.price), formatmoney(item.oldPrice),
        //     item.createdDate, item.category.name, item.trademark.name, item.quantitySold, item.quantitySold
        // ]);
        const dataToAdd = [];
        for(j=0; j<list[i].products.length; j++){
            var item = list[i].products[j]
            var pro = [item.id, item.code, item.name, formatmoney(item.price), formatmoney(item.oldPrice), item.createdDate, item.category.name, item.trademark.name, item.quantitySold];
           
            var total = 0;
            var ms = '';
            for(k = 0; k < item.sizes.length; k++){
                total += Number(item.sizes[k].quantity)
                ms += " size: "+ item.sizes[k].name+": "+item.sizes[k].quantity+", \n"
            }
            var tonkho = "Tổng: "+total+"\n"+ms
            pro.push(tonkho)
            dataToAdd.push(pro)
        }

        XLSX.utils.sheet_add_aoa(worksheet, dataToAdd, { origin: 'A4' });
        XLSX.utils.book_append_sheet(workbook, worksheet, list[i].name);
    }
    XLSX.writeFile(workbook, 'data.XLSX');
    console.log('Dữ liệu đã được xuất ra file data.XLSX');
}


function currentDate(){
    const currentDate = new Date();
    const currentYear = currentDate.getFullYear();
    const currentMonth = String(currentDate.getMonth() + 1).padStart(2, '0');
    const currentDay = String(currentDate.getDate()).padStart(2, '0');

    const formattedDate = `${currentYear}-${currentMonth}-${currentDay}`;
    return formattedDate;
}

function currentTime(){
    const currentDate = new Date();
    const currentHours = String(currentDate.getHours()).padStart(2, '0');
    const currentMinutes = String(currentDate.getMinutes()).padStart(2, '0');

    const currentTime = `${currentHours}:${currentMinutes}`;
    return currentTime;
}