const updateTable = (async () => {

    const getUpdatetable = async (url, row, table) => {
        return async () =>{}
        try {
            const resp = await fetch(url);
            if (resp.ok) {
                let users = await resp.json();
                document.getElementById(table).innerHTML = users.map(row).reduce((a, b) => a + b, "");
            } else {
                console.error(resp.status, " Couldn't get the data from server")
            }
        } catch (e) {
            console.error("Error >>> ", e);
        }
    };

    return updateTable;
})()