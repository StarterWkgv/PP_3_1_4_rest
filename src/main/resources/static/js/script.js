(async function () {
    const csrfValue = document.querySelector("[name=_csrf]").value;
    const csrfHeader = "X-CSRF-TOKEN";
    const modalDelete = document.getElementById("modal-delete");
    const modalEdit = document.getElementById("modal-edit");
    const deleteFields = new Map();
    const editFields = new Map();
    const addNewUserFields = new Map();
    const errorEdit = new Map();
    const errorAddNewUser = new Map();
    let errorObj = null;
    const showElement = e => e.style.display = "block";
    const hideElement = e => e.style.display = "none";

    document.getElementById("modal-delete").querySelectorAll("[data-name]").forEach(node => {
        deleteFields.set(node.dataset.name, node);
    });
    document.getElementById("modal-edit").querySelectorAll("[data-name]").forEach(node => {
        editFields.set(node.dataset.name, node);
        let errorNode = node.nextElementSibling;
        if (errorNode) errorEdit.set(node.dataset.name, errorNode);

    });
    document.getElementById("addNewUser").querySelectorAll("[data-name]").forEach(node => {
        addNewUserFields.set(node.dataset.name, node);
        let errorNode = node.nextElementSibling;
        if (errorNode) errorAddNewUser.set(node.dataset.name, errorNode);
    })

    const clearErrorFields = (ef) => {
        errorObj = null;
        ef.forEach(v => {
            hideElement(v);
            v.textContent = "";
        });
    }

    const fillInputs = (user, fields) => {
        if (!user) return;
        Object.keys(user).forEach(k => {
            if (k === "password") return;
            if (k === "roles") {
                Array.from(fields.get(k).options).forEach(opt => {
                    opt.selected = user[k].includes(opt.value);
                })
            } else {
                fields.get(k).value = user[k];
            }
        })
    };

    $('#modal-delete').on('show.bs.modal', async function (event) {
        let response = await fetch(`/api/admin/users/${$(event.relatedTarget).data('uid')}`);
        let user = await response.json();
        fillInputs(user, deleteFields);
    });
    $('#modal-edit').on('show.bs.modal', async function (event) {
        let response = await fetch(`/api/admin/users/${$(event.relatedTarget).data('uid')}`);
        let user = await response.json();
        fillInputs(user, editFields);
    })


    const updateTable = async () => {
        const getRow = user => {
            let out = '<tr>';
            Object.keys(user).forEach(k => {
                if (k === "password") return;
                out += `<td data-name=${k}>${k === "roles" ? user[k].reduce((a, b) => a + ` ${b}`, "") : user[k]}</td>`;
            });
            out += ` <td>
                        <button type="button" class="btn btn-info px-1 py-1 "
                             data-uid=${user.id} data-toggle="modal" data-target="#modal-edit">Edit</button>
                    </td>
                    <td>
                        <button type="button" class="btn btn-danger px-1 py-1"
                             data-uid=${user.id} data-toggle="modal" data-target="#modal-delete">Delete</button>
                    </td>
                </tr>`;
            return out;
        };
        try {
            const table = document.getElementById("usersTable").querySelector("tbody");
            const resp = await fetch("/api/admin/users");

            if (resp.ok) {
                let users = await resp.json();
                table.innerHTML = users.map(getRow).reduce((a, b) => a + b, "");
            } else {
                console.error(resp.status, " Couldn't get the list of users")
            }

        } catch (e) {
            console.error("Error >>> ", e);
        }
    };

    const fetchAndUpdate = (met, modalFields, errorFields, readData) => {
        return async () => {
            try {
                clearErrorFields(errorFields);
                let id = modalFields.get("id");
                const reqHeaders = new Headers();
                reqHeaders.append(csrfHeader, csrfValue);
                reqHeaders.append('Content-Type', 'application/json',);
                const response = await fetch(`/api/users/${id ? id.value : ''}`,
                    {
                        method: met,
                        headers: reqHeaders,
                        body: readData ? readData() : ''
                    });

                if (response.ok) {
                    console.log(response.status);
                        await updateTable();
                } else if (response.status === 400) {
                    errorObj = await response.json();
                    if (errorObj && errorObj["isValidation"]) {
                        for (let i in errorObj) {
                            if (i === "isValidation") continue;
                            errorFields.get(i).textContent = errorObj[i];
                            showElement(errorFields.get(i))
                        }
                    } else {
                        console.error(response.status, ` couldn't perform ${met} operation`);
                        errorObj = null;
                    }
                } else {
                    console.error(response.status, ` couldn't perform ${met} operation`);
                }
            } catch (e) {
                console.error("Error >>> ", e);
            } finally {
                //TODO: переделать
                if (!errorObj) showModal(modal, false);
            }
        }
    };

    await updateTable();
})()