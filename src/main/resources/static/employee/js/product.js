

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
                </tr>`
    }
    document.getElementById("listproduct").innerHTML = main
    $('#example').DataTable();
}



async function xuatExcel(type) {
    var url = 'http://localhost:8080/api/product/employee/export-excel?type='+type;
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