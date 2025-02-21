(async () => {
    const USERS_URL = "/api/admin/users";
    const USERS_TABLE = "usersTable";
    const csrfValue = document.querySelector("[name=_csrf]").value;
    const csrfHeader = "X-CSRF-TOKEN";
    const modalDelete = $('#modal-delete');
    const modalEdit = $('#modal-edit');
    const deleteFields = new Map();
    const editFields = new Map();
    const addNewUserFields = new Map();
    const errorEdit = new Map();
    const errorAddNewUser = new Map();
    let errorObj = null;
    const showElement = e => e.style.display = "block";
    const hideElement = e => e.style.display = "none";

    const fillMap = (modalId, fieldsMap, errorMap) => {
        document.getElementById(modalId).querySelectorAll("[data-name]").forEach(node => {
            fieldsMap.set(node.dataset.name, node);
            if (!errorMap) return;
            let errorNode = node.nextElementSibling;
            if (errorNode) errorMap.set(node.dataset.name, errorNode);
        })
    }

    fillMap("modal-delete", deleteFields);
    fillMap("modal-edit", editFields, errorEdit);
    fillMap("addNewUser", addNewUserFields, errorAddNewUser);

    const clearErrorFields = (ef) => {
        errorObj = null;
        ef.forEach(v => {
            hideElement(v);
            v.textContent = "";
        });
    };

    const clearFields = (fields) => {
        fields.forEach((v, k) => {
            if (k === "roles") {
                Array.from(v.options).forEach(opt => {
                    opt.selected = false;
                })
            } else {
                v.value = '';
            }
        })
    };

    const readFields = fields => {
        return () => {
            let user = {};
            fields.forEach((v, k) => {
                user[k] = k === "roles" ? Array.from(v.selectedOptions).map(r => r.value) : v.value;
            });
            return JSON.stringify(user);
        }
    };

    const fillInputs = fields => {
        return async (event) => {
            try {
                let response = await fetch(`/api/admin/users/${$(event.relatedTarget).data('uid')}`);
                let user = await response.json();
                if (!user) return;
                Object.keys(user).forEach(k => {
                    if (k === "password") return;
                    if (k === "roles") {
                        Array.from(fields.get(k).options).forEach(opt => {
                            opt.selected = user[k].includes(opt.value);
                        })
                        return;
                    }
                    fields.get(k).value = user[k];
                })
            } catch (e) {
                console.error("Couldn't get user data from the server", e);
            }
        }
    };

    const but = (c, i, t) => ` <td> <button type="button" class="btn btn-${c} px-1 py-1 "
                                                 data-uid=${i} data-toggle="modal" 
                                                 data-target="#modal-${t}"> ${t.charAt(0).toUpperCase() + t.slice(1)} 
                                                 </button></td>`;

    const row = user => {
        let out = '<tr>';
        Object.keys(user).forEach(k => {
            if (k === "password") return;
            out += `<td data-name=${k}> ${k === "roles" ? user[k].reduce((a, b) => a + ` ${b}`, "") : user[k]} </td>`;
        });
        out += `${but("info", user.id, "edit")} ${but("danger", user.id, "delete")}`;
        return out;
    };


    const fetchAndValidate = (met, modalFields, errorFields, readData, hideModal) => {
        return async () => {
            try {
                clearErrorFields(errorFields);
                let id = modalFields.get("id");
                const reqHeaders = new Headers();
                reqHeaders.append(csrfHeader, csrfValue);
                reqHeaders.append('Content-Type', 'application/json',);
                const response = await fetch(`/api/admin/users/${id ? id.value : ''}`,
                    {
                        method: met,
                        headers: reqHeaders,
                        body: readData ? readData() : ''
                    });

                if (response.ok) {
                    console.log(response.status);
                    await (await updateTable)(USERS_URL, row, USERS_TABLE);
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
                if (!errorObj) hideModal();
            }
        }
    };

    const validateAge = e => {
        try {
            let v = parseInt(e.target.value);
            if (v > 127)
                e.target.value = 127;
            if (v < 0) e.target.value = 0;
        } catch (e) {
            e.target.value = 0;
        }
    }

    modalDelete.on('shown.bs.modal', fillInputs(deleteFields));
    modalEdit.on('shown.bs.modal', fillInputs(editFields));
    modalEdit.on('hide.bs.modal', () => clearErrorFields(errorEdit));

    $('#nav-newUser-tab').on('hide.bs.tab', () => {
        clearFields(addNewUserFields);
        clearErrorFields(errorAddNewUser);
    });

    document.getElementById("editAge").addEventListener("input", validateAge);
    document.getElementById("newAge").addEventListener("input", validateAge);

    document.getElementById("button-delete")
        .addEventListener("click", fetchAndValidate("DELETE", deleteFields,
            new Map(), null, () => modalDelete.modal('hide')));
   
    document.getElementById("button-edit")
        .addEventListener("click", fetchAndValidate("PUT", editFields, errorEdit,
            readFields(editFields), () => modalEdit.modal('hide')));

    document.getElementById("button-addNewUser")
        .addEventListener("click", fetchAndValidate("POST", addNewUserFields, errorAddNewUser,
            readFields(addNewUserFields), () => $('#nav-users-tab').tab('show')));

    await (await updateTable)(USERS_URL, row, USERS_TABLE);
})()