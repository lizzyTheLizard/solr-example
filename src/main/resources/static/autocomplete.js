$(function () {
    $('#search').on('input', function () {
        closeAllLists();
        fetchAutocomplete($(this));
    });

    $('html').on('click', function () {
        closeAllLists();
    });
});

function closeAllLists() {
    $('.autocomplete-items').each(function () {
        this.parentNode.removeChild(this);
    });
    $('#search').removee
}

function fetchAutocomplete(input) {
    $.ajax('http://localhost:8080/api/games/autocomplete?search=' + input.val(), {
        success: function (val) {
            fillAutocompleteList(input, val);
        },
        error: function (err) {
            console.error(err)
        }
    });

}

function fillAutocompleteList(input, val) {
    let currentFocus = -1;
    const list = $("<div>", {"class": "autocomplete-items"});
    const elements = val.map(s => {
        const b1 = $("<div>");
        b1.append("<strong>" + s.result + "</strong> (" + s.field + ")");
        b1.on("click", function () {
            console.log(s)
            if (s.id) {
                window.location.href = '/games/' + s.id;
                return;
            }
            const query = s.field + ':' + s.result.replace("<em>", "").replace("</em>", "");
            window.location.href = '/games?search=' + encodeURIComponent(query);
        });
        return b1;
    });
    elements.forEach(e => list.append(e));
    input.parent().append(list);
    input.off('keydown.autocomplete');
    input.on('keydown.autocomplete', function (e) {
        if (e.keyCode === 40) {
            currentFocus++;
            elements.forEach(e => e.removeClass("autocomplete-active"));
            const selected = elements[currentFocus];
            selected.addClass("autocomplete-active")
        } else if (e.keyCode === 38) {
            currentFocus--;
            elements.removeClass("autocomplete-active");
            const selected = elements[currentFocus];
            selected.addClass("autocomplete-active")
        } else if (e.keyCode === 13) {
            const selected = elements[currentFocus];
            if (selected) {
                console.log(selected);
                selected.click();
                e.preventDefault();
            }
        }
    })
}