let BASEURL = "http://localhost:9196/services/camel-crud/persons"

$(document).ready(function () {

    loadDataInTable()
    $("#personForm").on("submit", function (event) {
        event.preventDefault()
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
        })
    });

})

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
                    $('<td>').append($('<button class="btn btn-warning" id="btnUpdate">Update</button>')),
                    $('<td>').append($('<button class="btn btn-danger" id="btnDelete">Delete</button>')),
                ); //.appendTo('#records_table');
                console.log($tr.wrap('<p>').html());
                $("#tbody").append($tr)
            });
            // $("#result").text("your data saved successfully")
        }
        , error: function (data) {
            let errorResponse = JSON.stringify(data.responseJSON)
            console.log("error ====> " + errorResponse)
            $("#result").text(data.responseJSON.message)
        }
    })
}