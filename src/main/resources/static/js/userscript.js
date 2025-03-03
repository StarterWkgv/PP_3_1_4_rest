const getUpdateTable = (() => {
    const USER_URL = "/api/user";
    const USER_TABLE = "userInfo";
    const HEADER_ID = "navbar-username";
    const row = user => {
        let out = '<tr>';
        Object.keys(user).forEach(k => {
            if (k === "password") return;
            out += `<td data-name=${k}> ${k === "roles" ? user[k].reduce((a, b) => a + ` ${b}`, "") : user[k]} </td>`;
        });
        return out;
    };

    const header = user => {
        return `<strong >${user['email']}</strong>
        with roles:
        <span>${user['roles'].reduce((a, b) => `${a} ${b}`, "")}</span>`;
    };

    const getUpdateTable = (url, row, table) => {
        return async () => {
            try {
                console.log('updateTable url: ' + url);
                const resp = await fetch(url);
                if (resp.ok) {
                    let users = await resp.json();
                    document.getElementById(table).innerHTML = Array.of(users).flatMap(a => a).map(row).reduce((a, b) => a + b, "");
                } else {
                    console.error(resp.status, " Couldn't get the data from server")
                }
            } catch (e) {
                console.error("Error >>> ", e);
            }
        }
    };

    const init = async () => {
        const updateTable = getUpdateTable(USER_URL, row, USER_TABLE);
        $('#v-pills-user-tab').on('show.bs.tab', updateTable);
        await getUpdateTable(USER_URL, header, HEADER_ID)();
        await updateTable();
    }

    document.addEventListener("DOMContentLoaded", init);
    return getUpdateTable;
})()