$(function () {
    $('#searchResults').bootstrapTable({
        onClickRow(item) {
            window.location.href = '/games/' + item[0];
        }
    })
});
