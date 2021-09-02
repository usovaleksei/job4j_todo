let user;

function showAllItems() {
    if (document.querySelector('#allItems').checked) {
        loadAllItems();
    } else {
        loadNotDoItems();
    }
}

function changeTaskStatus(id, done) {
    console.log('changeTaskStatus: id = ' + id + ', done = ' + done);
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/todo/itemedit.do",
        data: {
            "id": id,
            "done": done
        },
        success: function (response) {
            swal(response);
            console.log("Изменение статуса произошло");
            loadNotDoItems();
        }
    })
}

function sendItem() {
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8080/todo/item.do',
        data: {
            "description": $("#newItemName").val(),
        },
        success: function (response) {
            swal(response);
            document.getElementById("itemForm").reset();
            document.querySelector('#allItems').checked = false;
            loadNotDoItems();
        }
    });
}

function validate() {
    let rsl = true;
    let item = $("#newItemName").val();
    if (item === "") {
        swal("Введите описание задачи");
        rsl = false;
    }
    console.log(item);
    return rsl;
}

function loadNotDoItems() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/todo/notdoitem.do",
        dataType: "json",
        success: function (data) {
            $('#itemsTable tbody').empty();

            let table = document.querySelector('#itemsTable');
            let tbody = document.createElement('tbody');
            table.appendChild(tbody);
            for (let i = 0; i < data.length; i++) {
                let tr = document.createElement('tr');
                let date = new Date(data[i].created).toLocaleString();
                user = data[i].user;
                let name = user['name'];
                console.log(user);
                tr.innerHTML = `<td>${data[i].description}</td>
                               <td>${name}</td> 
                               <td>${date}</td>
                               <td><input class="form-check-input" type="checkbox" value="" id=${data[i].id}
                                onchange="changeTaskStatus(this.id, this.checked)"> В работе </td>`
                tbody.appendChild(tr);
            }
        }
    })
}

function loadAllItems() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/todo/item.do",
        dataType: "json",
        success: function (data) {
            $('#itemsTable tbody').empty();
            let table = document.querySelector('#itemsTable');
            let tbody = document.createElement('tbody');
            table.appendChild(tbody);
            for (let i = 0; i < data.length; i++) {
                let tr = document.createElement('tr');
                let dataDone = Boolean(data[i].done);
                let date = new Date(data[i].created).toLocaleString();
                user = data[i].user;
                let name = user['name'];
                if (dataDone) {
                    tr.innerHTML = `<td>${data[i].description}</td>
                               <td>${name}</td> 
                               <td>${date}</td>
                               <td><input class="form-check-input" type="checkbox" value="" id=id=${data[i].id}
                                checked="checked" onchange="changeTaskStatus(this.id, this.checked)"> Выполнена </td>`
                } else {
                    tr.innerHTML = `<td>${data[i].description}</td>
                               <td>${name}</td>
                               <td>${date}</td>
                               <td><input class="form-check-input" type="checkbox" value="" id=${data[i].id}
                                onchange="changeTaskStatus(this.id, this.checked)"> В работе </td>`
                }
                tbody.appendChild(tr);
            }
        }
    })
}

function btnClick() {
    if (validate()) {
        sendItem();
    }
}