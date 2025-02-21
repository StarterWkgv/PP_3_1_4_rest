const getUpdateTable = (() => {
    const USERS_URL = "/api/user";
    const USERS_TABLE = "userInfo";

    const row = user => {
        let out = '<tr>';
        Object.keys(user).forEach(k => {
            if (k === "password") return;
            out += `<td data-name=${k}> ${k === "roles" ? user[k].reduce((a, b) => a + ` ${b}`, "") : user[k]} </td>`;
        });
        // out += `${but("info", user.id, "edit")} ${but("danger", user.id, "delete")}`;
        return out;
    };

    const getUpdateTable = (url, row, table) => {
        return async () => {
            try {
                console.log('updateTable url: ' + url);
                const resp = await fetch(url);
                if (resp.ok) {
                    let users = await resp.json();
                    document.getElementById(table).innerHTML = Array.of(users).flatMap(a=>a).map(row).reduce((a, b) => a + b, "");
                } else {
                    console.error(resp.status, " Couldn't get the data from server")
                }
            } catch (e) {
                console.error("Error >>> ", e);
            }
        }
    };

    const init = async () => {
        const updateTable =  getUpdateTable(USERS_URL, row, USERS_TABLE);
        $('#v-pills-user-tab').on('show.bs.tab',updateTable );
        await updateTable();
    }

    document.addEventListener("DOMContentLoaded", init);
    return getUpdateTable;
})()