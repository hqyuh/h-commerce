
const showDeleteConfirmModal = (link, entityName) => {
    let entityID = link.attr('entityID');

    $('#yesButton').attr('href', link.attr('href')); // url
    $('#confirmText').text('Are you sure you want to delete this ' + entityName + " ID " + entityID + "?");
    $('#confirmModal').modal();
}

const clearFilter = () => {
    window.location = moduleURL;
}