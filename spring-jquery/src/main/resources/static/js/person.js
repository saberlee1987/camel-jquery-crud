let BASEURL = "http://localhost:9196/services/camel-crud/persons"

$(document).ready(function () {

    loadDataInTable()

    $("#addPerson").on('click', function () {
        let firstName = $("#firstName").val()
        let lastName = $("#lastName").val()
        let nationalCode = $("#nationalCode").val()
        let age = $("#age").val()
        let email = $("#email").val()
        let mobile = $("#mobile").val()

        if (firstName === "") {
            $("#firstNameRequired").css('visibility', "visible")
            return;
        } else {
            $("#firstNameRequired").css('visibility', "hidden")
        }
        if (lastName === "") {
            $("#lastNameRequired").css('visibility', "visible")
            return;
        } else {
            $("#lastNameRequired").css('visibility', "hidden")
        }
        if (nationalCode === "") {
            $("#nationalCodeRequired").css('visibility', "visible")
            console.log("error ================")
            return
        } else {
            $("#nationalCodeRequired").css('visibility', "hidden")
        }
        if (!nationalCode.match("\\d{10}")) {
            $("#nationalCodeRequired")
                .css('visibility', "visible")
                .text("Please enter valid nationalCode")
            return;
        } else {
            $("#nationalCodeRequired").css('visibility', "hidden")
        }
        //
        if (age === "" || !age.match("\\d+")) {
            $("#ageRequired").css('visibility', "visible")
            return;
        } else {
            $("#ageRequired").css('visibility', "hidden")
        }

        if (!age.match("\\d+")) {
            $("#ageRequired")
                .css('visibility', "visible")
                .text("Please enter valid age")
            return;
        } else {
            $("#ageRequired").css('visibility', "hidden")
        }

        if (email === "") {
            $("#emailRequired").css('visibility', "visible")
            return;
        } else {
            $("#emailRequired").css('visibility', "hidden")
        }
        // let mobileRegx = "00[0-9]{9}"
        if (mobile === "") {
            $("#mobileRequired").css('visibility', "visible")
            return;
        } else {
            $("#mobileRequired").css('visibility', "hidden")
        }

        if (!mobile.match("\\d{11}")) {
            $("#mobileRequired")
                .css('visibility', "visible")
                .text("Please enter valid mobile")
            return;
        } else {
            $("#mobileRequired").css('visibility', "hidden")
        }
        let json = {
            firstname: firstName,
            lastname: lastName,
            nationalCode: nationalCode,
            age: age,
            email: email,
            mobile: mobile,
        }
        console.log("json ===> " + JSON.stringify(json))

        json = JSON.stringify(json)
        let AddPersonUrl = `${BASEURL}/add`

        console.log("request for add person ===> " + AddPersonUrl)
        $.ajax({
            url: AddPersonUrl,
            data: json,
            body: json,
            method: 'POST',
            accept: 'application/json',
            contentType: 'application/json'
            , success: function (data) {
                console.log("success ====> " + JSON.stringify(data.responseJSON))
                $("#result").text("your data saved successfully")
            }
            , error: function (data) {
                let errorResponse = JSON.stringify(data.responseJSON)
                console.log("error ====> " + errorResponse)
                $("#result").text(data.responseJSON.message)
            }
            , complete: function () {
               clearData()
                loadDataInTable()
            }
        })


    })

    $("#personForm").on("submit", function (event) {
        event.preventDefault()

    });

    $(document).on('click', '#btnUpdate', function () {
        console.log("btn update clicked .................")
        $this = $(this)
        let nationalCode = $this.attr('data-person-nationalCode')

        let findPersonUrl = `${BASEURL}/findByNationalCode/${nationalCode}`
        console.log("request for find person  for nationalCode " + nationalCode + "===> " + findPersonUrl)

        $.ajax({
            url: findPersonUrl,
            method: 'GET',
            accept: 'application/json',
            contentType: 'application/json'
            , success: function (response) {
                console.log(JSON.stringify(response))

                $("#firstName").val(response.firstname)
                $("#lastName").val(response.lastname)
                $("#nationalCode").val(response.nationalCode)
                $("#age").val(response.age)
                $("#email").val(response.email)
                $("#mobile").val(response.mobile)

            }
            , error: function (err) {
                let errorResponse = JSON.stringify(err.responseJSON)
                console.log("error ====> " + errorResponse)
                $("#result").text(err.responseJSON.message)
            }
        })

    });

    $(document).on('click', '#btnDelete', function () {
        console.log("btn delete clicked .................")
        $this = $(this)
        let nationalCode = $this.attr('data-person-nationalCode')
        console.log("nationalCode ===> " + nationalCode)
        let deletePersonUrl = `${BASEURL}/delete/${nationalCode}`
        console.log("request for delete person  for nationalCode " + nationalCode + "===> " + deletePersonUrl)

        $.ajax({
            url: deletePersonUrl,
            method: 'DELETE',
            accept: 'application/json',
            contentType: 'application/json'
            , success: function (data) {
                console.log("success ====> " + JSON.stringify(data.responseJSON))
                $("#result").text("your data deleted successfully")
            }
            , error: function (data) {
                let errorResponse = JSON.stringify(data.responseJSON)
                console.log("error ====> " + errorResponse)
                $("#result").text(data.responseJSON.message)
            }, complete: function () {
                loadDataInTable()
            }
        })

    });

    $("#updatePerson").on('click', function () {

        let firstName = $("#firstName").val()
        let lastName = $("#lastName").val()
        let nationalCode = $("#nationalCode").val()
        let age = $("#age").val()
        let email = $("#email").val()
        let mobile = $("#mobile").val()

        let json = {
            firstname: firstName,
            lastname: lastName,
            nationalCode: nationalCode,
            age: age,
            email: email,
            mobile: mobile,
        }
        console.log("json ===> " + JSON.stringify(json))
        json = JSON.stringify(json)

        let updatePersonUrl = `${BASEURL}/update/${nationalCode}`
        console.log("request for update person ===> " + updatePersonUrl)


        $.ajax({
            url: updatePersonUrl,
            data: json,
            body: json,
            method: 'PUT',
            accept: 'application/json',
            contentType: 'application/json'
            , success: function (data) {
                console.log("success ====> " + JSON.stringify(data.responseJSON))
                $("#result").text("your data updated successfully")
            }
            , error: function (data) {
                let errorResponse = JSON.stringify(data.responseJSON)
                console.log("error ====> " + errorResponse)
                $("#result").text(data.responseJSON.message)
            }
            , complete: function () {
                clearData()
                loadDataInTable()
            }
        })

    });

    $("#cancelPerson").on('click', function () {
        clearData()
    })
})

function clearData() {
    $("#firstName").val('')
    $("#lastName").val('')
    $("#nationalCode").val('')
    $("#age").val('')
    $("#email").val('')
    $("#mobile").val('')
    $("#result").val('')
}

function loadDataInTable() {
    let findAllPersonUrl = `${BASEURL}/findAll`
    console.log("request for findAll person ===> " + findAllPersonUrl)
    $.ajax({
        url: findAllPersonUrl,
        method: 'GET',
        accept: 'application/json',
        contentType: 'application/json'
        , success: function (response) {
            console.log("success ====> " + JSON.stringify(response))
            $("#tbody").empty()
            $.each(response.persons, function (i, item) {
                let $tr = $('<tr>').append(
                    $('<td>').text(item.firstname),
                    $('<td>').text(item.lastname),
                    $('<td>').text(item.nationalCode),
                    $('<td>').text(item.age),
                    $('<td>').text(item.email),
                    $('<td>').text(item.mobile),
                    $('<td>').append($(`<button class="btn btn-warning" id="btnUpdate" data-person-nationalCode="${item.nationalCode}">Update</button>`)),
                    $('<td>').append($(`<button class="btn btn-danger" id="btnDelete" data-person-nationalCode="${item.nationalCode}">Delete</button>`)),
                ); //.appendTo('#records_table');
                console.log($tr.wrap('<p>').html());
                $("#tbody").append($tr)
            });
        }
        , error: function (err) {
            let errorResponse = JSON.stringify(err.responseJSON)
            console.log("error ====> " + errorResponse)
            $("#result").text(err.responseJSON.message)
        }
    })


}