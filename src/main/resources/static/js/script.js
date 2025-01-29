(function () {
    const modal = document.querySelector(".modal-container");
    const csrfValue = document.querySelector("[name=_csrf]").value;
    const csrfHeader = "X-CSRF-TOKEN";
    const modalFields = new Map();
    const errorFields = new Map();
    let errorObj = null;
    const showElement = e => e.style.display = "block";
    const hideElement = e => e.style.display = "none";
    const setModalCaption = c => modal.querySelector("#modal-caption").textContent = c;

    modal.querySelectorAll("[data-name]")
        .forEach(node => errorFields.set(node.dataset.name, node));

    document.getElementById("modal-fields")
        .querySelectorAll("input, select")
        .forEach(node => modalFields.set(node.id, node));


    const clearErrorFields = (ef) => {
        ef.forEach((v, k) => {
            hideElement(v);
            v.textContent = "";
        });
        errorObj = null;
    }

    const showModal = (m, show) => {
        if (!show) {
            hideElement(m);
            return;
        }
        clearErrorFields(errorFields)
        showElement(m);
    };

    const convertModal = deleteUser => {
        if (deleteUser) {
            modalFields.forEach((v, k) => {
                if (k === "password") {
                    hideElement(v);
                    hideElement(v.previousElementSibling);
                    return;
                }
                v.disabled = true;
            });
            setModalCaption("Delete user");
            showElement(modal.querySelector("#modal-delete"));
            hideElement(modal.querySelector("#modal-edit"));
            return;
        }
        modalFields.forEach((v, k) => {
            if (k === "id") return;
            if (k === "password") {
                showElement(v);
                showElement(v.previousElementSibling);
                return;
            }
            v.disabled = false;
        });
        setModalCaption("Edit user");
        hideElement(modal.querySelector("#modal-delete"));
        showElement(modal.querySelector("#modal-edit"));
    }

    const fillInputs = (row) => {
        if (!row) return;
        row.querySelectorAll('[data-name]').forEach(n => {
            let modalField = modalFields.get(n.dataset.name);
            if (n.dataset.name === "roles") {
                Array.from(modalField.options).forEach(opt => {
                    opt.selected = n.textContent.includes(opt.value);
                })
            } else {
                modalField.value = n.textContent;
            }
        })
    }

    const updateTable = async () => {
        const getRow = user => {
            let out = '<tr>';
            Object.keys(user).forEach(k => {
                if (k === "password") return;
                out += `<td data-name=${k}>${k === "roles" ? user[k].reduce((a, b) => a + ` ${b}`, "") : user[k]}</td>`;
            });
            out += `<td>
                <button class="button button-edit">Edit</button>
                <td>
                <button class="button button-delete">Delete</button>
                </td>
                </tr>`;
            return out;
        };
        try {
            const table = document.querySelector("tbody");
            const resp = await fetch("/api/users");

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
    const fetchAndUpdate = (met, readData) => {
        return async () => {
            try {
                clearErrorFields(errorFields);
                let id = modalFields.get("id");
                const reqHeaders = new Headers();
                reqHeaders.append(csrfHeader, csrfValue);
                reqHeaders.append('Content-Type', 'application/json',);
                const response = await fetch(`/api/users/${id.value}`,
                    {
                        method: met,
                        headers: reqHeaders,
                        body: readData ? readData() : ''
                    });

                if (response.ok) {
                    console.log(response.status);
                    try {
                        clearErrorFields(errorFields);
                        await updateTable();
                    } catch (e) {
                        console.error("Couldn't update the table");
                    }
                } else if (response.status === 400) {
                    errorObj = await response.json();
                    if (errorObj) {
                        for (let i in errorObj) {
                            errorFields.get(i).textContent = errorObj[i];
                            showElement(errorFields.get(i))
                        }
                    } else {
                        console.error(response.status, ` couldn't perform ${met} operation`);
                    }
                } else {
                    console.error(response.status, ` couldn't perform ${met} operation`);
                }
            } catch (e) {
                console.error("Error >>> ", e);
            } finally {
                if (!errorObj) showModal(modal, false);
            }
        }
    };

    const readFields = () => {
        let user = {};
        modalFields.forEach((v, k) => {
            user[k] = k === "roles" ? Array.from(v.selectedOptions).map(r => r.value) : v.value;
        });
        return JSON.stringify(user);
    };

    document.getElementById("info").addEventListener("click", evt => {

        if (evt.target.classList.contains("button-edit")) {
            showModal(modal, true);
            fillInputs(evt.target.parentElement.parentElement);
            convertModal(false);
        }

        if (evt.target.classList.contains("button-delete")) {
            showModal(modal, true);
            fillInputs(evt.target.parentElement.parentElement);
            convertModal(true);
        }
    });

    modal.addEventListener("click", evt => {
        if (evt.target.classList.contains("modal-container") || (evt.target.id === "modal-cross") || (evt.target.id === "modal-close")) {
            showModal(modal, false);
        }
    });

    document.getElementById("modal-delete").addEventListener("click", fetchAndUpdate("DELETE"));

    document.getElementById("modal-edit").addEventListener("click", fetchAndUpdate("PUT", readFields));

})();